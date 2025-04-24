// Définit une suite de tests pour la fonctionnalité de connexion
describe('Login spec', () => {
  // Définit l'URL de la page de connexion
  const loginUrl = '/login';

  // Fonction helper pour saisir email/mot de passe et soumettre le formulaire
  const typeEtSubmit = (email: string, password: string) => {
    // Sélectionne le champ email et y tape la valeur fournie
    cy.get('input[formControlName=email]').type(email);
    // Sélectionne le champ password, y tape la valeur et simule la touche Entrée (2 fois)
    cy.get('input[formControlName=password]').type(`${password}{enter}{enter}`);
  };

  // Fonction pour vérifier que le formulaire est invalide
  const AffirmerFormInvalide = () => {
    // Vérifie que le champ email a la classe ng-invalid
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    // Vérifie que le champ password a la classe ng-invalid
    cy.get('input[formControlName=password]').should(
      'have.class',
      'ng-invalid'
    );
    // Vérifie qu'un message d'erreur est affiché
    cy.get('.error').should('contain.text', 'An error occurred');
    // Vérifie que le bouton de soumission est désactivé
    cy.get('button[type="submit"]').should('be.disabled');
  };

  // Fonction pour vérifier que le formulaire est valide
  const AffirmerFormValide = () => {
    // Vérifie que le champ email a la classe ng-valid
    cy.get('input[formControlName=email]').should('have.class', 'ng-valid');
    // Vérifie que le champ password a la classe ng-valid
    cy.get('input[formControlName=password]').should('have.class', 'ng-valid');
    // Vérifie que le bouton de soumission est activé
    cy.get('button[type="submit"]').should('not.be.disabled');
  };

  // Test de connexion réussie
  const loginSuccessTest = () => {
    // Visite la page de connexion
    cy.visit(loginUrl);

    // Intercepte la requête POST de login et renvoie une réponse mockée
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    // Intercepte la requête GET pour les sessions et renvoie un tableau vide
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    // Remplit et soumet le formulaire avec des identifiants valides
    typeEtSubmit('yoga@studio.com', 'test!1234');

    // Vérifie que l'URL a changé vers la page des sessions
    cy.url().should('include', '/sessions');
  };

  // Test de formulaire invalide
  const invalidLoginFormTest = () => {
    // Visite la page de connexion
    cy.visit(loginUrl);

    // Intercepte la requête POST et renvoie une erreur 401
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { error: 'Invalid Credentials' },
    });

    // Test 1: Email trop court et password vide
    typeEtSubmit('te', '');
    // Vérifie que le formulaire est invalide
    AffirmerFormInvalide();

    // Test 2: Email sans domaine et password vide
    typeEtSubmit('sting', '');
    // Vérifie que le formulaire est invalide
    AffirmerFormInvalide();

    // Test 3: Email valide mais password trop court
    typeEtSubmit('@test.com', 'test');
    // Vérifie que le formulaire est valide (mais la soumission échouera)
    AffirmerFormValide();
  };

  // Test d'échec de connexion
  const TestConnexionEchec = () => {
    // Visite la page de connexion
    cy.visit(loginUrl);

    // Intercepte la requête POST et renvoie une erreur 401
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { error: 'Invalid Credentials' },
    });

    // Remplit et soumet le formulaire avec des identifiants (qui échoueront)
    typeEtSubmit('yoga@studio.com', 'test!1234');

    // Vérifie qu'un message d'erreur est affiché
    cy.get('.error').should('contain.text', 'An error occurred');
    // Vérifie qu'on est toujours sur la page de login
    cy.url().should('include', loginUrl);
  };

  // Test pour vérifier les données envoyées à l'API
  const VerifDataAPIConnexion = () => {
    // Visite la page de connexion
    cy.visit(loginUrl);

    // Intercepte la requête POST et renvoie un objet vide
    cy.intercept('POST', '/api/auth/login', {
      body: {},
    }).as('loginInterception');

    // Remplit et soumet le formulaire
    typeEtSubmit('yoga@studio.com', 'test!1234');

    // Attend que l'interception soit complète et vérifie les données envoyées
    cy.wait('@loginInterception').then((interception) => {
      // Vérifie que l'email est correct dans la requête
      expect(interception.request.body).to.have.property(
        'email',
        'yoga@studio.com'
      );
      // Vérifie que le password est correct dans la requête
      expect(interception.request.body).to.have.property(
        'password',
        'test!1234'
      );
    });
  };

  // Exécution des différents tests
  it('Données correctes dans appel API', VerifDataAPIConnexion);
  it('Connexion réussie', loginSuccessTest);
  it('Formulaire de connexion invalide', invalidLoginFormTest);
  it('Échec de connexion', TestConnexionEchec);
});