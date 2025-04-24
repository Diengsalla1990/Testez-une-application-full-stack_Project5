package com.openclassrooms.starterjwt.models;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

//Déclare que cette classe est une entité JPA (persistée en base de données)
@Entity
//Spécifie la table SQL "USERS" et une contrainte d'unicité sur la colonne email
@Table(name = "USERS", uniqueConstraints = {
 @UniqueConstraint(columnNames = "email")
})
//Annotations Lombok pour générer automatiquement le code boilerplate
@Data // Génère getters/setters, equals, hashCode, toString
@Accessors(chain = true) // Permet le chaînage des setters (style builder)
@EntityListeners(AuditingEntityListener.class) // Active l'audit automatique des dates
@EqualsAndHashCode(of = {"id"}) // Génère equals/hashCode basé uniquement sur l'id
@Builder // Génère un builder pattern pour la classe
@NoArgsConstructor // Génère un constructeur sans arguments
@RequiredArgsConstructor // Génère un constructeur avec les champs @NonNull
@AllArgsConstructor // Génère un constructeur avec tous les champs
@ToString // Génère la méthode toString()
public class User {

// Identifiant unique généré automatiquement par la base de données
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

// Email de l'utilisateur avec contraintes :
@NonNull // Ne peut pas être null (Lombok)
@Size(max = 50) // Taille max de 50 caractères
@Email // Doit être un email valide
private String email;

// Nom de famille avec contraintes :
@NonNull
@Size(max = 20)
@Column(name = "last_name") // Mappe vers la colonne last_name en base
private String lastName;

// Prénom avec contraintes :
@NonNull
@Size(max = 20)
@Column(name = "first_name") // Mappe vers first_name en base
private String firstName;

// Mot de passe (devrait être hashé en pratique) :
@NonNull
@Size(max = 120) // Taille large pour stocker un hash
private String password;

// Indique si l'utilisateur a des droits admin :
@NonNull
private boolean admin;

// Date de création automatiquement gérée :
@CreatedDate
@Column(name = "created_at", updatable = false) // Non modifiable après création
private LocalDateTime createdAt;

// Date de mise à jour automatiquement gérée :
@UpdateTimestamp
@Column(name = "updated_at") // Mise à jour automatique à chaque modification
private LocalDateTime updatedAt;
}