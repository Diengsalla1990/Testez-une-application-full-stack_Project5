describe('Menu spec', () => {
  const loginUrl = '/login';

  const login = () => {
    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('input[formControlName=password]').type(
      `${'password'}{enter}{enter}`
    );
  };

  const menuIfNotConnected = () => {
    cy.visit(loginUrl);

    cy.get('span.link:contains("Login")').should(
      'have.attr',
      'routerLink',
      'login'
    );
    cy.get('span.link:contains("Register")').should(
      'have.attr',
      'routerLink',
      'register'
    );

    cy.get('span.link:contains("Sessions")').should('not.exist');
  };

  const menuIfConnected = () => {
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

    cy.url().should('include', '/sessions');

    cy.get('span.link:contains("Sessions")').should(
      'have.attr',
      'routerLink',
      'sessions'
    );
    cy.get('span.link:contains("Account")').should(
      'have.attr',
      'routerLink',
      'me'
    );
    cy.get('span.link:contains("Logout")').should('exist');

    cy.get('span.link:contains("Login")').should('not.exist');
  };

  it('Menu if not connected', menuIfNotConnected);
  it('Menu if connected', menuIfConnected);
});
