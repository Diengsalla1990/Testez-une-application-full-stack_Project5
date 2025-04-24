import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { fakeAsync, tick } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockRouter: any;
  let mockMatSnackBar: any;
  let mockUserService: any;
  let mockSessionService: any;

  const mockUserData = {
    id: 1,
    lastName: 'testLastName',
    firstName: 'testFirstName',
    admin: 0,
    email: 'test@test.com',
    password: 'test1234',
    createdAt: '07/07/2007',
    updatedAt: '07/07/2007',
  };

  beforeEach(async () => {
    mockRouter = { navigate: jest.fn() };
    mockSessionService = { logOut: jest.fn(), sessionInformation: { id: '1' } };
    mockUserService = {
      getById: jest.fn().mockReturnValue(of(mockUserData)),
      delete: jest.fn().mockReturnValue(of(null)),
    };

    mockMatSnackBar = { open: jest.fn() };
    window.history.back = jest.fn();

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule,
      ],
      providers: [
        {
          provide: SessionService,
          useValue: mockSessionService,
        },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should call userService.getById and update user property', () => {
    component.ngOnInit();

    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUserData);
  });

  test('should go back when Back() is called', () => {
    component.back();

    expect(window.history.back).toHaveBeenCalledTimes(1);
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