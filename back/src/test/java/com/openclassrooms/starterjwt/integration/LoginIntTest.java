package com.openclassrooms.starterjwt.integration;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//Annotation Spring Boot pour les tests d'intégration
@SpringBootTest
@AutoConfigureMockMvc // Configure automatiquement MockMvc
public class LoginIntTest {

// Injection de MockMvc pour simuler les requêtes HTTP
@Autowired
private MockMvc mockMvc;

// Mock des composants de sécurité et de persistence
@MockBean
private AuthenticationManager authenticationManager; // Gère l'authentification
@MockBean
private JwtUtils jwtUtils; // Gère la génération des tokens JWT
@MockBean
private PasswordEncoder passwordEncoder; // Encode les mots de passe
@MockBean
private UserRepository userRepository; // Accès aux données utilisateurs

/**
* Test: Authentification réussie
* Scénario: Un utilisateur fournit des identifiants valides
* Résultat attendu: 
* - Status HTTP 200 OK
* - Retourne un token JWT
*/
@Test
public void testerLogin_Retourne_JwtResponse() throws Exception {
 // 1. Configuration des mocks
 Authentication authentication = mock(Authentication.class);
 UserDetailsImpl userDetails = new UserDetailsImpl(
   10L, 
   "ibra@test.com",
   "ibra", 
   "dieng", 
   true, 
   "password123"
 );
 
 // Simulation du processus d'authentification
 when(authenticationManager.authenticate(any())).thenReturn(authentication);
 when(authentication.getPrincipal()).thenReturn(userDetails);
 
 // Simulation de la génération du token
 when(jwtUtils.generateJwtToken(any())).thenReturn("mockedToken");
 
 // Simulation de la recherche en base de données
 when(userRepository.findByEmail(any()))
   .thenReturn(Optional.of(
     new User("ibra@test.com", "dieng", "ibra", "password123", true)
   ));

 // 2. Préparation de la requête
 String requestBody = 
   "{ \"email\": \"ibra@test.com\", \"password\": \"password123\" }";

 // 3. Exécution et vérifications
 mockMvc
   .perform(
     MockMvcRequestBuilders
       .post("/api/auth/login") // Endpoint testé
       .content(requestBody) // Corps de la requête
       .contentType("application/json") // Type de contenu
   )
   .andExpect(MockMvcResultMatchers.status().isOk()) // Vérifie le status 200
   .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists()); // Vérifie la présence du token
}

/**
* Test: Authentification échouée
* Scénario: Un utilisateur fournit des identifiants invalides
* Résultat attendu:
* - Status HTTP 401 Unauthorized
* - Structure d'erreur conforme
* - Pas de token retourné
*/
@Test
public void testerLoginAvec_MauvaiseInfo_ReturnePasAutoriser() throws Exception {
 // Simulation d'une authentification échouée
 when(authenticationManager.authenticate(any()))
   .thenThrow(new BadCredentialsException("Bad credentials"));

 // Préparation de la requête avec mauvais mot de passe
 String requestBody = 
   "{ \"email\": \"ibra@test.com\", \"password\": \"Mpassword123\" }";

 // Exécution et vérifications
 mockMvc
   .perform(
     MockMvcRequestBuilders
       .post("/api/auth/login")
       .content(requestBody)
       .contentType("application/json")
   )
   .andExpect(MockMvcResultMatchers.status().isUnauthorized()) // Status 401
   .andExpect(MockMvcResultMatchers.jsonPath("$.token").doesNotExist()) // Pas de token
   .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401)) // Code erreur
   .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Unauthorized")) // Type erreur
   .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials")); // Message
}
}