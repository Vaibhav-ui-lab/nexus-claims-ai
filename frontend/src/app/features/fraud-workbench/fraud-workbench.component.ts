import { Component, Signal, WritableSignal, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface FlaggedClaim {
  claimId: string;
  providerId: string;
  amount: number;
  riskScore: number;
  classification: string;
  reasoning: string;
}

@Component({
  selector: 'app-fraud-workbench',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="p-8 max-w-7xl mx-auto">
      <header class="flex justify-between items-center mb-8 border-b border-slate-800 pb-4">
        <div>
          <h1 class="text-3xl font-bold text-sky-400">Adjuster Fraud Triage Workbench</h1>
          <p class="text-slate-400 text-sm mt-1">Review Spring AI anomaly findings and submit saga override decisions</p>
        </div>
        <div class="bg-slate-900 border border-slate-800 px-4 py-2 rounded-xl text-sm">
          Flagged Claims: <span class="font-bold text-amber-400">{{ highRiskCount() }}</span>
        </div>
      </header>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- List View -->
        <div class="lg:col-span-2 space-y-4">
          <div *ngFor="let item of claims()" 
               (click)="selectClaim(item)"
               [class.border-sky-500]="selectedClaim()?.claimId === item.claimId"
               class="p-5 bg-slate-900 rounded-2xl border border-slate-800 hover:border-slate-700 cursor-pointer transition">
            <div class="flex justify-between items-start">
              <div>
                <span class="font-mono text-xs text-sky-400">{{ item.claimId }}</span>
                <h3 class="font-semibold text-lg text-white mt-1">{{ item.providerId }}</h3>
                <span class="text-slate-400 text-sm">{{ item.amount | currency }}</span>
              </div>
              <span [class]="getRiskBadgeClass(item.classification)">
                {{ item.classification }} ({{ item.riskScore | percent:'1.0-1' }})
              </span>
            </div>
            <p class="text-slate-300 text-sm mt-3 line-clamp-2">{{ item.reasoning }}</p>
          </div>
        </div>

        <!-- Detail AI Assistant Drawer -->
        <div class="bg-slate-900 p-6 rounded-2xl border border-slate-800 flex flex-col justify-between h-[500px]">
          <div *ngIf="selectedClaim(); else noSelection">
            <div class="flex items-center space-x-2 text-sky-400 mb-4">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/></svg>
              <h2 class="text-xl font-bold">Spring AI Audit Reasoning</h2>
            </div>
            <div class="bg-slate-950 p-4 rounded-xl border border-slate-800 text-sm space-y-3">
              <p><strong class="text-slate-400">Claim ID:</strong> {{ selectedClaim()?.claimId }}</p>
              <p><strong class="text-slate-400">Amount:</strong> {{ selectedClaim()?.amount | currency }}</p>
              <p><strong class="text-slate-400">Risk Score:</strong> <span class="text-rose-400 font-bold">{{ selectedClaim()?.riskScore }}</span></p>
              <hr class="border-slate-800"/>
              <p class="text-slate-300 leading-relaxed">{{ selectedClaim()?.reasoning }}</p>
            </div>
          </div>
          <ng-template #noSelection>
            <div class="text-center text-slate-500 my-auto">Select a claim to review AI anomaly explanation</div>
          </ng-template>

          <div class="mt-6 flex space-x-3" *ngIf="selectedClaim()">
            <button (click)="overrideDecision('APPROVED')" class="flex-1 bg-emerald-600 hover:bg-emerald-500 text-white py-3 rounded-xl font-semibold transition">Approve Claim</button>
            <button (click)="overrideDecision('REJECTED')" class="flex-1 bg-rose-600 hover:bg-rose-500 text-white py-3 rounded-xl font-semibold transition">Reject Claim</button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class FraudWorkbenchComponent {
  claims: WritableSignal<FlaggedClaim[]> = signal([
    {
      claimId: 'CLM-99482',
      providerId: 'PRV-8831 (Apex Health)',
      amount: 14500.00,
      riskScore: 0.89,
      classification: 'HIGH_RISK_FRAUD',
      reasoning: 'Suspicious billing pattern detected: Complex procedure CPT-99215 billed concurrently with duplicate unbundled diagnostic scans within 24 hours.'
    },
    {
      claimId: 'CLM-99483',
      providerId: 'PRV-4412 (Valley Clinic)',
      amount: 22000.00,
      riskScore: 0.78,
      classification: 'SUSPICIOUS',
      reasoning: 'Provider billing frequency is 4.2x higher than regional baseline for cardiology codes.'
    }
  ]);

  selectedClaim: WritableSignal<FlaggedClaim | null> = signal(null);

  highRiskCount: Signal<number> = computed(() => 
    this.claims().filter(c => c.riskScore > 0.70).length
  );

  selectClaim(item: FlaggedClaim) {
    this.selectedClaim.set(item);
  }

  overrideDecision(decision: 'APPROVED' | 'REJECTED') {
    const claim = this.selectedClaim();
    if (!claim) return;
    this.claims.update(list => list.filter(c => c.claimId !== claim.claimId));
    this.selectedClaim.set(null);
  }

  getRiskBadgeClass(classification: string): string {
    switch (classification) {
      case 'HIGH_RISK_FRAUD': return 'px-3 py-1 text-xs rounded-full bg-rose-500/20 text-rose-400 border border-rose-500/30 font-semibold';
      case 'SUSPICIOUS': return 'px-3 py-1 text-xs rounded-full bg-amber-500/20 text-amber-400 border border-amber-500/30 font-semibold';
      default: return 'px-3 py-1 text-xs rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 font-semibold';
    }
  }
}
