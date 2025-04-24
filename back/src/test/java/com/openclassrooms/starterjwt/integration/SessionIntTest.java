package com.openclassrooms.starterjwt.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionIntTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SessionService sessionService;

  @MockBean
  private SessionMapperImpl sessionMapper;

  private Date fixedDate = new Date(1234567890123L);
  private User user1 = new User();
  private User user2 = new User();
  private java.util.List<User> userList = new ArrayList<User>();

  private Session mockSession;
  private SessionDto mockSessionDto;

  @BeforeEach
  public void setup() {
    userList.add(user1);
    userList.add(user2);

    mockSession = new Session();
    mockSession.setId(1L);
    mockSession.setDate(fixedDate);
    mockSession.setDescription("description");
    mockSession.setName("name");
    mockSession.setTeacher(new Teacher());
    mockSession.setUsers(userList);

    mockSessionDto = new SessionDto();
    mockSessionDto.setId(1L);
    mockSessionDto.setDate(fixedDate);
    mockSessionDto.setDescription("description");
    mockSessionDto.setName("name");
    mockSessionDto.setTeacher_id(10L);
    mockSessionDto.setUsers(new ArrayList<Long>(Arrays.asList(1L, 2L)));
  }

  @Test
  @WithMockUser
  public void testerSession_GetById_RetournenSessionDto() throws Exception {
    Session session = mockSession;
    SessionDto sessionDto = mockSessionDto;

    when(sessionService.getById(anyLong())).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/session/1"))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(sessionDto.getId())
      );

    verify(sessionService, times(1)).getById(anyLong());
  }

  @Test
  @WithMockUser
  public void testerSession_FindAll_RetourneListeSessionDto()
    throws Exception {
    Session session = mockSession;
    List<Session> sessionList = new ArrayList<>();
    sessionList.add(session);

    SessionDto sessionDto = mockSessionDto;
    List<SessionDto> sessionDtoList = new ArrayList<>();
    sessionDtoList.add(sessionDto);

    when(sessionService.findAll()).thenReturn(sessionList);
    when(sessionMapper.toDto(sessionList)).thenReturn(sessionDtoList);

    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/session"))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$[0].id").value(sessionDto.getId())
      );

    verify(sessionService, times(1)).findAll();
  }

  @Test
  public void testeSession_Sans_Authentification_GetById_RetourneUnauthorized()
    throws Exception {
    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/session/1"))
      .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    verify(sessionService, never()).getById(anyLong());
  }

  @Test
  @WithMockUser
  public void testSession_PostCreate_ShouldReturnSessionDto() throws Exception {
    SessionDto sessionDto = mockSessionDto;

    Session session = mockSession;

    when(sessionService.create(any())).thenReturn(session);
    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post("/api/session")
          .content(asJsonString(sessionDto))
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(sessionDto.getId())
      );

    verify(sessionService, times(1)).create(any());
  }

  @Test
  @WithMockUser
  public void testeSession_PutUpdate_RetourneSessionDto() throws Exception {
    Session session = mockSession;

    SessionDto sessionDto = mockSessionDto;

    when(sessionService.update(1L, session)).thenReturn(session);
    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .put("/api/session/1")
          .content(asJsonString(sessionDto))
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(sessionDto.getId())
      );

    verify(sessionService, times(1)).update(anyLong(), any());
  }

  @Test
  @WithMockUser
  public void testeSession_Avec_IdInvalide_PutUpdate_RetourneBadRequest()
    throws Exception {
    SessionDto sessionDto = mockSessionDto;

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .put("/api/session/invalidId") // Utilisation d'un ID invalide
          .content(asJsonString(sessionDto))
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(sessionService, never()).update(anyLong(), any());
  }

  @Test
  @WithMockUser
  public void testeSession_DeleteSession_RetourneOk() throws Exception {
    long sessionId = 1L;
    Session session = mockSession;

    when(sessionService.getById(sessionId)).thenReturn(session);

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/session/{id}", sessionId))
      .andExpect(MockMvcResultMatchers.status().isOk());

    verify(sessionService, times(1)).delete(sessionId);
  }

  @Test
  @WithMockUser
  public void testerSession_Delete_Avec_SessionExistePas_RetourneNotFound()
    throws Exception {
    long sessionId = 1L;
    when(sessionService.getById(sessionId)).thenReturn(null);

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/session/{id}", sessionId))
      .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(sessionService, never()).delete(sessionId);
  }

  @Test
  @WithMockUser
  public void testeSession_ParticipateInSession_RetourneOk()
    throws Exception {
    long sessionId = 1L;
    long userId = 2L;

    mockMvc
      .perform(
        MockMvcRequestBuilders.post(
          "/api/session/{id}/participate/{userId}",
          sessionId,
          userId
        )
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    verify(sessionService, times(1)).participate(sessionId, userId);
  }

  @Test
  @WithMockUser
  public void testerSession_Participate_Avec_InvalidId_RetourneBadRequest()
    throws Exception {
    String invalidSessionId = "invalid";
    long userId = 2L;

    mockMvc
      .perform(
        MockMvcRequestBuilders.post(
          "/api/session/{id}/participate/{userId}",
          invalidSessionId,
          userId
        )
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(sessionService, never()).participate(anyLong(), anyLong());
  }

  @Test
  @WithMockUser
  public void testerSession_ParticipateWithUserAlreadyParticipating_ShouldReturnBadRequest()
    throws Exception {
    long sessionId = 1L;
    long userId = 2L;

    doThrow(new BadRequestException())
      .when(sessionService)
      .participate(sessionId, userId);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post(
          "/api/session/{id}/participate/{userId}",
          sessionId,
          userId
        )
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(sessionService, times(1)).participate(sessionId, userId);
  }

  @Test
  @WithMockUser
  public void testSession_NoLongerParticipateInSession_ShouldReturnOk()
    throws Exception {
    long sessionId = 1L;
    long userId = 2L;

    mockMvc
      .perform(
        MockMvcRequestBuilders.delete(
          "/api/session/{id}/participate/{userId}",
          sessionId,
          userId
        )
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    verify(sessionService, times(1)).noLongerParticipate(sessionId, userId);
  }

  @Test
  @WithMockUser
  public void testSession_NoLongerParticipateWithInvalidId_ShouldReturnBadRequest()
    throws Exception {
    String invalidSessionId = "invalid";
    long userId = 2L;

    mockMvc
      .perform(
        MockMvcRequestBuilders.delete(
          "/api/session/{id}/participate/{userId}",
          invalidSessionId,
          userId
        )
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
  }

  @Test
  @WithMockUser
  public void testSession_NoLongerParticipateWithUserNotParticipating_ShouldReturnBadRequest()
    throws Exception {
    long sessionId = 1L;
    long userId = 2L;

    doThrow(new BadRequestException())
      .when(sessionService)
      .noLongerParticipate(sessionId, userId);

    mockMvc
      .perform(
        MockMvcRequestBuilders.delete(
          "/api/session/{id}/participate/{userId}",
          sessionId,
          userId
        )
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(sessionService, times(1)).noLongerParticipate(sessionId, userId);
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}