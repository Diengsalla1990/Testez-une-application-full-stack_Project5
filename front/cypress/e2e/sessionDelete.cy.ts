describe('Session Delete spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session/1', {
      fixture: 'session.json',
    }).as('sessionsApiCallOne');

    cy.intercept('GET', '/api/teacher/10', {
      fixture: 'teacher.json',
    }).as('teacherApiCallOne');
  });

  const loginUrl = '/login';
  const sessionUrl = '/sessions';
  const sessionDetailUrl = '/sessions/detail/1';

  const login = () => {
    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('input[formControlName=password]').type(
      `${'password'}{enter}{enter}`
    );
  };

  const connectDeAdmin = () => {
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

  const NaviguerSurPageDetailSession = () => {
    cy.get('mat-card.item')
      .first()
      .find('button.mat-raised-button')
      .first()
      .click();

    cy.url().should('include', sessionDetailUrl);
  };

  const sessionDetailPageViewOnAdmin = () => {
    connectDeAdmin();
    NaviguerSurPageDetailSession();
  };

  const VerifieSiDeleteButtonEnvoiDeleteApiRequest = () => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCallAll');

    cy.intercept('DELETE', '/api/session/1', {
      fixture: 'teacher.json',
    }).as('teacherApiCallOne');

    sessionDetailPageViewOnAdmin();
    cy.contains('span.ml1', 'Delete').click();

    cy.url().should('include', sessionUrl);

    cy.contains(
      'span.mat-simple-snack-bar-content',
      'Session deleted !'
    ).should('exist');

    cy.wait(4000);

    cy.contains(
      'span.mat-simple-snack-bar-content',
      'Session deleted !'
    ).should('not.exist');
  };

  it(
    'Delete button devrait supprimer la session',
    VerifieSiDeleteButtonEnvoiDeleteApiRequest
  );
});
