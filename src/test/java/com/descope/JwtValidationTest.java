package com.descope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

class JwtValidationTest {
    // Replace with your project id and jwt
    public static final String PROJECT_ID = "";
    public static final String TEST_JWT = "";

    private JwtValidation jwtValidation;

    @BeforeEach
    void setUp() {
        this.jwtValidation = new JwtValidation(PROJECT_ID);
    }

    @SneakyThrows
    @Test
    void validateAndCreateToken() {
        Assertions.assertThat(PROJECT_ID).isNotBlank();
        Assertions.assertThat(TEST_JWT).isNotBlank();
        var t = jwtValidation.validateAndCreateToken(TEST_JWT);
        Assertions.assertThat(t).isNotNull();
        Assertions.assertThat(t.getProjectId()).isEqualTo(PROJECT_ID);
        System.out.println(t);
    }  
}
