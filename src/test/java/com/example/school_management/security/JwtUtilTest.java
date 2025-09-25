package com.example.school_management.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=mySecretKey123456789012345678901234567890",
    "jwt.expiration=3600000"
})
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testGenerateToken() {
        String email = "test@email.com";
        String token = jwtUtil.generateToken(email);
        
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testValidateToken_ValidToken() {
        String email = "test@email.com";
        String token = jwtUtil.generateToken(email);
        
        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.here";
        
        boolean isValid = jwtUtil.validateToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    void testGetEmailFromToken_ValidToken() {
        String email = "test@email.com";
        String token = jwtUtil.generateToken(email);
        
        String extractedEmail = jwtUtil.getEmailFromToken(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testGetEmailFromToken_InvalidToken() {
        String invalidToken = "invalid.token.here";
        
        assertThrows(RuntimeException.class, () -> {
            jwtUtil.getEmailFromToken(invalidToken);
        });
    }
}
