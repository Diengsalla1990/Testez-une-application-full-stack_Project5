import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let mockSessionService: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockSessionService = {
      $isLogged: jest.fn().mockReturnValue(of(false)),
      logOut: jest.fn(),
    };
    mockRouter = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      imports: [HttpClientModule, MatToolbarModule],
      declarations: [AppComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create the app', () => {
    expect(component).toBeTruthy();
  });

  test('should call $isLogged method and return a boolean as observable', () => {
    let isLoggedValue = false;
    const result = component.$isLogged();
    expect(mockSessionService.$isLogged).toHaveBeenCalled();
    result.subscribe((value) => {
      expect(value).toBe(isLoggedValue);
    });
  });

  test('should call logOut and redirect user', () => {
    component.logout();
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});