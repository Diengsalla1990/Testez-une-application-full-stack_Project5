import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  let mockAuthService: any;
  let mockRouter: any;

  let mockRegisterData = {
    email: 'ibra@test.com',
    firstName: 'ibra',
    lastName: 'dieng',
    password: 'ibra123',
  };


  beforeEach(async () => {

    mockRouter = { navigate: jest.fn() };
    mockAuthService = { register: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('créer un formulaire inscription avec des valeurs initiales', () => {
    expect(component.form.get('email')?.value).toEqual('');
    expect(component.form.get('firstName')?.value).toEqual('');
    expect(component.form.get('lastName')?.value).toEqual('');
    expect(component.form.get('password')?.value).toEqual('');
  });

  test('e-mail obligatoire et un validateur dans le champ du formulaire e-mail', () => {
    const email = component.form.get('email');
    expect(email?.valid).toBeFalsy();
    email?.setValue('ibraemail');
    expect(email?.valid).toBeFalsy();
    email?.setValue('ibra@test.com');
    expect(email?.valid).toBeTruthy();
  });

  test('devrait avoir une longueur obligatoire et min et max sur le champ de formulaire prénom', () => {
    const firstName = component.form.get('firstName');
    expect(firstName?.valid).toBeFalsy();
    firstName?.setValue('aa');
    expect(firstName?.valid).toBeFalsy();
    firstName?.setValue('aaaaaaaaaaaaaaaaaaaaaaaaa');
    expect(firstName?.valid).toBeFalsy();
    firstName?.setValue('Ibrahim');
    expect(firstName?.valid).toBeTruthy();
  });

  test('devrait avoir une longueur obligatoire et min et max sur le champ de formulaire password', () => {
    const password = component.form.get('password');
    expect(password?.valid).toBeFalsy();
    password?.setValue('aa');
    expect(password?.valid).toBeFalsy();
    password?.setValue('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
    expect(password?.valid).toBeFalsy();
    password?.setValue('dieng123');
    expect(password?.valid).toBeTruthy();
  });

  test('doit définir onError sur true en cas erreur enregistrement', () => {
    mockAuthService.register.mockReturnValue(
      throwError(() => new Error('error'))
    );
    expect(component.onError).toBe(false);
    component.submit();
    expect(component.onError).toBe(true);
  });

  test('doit appeler authService.register avec le paramètre correct et rediriger User si reussi', () => {
    mockAuthService.register.mockReturnValue(of(undefined));

    const email = component.form.get('email');
    const firstName = component.form.get('firstName');
    const lastName = component.form.get('lastName');
    const password = component.form.get('password');

    email?.setValue(mockRegisterData.email);
    firstName?.setValue(mockRegisterData.firstName);
    lastName?.setValue(mockRegisterData.lastName);
    password?.setValue(mockRegisterData.password);

    component.submit();

    expect(mockAuthService.register).toHaveBeenCalledWith(mockRegisterData);
    expect(component.onError).toBe(false);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });
});
