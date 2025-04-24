package com.openclassrooms.starterjwt.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//Configuration de l'extension Mockito pour les tests
@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

// Mock des services nécessaires pour le mapper
@Mock
private TeacherService teacherService;

@Mock
private UserService userService;

// Injection des mocks dans le mapper à tester (implémentation concrète)
@InjectMocks
private SessionMapperImpl sessionMapper;

// Test de conversion d'un SessionDto vers une entité Session
@Test
void tester_Avec_Entity() {
 // Arrange : préparation des données de test
 SessionDto sessionDto = new SessionDto();
 sessionDto.setDescription("Test Session DTO");
 sessionDto.setTeacher_id(1L); // ID du teacher
 sessionDto.setUsers(Arrays.asList(2L, 3L)); // IDs des users

 // Configuration des mocks
 Teacher teacher = new Teacher();
 when(teacherService.findById(anyLong())).thenReturn(teacher);

 User user1 = new User();
 User user2 = new User();
 when(userService.findById(2L)).thenReturn(user1);
 when(userService.findById(3L)).thenReturn(user2);

 // Act : appel de la méthode à tester
 Session result = sessionMapper.toEntity(sessionDto);

 // Assert : vérifications
 assertThat(result.getDescription()).isEqualTo(sessionDto.getDescription());
 assertThat(result.getTeacher()).isEqualTo(teacher); // Vérifie que le teacher est bien associé
 assertThat(result.getUsers()).containsExactly(user1, user2); // Vérifie la liste des users
}

// Test avec un SessionDto null
@Test
void testetEntity_AvecSessionDTONull_RetourneNull() {
 // Arrange
 SessionDto nullSessionDto = null;

 // Act
 Session result = sessionMapper.toEntity(nullSessionDto);

 // Assert
 assertThat(result).isNull();
 // Vérifie qu'aucun service n'a été appelé inutilement
 verify(userService, never()).findById(anyLong());
 verify(teacherService, never()).findById(anyLong());
}

// Test de conversion d'une entité Session vers un SessionDto
@Test
void tester_Avec_Dto() {
 // Arrange
 Session session = new Session();
 session.setDescription("Test Session");
 session.setId(1L);
 session.setTeacher(new Teacher()); 

 User user1 = new User();
 user1.setId(1L);
 User user2 = new User();
 user2.setId(2L);
 session.setUsers(Arrays.asList(user1, user2));

 // Act
 SessionDto result = sessionMapper.toDto(session);

 // Assert
 assertThat(result.getDescription()).isEqualTo(session.getDescription());
 assertThat(result.getTeacher_id()).isEqualTo(session.getTeacher().getId());
 assertThat(result.getUsers()).containsExactly(1L, 2L); // Vérifie les IDs des users
}

// Test avec une Session null
@Test
void testerDto_AvecSessionNull_RetourneNull() {
 // Arrange
 Session nullSession = null;

 // Act
 SessionDto result = sessionMapper.toDto(nullSession);

 // Assert
 assertThat(result).isNull();
 // Vérifie qu'aucun service n'a été appelé
 verify(userService, never()).findById(anyLong());
 verify(teacherService, never()).findById(anyLong());
}

// Test de conversion d'une liste de SessionDto vers une liste de Session
@Test
void testerListSessionDto_A_Entity_RetournListSession() {
 // Arrange
 List<SessionDto> sessionDtoList = new ArrayList<>();
 SessionDto sessionDto1 = new SessionDto();
 sessionDto1.setId(1L);
 SessionDto sessionDto2 = new SessionDto();
 sessionDto2.setId(2L);

 sessionDtoList.add(sessionDto1);
 sessionDtoList.add(sessionDto2);

 // Act
 List<Session> resultList = sessionMapper.toEntity(sessionDtoList);

 // Assert
 assertThat(resultList).hasSize(2); // Vérifie la taille de la liste
 
 // Vérifie les éléments individuels
 Session session1 = resultList.get(0);
 Session session2 = resultList.get(1);

 assertThat(session1.getId()).isEqualTo(1L);
 assertThat(session2.getId()).isEqualTo(2L);
}

// Test avec une liste de SessionDto null
@Test
void testNullSessionDtoList_ToEntity_ShouldReturnNull() {
 // Arrange
 List<SessionDto> nullSessionDtoList = null;

 // Act
 List<Session> result = sessionMapper.toEntity(nullSessionDtoList);

 // Assert
 assertThat(result).isNull();
}

// Test de conversion d'une liste de Session vers une liste de SessionDto
@Test
void testerListSession_A_Dto_RetourneListSessionDto() {
 // Arrange
 List<Session> sessionList = new ArrayList<>();
 Session session1 = new Session();
 session1.setId(1L);
 Session session2 = new Session();
 session2.setId(2L);

 sessionList.add(session1);
 sessionList.add(session2);

 // Act
 List<SessionDto> resultList = sessionMapper.toDto(sessionList);

 // Assert
 assertThat(resultList).hasSize(2); // Vérifie la taille de la liste
 
 // Vérifie les éléments individuels
 SessionDto sessionDto1 = resultList.get(0);
 SessionDto sessionDto2 = resultList.get(1);

 assertThat(sessionDto1.getId()).isEqualTo(1L);
 assertThat(sessionDto2.getId()).isEqualTo(2L);
}

// Test avec une liste de Session null
@Test
void testerListNullSession_A_Dto_RetournNull() {
 // Arrange
 List<Session> nullSessionList = null;

 // Act
 List<SessionDto> result = sessionMapper.toDto(nullSessionList);

 // Assert
 assertThat(result).isNull();
}
}