import { ComponentFixture, fakeAsync, TestBed, tick, waitForAsync } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { expect } from '@jest/globals';
import { By } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('MeComponent (integration)', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockMatSnackBar: any;
  let mockRouter: any;
  let mockSessionService: any;
  
  const mockUserService = {
    getById: jest.fn().mockReturnValue(
      of({
        firstName: 'Ibra',
        lastName: 'DIENG',
        email: 'dieng@example.com',
        admin: false,
        createdAt: new Date('2025-04-22'),
        updatedAt: new Date('2025-04-22')
      })
    ),
    delete: jest.fn().mockReturnValue(of(null))
  };

  beforeEach(waitForAsync(() => {
    mockSessionService = { logOut: jest.fn(), sessionInformation: { id: '1' } };
    mockRouter = { navigate: jest.fn() };
    mockMatSnackBar = { open: jest.fn() };
    window.history.back = jest.fn();
    TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule,],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA] // ignore les composants Angular Material dans le DOM
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MeComponent);
    fixture.detectChanges(); // ngOnInit + binding
    
  });

  it('should load and display user information', fakeAsync(() => {
    fixture.detectChanges();
    tick();

    const nameElement = fixture.debugElement.query(By.css('p:nth-of-type(1)')).nativeElement;
    const emailElement = fixture.debugElement.query(By.css('p:nth-of-type(2)')).nativeElement;
    const createdAtElement = fixture.debugElement.query(By.css('div.p2.w100 p:nth-of-type(1)')).nativeElement;
    const updatedAtElement = fixture.debugElement.query(By.css('div.p2.w100 p:nth-of-type(2)')).nativeElement;

    expect(nameElement.textContent).toContain('Name: Ibra DIENG');
    expect(emailElement.textContent).toContain('Email: dieng@example.com');
    expect(createdAtElement.textContent).toContain('Create at:  April 22, 2025');
    expect(updatedAtElement.textContent).toContain('Last update:  April 22, 2025');
  }));

  test('should go back when Back() is called', () => {
    component.back();

    expect(window.history.back).toHaveBeenCalledTimes(1);
  });

  it('should display full name and email', () => {
    const content = fixture.nativeElement.textContent;
    expect(content).toContain('Name: Ibra DIENG');
    expect(content).toContain('Email: dieng@example.com');
  });

  it('should show created and updated dates', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Create at:');
    expect(compiled.textContent).toContain('Last update:');
  });

  test('should delete user, pop up snackBar, logout, and redirect', fakeAsync(() => {
    component.delete();
    tick();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      {
        duration: 3000,
      }
    );
    tick();
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  }));
});
