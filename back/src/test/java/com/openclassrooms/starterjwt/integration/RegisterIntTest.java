package com.openclassrooms.starterjwt.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//Annotations Spring Boot pour les tests d'intégration
@SpringBootTest // Indique qu'il s'agit d'un test Spring Boot
@AutoConfigureMockMvc // Configure automatiquement MockMvc pour les tests web
public class RegisterIntTest {

// Injection de MockMvc pour simuler les requêtes HTTP
@Autowired
private MockMvc mockMvc;

// Mock des dépendances
@MockBean
private PasswordEncoder passwordEncoder; // Pour le chiffrement des mots de passe
@MockBean
private UserRepository userRepository; // Pour l'accès aux données utilisateurs

/**
* Test: Enregistrement réussi d'un nouvel utilisateur
* Scénario: Un nouvel utilisateur fournit des informations valides
* Résultat attendu:
* - Status HTTP 200 OK
* - Message de succès
*/
@Test
public void testerUserRegister_RetourneSuccess() throws Exception {
 // 1. Préparation des données de test
 String requestBody = "{ \"email\": \"fatou@test.com\", \"firstName\": \"fatou\", \"lastName\": \"dieng\", \"password\": \"password123\" }";

 // 2. Configuration des mocks
 when(userRepository.existsByEmail(any())).thenReturn(false); // Simule un email non existant
 when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword"); // Simule le chiffrement

 // 3. Exécution de la requête et vérifications
 mockMvc
   .perform(
     MockMvcRequestBuilders
       .post("/api/auth/register") // Endpoint testé
       .content(requestBody) // Corps de la requête
       .contentType("application/json") // Type de contenu
   )
   .andExpect(MockMvcResultMatchers.status().isOk()) // Vérifie le status 200
   .andExpect(
     MockMvcResultMatchers
       .jsonPath("$.message")
       .value("User registered successfully!") // Vérifie le message
   );
}

/**
* Test: Tentative d'enregistrement avec un email existant
* Scénario: Un utilisateur fournit un email déjà utilisé
* Résultat attendu:
* - Status HTTP 400 Bad Request
* - Message d'erreur approprié
*/
@Test
public void testerUserRegister_AvecEmailExistant_Retourne_BadRequest() throws Exception {
 // 1. Préparation des données de test
 String requestBody = "{ \"email\": \"ibra@test.com\", \"firstName\": \"ibra\", \"lastName\": \"dieng\", \"password\": \"password123\" }";

 // 2. Configuration des mocks
 when(userRepository.existsByEmail(any())).thenReturn(true); // Simule un email existant

 // 3. Exécution de la requête et vérifications
 mockMvc
   .perform(
     MockMvcRequestBuilders
       .post("/api/auth/register")
       .content(requestBody)
       .contentType("application/json")
   )
   .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Vérifie le status 400
   .andExpect(
     MockMvcResultMatchers
       .jsonPath("$.message")
       .value("Error: Email is already taken!") // Vérifie le message d'erreur
   );
}
}