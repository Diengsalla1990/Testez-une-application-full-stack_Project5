describe('Session Participate spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCallAll');

    cy.intercept('GET', '/api/session/1', {
      fixture: 'session.json',
    }).as('sessionsApiCallOne');

    cy.intercept('GET', '/api/teacher/10', {
      fixture: 'teacher.json',
    }).as('teacherApiCallOne');

    cy.intercept('POST', '/api/session/1/participate/1', {
      body: {},
    });

    cy.intercept('DELETE', '/api/session/1/participate/1', {
      body: {},
    });
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

  const connectDeNotAdmin = () => {
    cy.visit(loginUrl);

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
    cy.url().should('include', sessionUrl);
  };

  const navigateOnSessionDetailPage = () => {
    cy.get('mat-card.item')
      .first()
      .find('button.mat-raised-button')
      .first()
      .click();

    cy.url().should('include', sessionDetailUrl);
  };

  const sessionDetailPageViewOnNotAdmin = () => {
    connectDeNotAdmin();
    navigateOnSessionDetailPage();
  };

  const participateToSession = () => {
    sessionDetailPageViewOnNotAdmin();
    cy.contains('span.ml1', 'Participate').click();
  };

  const checkIfParticipationTriggerParticipateButtonChange = () => {
    cy.intercept('GET', '/api/session/1', {
      fixture: 'sessionParticipate.json',
    }).as('sessionsApiCallOneUserParticipate');

    sessionDetailPageViewOnNotAdmin();
    cy.contains('span.ml1', 'Participate').should('not.exist');
    cy.contains('span.ml1', 'Do not participate').should('exist').click();
  };

  it('Devrait participer à la session', participateToSession);
  it(
    'Devrait avoir un bouton de désinscription si User participe',
    checkIfParticipationTriggerParticipateButtonChange
  );
});
