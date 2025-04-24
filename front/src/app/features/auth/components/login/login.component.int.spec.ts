import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { HttpClientModule } from '@angular/common/http';
import { expect } from '@jest/globals'; // Import explicite

describe('LoginComponent (integration)', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  const mockSessionInfo = {
    token: 'mock-token',
    type: 'Bearer',
    id: 1,
    username: 'john.doe',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,
        FormsModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        HttpClientModule
      ],
      providers: [
        {
          provide: AuthService,
          useValue: {
            login: jest.fn().mockReturnValue(of(mockSessionInfo))
          }
        },
        {
          provide: SessionService,
          useValue: {
            logIn: jest.fn()
          }
        },
        {
          provide: Router,
          useValue: {
            navigate: jest.fn()
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should simulate full login process via DOM interaction', fakeAsync(() => {
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;

    emailInput.value = 'john@example.com';
    emailInput.dispatchEvent(new Event('input'));

    passwordInput.value = 'secret123';
    passwordInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();
    tick();

    const form = component.form;
    expect(form.valid).toBeTruthy();

    // Click submit button
    const button = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    button.click();

    fixture.detectChanges();
    tick();

    expect(authService.login).toHaveBeenCalledWith({
      email: 'john@example.com',
      password: 'secret123'
    });
    expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  }));
});
