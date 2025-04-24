# Yoga App Spring Boot API Teste Projet

Ce référentiel contient une API Backend Spring Boot pour le projet de test d'applications Yoga, implémentant des suites de tests avec JUnit5, AssertJ et Mockito.

## Conditions prealables

Avant de commencer à configurer l'API Spring Boot de l'application Yoga, assurez-vous que votre système satisfait aux conditions préalables suivantes :

- **Java Development Kit (JDK):** Téléchargez et installez la dernière version du [JDK](https://adoptopenjdk.net/) adapté à votre plateforme.

- **Apache Maven:** Ce projet s'appuie sur [Maven](https://maven.apache.org/) Pour gérer les dépendances et la construction. Installez Maven pour faciliter la configuration et la maintenance du projet.

- **MySQL:** Ce projet s'appuie sur une base de données MySQL. Si vous n'avez pas encore installé MySQL, consultez le guide d'installation [ici](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/).

Assurez-vous que ces conditions préalables sont remplies avant de procéder aux étapes de configuration.

## Configuration Settings

### 1. Java

**Configuration de la variable d'environnement Java :**

Assurez-vous que la variable d'environnement Java est correctement configurée sur votre système. Cette variable est essentielle au bon fonctionnement des applications Java. Suivez les étapes correspondant à votre système d'exploitation :

#### Windows:

1. Ouvrez les propriétés système.
2. Accédez à l’onglet « Avancé ».
3. Cliquez sur le bouton « Variables d’environnement ».
4. Sous « Variables système »,faites défiler vers le bas pour trouver la variable « Chemin » et cliquez sur « Modifier ».
5. Ajoutez le chemin vers le répertoire binaire de votre JDK (e.g., `C:\Program Files\Java\jdk[VERSION]\bin`) à la liste de valeurs. Assurez-vous de la séparer des autres entrées par un point-virgule.
6. Cliquez sur « OK » pour enregistrer vos modifications et exécutez la commande suivante :

```shell
java -version
```

Vous devriez voir des informations sur la version Java installée.

### 2. MySQL Database

Si vous cherchez de l'inspiration pour le nom de la base de données, pensez à utiliser « yoga », c'est mon choix !

4. Exécutez le fichier SQL `script.sql` qui se trouve dans le dossier ressources situé dans le dossier racine du Front/Back application (/Testez-une-application-full-stack/ressources/sql).

```sql
source C:\folder\...\Testez-une-application-full-stack\ressources\sql\script.sql
```

## Installation Guide

**Clonage du projet:**

1. Cloner ce dépôt depuis GitHub: `git clone https://github.com/Mickael-Klein/OpenClassRooms-Dev-FullStack-Projet_5.git`

**Configurer les variables d'environnement dans le fichier application.properties**

### 2. Configuring the `application.properties` file:

Le projet inclut un fichier « application.properties » situé dans le dossier « src/main/resources/ ». Voici les éléments de variables d'environnement à configurer :

```properties
# Data source configuration (MySQL)
spring.datasource.url=jdbc:mysql://localhost:{port}/{dbName}?allowPublicKeyRetrieval=true
spring.datasource.username={dbUsername}
spring.datasource.password={dbPassword}
```

- Le `spring.datasource.url` MySQL par défaut `{port}` est toujours `3306`.
- Le `spring.datasource.url` MySQL `{dbName}` est le nom de la base de données que vous avez créée lors des paramètres de configuration pour MySQL.
- Le `spring.datasource.username` et `spring.datasource.password` sont vos informations d'identification MySQL.

```properties
# Secret key for JWT (Json Web Token)
oc.app.jwtSecret={jwtKey}
```

La clé « jwtKey » doit contenir votre clé de chiffrement JWT, qui sera utilisée à des fins d'authentification. Utilisez une clé robuste de 256 bits et préservez sa confidentialité.

3. Démarer votre application en utilisant ton IDE .

4. Vous pouvez également utiliser Postman pour tester les appels API. La collection Postman se trouve dans le `Testez-une-application-full-stack/ressources/postman` dossier. (vous devrez utiliser un jeton porteur qui sera renvoyé après un appel de connexion réussi à l'API, puis l'utiliser pour toutes les autres demandes aux points de terminaison de l'API)

## Architecture du Projet

Le projet adhère à une architecture en couches conventionnelle (Controller/Service/Java Persistence API Repository) afin de garantir la modularité et la maintenabilité de la base de code, conformément aux meilleures pratiques du secteur. Tous les tests sont disponibles dans le `src/test/java/com/openclassrooms` dossier.

```
├───main
│   ├───java
│   │   └───com
│   │       └───openclassrooms
│   │           └───starterjwt
│   │               ├───controllers
│   │               ├───dto
│   │               ├───exception
│   │               ├───mapper
│   │               ├───models
│   │               ├───payload
│   │               │   ├───request
│   │               │   └───response
│   │               ├───repository
│   │               ├───security
│   │               │   ├───jwt
│   │               │   └───services
│   │               └───services
│   └───resources
└───test
    └───java
        └───com
            └───openclassrooms
                └───starterjwt
                    ├───integration
                    └───unit
                        ├───controllers
                        ├───mapper
                        ├───models
                        ├───payload
                        │   ├───request
                        │   └───response
                        ├───security
                        │   ├───jwt
                        │   └───services
                        └───services
```

## Securité et Authentification

L'authentification est gérée par Spring Security avec JWT. Toutes les routes nécessitent une authentification, sauf celles liées à la création ou à la connexion d'un compte. Les mots de passe sont cryptés pour garantir leur stockage sécurisé dans la base de données.

## Teste

Pour lancer et générer le code jacoco coverage:
`mvn clean test`

Ensuite, un rapport de couverture complet peut être trouvé dans le fichier index.html sous: `Testez-une-application-full-stack\back\target\site\jacoco\index.html`. Vous pouvez lancer `live server` pour voir les résultats dans le navigateur Web.

Tout les Tests Unitaires dans  `Testez-une-application-full-stack\back\src\test\java\com\openclassrooms\starterjwt\unit`
Tout les Tests d'intégration dans `Testez-une-application-full-stack\back\src\test\java\com\openclassrooms\starterjwt\integration`
