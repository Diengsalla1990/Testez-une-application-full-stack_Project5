package com.openclassrooms.starterjwt.unit.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

	 @Mock
	  private Authentication authentication;

	  @InjectMocks
	  private JwtUtils jwtUtils;

	  @BeforeEach
	  public void setup() {
	    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
	    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey");
	  }

	  @Test
	  void testGenerateJwtToken() {
	    // Arrange
	    UserDetailsImpl userDetails = createUserDetails();
	    when(authentication.getPrincipal()).thenReturn(userDetails);

	    // Act
	    String token = jwtUtils.generateJwtToken(authentication);

	    // Assert
	    assertThat(token).isNotNull();
	  }

	  @Test
	  void testGetUserNameFromJwtToken() {
	    // Arrange
	    String token = generateTestToken();

	    // Act
	    String username = jwtUtils.getUserNameFromJwtToken(token);

	    // Assert
	    assertThat(username).isEqualTo("test@test.com");
	  }

	  @Test
	  void testValidateJwtToken() {
	    // Arrange
	    String validToken = generateTestToken();
	    String invalidToken = "invalidToken";

	    // Act & Assert
	    assertThat(jwtUtils.validateJwtToken(validToken)).isTrue();
	    assertThat(jwtUtils.validateJwtToken(invalidToken)).isFalse();
	  }

	  @Test
	  void testInvalidSignature() {
	    // Arrange
	    String invalidToken = generateTestTokenWithInvalidSignature();

	    // Act & Assert
	    assertThat(jwtUtils.validateJwtToken(invalidToken)).isFalse();
	  }

	  @Test
	  void testMalformedToken() {
	    // Arrange
	    String malformedToken = "malformedToken";

	    // Act & Assert
	    assertThat(jwtUtils.validateJwtToken(malformedToken)).isFalse();
	  }

	  @Test
	  void testExpiredToken() {
	    // Arrange
	    String expiredToken = generateExpiredTestToken();

	    // Act & Assert
	    assertThat(jwtUtils.validateJwtToken(expiredToken)).isFalse();
	  }

	  @Test
	  void testEmptyClaimsString() {
	    // Arrange
	    String emptyClaimsToken = generateEmptyClaimsTestToken();

	    // Act & Assert
	    assertThat(jwtUtils.validateJwtToken(emptyClaimsToken)).isFalse();
	  }

	  private String generateTestToken() {
	    UserDetailsImpl userDetails = createUserDetails();
	    when(authentication.getPrincipal()).thenReturn(userDetails);

	    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
	    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey");

	    return jwtUtils.generateJwtToken(authentication);
	  }

	  private String generateTestTokenWithInvalidSignature() {
	    String validToken = generateTestToken();

	    return validToken.substring(0, validToken.length() - 5) + "INVALID";
	  }

	  private String generateExpiredTestToken() {
	    UserDetailsImpl userDetails = createUserDetails();
	    when(authentication.getPrincipal()).thenReturn(userDetails);

	    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -3600000);
	    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey");

	    return jwtUtils.generateJwtToken(authentication);
	  }

	  private String generateEmptyClaimsTestToken() {
	    return "eyJhbGciOiJIUzUxMiJ9.e30.";
	  }

	  private UserDetailsImpl createUserDetails() {
	    UserDetailsImpl userDetails = new UserDetailsImpl(
	      1L,
	      "test@test.com",
	      "firstname",
	      "lastname",
	      false,
	      "password"
	    );

	    return userDetails;
	  }
}
