package com.openclassrooms.starterjwt.unit.payload.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import org.junit.jupiter.api.Test;
public class JwtResponseTest {
	
	 @Test
	  void testerJwtResponseConstructorAndGetters() {
	    // Arrange
	    String accessToken = "mockAccessToken";
	    Long id = 1L;
	    String username = "username";
	    String firstName = "firstname";
	    String lastName = "lastname";
	    Boolean admin = true;

	    // Act
	    JwtResponse jwtResponse = new JwtResponse(
	      accessToken,
	      id,
	      username,
	      firstName,
	      lastName,
	      admin
	    );
	    
	 // Assert
	    assertThat(jwtResponse.getToken()).isEqualTo(accessToken);
	    assertThat(jwtResponse.getId()).isEqualTo(id);
	    assertThat(jwtResponse.getUsername()).isEqualTo(username);
	    assertThat(jwtResponse.getFirstName()).isEqualTo(firstName);
	    assertThat(jwtResponse.getLastName()).isEqualTo(lastName);
	    assertThat(jwtResponse.getAdmin()).isEqualTo(admin);
	    assertThat(jwtResponse.getType()).isEqualTo("Bearer");

       }
	 
	 @Test
	  void testerJwtResponseSetters() {
	    // Arrange
	    String accessToken = "mockAccessToken";
	    Long id = 1L;
	    String username = "diengsalla";
	    String firstName = "Ibra";
	    String lastName = "dieng";
	    Boolean admin = true;

	    // Act
	    JwtResponse jwtResponse = new JwtResponse(
	      accessToken,
	      id,
	      username,
	      firstName,
	      lastName,
	      admin
	    );
	    
	    jwtResponse.setAdmin(false);
	    jwtResponse.setFirstName("Ibra");
	    jwtResponse.setId(2L);
	    jwtResponse.setLastName("dieng");
	    jwtResponse.setToken("new Token");
	    jwtResponse.setUsername("diengsalla");
	    jwtResponse.setType("other type");

	    // Assert
	    assertThat(jwtResponse.getToken()).isEqualTo("new Token");
	    assertThat(jwtResponse.getId()).isEqualTo(2L);
	    assertThat(jwtResponse.getUsername()).isEqualTo("diengsalla");
	    assertThat(jwtResponse.getFirstName()).isEqualTo("Ibra");
	    assertThat(jwtResponse.getLastName()).isEqualTo("dieng");
	    assertThat(jwtResponse.getAdmin()).isEqualTo(false);
	    assertThat(jwtResponse.getType()).isEqualTo("other type");
	  }

}
