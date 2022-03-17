import { formatDate } from '@angular/common';
import { Injectable } from '@angular/core';
import { CognitoUser, ISignUpResult } from 'amazon-cognito-identity-js';
import { Auth } from 'aws-amplify';
import { BehaviorSubject } from 'rxjs';
import { User } from '../model/User';

const LOGGED_IN_USER: string = "TRADEPLAN_LOGGED_IN_USER";
const LOGGED_IN_TIME: string = "TRADEPLAN_LOGGED_IN_TIME";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public currentUser: BehaviorSubject<User>;

  constructor() {
    let user: User = this.getUserFromLocalStorage();
    this.currentUser = new BehaviorSubject<User>(user);
  }

  public getUserFromLocalStorage(): User {
    let lastLoginDateStr = localStorage.getItem(LOGGED_IN_TIME);
    let currentDateStr = formatDate(new Date(), 'dd-MM-yyyy', 'en_US');
    if (lastLoginDateStr == currentDateStr) {
      let userStr = localStorage.getItem(LOGGED_IN_USER);
      if (userStr) {
        return JSON.parse(userStr);
      }
    } else {
      this.deleteLoginTimeFromLocalStorage();
    }
    return null;
  }

  /**
   * Peforms the Login.
   * 
   * @param email
   * @param password 
   * @returns 
   */
  public login(email: string, password: string): Promise<User> {
    return Auth.signIn(email, password)
      .then(cognitoUser => this.toUser(cognitoUser));
  }

  /**
   * Performs the logout.
   */
  public logout() {
    this.deleteUserFormLocalStorage();
    this.deleteUserFormLocalStorage();
    this.currentUser.next(null);
  }

  /**
   * Performs the Signup.
   * @param name 
   * @param email 
   * @param password 
   * @param handler 
   */
  public signup(name: string, email: string, password: string): Promise<ISignUpResult> {
    return Auth.signUp({
      username: email,
      password: password,
      attributes: {
        name: name
      }
    });
  }

  /**
   * Triggers password reset flow.
   * @param email 
   */
  public forgotPassword(email: string): Promise<any> {
    return Auth.forgotPassword(email);
  }

  /**
   * Resets the password.
   * @param email 
   * @param code 
   * @param password 
   */
  public resetPassword(email: string, code: string, password: string): Promise<String> {
    return Auth.forgotPasswordSubmit(email, code, password);
  }

  /**
   * Verifies the email.
   * @param email 
   * @param code 
   * @returns 
   */
  public verifyEmail(email: string, code: string): Promise<any> {
    return Auth.confirmSignUp(email, code);
  }

  /**
   * Resends the email verification code.
   * @param email
   */
  public resendEmailVerification(email: string): Promise<any> {
    return Auth.resendSignUp(email);
  }

  private toUser(cognitoUser: CognitoUser): Promise<User> {
    return new Promise((resolve, reject) => {
      cognitoUser.getUserAttributes((error, attributes) => {
        if (error) {
          reject(error);
        } else if (attributes) {
          let name = attributes.find(attribute => attribute.Name == "name")?.Value as string;
          let email = attributes.find(attribute => attribute.Name == "email")?.Value as string;
          let user: User = new User(name, email);
          this.saveUserToLocalStorage(user);
          this.saveLoginTimeToLocalStorage();
          this.currentUser.next(user);
          resolve(user);
        }
      });
    });
  }

  private saveUserToLocalStorage(user: User): void {
    let userStr = JSON.stringify(user);
    localStorage.setItem(LOGGED_IN_USER, userStr);
  }

  private deleteUserFormLocalStorage(): void {
    localStorage.removeItem(LOGGED_IN_USER);
  }

  private saveLoginTimeToLocalStorage(): void {
    let loginTimeStr = formatDate(new Date(), 'dd-MM-yyyy', 'en_US');
    localStorage.setItem(LOGGED_IN_TIME, loginTimeStr);
  }

  private deleteLoginTimeFromLocalStorage(): void {
    localStorage.removeItem(LOGGED_IN_TIME);
  }
}
