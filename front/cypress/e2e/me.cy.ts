// Définit une suite de tests pour la page "Me/Account"
describe('Me / Account spec', () => {
  // Avant chaque test, intercepte les requêtes GET vers /api/session
  // et renvoie les données du fichier sessions.json
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCall');
  });

  // Définit les URLs utilisées dans les tests
  const loginUrl = '/login';
  const sessionUrl = '/sessions';

  // Fonction helper pour effectuer une connexion avec des identifiants par défaut
  const login = () => {
    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('input[formControlName=password]').type(
      `${'password'}{enter}{enter}`
    );
  };

  // Fonction pour se connecter en tant qu'administrateur
  const SeConnecterEnTantQueAdmin = () => {
    cy.visit(loginUrl);

    // Intercepte la requête POST de login et renvoie une réponse mockée d'admin
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    login();
    // Vérifie que la redirection vers la page des sessions a bien eu lieu
    cy.url().should('include', sessionUrl);
  };

  // Fonction pour se connecter en tant qu'utilisateur non-admin
  const SeConnecterEnTantQueNoAdmin = () => {
    cy.visit(loginUrl);

    // Intercepte la requête POST de login et renvoie une réponse mockée de non-admin
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false,
      },
    });

    login();
    // Vérifie que la redirection vers la page des sessions a bien eu lieu
    cy.url().should('include', sessionUrl);
  };

  // Fonction pour vérifier l'affichage des données utilisateur
  const VerifAffichageDonnéUser = () => {
    // Vérifie l'affichage du nom
    cy.contains('p', 'Name: firstName LASTNAME').should('exist');
    // Vérifie l'affichage de l'email
    cy.contains('p', 'Email: email@test.com').should('exist');
    // Vérifie l'affichage de la date de création
    cy.contains('p', 'Create at: April 19, 2024').should('exist');
    // Vérifie l'affichage de la date de mise à jour
    cy.contains('p', 'Last update: April 19, 2024').should('exist');
  };

  // Fonction pour vérifier l'affichage spécifique aux admins
  const adminGetAdminMessageDisplay = () => {
    // Vérifie la présence du message "You are admin"
    cy.contains('p.my2', 'You are admin').should('exist');

    // Vérifie l'absence des éléments de suppression de compte
    cy.contains('p', 'Delete my account:').should('not.exist');
    cy.contains('button.mat-raised-button.mat-warn', 'Detail').should(
      'not.exist'
    );
  };

  // Fonction pour vérifier l'affichage spécifique aux non-admins
  const nonAdminGetDeleteAccountButton = () => {
    // Vérifie la présence des éléments de suppression de compte
    cy.contains('p', 'Delete my account:').should('exist');
    cy.contains('button.mat-raised-button.mat-warn', 'Detail').should('exist');

    // Vérifie l'absence du message admin
    cy.contains('p.my2', 'You are admin').should('not.exist');
  };

  // Fonction pour afficher la page d'un compte admin
  const PageAffichageDeAdmin = () => {
    // Intercepte la requête GET des infos utilisateur et renvoie des données mockées d'admin
    cy.intercept('get', '/api/user/1', {
      body: {
        id: 1,
        email: 'email@test.com',
        lastName: 'lastName',
        firstName: 'firstName',
        admin: true,
        password: 'password',
        createdAt: '2025-04-19',
        updatedAt: '2025-04-19',
      },
    });

    // Se connecte en tant qu'admin et clique sur le lien "Account"
    SeConnecterEnTantQueAdmin();
    cy.contains('span.link[routerlink="me"]', 'Account').click();
  };

  // Fonction pour afficher la page d'un compte non-admin
  const PageAffichageDeNoAdmin = () => {
    // Intercepte la requête GET des infos utilisateur et renvoie des données mockées de non-admin
    cy.intercept('get', '/api/user/1', {
      body: {
        id: 1,
        email: 'email@test.com',
        lastName: 'lastName',
        firstName: 'firstName',
        admin: false,
        password: 'password',
        createdAt: '2025-04-19',
        updatedAt: '2025-04-19',
      },
    });

    // Se connecte en tant que non-admin et clique sur le lien "Account"
    SeConnecterEnTantQueNoAdmin();
    cy.contains('span.link[routerlink="me"]', 'Account').click();
  };

  // Fonction pour tester l'affichage complet d'un compte admin
  const AffichageCorrectSurDonnéeAdmin = () => {
    PageAffichageDeAdmin();
    VerifAffichageDonnéUser();
    adminGetAdminMessageDisplay();
  };

  // Fonction pour tester l'affichage complet d'un compte non-admin
  const AffichageCorrectSurDonnéeNoAdmin = () => {
    PageAffichageDeNoAdmin();
    VerifAffichageDonnéUser();
    nonAdminGetDeleteAccountButton();
  };

  // Tests exécutés
  it('Afficher la page du compte sur Admin', PageAffichageDeAdmin);
  it('Afficher la page du compte sur Non Admin', PageAffichageDeNoAdmin);
  it('devrait avoir un affichage des données de User Admin', AffichageCorrectSurDonnéeAdmin);
  it('devrait avoir un affichage des données User non Admin', AffichageCorrectSurDonnéeNoAdmin);
});