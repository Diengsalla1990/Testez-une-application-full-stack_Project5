import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { expect } from '@jest/globals';  // Correct

import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';

describe('AuthService', () => {
  let authService: AuthService;
  let httpTestingController: HttpTestingController;

  const registerRequestData: RegisterRequest = {
    email: 'test@test.com',
    firstName: 'firstName',
    lastName: 'lastName',
    password: 'password',
  };

  const loginRequestData: LoginRequest = {
    email: 'test@test.com',
    password: 'password',
  };

  const loginSessionResponse: SessionInformation = {
    token: 'randomToken',
    type: 'type',
    id: 101010,
    username: 'username',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: false,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });

    authService = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  it('should send a register request', () => {
    authService.register(registerRequestData).subscribe();

    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(registerRequestData);

    req.flush(null);
  });

  it('should send a login request', () => {
    authService.login(loginRequestData).subscribe();

    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(loginRequestData);

    req.flush(loginSessionResponse);
  });
});