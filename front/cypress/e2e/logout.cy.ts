describe('Logout spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCall');
  });

  const loginUrl = '/login';
  const sessionUrl = '/sessions';

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

  const shouldLogout = () => {
    connectAsAdmin();
    cy.contains('span.link', 'Logout').click();

    cy.url().should('eq', 'http://localhost:4200/');
  };

  it('should logout and redirect', shouldLogout);
});
