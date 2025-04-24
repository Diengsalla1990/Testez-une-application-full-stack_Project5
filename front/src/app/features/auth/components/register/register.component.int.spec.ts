import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { expect } from '@jest/globals'; // Import explicite

describe('RegisterComponent (integration)', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: any;
  let mockRouter: any;

  const mockRegisterData = {
    email: 'ibra@test.com',
    firstName: 'ibra',
    lastName: 'dieng',
    password: 'ibra123',
  };

  beforeEach(async () => {
    mockAuthService = {
      register: jest.fn().mockReturnValue(of(undefined))
    };

    mockRouter = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        ReactiveFormsModule,
        FormsModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should simulate registration flow via DOM interactions', fakeAsync(() => {
    // Simuler remplissage du formulaire
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const firstNameInput = fixture.debugElement.query(By.css('input[formControlName="firstName"]')).nativeElement;
    const lastNameInput = fixture.debugElement.query(By.css('input[formControlName="lastName"]')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;

    emailInput.value = mockRegisterData.email;
    firstNameInput.value = mockRegisterData.firstName;
    lastNameInput.value = mockRegisterData.lastName;
    passwordInput.value = mockRegisterData.password;

    emailInput.dispatchEvent(new Event('input'));
    firstNameInput.dispatchEvent(new Event('input'));
    lastNameInput.dispatchEvent(new Event('input'));
    passwordInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();
    tick();

    // Vérification validité du formulaire
    expect(component.form.valid).toBeTruthy();

    // Clic sur le bouton submit
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    submitButton.click();

    fixture.detectChanges();
    tick();

    // Vérifie les appels
    expect(mockAuthService.register).toHaveBeenCalledWith(mockRegisterData);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  }));

  it('should show error when registration fails', fakeAsync(() => {
    mockAuthService.register.mockReturnValueOnce(throwError(() => new Error('Registration failed')));

    component.form.setValue(mockRegisterData);
    fixture.detectChanges();

    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    submitButton.click();

    fixture.detectChanges();
    tick();

    expect(component.onError).toBe(true);
  }));
});
