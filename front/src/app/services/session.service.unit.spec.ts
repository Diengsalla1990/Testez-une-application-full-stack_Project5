import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  
  // Crée un mock d'utilisateur pour les tests
  const mockUser: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 10,
    username: 'username',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: false,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService],
    });
    service = TestBed.inject(SessionService);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test 2: Vérifie l'état initial du service
  test('Doit initialiser avec isLogged comme false et sessionInformation comme undefined', () => {
    // Vérifie que isLogged est false par défaut
    expect(service.isLogged).toBe(false);
    // Vérifie que sessionInformation est undefined par défaut
    expect(service.sessionInformation).toBe(undefined);
  });

  // Test 3: Vérifie le comportement initial de l'Observable $isLogged
  test('devrait émettre la valeur initiale de isLogged avec $isLogged', () => {
    // Souscrit à l'Observable $isLogged
    service.$isLogged().subscribe((isLogged) => {
      // Vérifie que la valeur initiale est false
      expect(isLogged).toBe(false);
    });
  });

  // Test 4: Teste la fonctionnalité de connexion (logIn)
  test('doit connecter User et mettre à jour isLogged et sessionInformation', () => {
    // Appelle la méthode de connexion avec le mock user
    service.logIn(mockUser);

    // Vérifie que isLogged est passé à true
    expect(service.isLogged).toBe(true);
    // Vérifie que sessionInformation contient les données du mock
    expect(service.sessionInformation).toEqual(mockUser);

    // Vérifie que l'Observable émet la nouvelle valeur
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
    });
  });

  // Test 5: Teste la fonctionnalité de déconnexion (logOut)
  test('doit déconnecter User et mettre à jour isLogged et sessionInformation', () => {
    // Simule d'abord une connexion
    service.logIn(mockUser);
    // Puis appelle la méthode de déconnexion
    service.logOut();

    // Vérifie que isLogged est revenu à false
    expect(service.isLogged).toBe(false);
    // Vérifie que sessionInformation est redevenu undefined
    expect(service.sessionInformation).toBeUndefined();

    // Vérifie que l'Observable émet la nouvelle valeur
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
    });
  });
});