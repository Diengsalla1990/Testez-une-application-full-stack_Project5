package com.openclassrooms.starterjwt.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//Configuration de l'extension Mockito pour les tests
@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

	  // Mock du service SessionService
	  @Mock
	  private SessionService sessionService;

	  // Mock du mapper SessionMapper
	  @Mock
	  private SessionMapper sessionMapper;

	  // Injection des mocks dans le contrôleur à tester
	  @InjectMocks
	  private SessionController sessionController;

	  // Test pour la méthode findById quand la session existe
	  @Test
	  void findById_SessionExistes_ReturneSessionDto() {
	    // Arrange : préparation des données et comportements mockés
	    Long sessionId = 1L;
	    Session session = new Session();
	    when(sessionService.getById(sessionId)).thenReturn(session);

	    SessionDto sessionDto = new SessionDto();
	    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

	    // Act : appel de la méthode à tester
	    ResponseEntity<?> responseEntity = sessionController.findById(
	      sessionId.toString()
	    );

	    // Assert : vérifications des résultats
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(responseEntity.getBody()).isEqualTo(sessionDto);
	  }
	  
	  // Test pour la méthode findById quand la session n'existe pas
	  @Test
	  void findById_SessionExistePas_ReturneNotFound() {
	    // Arrange
	    Long sessionId = 1L;
	    when(sessionService.getById(sessionId)).thenReturn(null);

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.findById(
	      sessionId.toString()
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	    assertThat(responseEntity.getBody()).isNull();
	  }
	  
	  // Test pour la méthode findAll
	  @Test
	  void findAll_ReturneSessionDtoList() {
	    // Arrange
	    List<Session> sessions = Collections.singletonList(new Session());
	    when(sessionService.findAll()).thenReturn(sessions);

	    List<SessionDto> sessionDtos = Collections.singletonList(new SessionDto());
	    when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.findAll();

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(responseEntity.getBody()).isEqualTo(sessionDtos);
	  }

	  // Test pour la méthode create
	  @Test
	  void testCreate_ReturneSessionDto() {
	    // Arrange
	    SessionDto sessionDto = new SessionDto();
	    Session session = new Session();
	    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
	    when(sessionService.create(session)).thenReturn(session);
	    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(responseEntity.getBody()).isEqualTo(sessionDto);
	  }

	  // Test pour la méthode update quand la session existe
	  @Test
	  void testUpdate_SessionExistes_ReturnUpdatedSessionDto() {
	    // Arrange
	    String sessionId = "1";
	    SessionDto sessionDto = new SessionDto();
	    Session session = new Session();
	    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
	    when(sessionService.update(anyLong(), eq(session))).thenReturn(session);
	    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.update(
	      sessionId,
	      sessionDto
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(responseEntity.getBody()).isEqualTo(sessionDto);
	  }

	  // Test pour la méthode update avec un ID invalide
	  @Test
	  void testeUpdate_FormatIdInvalide_ReturneBadRequest() {
	    // Arrange
	    String invalidSessionId = "invalidId";
	    SessionDto sessionDto = new SessionDto();

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.update(
	      invalidSessionId,
	      sessionDto
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode())
	      .isEqualTo(HttpStatus.BAD_REQUEST);
	  }

	  // Test pour la méthode delete (save) quand la session existe
	  @Test
	  void testeDeleteSave_SessionExistes_ReturneOk() {
	    // Arrange
	    String sessionId = "1";
	    when(sessionService.getById(anyLong())).thenReturn(new Session());

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.save(sessionId);

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	  }

	  // Test pour la méthode delete (save) quand la session n'existe pas
	  @Test
	  void testeDeleteSave_SessionExistePas_ReturneNotFound() {
	    // Arrange
	    String sessionId = "1";
	    when(sessionService.getById(anyLong())).thenReturn(null);

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.save(sessionId);

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	  }

	  // Test pour la méthode delete (save) avec un ID invalide
	  @Test
	  void testeDeleteSave_FormatIdInvalid_ReturneBadRequest() {
	    // Arrange
	    String invalidSessionId = "invalidId";

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.save(invalidSessionId);

	    // Assert
	    assertThat(responseEntity.getStatusCode())
	      .isEqualTo(HttpStatus.BAD_REQUEST);
	  }

	  // Test pour la méthode participate
	  @Test
	  void testParticipate_ReturneOk() {
	    // Arrange
	    String sessionId = "1";
	    String userId = "2";

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.participate(
	      sessionId,
	      userId
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    verify(sessionService, times(1)).participate(anyLong(), anyLong());
	  }

	  // Test pour la méthode participate avec un ID invalide
	  @Test
	  void tesetParticipate_FormatIdInvalid_ReturneBadRequest() {
	    // Arrange
	    String invalidSessionId = "invalidId";
	    String userId = "2";

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.participate(
	      invalidSessionId,
	      userId
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode())
	      .isEqualTo(HttpStatus.BAD_REQUEST);
	    verify(sessionService, never()).participate(anyLong(), anyLong());
	  }

	  // Test pour la méthode noLongerParticipate
	  @Test
	  void testeNoLongerParticipate_ReturneOk() {
	    // Arrange
	    String sessionId = "1";
	    String userId = "2";

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(
	      sessionId,
	      userId
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    verify(sessionService, times(1)).noLongerParticipate(anyLong(), anyLong());
	  }

	  // Test pour la méthode noLongerParticipate avec un ID invalide
	  @Test
	  void testeNoLongerParticipate_FormatIdInvalid_ReturneBadRequest() {
	    // Arrange
	    String invalidSessionId = "invalidId";
	    String userId = "2";

	    // Act
	    ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(
	      invalidSessionId,
	      userId
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode())
	      .isEqualTo(HttpStatus.BAD_REQUEST);
	    verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
	  }
}