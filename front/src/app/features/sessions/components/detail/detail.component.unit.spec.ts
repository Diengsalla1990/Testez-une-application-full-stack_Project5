import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let mockSessionService: any;
  let mockSessionApiService: any;
  let mockTeacherService: any;
  let mockMatSnackBar: any;
  let mockRouter: any;
  let mockActivatedRoute: any;
  let mockSessionInformationData: any;
  let mockTeacherData: any;

   

  beforeEach(async () => {

    mockSessionService = {
      sessionInformation: {
        admin: true,
        id: 1
      }
    }
  
   
      mockActivatedRoute = {
        snapshot: {
          paramMap: {
            get: jest.fn().mockReturnValue('1'),
          },
        },
      };
  
      mockSessionInformationData = {
        id: 1,
        name: 'Goat Yoga',
        description: 'yoga yoga yoga ?',
        date: new Date(),
        teacher_id: 10,
        users: [2, 3, 4],
        createdAt: new Date('2025-03-15'),
        updatedAt: new Date('2025-03-16'),
      };
  
      mockTeacherData = {
        id: 10,
        lastName: 'ibra',
        firstName: 'dieng',
        createdAt: new Date(),
        updatedAt: new Date(),
      };
     
      mockSessionApiService = {
        detail: jest.fn().mockReturnValue(of(mockSessionInformationData)),
        delete: jest.fn().mockReturnValue(of(undefined)),
        participate: jest.fn().mockImplementation(() => {
          mockSessionInformationData.users.push(1);
          return of(undefined);
        }),
        unParticipate: jest.fn().mockImplementation(() => {
          mockSessionInformationData.users.pop();
          return of(undefined);
        }),
      };
  
      mockTeacherService = {
        detail: jest.fn().mockReturnValue(of(mockTeacherData)),
      };

      mockMatSnackBar = { open: jest.fn() };

      mockRouter = { navigate: jest.fn() };

      window.history.back = jest.fn();

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule
      ],
      declarations: [DetailComponent], 
       providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('devrait appeler fetchSession onInit, appeler SessionApiService pour hydrater la session, isParticipate et appeler teacherService pour hydrater teacher', () => {
    //@ts-ignore
    const fetchSessionSpy = jest.spyOn(component, 'fetchSession');

    component.ngOnInit();
    expect(fetchSessionSpy).toHaveBeenCalled();
    expect(mockActivatedRoute.snapshot.paramMap.get).toHaveBeenCalled();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.sessionId).toEqual('1');
    expect(component.session).toEqual(mockSessionInformationData);
    expect(component.isParticipate).toBe(false);
    expect(mockTeacherService.detail).toHaveBeenCalledWith('10');
    expect(component.teacher).toEqual(mockTeacherData);
  });

  test('Doit définir isParticipate sur true onInit si userId est dans le tableau User de session', () => {
    mockSessionInformationData.users.push(1);
    component.ngOnInit();
    expect(component.isParticipate).toBe(true);
  });

  test('Doit revenir en arrière lorsque Back() est appelé', () => {
    component.back();
    expect(window.history.back).toHaveBeenCalledTimes(1);
  });

  test('Lors de la suppression doit appelé sessionApiService.delete, afficher la barre état et rediriger User', () => {
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalled();
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Session deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  test('should on participate, appel sessionApiService.participate, appel fetchSession method, final: isParticipate doit être true', () => {
    //@ts-ignore
    const fetchSessionSpy = jest.spyOn(component, 'fetchSession');

    expect(component.isParticipate).toBe(false);

    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');

    expect(fetchSessionSpy).toHaveBeenCalled();
    expect(component.isParticipate).toBe(true);
  });

  test('should on unParticipate, appel sessionApiService.unParticipate, alors appel fetchSession method, final: isParticipate doit  être false', () => {
    //@ts-ignore
    // On espionne la méthode fetchSession du composant
    const fetchSessionSpy = jest.spyOn(component, 'fetchSession');

    // On ajoute l'ID user (1) à la liste des participants de la session mockée
    mockSessionInformationData.users.push(1);
    // On déclenche l'initialisation du composant
    component.ngOnInit();
    // Vérification que isParticipate est true après l'init (user est participant)
    expect(component.isParticipate).toBe(true);
     
    // On appelle la méthode unParticipate() du composant
    component.unParticipate();
    
    //Vérifie que le service a bien été appelé avec les bons paramètres
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
    // Vérifie que fetchSession a bien été appelé après unParticipate
    expect(fetchSessionSpy).toHaveBeenCalled();
    //Vérifie que le statut isParticipate est bien passé à false
    expect(component.isParticipate).toBe(false);
  });

});

