package com.openclassrooms.starterjwt.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
public class SessionServiceTest {
	
	  @InjectMocks
	  private SessionService sessionService;

	  @Mock
	  private SessionRepository sessionRepository;

	  @Mock
	  private UserRepository userRepository;

	  private Long testSessionId = 1L;
	  private Long testUserId = 1L;
	  private LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
	  private Date fixedDate = new Date(1234567890123L);

	  private Teacher testTeacher;
	  private User testUser;
	  private List<User> testUserList;
	  private Session testSession;
	  private List<Session> testSessionList;

	  @BeforeEach
	  public void setUp() {
	    testTeacher = new Teacher();
	    testTeacher.setId(1L);
	    testTeacher.setFirstName("Ibra");
	    testTeacher.setLastName("Dieng");
	    testTeacher.setCreatedAt(fixedDateTime);
	    testTeacher.setUpdatedAt(fixedDateTime);

	    testUser = new User();
	    testUser.setId(1L);
	    testUser.setFirstName("Ibra");
	    testUser.setLastName("Dieng");
	    testUser.setEmail("Ibra@test.com");
	    testUser.setAdmin(false);
	    testUser.setPassword("password");
	    testUser.setCreatedAt(fixedDateTime);
	    testUser.setUpdatedAt(fixedDateTime);

	    testUserList = new ArrayList<User>();

	    testSession = new Session();
	    testSession.setId(1L);
	    testSession.setName("Session 1");
	    testSession.setTeacher(testTeacher);
	    testSession.setUsers(testUserList);
	    testSession.setDescription("Description");
	    testSession.setCreatedAt(fixedDateTime);
	    testSession.setUpdatedAt(fixedDateTime);
	    testSession.setDate(fixedDate);

	    testSessionList = new ArrayList<Session>();
	    testSessionList.add(testSession);
	    testSessionList.add(testSession);
	  }
	  
	  
	  
	  @Test
	  public void testerAjouterSession() {
	    // Arrange
	    Session sessionToCreateUnderTest = testSession;
	    when(sessionRepository.save(sessionToCreateUnderTest))
	      .thenReturn(sessionToCreateUnderTest);

	    // Act
	    Session createdSession = sessionService.create(sessionToCreateUnderTest);

	    // Assert
	    verify(sessionRepository).save(sessionToCreateUnderTest);
	    assertThat(createdSession).isEqualTo(sessionToCreateUnderTest);
	  }

	  @Test
	  public void testerSupprimerSession() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    // Act
	    sessionService.delete(testSessionIdUnderTest);

