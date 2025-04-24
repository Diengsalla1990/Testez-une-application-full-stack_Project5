package com.openclassrooms.starterjwt.unit.payload.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

public class LoginRequestTest {
	 private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	 private final Validator validator = validatorFactory.getValidator();

	 @Test
	  void testerLoginValideRequest() {
	    // Arrange
	    LoginRequest request = new LoginRequest();
	    request.setEmail("test@test.com");
	    request.setPassword("password");

	    // Act & Assert
	    assertThat(request.getEmail()).isEqualTo("test@test.com");
	    assertThat(request.getPassword()).isEqualTo("password");
	  }
	 
	 @Test
	  void testeValidationNonVide() {
	    // Arrange
	    LoginRequest request = new LoginRequest();
	    request.setEmail("");
	    request.setPassword("");

	    // Act
	    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(
	      request
	    );

	    // Assert
	    assertThat(violations).hasSize(2);
	  }

}
