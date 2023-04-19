package com.descope;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
  private String refreshExpiration;
  private Long expiration;
  private Long issuedAt;
  private String jwt;
  private String id;
  private String projectId;
  private Map<String, Object> claims;
}
