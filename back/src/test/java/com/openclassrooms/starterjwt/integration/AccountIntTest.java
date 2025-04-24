package com.openclassrooms.starterjwt.integration;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


//Annotation indiquant qu'il s'agit d'un test Spring Boot avec configuration automatique de MockMvc
@SpringBootTest
@AutoConfigureMockMvc
public class AccountIntTest {

  // Injection de MockMvc pour simuler les requêtes HTTP
  @Autowired
  private MockMvc mockMvc;

  //Mock des services et mapper nécessaires
  @MockBean
  private UserService userService;
  @MockBean
  private UserMapper userMapper;

  //Variables de test
  private Long userId = 1L;
  private User mockUser;
  private UserDto mockUserDto;

  //Méthode exécutée avant chaque test pour initialiser les données
  @BeforeEach
  public void setup() {
	// Création d'un utilisateur mock
    mockUser = new User();
    mockUser.setAdmin(false);
    mockUser.setEmail("ibra@test.com");
    mockUser.setFirstName("ibra");
    mockUser.setId(1L);
    mockUser.setLastName("dieng");
    mockUser.setPassword("password123");
    
    // Création d'un DTO mock correspondant
    mockUserDto = new UserDto();
    mockUserDto.setAdmin(false);
    mockUserDto.setEmail("ibra@test.com");
    mockUserDto.setFirstName("ibra");
    mockUserDto.setId(1L);
    mockUserDto.setLastName("dieng");
    mockUserDto.setPassword("password123");
  }
  
  /**
   * Test : Récupération d'un utilisateur par ID - Cas valide
   * Vérifie que :
   * 1. Le endpoint retourne un status 200 OK
   * 2. Les données retournées correspondent à l'utilisateur demandé
   * 3. Le mot de passe n'est pas inclus dans la réponse
   */

  @Test
  @WithMockUser // Simule un utilisateur authentifié
  public void testerUser_Avec_GetFindUserById_RetourneUserDto() throws Exception {
    long userId = 1L;
    User user = mockUser;
    UserDto userDto = mockUserDto;
 
    // Configuration des mocks
    when(userService.findById(userId)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(userDto);

    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/user/{id}", userId))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist()); 
  }

  /**
   * Test : Récupération avec un ID au format invalide
   * Vérifie que le endpoint retourne un status 400 Bad Request
   * quand l'ID n'est pas un nombre
   */
  @Test
  @WithMockUser
  public void testerUser_GetFindUserById_AvecFormatIdInvalid_RetourneBadRequest()
    throws Exception {
    String invalidUserId = "invalideId";

    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/user/{id}", invalidUserId))
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
  
  
  /**
   * Test : Récupération avec un ID qui n'existe pas
   * Vérifie que le endpoint retourne un status 404 Not Found
   * quand l'utilisateur n'existe pas
   */
  @Test
  @WithMockUser
  public void testerUser_GetFindUserById_Avec_IdExistePas_ReturneNotFound()
    throws Exception {
    Long invalidUserId = 2L;

    doThrow(NotFoundException.class).when(userService).findById(anyLong());

    mockMvc
      .perform(MockMvcRequestBuilders.get("/api/user/{id}", invalidUserId))
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  /**
   * Test : Suppression d'un utilisateur - Cas valide
   * Vérifie que :
   * 1. L'utilisateur authentifié peut supprimer son propre compte
   * 2. Le endpoint retourne un status 200 OK
   */
  @Test
  @WithMockUser(username = "ibra@test.com")
  public void testerUser_SupprimerUser_ReturneOk() throws Exception {
    User user = mockUser;

    when(userService.findById(userId)).thenReturn(user);// Simule l'utilisateur propriétaire du compte

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
      .andExpect(MockMvcResultMatchers.status().isOk());
  }

  /**
   * Test : Suppression avec un ID au format invalide
   * Vérifie que le endpoint retourne un status 400 Bad Request
   * quand l'ID n'est pas un nombre
   */
  @Test
  @WithMockUser
  public void testerUser_SupprimerUser_Avec_IdInvalide_ReturneBadRequest()
    throws Exception {
    String invalidUserId = "invalid";

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/user/{id}", invalidUserId))
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
 
  
  /**
   * Test : Suppression par un utilisateur non autorisé
   * Vérifie que :
   * 1. Un utilisateur ne peut pas supprimer le compte d'un autre utilisateur
   * 2. Le endpoint retourne un status 401 Unauthorized
   */
  @Test
  @WithMockUser(username = "fatou@test.com")// Simule un utilisateur différent du propriétaire
  public void testerUser_SupprimerUser_Avec_UnauthorizedUser_RetourneUnauthorized()
    throws Exception {
    User user = mockUser;
    UserDto userDto = mockUserDto;

    when(userService.findById(userId)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(userDto);

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
      .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  /**
   * Test : Suppression d'un utilisateur qui n'existe pas
   * Vérifie que le endpoint retourne un status 404 Not Found
   * quand l'utilisateur n'existe pas
   */
  @Test
  @WithMockUser
  public void testerUser_SupprimerUser_Avec_NotFoundUser_RetourneNotFound()
    throws Exception {
    when(userService.findById(userId)).thenReturn(null);

    mockMvc
      .perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}