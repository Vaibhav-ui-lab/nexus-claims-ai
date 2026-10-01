import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ClaimSubmission {
  providerId: string;
  patientIdHash: string;
  totalAmount: number;
  primaryDiagnosisCode: string;
  clinicalNotes: string;
}

export interface ClaimResponse {
  claimId: string;
  providerId: string;
  totalAmount: number;
  status: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClaimService {
  private http = inject(HttpClient);
  private gatewayUrl = 'http://localhost:8080/api/v1/claims';

  submitClaim(claim: ClaimSubmission): Observable<ClaimResponse> {
    return this.http.post<ClaimResponse>(this.gatewayUrl, claim);
  }

  getClaim(claimId: string): Observable<ClaimResponse> {
    return this.http.get<ClaimResponse>(`${this.gatewayUrl}/${claimId}`);
  }
}
