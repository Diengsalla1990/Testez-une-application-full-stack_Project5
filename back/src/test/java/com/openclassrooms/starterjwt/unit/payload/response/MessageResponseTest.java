package com.openclassrooms.starterjwt.unit.payload.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import org.junit.jupiter.api.Test;
public class MessageResponseTest {
	
	@Test
	  void testerMessageResponseConstructorAndGetters() {
	    // Arrange
	    String messageContent = "message Test";

	    // Act
	    MessageResponse messageResponse = new MessageResponse(messageContent);

	    // Assert
	    assertThat(messageResponse.getMessage()).isEqualTo(messageContent);
	  }
	
	@Test
	  void testerMessageResponseSetter() {
	    // Arrange
	    MessageResponse messageResponse = new MessageResponse("message Initial");

	    // Act
	    messageResponse.setMessage("Updated message");

	    // Assert
	    assertThat(messageResponse.getMessage()).isEqualTo("Updated message");
	  }

	

}
