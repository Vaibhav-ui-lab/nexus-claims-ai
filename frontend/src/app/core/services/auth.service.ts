import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentToken = signal<string | null>('demo-jwt-token-nexus-claims');
  private userId = signal<string>('ADJUSTER-101');

  isAuthenticated(): boolean {
    return !!this.currentToken();
  }

  getToken(): string | null {
    return this.currentToken();
  }

  getCurrentUserId(): string {
    return this.userId();
  }
}
