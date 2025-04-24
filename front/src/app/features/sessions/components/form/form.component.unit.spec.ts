import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { of } from 'rxjs';

describe('FormComponent', () => {
   // Déclaration des variables utilisées dans les tests
   let component: FormComponent;
   let fixture: ComponentFixture<FormComponent>;
   
   // Mocks des services et dépendances
   let mockActivatedRoute: any;
   let mockMatSnackBar: any;
   let mockSessionApiService: any;
   let mockTeacherService: any;
   let mockRouter: any;
   
   // Données mockées pour les tests
   let mockerDonneSessionInformation: any;
   let mockerUpdateDonneInformationSession: any;
   let mockerDonnerTeacher: any;

  // Mock du service Session avec un admin par défaut
  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  beforeEach(async () => {
    // Configuration des mocks avant chaque test
    
    // Mock pour ActivatedRoute avec un paramètre d'URL 'id' = '1'
    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),
        },
      },
    };

     // Données mockées pour une session existante
    mockerDonneSessionInformation = {
      id: 1,
      name: 'Goat Yoga',
      description: 'je suis diengsalla',
      date: new Date(),
      teacher_id: 10,
      users: [2, 3, 4],
      createdAt: new Date('2025-04-14'),
      updatedAt: new Date('2025-04-14'),
    };

    // Données mockées pour une mise à jour de session
    mockerUpdateDonneInformationSession = {
      name: 'Nouveau Nom',
      description: 'Nouveau Description',
      date: new Date('2025-04-14'),
      teacher_id: 9,
    };

    // Données mockées pour la liste des enseignants
    mockerDonnerTeacher = [
      { id: 10, lastName: 'Ibra', firstName: 'Dieng' },
      { id: 11, lastName: 'Fatou', firstName: 'Dieng' },
      { id: 12, lastName: 'Astou', firstName: 'Dieng' },
    ];

    // Mock pour MatSnackBar (notifications)
    mockMatSnackBar = { open: jest.fn() };

    // Mock pour SessionApiService avec différentes méthodes
    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockerDonneSessionInformation)),
      create: jest.fn().mockReturnValue(of(mockerDonneSessionInformation)),
      update: jest.fn().mockImplementation(() => {
        mockerDonneSessionInformation.description = 'A new way to practice';
        return of(mockerDonneSessionInformation);
      }),
    };

    // Mock pour TeacherService
    mockTeacherService = {
      all: jest.fn().mockReturnValue(of(mockerDonnerTeacher)),
    };

    // Mock pour Router
    mockRouter = {
      navigate: jest.fn(),
      url: '/create',
    };
    
    // Configuration du module de test Angular
    await TestBed.configureTestingModule({
    
      imports: [
        // Modules nécessaires pour le composant
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        // Injection des services mockés
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    // Création du composant et détection des changements
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  // TEST 2: Vérification des droits admin
  test('vérifier si User est Admin et rediriger si ce nest pas le cas', () => {
    mockSessionService.sessionInformation.admin = false;//Simule un non-admin
    component.ngOnInit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);// Doit rediriger
  });


  // TEST 3: Comportement en mode mise à jour
  test('doit onInit si lURL inclut « update », mettre onUpdate à true, appeler sessionApiService.detail', () => {
    //@ts-ignore
    const initFormSpy = jest.spyOn(component, 'initForm');// Espionne initForm
    
    mockRouter.url = 'update'; // Simule une URL de mise à jour
    
    component.ngOnInit();
    expect(component.onUpdate).toBe(true); // Doit être en mode update
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1'); // Doit charger les détails
    expect(initFormSpy).toHaveBeenCalledWith(mockerDonneSessionInformation); // Doit initialiser le formulaire
    expect(component.sessionForm?.get('name')?.value).toEqual(
      mockerDonneSessionInformation.name
    ); // Doit pré-remplir le champ name
  });

  // TEST 4: Comportement en mode création
  test('si onInit, si lURL inclut « create », doit avoir onUpdate sur false, ne pas appeler sessionApiService.detail et initForm avec des valeurs vides', () => {
    //@ts-ignore
    const initFormSpy = jest.spyOn(component, 'initForm');

    component.ngOnInit();
    expect(component.onUpdate).toBe(false); // Doit être en mode création
    expect(mockSessionApiService.detail).not.toHaveBeenCalled(); // Ne doit pas charger de détails
    expect(initFormSpy).toHaveBeenCalledWith(); // Doit initialiser un formulaire vide
    expect(component.sessionForm?.get('name')?.value).toEqual(''); // Le champ name doit être vide
  });

   // TEST 5: Soumission en mode création
  test('devrait soumettre si la session nest pas mise à jour, appeler sessionApiService.create avec les valeurs du formulaire comme session et appeler la méthode exitPage avec le message', () => {
    //@ts-ignore
    const exitPageSpy = jest.spyOn(component, 'exitPage');
   // Nettoie les données mockées pour une création
   delete mockerDonneSessionInformation.id;
   delete mockerDonneSessionInformation.createdAt;
   delete mockerDonneSessionInformation.updatedAt;
   delete mockerDonneSessionInformation.users;
   
   component.sessionForm?.setValue(mockerDonneSessionInformation);
   component.submit();
   
   expect(mockSessionApiService.create).toHaveBeenCalledWith(
     mockerDonneSessionInformation
   ); // Doit appeler la création
   expect(mockMatSnackBar.open).toHaveBeenCalledWith(
     'Session created !', 'Close', { duration: 3000 }
   ); // Doit afficher une notification
   expect(exitPageSpy).toHaveBeenCalledWith('Session created !'); // Doit appeler exitPage
   expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']); // Doit rediriger
  });

  // TEST 6: Soumission en mode mise à jour
  test('doit être soumis lors de la mise à jour de la session, appeler sessionApiService.update et appeler la méthode exitPage avec le message', () => {
    //@ts-ignore
    const exitPageSpy = jest.spyOn(component, 'exitPage');

    mockRouter.url = 'update'; // Simule une URL de mise à jour
    component.ngOnInit(); // Initialise en mode update

    component.sessionForm?.setValue(mockerUpdateDonneInformationSession);
    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith(
      '1', mockerUpdateDonneInformationSession
    ); // Doit appeler la mise à jour
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Session updated !', 
      'Close', 
      { duration: 3000 }
    ); // Doit afficher une notification
    expect(exitPageSpy).toHaveBeenCalledWith('Session updated !'); // Doit appeler exitPage
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']); // Doit rediriger
  });
});