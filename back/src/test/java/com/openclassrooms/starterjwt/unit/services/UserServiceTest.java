package com.openclassrooms.starterjwt.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

@SpringBootTest
public class UserServiceTest {
	
	  @InjectMocks
	  private UserService userService;

	  @Mock
	  private UserRepository userRepository;

	  private Long testUserId = 1L;
	  private LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

	  private User testUser;

	  @BeforeEach
	  public void setUp() {
	    testUser = new User();
	    testUser.setId(1L);
	    testUser.setFirstName("Ibra");
	    testUser.setLastName("Dieng");
	    testUser.setEmail("ibra@test.com");
	    testUser.setAdmin(false);
	    testUser.setPassword("ibra123");
	    testUser.setCreatedAt(fixedDateTime);
	    testUser.setUpdatedAt(fixedDateTime);
	  }
	  
	  @Test
	  public void testerSupprimerUser() {
	    // Arrange
	    Long userIdUnderTest = testUserId;

	    // Act
	    userService.delete(userIdUnderTest);

	    // Assert
	    verify(userRepository).deleteById(userIdUnderTest);
	  }
	  
	  @Test
	  public void testerRechercherUserParId_IdExiste() {
	    // Arrange
	    Long userIdTest = testUserId;
	    User userTest = testUser;
	    when(userRepository.findById(userIdTest))
	      .thenReturn(Optional.of(testUser));

	    // Act
	    User retrievedUser = userService.findById(userIdTest);

	    // Assert
	    assertThat(retrievedUser).isEqualTo(userTest);
	    verify(userRepository).findById(userIdTest);
	  }
	  
	  @Test
	  public void testerRechercherUserParId_IdNonExist() {
	    // Arrange
	    Long UserIdNonExiste = 99L;
	    when(userRepository.findById(UserIdNonExiste))
	      .thenReturn(Optional.empty());

	    // Act
	    User retrievedUser = userService.findById(UserIdNonExiste);

	    // Assert
	    assertThat(retrievedUser).isNull();
	    verify(userRepository).findById(UserIdNonExiste);
	  }


}
