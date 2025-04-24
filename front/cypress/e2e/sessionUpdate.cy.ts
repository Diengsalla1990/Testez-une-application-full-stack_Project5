describe('Session Update spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCallAll');

    cy.intercept('GET', '/api/session/1', {
      fixture: 'session.json',
    }).as('sessionsApiCallOne');

    cy.intercept('GET', '/api/teacher', {
      fixture: 'teachers.json',
    }).as('teacherApiCallAll');

    cy.intercept('PUT', '/api/session/1', {
      body: {},
    }).as('sessionApiCallPut');
  });

  const loginUrl = '/login';
  const sessionUrl = '/sessions';
  const sessionUpdateUrl = '/sessions/update/1';

  const login = () => {
    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('input[formControlName=password]').type(
      `${'password'}{enter}{enter}`
    );
  };

  const connectAsAdmin = () => {
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

  const navigateOnSessionEditPage = () => {
    connectAsAdmin();

    cy.get('mat-card.item')
      .first()
      .find('button.mat-raised-button')
      .eq(1)
      .click();

    cy.url().should('include', sessionUpdateUrl);
  };

  const type = (name: string, date: string, description: string) => {
    cy.get('input[formControlName=name]').type(name);
    cy.get('input[formControlName=date]').type(date);
    cy.get('textarea[formControlName=description]').type(description);
  };

  const submitShouldBeDisable = () => {
    cy.get('button.mat-raised-button.mat-primary[type="submit"]')
      .should('exist')
      .should('be.disabled');
  };

  const submit = () => {
    cy.get('button.mat-raised-button.mat-primary[type="submit"]').click();
  };

  const shouldHaveFormInitCorrect = () => {
    // cy.get('input[formControlName=name]').contains('Session 1').should('exist');
    cy.get('input[formControlName=name]')
      .should('exist')
      .invoke('val')
      .should('eq', 'Session 1');
    cy.get('input[formControlName=date]')
      .should('exist')
      .invoke('val')
      .should('eq', '2024-01-25');
    cy.get('textarea[formControlName=description]')
      .should('exist')
      .invoke('val')
      .should('eq', 'Prepare yourself');
    cy.get('mat-select[formcontrolname="teacher_id"]')
      .should('exist')
      .contains('firstName lastName');
  };

  const emptyTextFields = () => {
    cy.get('input[formControlName=name]').clear();
    cy.get('input[formControlName=date]').clear();
    cy.get('textarea[formControlName=description]').clear();
  };

  const invalidTextUpdateSessionForm = () => {
    emptyTextFields();
    cy.get('mat-form-field.ng-invalid').should('have.length', 3);
    submitShouldBeDisable();
  };

  const validDropdownSelectionUpdateSessionForm = () => {
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').should('have.length', 3);
    cy.get('mat-option').eq(1).click();
    cy.contains('span.mat-select-min-line', 'firstName11 lastName11').should(
      'exist'
    );
  };

  const shouldHaveValidSessionUpdatePage = () => {
    navigateOnSessionEditPage();
  };

  const shouldHaveUpdadeFormInitWithSessionData = () => {
    navigateOnSessionEditPage();
    shouldHaveFormInitCorrect();
  };

  const shouldHaveIncorrectFormAfterDataClear = () => {
    navigateOnSessionEditPage();
    emptyTextFields();
    invalidTextUpdateSessionForm();
  };

  const shouldUpdateSessionAndRedirectUserAndUseSnackBar = () => {
    shouldHaveIncorrectFormAfterDataClear();
    type('Session Updated', '2030-01-01', 'Description Updated');
    validDropdownSelectionUpdateSessionForm();

    submit();
    cy.url().should('include', sessionUrl);

    cy.contains(
      'span.mat-simple-snack-bar-content',
      'Session updated !'
    ).should('exist');

    cy.wait(4000);

    cy.contains(
      'span.mat-simple-snack-bar-content',
      'Session created !'
    ).should('not.exist');
  };

  const shouldHaveCorrectDataOnUpdateSessionApiCall = () => {
    shouldUpdateSessionAndRedirectUserAndUseSnackBar();

    cy.wait('@sessionApiCallPut').then((interception) => {
      expect(interception.request.body).to.have.property('date', '2030-01-01');
      expect(interception.request.body).to.have.property(
        'description',
        'Description Updated'
      );
      expect(interception.request.body).to.have.property(
        'name',
        'Session Updated'
      );
      expect(interception.request.body).to.have.property('teacher_id', 11);
    });
  };

  it('vue de la page de la session de mise à jour', shouldHaveValidSessionUpdatePage);
  it(
    'La page de mise à jour de la session doit avoir un formulaire initialisation correct',
    shouldHaveUpdadeFormInitWithSessionData
  );
  it(
    'devrait avoir des champs de formulaire de texte incorrects',
    shouldHaveIncorrectFormAfterDataClear
  );
  it(
    'devrait mettre à jour la session et rediriger User, ouvrir la barre de collation avec le message puis la fermer',
    shouldUpdateSessionAndRedirectUserAndUseSnackBar
  );
  it(
    'devrait avoir des données correctes dans la demande UPDATE à API',
    shouldHaveCorrectDataOnUpdateSessionApiCall
  );
  
});
