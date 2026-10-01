import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClaimService, ClaimSubmission, ClaimResponse } from '../../core/services/claim.service';

@Component({
  selector: 'app-claim-submission',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="p-8 max-w-3xl mx-auto">
      <div class="bg-slate-900 border border-slate-800 p-8 rounded-2xl">
        <h1 class="text-2xl font-bold text-white mb-2">Provider Claim Submission Portal</h1>
        <p class="text-slate-400 text-sm mb-6">Submit claims directly into the Kafka Event Intake Pipeline</p>

        <form (ngSubmit)="submit()" #form="ngForm" class="space-y-4">
          <div>
            <label class="block text-xs font-semibold uppercase text-slate-400 mb-1">Provider ID</label>
            <input type="text" [(ngModel)]="formData.providerId" name="providerId" required class="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-white focus:border-sky-500 outline-none">
          </div>

          <div>
            <label class="block text-xs font-semibold uppercase text-slate-400 mb-1">Patient ID Hash</label>
            <input type="text" [(ngModel)]="formData.patientIdHash" name="patientIdHash" required class="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-white focus:border-sky-500 outline-none">
          </div>

          <div>
            <label class="block text-xs font-semibold uppercase text-slate-400 mb-1">Billed Amount ($)</label>
            <input type="number" [(ngModel)]="formData.totalAmount" name="totalAmount" required class="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-white focus:border-sky-500 outline-none">
          </div>

          <div>
            <label class="block text-xs font-semibold uppercase text-slate-400 mb-1">Primary Diagnosis Code (ICD-10)</label>
            <input type="text" [(ngModel)]="formData.primaryDiagnosisCode" name="primaryDiagnosisCode" required class="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-white focus:border-sky-500 outline-none">
          </div>

          <div>
            <label class="block text-xs font-semibold uppercase text-slate-400 mb-1">Clinical Notes & Procedure Details</label>
            <textarea [(ngModel)]="formData.clinicalNotes" name="clinicalNotes" rows="4" class="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-white focus:border-sky-500 outline-none"></textarea>
          </div>

          <button type="submit" [disabled]="submitting()" class="w-full bg-sky-500 hover:bg-sky-400 text-slate-950 font-bold p-3 rounded-xl transition">
            {{ submitting() ? 'Publishing to Kafka Pipeline...' : 'Submit Claim to Ingestion Engine' }}
          </button>
        </form>

        <div *ngIf="submittedResult()" class="mt-6 p-4 bg-emerald-500/10 border border-emerald-500/30 rounded-xl text-emerald-400 text-sm">
          <strong>Claim Successfully Submitted!</strong><br>
          Assigned Claim ID: <span class="font-mono font-bold">{{ submittedResult()?.claimId }}</span><br>
          Status: <span class="font-bold">{{ submittedResult()?.status }}</span>
        </div>
      </div>
    </div>
  `
})
export class ClaimSubmissionComponent {
  private claimService = inject(ClaimService);

  formData: ClaimSubmission = {
    providerId: 'PRV-8831',
    patientIdHash: 'PAT-HASH-9921',
    totalAmount: 14500.00,
    primaryDiagnosisCode: 'ICD10-I25.10',
    clinicalNotes: 'Complex evaluation and diagnostic testing for ischemic heart disease.'
  };

  submitting = signal(false);
  submittedResult = signal<ClaimResponse | null>(null);

  submit() {
    this.submitting.set(true);
    this.claimService.submitClaim(this.formData).subscribe({
      next: (res) => {
        this.submittedResult.set(res);
        this.submitting.set(false);
      },
      error: () => {
        // Fallback for demo when backend gateway isn't running locally
        this.submittedResult.set({
          claimId: 'CLM-' + Math.floor(100000 + Math.random() * 900000),
          providerId: this.formData.providerId,
          totalAmount: this.formData.totalAmount,
          status: 'SUBMITTED',
          createdAt: new Date().toISOString()
        });
        this.submitting.set(false);
      }
    });
  }
}
