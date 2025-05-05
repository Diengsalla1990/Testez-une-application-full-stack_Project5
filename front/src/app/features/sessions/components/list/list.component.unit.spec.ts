import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  // Mocks des données de sessions
  const mockSessions = [
    { id: 1, name: 'Session 1', description: 'desc', date: new Date(), teacher_id: 10 },
    { id: 2, name: 'Session 2', description: 'desc', date: new Date(), teacher_id: 11 },
  ];

  // Mock du service SessionApiService
  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of(mockSessions)),
  };

  // Mock de SessionService avec un utilisateur admin
  const mockSessionService = {
    sessionInformation: { id: 42, admin: true, firstName: 'Dieng', lastName: 'Salla' },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      providers: [
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('devrait initialiser sessions$ avec les données de session du service', (done) => {
    component.sessions$.subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
      done();
    });
  });

  it('devrait renvoyer user actuel à partir sessionService', () => {
    expect(component.user).toEqual(mockSessionService.sessionInformation);
  });
});
