import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpTestingController: HttpTestingController;

  // Crée un mock d'un enseignant pour les tests
  const mockerTeacher: Teacher = {
    id: 10,
    lastName: 'lastName',
    firstName: 'firstName',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

   // Crée un tableau mocké avec deux enseignants (même données)
  const mockTeachers: Teacher[] = [mockerTeacher, mockerTeacher];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService],
    });
    // Récupère l'instance du service
    service = TestBed.inject(TeacherService);
    
    // Récupère le contrôleur pour les tests HTTP
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  // Test 1: Vérifie que le service est bien créé
  test('should be created', () => {
    expect(service).toBeTruthy();  // Assertion: le service doit exister
  });

  // Test 2: Vérifie la récupération de tous les enseignants
  test('Doit récupérer tous les Teacher de API via GET', () => {
    // Appelle la méthode all() du service et souscrit à l'observable
    service.all().subscribe((teachers) => {
      // Vérifie que la réponse correspond au mock
      expect(teachers).toEqual(mockTeachers);
    });

    // Capture la requête HTTP faite par le service
    const req = httpTestingController.expectOne('api/teacher');
    
    // Vérifie que la méthode HTTP est GET
    expect(req.request.method).toBe('GET');

    // Simule une réponse du serveur avec les données mockées
    req.flush(mockTeachers);
  });

  // Test 3: Vérifie la récupération des détails d'un enseignant
  test('Doit récupérer teacher detail de API via GET', () => {
    // Appelle la méthode detail() avec l'ID '1'
    service.detail('1').subscribe((teacher) => {
      // Vérifie que la réponse correspond au mock
      expect(teacher).toEqual(mockerTeacher);
    });

    // Capture la requête HTTP
    const req = httpTestingController.expectOne('api/teacher/1');
    
    // Vérifie que la méthode HTTP est GET
    expect(req.request.method).toBe('GET');

    // Simule une réponse du serveur avec les données mockées
    req.flush(mockerTeacher);
  });
});