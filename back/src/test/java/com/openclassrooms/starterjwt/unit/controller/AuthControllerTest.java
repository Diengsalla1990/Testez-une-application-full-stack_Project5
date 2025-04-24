package com.openclassrooms.starterjwt.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
	
	  @Mock
	  private AuthenticationManager authenticationManager;

	  @Mock
	  private JwtUtils jwtUtils;

	  @Mock
	  private PasswordEncoder passwordEncoder;

	  @Mock
	  private UserRepository userRepository;

	  @InjectMocks
	  private AuthController authController;
	  
	  @Test
	  void authentificationUser_ReturneJwtResponse() {
	    // Arrange
	    LoginRequest loginRequest = new LoginRequest();
	    loginRequest.setEmail("papedieng@gmail.com");
	    loginRequest.setPassword("pape123");
	    Authentication authentication = mock(Authentication.class);

	    UserDetailsImpl userDetails = new UserDetailsImpl(
	      10L,
	      "papedieng@gmail.com",
	      "Ibra",
	      "Dieng",
	      true,
	      "pape123"
	    );

	    when(authenticationManager.authenticate(any())).thenReturn(authentication);
	    when(authentication.getPrincipal()).thenReturn(userDetails);
	    when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockedToken");
	    when(userRepository.findByEmail(userDetails.getUsername()))
	      .thenReturn(
	        Optional.of(
	          new User("papedieng@gmail.com", "Ibra", "Dieng", "pape123", true)
	        )
	      );

	    // Act
	    ResponseEntity<?> responseEntity = authController.authenticateUser(
	      loginRequest
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(responseEntity.getBody()).isInstanceOf(JwtResponse.class);
	    JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
	    assertThat(jwtResponse.getToken()).isEqualTo("mockedToken");

	    assertThat(SecurityContextHolder.getContext().getAuthentication())
	      .isEqualTo(authentication);

	    assertThat(jwtResponse.getId()).isEqualTo(userDetails.getId());
	    assertThat(jwtResponse.getUsername()).isEqualTo(userDetails.getUsername());
	    assertThat(jwtResponse.getFirstName())
	      .isEqualTo(userDetails.getFirstName());
	    assertThat(jwtResponse.getLastName()).isEqualTo(userDetails.getLastName());
	    assertThat(jwtResponse.getAdmin()).isEqualTo(userDetails.getAdmin());
	    assertThat(jwtResponse.getType()).isEqualTo("Bearer");

	    verify(userRepository, times(1)).findByEmail(userDetails.getUsername());
	  }
	  
	  void authenticationUser_AvecEmailInconnu_RetourneUnauthorized() {
		    // Arrange
		    LoginRequest loginRequest = new LoginRequest();
		    loginRequest.setEmail("inconnu@gmail.com");
		    loginRequest.setPassword("password123");

		    when(authenticationManager.authenticate(any()))
		      .thenThrow(BadCredentialsException.class);

		    // Act & Assert
		    assertThrows(
		      BadCredentialsException.class,
		      () -> {
		        ResponseEntity<?> responseEntity = authController.authenticateUser(
		          loginRequest
		        );
		        assertThat(responseEntity.getStatusCode())
		          .isEqualTo(HttpStatus.UNAUTHORIZED);
		        assertThat(responseEntity.getBody())
		          .isInstanceOf(MessageResponse.class);
		        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
		        assertThat(messageResponse.getMessage()).isEqualTo("Bad credentials");
		        verify(userRepository, never()).findByEmail(anyString());
		      }
		    );
		  }
	  
	  @Test
	  void registerUser_RetourneSuccessResponse() {
	    // Arrange
	    SignupRequest signupRequest = new SignupRequest();
	    signupRequest.setEmail("papedieng@gmail.com");
	    signupRequest.setFirstName("Ibra");
	    signupRequest.setLastName("Dieng");
	    signupRequest.setPassword("pape123");

	    when(userRepository.existsByEmail(signupRequest.getEmail()))
	      .thenReturn(false);
	    when(passwordEncoder.encode(signupRequest.getPassword()))
	      .thenReturn("encodedPassword");
	    when(userRepository.save(any()))
	      .thenReturn(
	        new User("papedieng@gmail.com", "Ibra", "Dieng", "pape123", false)
	      );

	    // Act
	    ResponseEntity<?> responseEntity = authController.registerUser(
	      signupRequest
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(responseEntity.getBody()).isInstanceOf(MessageResponse.class);
	    MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
	    assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");

	    verify(userRepository, times(1)).existsByEmail("papedieng@gmail.com");
	    verify(userRepository, times(1)).save(any());
	  }
	  
	  @Test
	  void registerUser_AvecEmailExiste_ReturneBadRequest() {
	    // Arrange
	    SignupRequest signupRequest = new SignupRequest();
	    signupRequest.setEmail("pape@gmail.com");
	    signupRequest.setFirstName("Ibra");
	    signupRequest.setLastName("Dieng");
	    signupRequest.setPassword("pape123");

	    when(userRepository.existsByEmail("pape@gmail.com"))
	      .thenReturn(true);

	    // Act
	    ResponseEntity<?> responseEntity = authController.registerUser(
	      signupRequest
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode())
	      .isEqualTo(HttpStatus.BAD_REQUEST);
	    assertThat(responseEntity.getBody()).isInstanceOf(MessageResponse.class);
	    MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
	    assertThat(messageResponse.getMessage()).isEqualTo("Error: Email is already taken!");

	    verify(userRepository, times(1)).existsByEmail("pape@gmail.com");
	    verify(userRepository, never()).save(any());
	  }
	


}
