package com.descope;

import java.math.BigInteger;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;

/**
 * An example of how to validate Descope JWT tokens
 */
public class JwtValidation {
    private static final long SKEW_SECONDS = TimeUnit.SECONDS.toSeconds(5);
    private static final String DESCOPE_API = "https://api.descope.com";
    private static final String DESCOPE_API_VERSION = "v2";
    private static final String DESCOPE_API_KEYS_ENDPOINT = "keys";
    private static final String JSON_KEYS = "keys";
    /**
     * Cache of keys for the project for validating jwt signatures
     */
    private final List<PublicKey> projectKeys = new CopyOnWriteArrayList<>();
    /**
     * Our project id
     */
    private final String projectId;

    /**
     * Create JwtValidation for a given project
     * @param projectId the project to validate jwt for
     */
    public JwtValidation(String projectId) {
        this.projectId = projectId;
    }

    @SneakyThrows
    // https://mojoauth.com/blog/jwt-validation-with-jwks-java/
    private static PublicKey getPublicKey(SigningKey signingKey) {
      byte[] exponentB = Base64.getUrlDecoder().decode(signingKey.getE());
      byte[] modulusB = Base64.getUrlDecoder().decode(signingKey.getN());
      BigInteger bigExponent = new BigInteger(1, exponentB);
      BigInteger bigModulus = new BigInteger(1, modulusB);
  
      return KeyFactory.getInstance(signingKey.getKty())
          .generatePublic(new RSAPublicKeySpec(bigModulus, bigExponent));
    }
  
    /**
     * Load the keys for the project if the cache is empty or we are forced to reload
     * @param force pass true to force reload
     */
    @SneakyThrows
    protected void loadKeys(boolean force) {
        if (projectKeys.isEmpty() || force) {
            var mapper = new ObjectMapper();
            var node = mapper.readTree(
                URI.create(String.join("/",
                    DESCOPE_API,
                    DESCOPE_API_VERSION,
                    DESCOPE_API_KEYS_ENDPOINT,
                    projectId))
                    .toURL());
            var keys = node.get(JSON_KEYS);
            if (keys != null && keys.isArray()) {
                for (var k : keys) {
                    var signingKey = new SigningKey();
                    signingKey.setAlg(k.get("alg").asText());
                    signingKey.setE(k.get("e").asText());
                    signingKey.setKid(k.get("kid").asText());
                    signingKey.setKty(k.get("kty").asText());
                    signingKey.setN(k.get("n").asText());
                    signingKey.setUse(k.get("use").asText());
                    projectKeys.add(getPublicKey(signingKey));
                }
            }
        }
    }

    /**
     * Validate the given JWT and return a parsed token with the claims
     */
    @SneakyThrows
    public Token validateAndCreateToken(String jwt) {
        if (StringUtils.isBlank(jwt)) {
            throw new IllegalArgumentException("Must provide token");
        }
        for (var i = 0; i < 2; i++) {
            // If no key found, force reload
            loadKeys(i == 1);
            for (var k : projectKeys) {
                var jwtParser =
                    Jwts.parserBuilder()
                        .setSigningKey(k)
                        .setAllowedClockSkewSeconds(SKEW_SECONDS)
                        .build();
                try {
                    Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwt);
                    JwsHeader<?> header = claimsJws.getHeader();
                    var claims = claimsJws.getBody();
                    return Token.builder()
                        .jwt(jwt)
                        .projectId(header.getKeyId())
                        .id(claims.getSubject())
                        .expiration(claims.getExpiration().getTime())
                        .refreshExpiration(claims.get("rexp", String.class))
                        .claims(claims)
                        .build();    
                } catch (Exception e) {
                    // Ignore the errors (should log in real app)
                    System.err.println(e);
                }
            }
        }
        throw new IllegalArgumentException("No signing key found for the given JWT");
    }
}
