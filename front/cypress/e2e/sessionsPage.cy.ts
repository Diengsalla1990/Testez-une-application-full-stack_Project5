describe('Sessions Page spec', () => {
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

  const connectAsNotAdmin = () => {
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

  const sessionsPageViewOnAdmin = () => {
    connectAsAdmin();

    cy.get('button[routerlink="create"]').should('exist');
    cy.get('span:contains("edit")').should('exist');
    cy.get('mat-card.item').should('have.length', 3);
  };

  const sessionsPageViewOnNotAdmin = () => {
    connectAsNotAdmin();

    cy.get('button[routerlink="create"]').should('not.exist');
    cy.get('span:contains("edit")').should('not.exist');
    cy.get('mat-card.item').should('have.length', 3);
  };

  it('Afficher la page de session sur Admin', sessionsPageViewOnAdmin);
  it('Afficher la page de session sur Not Admin', sessionsPageViewOnNotAdmin);
});
