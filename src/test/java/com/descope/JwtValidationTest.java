package com.descope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

class JwtValidationTest {
    public static final String PROJECT_ID = "P2NQiPfsldwmWEUCCsRWMGzOT4cn"; // Authentiblood project for testing
    public static final String TEST_JWT = "eyJhbGciOiJSUzI1NiIsImtpZCI6IlAyTlFpUGZzbGR3bVdFVUNDc1JXTUd6T1Q0Y24iLCJ0eXAiOiJKV1QifQ.eyJhbXIiOlsib2F1dGgiXSwiZHJuIjoiRFMiLCJleHAiOjE2ODE5MzEwMjUsImlhdCI6MTY4MTkzMDQyNSwiaXNzIjoiUDJOUWlQZnNsZHdtV0VVQ0NzUldNR3pPVDRjbiIsInJleHAiOiIyMDIzLTA1LTE3VDE4OjUzOjQ1WiIsInN1YiI6IlUyT2VtY0x2RVRNSFBFdTk5SEU4ek5ESXV6MmgifQ.d1AJHj9AymxmVGyiEo5cttyMdo9P77wv-i8zJyhCa_IXaoWoCBbaxFNvELM7LnAhEZW5hcKpgJt9IsXi0w7q9X3NYHON6APpqwQDksvgPy9iGf2noo2KOUT40bF8HRt6UEwYaKi-KyYv_DOxcqmzO4vn595CWAGo93u7xSKOm6kWJs8TG6YH99Ody2iiYPAi1BegjGkCdu10PitfWb2F6yw-2TKBKFg4RN7W50rcbxTbZD76BjLEtU4tBmNejL6uHTC0RbKOqR_uHQZHPDdUIieb2XNvsLyKv019XFweV-2yUNYJ6aHPi4nL8jbiMn5_lEPe86iI5TN9mP1oxy1iaw";

    private JwtValidation jwtValidation;

    @BeforeEach
    void setUp() {
        this.jwtValidation = new JwtValidation(PROJECT_ID);
    }

    @SneakyThrows
    @Test
    void validateAndCreateToken() {
        var t = jwtValidation.validateAndCreateToken(TEST_JWT);
        Assertions.assertThat(t).isNotNull();
        Assertions.assertThat(t.getProjectId()).isEqualTo(PROJECT_ID);
        System.out.println(t);
    }  
}
