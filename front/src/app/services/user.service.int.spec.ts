import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  const mockerUser: User = {
    id: 1,
    email: 'email',
    lastName: 'lastname',
    firstName: 'firstName',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date(),

  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers:[UserService],
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

 // Test 1: Vérification de la création du service
 it('should be created', () => {
  // Assertion vérifiant que le service est bien instancié
  expect(service).toBeTruthy();
});

// Test 2: Test de la méthode getById
test('Doit sur « getById », récupérer User depuis API via GET', () => {
  // Appel de la méthode getById avec un ID
  service.getById('1').subscribe((user) => {
    // Vérification que l'utilisateur retourné correspond au mock
    expect(user).toBe(mockerUser);
  });

  // Interception de la requête HTTP
  const req = httpTestingController.expectOne('api/user/1');
  
  // Vérification que la méthode HTTP utilisée est GET
  expect(req.request.method).toBe('GET');

  // Simulation de la réponse du serveur avec le mock utilisateur
  req.flush(mockerUser);
});

// Test 3: Test de la méthode delete
test('Doit supprimer User via SUPPRIMER', () => {
  // Appel de la méthode delete avec un ID
  service.delete('1').subscribe(); // On souscrit sans vérification car la réponse est vide
  
  // Interception de la requête HTTP
  const req = httpTestingController.expectOne('api/user/1');
  
  // Vérification que la méthode HTTP utilisée est DELETE
  expect(req.request.method).toBe('DELETE');

  // Simulation d'une réponse vide du serveur
  req.flush({});
});
});