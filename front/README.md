# Yoga Application Angular Frontend Projet de Teste

Ce référentiel contient un frontend angulaire pour le projet de test d'applications Yoga, implémentant des suites de tests avec Jest et Cypress.
Ce projet a été généré avec [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.


## Conditions préalables

### 1. Node Package Manager (NPM)

**Installée NPM:**

Avant d'utiliser l'interface Yoga Angular, assurez-vous que Node Package Manager (NPM) est installé sur votre système. NPM est essentiel pour gérer les dépendances et exécuter les scripts. Suivez les étapes ci-dessous :

1. Telecharger et installe [Node.js](https://nodejs.org/).
2. Confirmez l’installation réussie en exécutant les commandes suivantes dans votre terminal ou votre invite de commande :

```shell
node -v
npm -v
```

Vous devriez voir des versions pour Node.js et NPM.

### 2. Angular CLI

**Installing Angular CLI:**

Angular CLI est une interface de ligne de commande pour les applications Angular. Installez la dernière version globalement avec la commande suivante :

```shell
npm install -g @angular/cli
```

Confirmez l'installation réussie en exécutant :

```shell
ng --version
```

Vous devriez voir des informations sur la version Angular CLI installée

### 3. Jest

**Installing Jest:**

Jest est un framework de test utilisé pour les tests unitaires dans ce projet. Installez Jest globalement avec la commande suivante :

```shell
npm install -g jest
```

Confirmez l'installation réussie en exécutant :

```shell
jest --version
```

Creer deux fichiers de configuration jest.integration.config.js et jest.unit.config.js pour séparer les tests unitaires et d'intégration


You should see information about the installed Angular CLI version

## Guide D'installation

**Cloner le projet:**

1. Clone this repository from GitHub: `git clone https://github.com/Mickael-Klein/OpenClassRooms-Dev-FullStack-Projet_5.git`

2. Naviger sur le dossier front

```shell
cd front
```

3. Installé les dependences

```shell
npm install
```

**Ce projet fonctionne avec l'API fournie dans la partie Backend de l'application, n'oubliez pas de l'installer et de l'exécuter avant d'exécuter le Frontend.**

4. Frontend de lancement

```shell
npm start
```

## Architecture du Projet

```
├───app
│   ├───components
│   │   ├───me
│   │   └───not-found
│   ├───features
│   │   ├───auth
│   │   │   ├───components
│   │   │   │   ├───login
│   │   │   │   └───register
│   │   │   ├───interfaces
│   │   │   └───services
│   │   └───sessions
│   │       ├───components
│   │       │   ├───detail
│   │       │   ├───form
│   │       │   └───list
│   │       ├───interfaces
│   │       └───services
│   ├───guards
│   ├───interceptors
│   ├───interfaces
│   └───services
├───assets
└───environments
```

## Tests

### Tests unitaires et d'intégration

#### Exécution des tests

##### Exécution de Tout les tests

Pour exécuter tous les tests, exécutez la commande suivante :

```shell
npm run test:all
```

##### Exécution des tests Unitaires

Pour exécuter les tests unitaires, exécutez la commande suivante :

```shell
npm run test:unit  
```

##### Exécution des tests d'intégrations

Pour exécuter les tests d'intégrations, exécutez la commande suivante :

```shell
npm run test:integration  
```

#### Couverture des tests

##### Couverture de tout les Tests

Pour obtenir un résumé de tout les tests de couverture, exécutez la commande suivante :

```shell
jest --coverage
```

##### Couverture des Tests Unitaires

Pour obtenir un résumé de tout les tests Unitaires de couverture, exécutez la commande suivante :

```shell
npm run test:coverage:unit
```

##### Couverture des Tests d'intégrations

Pour obtenir un résumé des tests d'intégration de couverture, exécutez la commande suivante :

```shell
npm run test:coverage:integration
```

### E2E

#### Exécution de tests E2E

Pour exécuter des tests E2E, exécutez la commande suivante :

```shell
npm run e2e
```

Cypress lancera un serveur de développement. Vous devrez ensuite sélectionner un navigateur pour sélectionner et exécuter les tests.
Le dossier des tests E2E est : `Testez-une-application-full-stack\front\cypress\e2e`.

#### Couverture des tests E2E

**Compte tenu de la façon dont la couverture Cypress est configurée et fonctionne, vous devrez exécuter des tests E2E avant**
Pour générer un rapport de couverture, exécutez la commande suivante :

```shell
npm run e2e:coverage
```

Le rapport de test unitaire est disponible sous forme de document HTML ici`Testez-une-application-full-stack-master/front/coverage/unit/lcov-report/index.html`. Vous pouvez le voir en live avec`live server` Par exemple.

Le rapport de test unitaire est disponible sous forme de document HTML ici`Testez-une-application-full-stack-master/front/coverage/integration/lcov-report/index.html`. Vous pouvez le voir en live avec`live server` Par exemple.
