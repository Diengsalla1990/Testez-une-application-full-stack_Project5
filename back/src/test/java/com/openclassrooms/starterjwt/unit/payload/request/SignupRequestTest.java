package com.openclassrooms.starterjwt.unit.payload.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class SignupRequestTest {
	 private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	 private final Validator validator = validatorFactory.getValidator();

	 private SignupRequest mockSignupRequest = new SignupRequest();
     
	 @BeforeEach
	  public void setUp() {
	    mockSignupRequest.setEmail("test@test.com");
	    mockSignupRequest.setFirstName("firstname");
	    mockSignupRequest.setLastName("lastname");
	    mockSignupRequest.setPassword("password");
	  }
	 
	 @Test
	  void testeValideSignupRequest() {
	    // Arrange
	    SignupRequest request = mockSignupRequest;

	    // Act & Assert
	    assertThat(request.getEmail()).isEqualTo("test@test.com");
	    assertThat(request.getFirstName()).isEqualTo("firstname");
	    assertThat(request.getLastName()).isEqualTo("lastname");
	    assertThat(request.getPassword()).isEqualTo("password");
	  }
	 
	 @Test
	  void testeValidationNonVide() {
	    // Arrange
	    SignupRequest request = new SignupRequest();
	    request.setEmail("");
	    request.setFirstName("");
	    request.setLastName("");
	    request.setPassword("");

	    // Act
	    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(
	      request
	    );

	    // Assert
	    assertThat(violations).hasSize(7);
	  }
	 
	 @Test
	  void testeEmailFormatValidation() {
	    // Arrange
	    SignupRequest request = mockSignupRequest;
	    request.setEmail("invalid-email");

	    // Act
	    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(
	      request
	    );

	    // Assert
	    assertThat(violations).hasSize(1);
	  }
	 @Test
	  void testeSizeValidation() {
	    // Arrange
	    SignupRequest request = mockSignupRequest;
	    request.setFirstName("A");
	    request.setLastName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

	    // Act
	    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(
	      request
	    );

	    // Assert
	    assertThat(violations).hasSize(2);
	  }
	 
	 
	 @Test
	  void testerEqualsAndHashCode() {
	    SignupRequest request1 = mockSignupRequest;

	    SignupRequest request2 = new SignupRequest();
	    request2.setEmail("test@test.com");
	    request2.setFirstName("firstname");
	    request2.setLastName("lastname");
	    request2.setPassword("password");

	    SignupRequest request3 = new SignupRequest();
	    request3.setEmail("test2@test.com");
	    request3.setFirstName("firstname2");
	    request3.setLastName("lastname2");
	    request3.setPassword("password2");

	    // Test equals
	    assertThat(request1).isEqualTo(request2);
	    assertThat(request1).isNotEqualTo(request3);

	    // Test hashCode
	    assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
	    assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
	  }
	 @Test
	  void testToString() {
	    SignupRequest request = mockSignupRequest;

	    // Test toString
	    assertThat(request.toString()).contains("email=test@test.com");
	    assertThat(request.toString()).contains("firstName=firstname");
	    assertThat(request.toString()).contains("lastName=lastname");
	    assertThat(request.toString()).contains("password=password");
	  }



	 
	 
}
