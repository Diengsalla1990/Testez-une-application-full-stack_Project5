package com.openclassrooms.starterjwt.unit.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;

@SpringBootTest
public class UserTest {
	// Données de test communes
    private Long testUserId = 1L;
    private LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1,1,1,1,1); // Date fixe pour les tests
    private final Validator validator = Validation // Validateur pour les tests de validation
            .buildDefaultValidatorFactory()
            .getValidator();
    
    private User testUser; // Instance de User réutilisable
    
    // Méthode exécutée avant chaque test pour initialiser les données
	@BeforeEach
	  public void setUp() {
	    testUser =
	      User
	        .builder()
	        .id(testUserId)
	        .firstName("ibra")
	        .lastName("dieng")
	        .email("user@test.com")
	        .admin(false)
	        .password("password")
	        .createdAt(fixedDateTime)
	        .updatedAt(fixedDateTime)
	        .build();
	  }
	
	/**
	 * Test unitaire pour vérifier la construction correcte d'un objet User via le builder Lombok.
	 * Contrôle que tous les champs sont correctement initialisés.
	 */
	@Test
	public void testUserModel() {
		User userUnderTest = User
			      .builder()
			      .id(testUserId)
			      .firstName("ibra")
			      .lastName("dieng")
			      .email("user@test.com")
			      .admin(false)
			      .password("password")
			      .createdAt(fixedDateTime)
			      .updatedAt(fixedDateTime)
			      .build();

			    assertThat(userUnderTest.getId()).isEqualTo(1L);
			    assertThat(userUnderTest.getFirstName()).isEqualTo("ibra");
			    assertThat(userUnderTest.getLastName()).isEqualTo("dieng");
			    assertThat(userUnderTest.getEmail()).isEqualTo("user@test.com");
			    assertThat(userUnderTest.isAdmin()).isFalse();
			    assertThat(userUnderTest.getPassword()).isEqualTo("password");
			    assertThat(userUnderTest.getCreatedAt()).isEqualTo(fixedDateTime);
			    assertThat(userUnderTest.getUpdatedAt()).isEqualTo(fixedDateTime);
		
	}
	
	/**
	 * Test unitaire pour vérifier le bon fonctionnement :
	 * - Des setters standards
	 * - Des différents constructeurs (no-args, partiel et complet)
	 * Vérifie que les instances sont correctement créées.
	 */
	@Test
	  public void testeUserModel_SettersEtConstructorsMethods() {
	    User userUnderTest = new User();
	    userUnderTest.setAdmin(true);
	    userUnderTest.setCreatedAt(fixedDateTime);
	    userUnderTest.setEmail("email");
	    userUnderTest.setFirstName("firstname");
	    userUnderTest.setId(testUserId);
	    userUnderTest.setLastName("lastname");
	    userUnderTest.setPassword("password");
	    userUnderTest.setUpdatedAt(fixedDateTime);

	    User userUnderTest2 = new User(
	      "email",
	      "lastName",
	      "firstName",
	      "password",
	      false
	    );
	    User userUnderTest3 = new User(
	      testUserId,
	      "email",
	      "lastName",
	      "firstName",
	      "password",
	      false,
	      fixedDateTime,
	      fixedDateTime
	    );

	    assertThat(userUnderTest).isInstanceOf(User.class);
	    assertThat(userUnderTest2).isInstanceOf(User.class);
	    assertThat(userUnderTest3).isInstanceOf(User.class);
	  }
	
	
	/**
	 * Test unitaire pour valider les contraintes de validation (@Size, etc.).
	 * Vérifie que les violations sont bien détectées quand :
	 * - Les champs firstName et lastName dépassent la taille maximale autorisée.
	 */
	
	@Test
	  @SuppressWarnings("ConstantConditions")
	  public void testeUserModel_Avec_ChampsVide() {
	    User userUnderTest = User
	      .builder()
	      .id(testUserId)
	      // Dépasse @Size(max=20)
	      .firstName(
	        "firstNameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
	      )
	     // Dépasse @Size(max=20)
	      .lastName(
	        "lastNameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
	      )
	      .email("user@test.com")
	      .admin(false)
	      .password("password")
	      .createdAt(fixedDateTime)
	      .updatedAt(fixedDateTime)
	      .build();

	    Set<ConstraintViolation<User>> violations = validator.validate(
	      userUnderTest
	    );

	    assertThat(violations).isNotEmpty();
	  }

	/**
	 * Test unitaire pour vérifier le format de la méthode toString().
	 * Contrôle que la représentation textuelle correspond exactement au format attendu.
	 */
	
	@Test
	  public void testeUserModel_ToString() {
	    User userUnderTest = testUser;

	    String expectedToString =
	      "User(id=" +
	      testUserId +
	      ", email=user@test.com, lastName=dieng, firstName=ibra, password=password, admin=false, createdAt=" +
	      fixedDateTime +
	      ", updatedAt=" +
	      fixedDateTime +
	      ")";

	    assertEquals(expectedToString, userUnderTest.toString());
	  }
	
	
	/**
	 * Test unitaire pour vérifier le comportement des méthodes equals() et hashCode().
	 * Contrôle que :
	 * - Deux instances avec le même ID sont considérées égales
	 * - Deux instances avec des IDs différents sont considérées différentes
	 * - Le hashcode est cohérent avec la méthode equals()
	 */
	@Test
	  public void testeEqualsEtHashCode() {
	    User userUnderTest1 = testUser;
	    User userUnderTest2 = testUser;
	    User userUnderTest3 = User
	      .builder()
	      .id(55L)
	      .firstName("fatou")
	      .lastName("dieng")
	      .email("user2@test.com")
	      .admin(true)
	      .password("password2")
	      .createdAt(fixedDateTime)
	      .updatedAt(fixedDateTime)
	      .build();

	    assertEquals(userUnderTest1, userUnderTest2);
	    assertNotEquals(userUnderTest1, userUnderTest3);

	    assertEquals(userUnderTest1.hashCode(), userUnderTest2.hashCode());
	    assertNotEquals(userUnderTest1.hashCode(), userUnderTest3.hashCode());
	  }


}
