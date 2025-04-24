import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';



import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockSessionService: any;
  let mockAuthService: any;
  let mockRouter: any;

  const mockSessionData = {
    token: 'randomTokenString',
    type: 'type',
    id: 101010,
    username: 'Diengsalla',
    firstName: 'Ibra',
    lastName: 'Dieng',
    admin: false,
  };

  beforeEach(async () => {
    mockSessionService = { logIn: jest.fn() };
    mockAuthService = { login: jest.fn() };
    mockRouter = { navigate: jest.fn() };


    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        FormBuilder,
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  test('créer un formulaire de connexion avec des valeurs initiales', () => {
    expect(component.form.get('email')?.value).toEqual('');
    expect(component.form.get('password')?.value).toEqual('');
  });

  test('Validateur requis pour e-mail', () => {
    const email = component.form.get('email');
    expect(email?.valid).toBeFalsy();
    email?.setValue('ibra@test.com');
    expect(email?.valid).toBeTruthy();
  });

  test('Doit avoir une longueur minimale et un validateur requis pour le mot de passe', () => {
    const password = component.form.get('password');
    expect(password?.valid).toBeFalsy();

    if (!password) return;

    // 1. Champ vide → invalide
    expect(password?.valid).toBeFalsy();
    expect(password.errors?.['required']).toBeTruthy();

    // 2. Mot de passe valide
    password.setValue('password123');
    expect(password?.valid).toBeTruthy();

    // 3. Mot de passe trop court
    password.setValue('pa');
    expect(password?.valid).toBeFalsy();
    expect(password.errors?.['minlength']).toBeTruthy();

  });

  test('Doit définir onError sur true en cas error de connexion', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('error')));
    component.submit();
    expect(mockAuthService.login).toHaveBeenLastCalledWith({
      email: '',
      password: '',
    });
    expect(component.onError).toBe(true);
  });


  test('Devrait appeler sessionService et rediriger utilisateur en cas de connexion réussie', () => {
    mockAuthService.login.mockReturnValue(of(mockSessionData));
    component.submit();
    expect(component.onError).toBe(false);
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionData);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });



});
