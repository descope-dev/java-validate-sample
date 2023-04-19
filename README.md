# java-validate-sample
A simple package for Descope JWT validation for Java, along with a test

## Prerequisites 
- [Java](https://www.java.com/en/download)
- [Maven](https://maven.apache.org/download.cgi)

## Quick start

```java
var PROJECT_ID = "<Descope-Project-ID>";
var TEST_JWT = "<JWT>";
var jwtValidation = new JwtValidation(PROJECT_ID);
try {
    var t = jwtValidation.validateAndCreateToken(TEST_JWT);
    System.out.printf("JWT token %s\n", t);
} catch (Exception e) {
    // print e
    System.out.printf("JWT validation error %s\n", e.getMessage());
}
```
 
## Run test
Run `mvn test`