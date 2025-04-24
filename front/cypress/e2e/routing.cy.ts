describe('Routing spec', () => {
  const sessionsUrl = '/sessions';
  const loginUrl = '/login';

  it('devrait rediriger sur la page de connexion pour un utilisateur non connecté', () => {
    cy.visit(sessionsUrl);

    cy.url().should('include', loginUrl);
  });
});