	    // Assert
	    verify(sessionRepository).deleteById(testSessionIdUnderTest);
	  }

	  @Test
	  public void testerFindAllSessions() {
	    // Arrange
	    List<Session> sessionsListUnderTest = testSessionList;
	    when(sessionRepository.findAll()).thenReturn(sessionsListUnderTest);

	    // Act
	    List<Session> retrievedSessions = sessionService.findAll();

	    // Assert
	    verify(sessionRepository).findAll();
	    assertThat(retrievedSessions).isEqualTo(sessionsListUnderTest);
	  }

	  @Test
	  public void testerGetSessionParId() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    Session sessionUnderTest = testSession;
	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.of(sessionUnderTest));

	    // Act
	    Session retrievedSession = sessionService.getById(testSessionIdUnderTest);

	    // Assert
	    verify(sessionRepository).findById(testSessionIdUnderTest);
	    assertThat(retrievedSession).isEqualTo(sessionUnderTest);
	  }

	  @Test
	  public void testerGetSessionParId_SessionNotFound() {
	    // Arrange
	    Long testSessionIdUnderTest = 123L;
	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.empty());

	    // Act
	    Session retrievedSession = sessionService.getById(testSessionIdUnderTest);

	    // Assert
	    verify(sessionRepository).findById(testSessionIdUnderTest);
	    assertThat(retrievedSession).isNull();
	  }

	  @Test
	  public void testerMiseAJourSession() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId + 1;
	    Session updatedSessionUnderTest = testSession;

	    when(sessionRepository.save(updatedSessionUnderTest))
	      .thenReturn(updatedSessionUnderTest);

	    // Act
	    Session result = sessionService.update(
	      testSessionIdUnderTest,
	      updatedSessionUnderTest
	    );

	    // Assert
	    verify(sessionRepository).save(updatedSessionUnderTest);
	    assertThat(result).isEqualTo(updatedSessionUnderTest);
	    assertThat(result.getId()).isEqualTo(testSessionId + 1);
	  }

	  @Test
	  public void testerParticipationInSession() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    Long userId = testUserId;

	    Session sessionUnderTest = testSession;
	    sessionUnderTest.setUsers(new ArrayList<User>());

	    User userUnderTest = testUser;
	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.of(sessionUnderTest));
	    when(userRepository.findById(userId))
	      .thenReturn(Optional.of(userUnderTest));

	    // Act
	    sessionService.participate(testSessionIdUnderTest, userId);

	    // Assert
	    verify(sessionRepository).save(sessionUnderTest);
	    assertThat(sessionUnderTest.getUsers()).contains(userUnderTest);
	  }

	  @Test
	  public void testerParticipationInSession_UserNotFound() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    Long userIdUnderTest = testUserId;
	    Session sessionUnderTest = testSession;
	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.of(sessionUnderTest));
	    when(userRepository.findById(userIdUnderTest)).thenReturn(Optional.empty());

	    // Act and Assert
	    assertThatExceptionOfType(NotFoundException.class)
	      .isThrownBy(() ->
	        sessionService.participate(testSessionIdUnderTest, userIdUnderTest)
	      );
	  }

	  @Test
	  public void testerParticipationInSession_SessionNotFound() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    Long userIdUnderTest = testUserId;
	    User userUnderTest = testUser;
	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.empty());
	    when(userRepository.findById(userIdUnderTest))
	      .thenReturn(Optional.of(userUnderTest));

	    // Act and Assert
	    assertThatExceptionOfType(NotFoundException.class)
	      .isThrownBy(() ->
	        sessionService.participate(testSessionIdUnderTest, userIdUnderTest)
	      );
	  }

	  @Test
	  public void testerParticipationInSession_AlreadyParticipating() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    Long userIdUnderTest = testUserId;
	    Session sessionUnderTest = testSession;
	    User userUnderTest = testUser;
	    sessionUnderTest.getUsers().add(userUnderTest);
	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.of(sessionUnderTest));
	    when(userRepository.findById(userIdUnderTest))
	      .thenReturn(Optional.of(userUnderTest));

	    // Act and Assert
	    assertThatExceptionOfType(BadRequestException.class)
	      .isThrownBy(() ->
	        sessionService.participate(testSessionIdUnderTest, userIdUnderTest)
	      );
	  }

	  @Test
	  public void testerNoLongerParticipationInSession() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    Long userIdUnderTest = testUserId;
	    Session sessionUnderTest = testSession;
	    User userUnderTest = testUser;
	    sessionUnderTest.getUsers().add(userUnderTest);
	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.of(sessionUnderTest));

	    // Act
	    sessionService.noLongerParticipate(testSessionIdUnderTest, userIdUnderTest);

	    // Assert
	    verify(sessionRepository).save(sessionUnderTest);
	    assertThat(sessionUnderTest.getUsers()).doesNotContain(userUnderTest);
	  }

	  @Test
	  public void testerNoLongerParticipationInSession_SessionNotFound() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    Long userIdUnderTest = testUserId;

	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.empty());

	    // Act and Assert
	    assertThatExceptionOfType(NotFoundException.class)
	      .isThrownBy(() ->
	        sessionService.noLongerParticipate(
	          testSessionIdUnderTest,
	          userIdUnderTest
	        )
	      );
	  }

	  @Test
	  public void testerNoLongerParticipationInSession_NotParticipating() {
	    // Arrange
	    Long testSessionIdUnderTest = testSessionId;
	    Long userId = testUserId;
	    Session sessionUnderTest = testSession;
	    when(sessionRepository.findById(testSessionIdUnderTest))
	      .thenReturn(Optional.of(sessionUnderTest));

	    // Act and Assert
	    assertThatExceptionOfType(BadRequestException.class)
	      .isThrownBy(() ->
	        sessionService.noLongerParticipate(testSessionIdUnderTest, userId)
	      );
	  }

}
