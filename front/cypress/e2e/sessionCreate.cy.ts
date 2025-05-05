describe('Sessions Create spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCallGet');

    cy.intercept('GET', '/api/teacher', {
      fixture: 'teachers.json',
    }).as('teacherApiCallAll');

    cy.intercept('POST', '/api/session', {
      fixture: 'session.json',
    }).as('sessionApiCallPost');
  });

  const loginUrl = '/login';
  const sessionUrl = '/sessions';
  const createSessionUrl = '/sessions/create';

  const login = () => {
    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('input[formControlName=password]').type(
      `${'password'}{enter}{enter}`
    );
  };

  const connectEtAdmin = () => {
    cy.visit(loginUrl);

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
    cy.url().should('include', sessionUrl);
  };

  const affichagePageSessionDeAdmin = () => {
    connectEtAdmin();
    cy.get('mat-card.item').should('have.length', 3);
  };

  const creationAffichageSession = () => {
    affichagePageSessionDeAdmin();

    cy.get('button[routerlink="create"]').click();
    cy.url().should('include', createSessionUrl);
  };

  const CliquerSurElemFormulaire = () => {
    cy.get('input[formControlName=name]').click();
    cy.get('input[formControlName=date]').click();
    cy.get('textarea[formControlName=description]').click();
    cy.get('input[formControlName=name]').click();
  };

  const type = (name: string, date: string, description: string) => {
    cy.get('input[formControlName=name]').type(name);
    cy.get('input[formControlName=date]').type(date);
    cy.get('textarea[formControlName=description]').type(description);
  };

  const submitDoitEtreDesactive = () => {
    cy.get('button.mat-raised-button:contains("Save")')
      .should('exist')
      .should('be.disabled');
  };

  const submit = () => {
    cy.get('button.mat-raised-button:contains("Save")').click();
  };

  const DevraitAvoirFormInitCorrect = () => {
    cy.get('input[formControlName=name]').should('exist');
    cy.get('input[formControlName=date]').should('exist');
    cy.get('textarea[formControlName=description]').should('exist');
    cy.get('mat-select[formcontrolname="teacher_id"]').should('exist');

    submitDoitEtreDesactive();
  };

  const FormulaureTextInvalide = () => {
    CliquerSurElemFormulaire();
    cy.get('mat-form-field.ng-invalid').should('have.length', 4);
    submitDoitEtreDesactive();
  };

  const FormulaireTextValide = () => {
    type('name', '2025-04-09', 'description');
    cy.get('mat-form-field.ng-invalid').should('have.length', 1);
    submitDoitEtreDesactive();
  };

  const SelectionDeroulantCorrecte = () => {
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').should('have.length', 3);
    cy.get('mat-option').first().click();
    cy.contains('span.mat-select-min-line', 'firstName lastName').should(
      'exist'
    );
  };

  const SoumissionFormCorrect = () => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessionsCreatedOne.json',
    }).as('sessionsApiCallGet');

    submit();
    cy.url().should('include', sessionUrl);

    cy.contains(
      'span.mat-simple-snack-bar-content',
      'Session created !'
    ).should('exist');

    cy.wait(4000);

    cy.contains(
      'span.mat-simple-snack-bar-content',
      'Session created !'
    ).should('not.exist');

    cy.get('mat-card.item').should('have.length', 4);
  };

  const sessionCreationDataCheckOnPostSessionApiCall = () => {
    creationAffichageSession();
    FormulaireTextValide();
    SelectionDeroulantCorrecte();
    SoumissionFormCorrect();

    cy.wait('@sessionApiCallPost').then((interception) => {
      expect(interception.request.body).to.have.property('date', '2025-04-09');
      expect(interception.request.body).to.have.property(
        'description',
        'description'
      );
      expect(interception.request.body).to.have.property('name', 'name');
      expect(interception.request.body).to.have.property('teacher_id', 10);
    });
  };

  it('créer une vue de page de session', creationAffichageSession);
  it(
    'La page Créer une session doit avoir un formulaire initialisation correct',
    DevraitAvoirFormInitCorrect
  );
  it('devrait avoir des champs de formulaire de texte incorrects', FormulaureTextInvalide);
  it('devrait avoir des champs de formulaire de texte corrects', FormulaireTextValide);
  it(
    'devrait avoir le nombre et le contenu corrects de option mat dans le menu déroulant de sélection de Teacher',
    SelectionDeroulantCorrecte
  );
  it('devrait soumettre et créer une session', SoumissionFormCorrect);
  it(
    'devrait avoir des données correctes dans la requête API POST de création de session',
    sessionCreationDataCheckOnPostSessionApiCall
  );
});
