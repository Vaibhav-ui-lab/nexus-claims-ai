import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="p-8 max-w-7xl mx-auto">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-white tracking-tight">Enterprise Operations & AI Metrics</h1>
        <p class="text-slate-400 mt-1">Real-time telemetry across Kafka events, Redis cache, and Spring AI fraud evaluations</p>
      </div>

      <!-- Stat Cards -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div class="bg-slate-900 border border-slate-800 p-6 rounded-2xl">
          <span class="text-slate-400 text-sm font-medium">Daily Ingested Claims</span>
          <div class="text-3xl font-bold text-white mt-2">14,290</div>
          <span class="text-xs text-emerald-400 mt-1 block">↑ 12% vs yesterday</span>
        </div>

        <div class="bg-slate-900 border border-slate-800 p-6 rounded-2xl">
          <span class="text-slate-400 text-sm font-medium">Spring AI High Risk Flagged</span>
          <div class="text-3xl font-bold text-amber-400 mt-2">142</div>
          <span class="text-xs text-slate-400 mt-1 block">Requires Adjuster Audit</span>
        </div>

        <div class="bg-slate-900 border border-slate-800 p-6 rounded-2xl">
          <span class="text-slate-400 text-sm font-medium">Redis AI Cache Hit Rate</span>
          <div class="text-3xl font-bold text-sky-400 mt-2">94.2%</div>
          <span class="text-xs text-emerald-400 mt-1 block">Embedding semantic cache</span>
        </div>

        <div class="bg-slate-900 border border-slate-800 p-6 rounded-2xl">
          <span class="text-slate-400 text-sm font-medium">Kafka Consumer Lag</span>
          <div class="text-3xl font-bold text-emerald-400 mt-2">0 ms</div>
          <span class="text-xs text-slate-400 mt-1 block">6 Partition Replicas</span>
        </div>
      </div>

      <!-- Recent Workflow Grid -->
      <div class="bg-slate-900 border border-slate-800 rounded-2xl p-6">
        <h2 class="text-xl font-bold text-white mb-4">Kafka Event Stream Activity</h2>
        <div class="overflow-x-auto">
          <table class="w-full text-left text-sm text-slate-300">
            <thead class="bg-slate-950 text-slate-400 uppercase text-xs">
              <tr>
                <th class="p-3">Claim ID</th>
                <th class="p-3">Provider</th>
                <th class="p-3">Amount</th>
                <th class="p-3">Saga Status</th>
                <th class="p-3">AI Fraud Score</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-800">
              <tr *ngFor="let claim of recentClaims()" class="hover:bg-slate-850">
                <td class="p-3 font-mono text-sky-400">{{ claim.id }}</td>
                <td class="p-3">{{ claim.provider }}</td>
                <td class="p-3">{{ claim.amount | currency }}</td>
                <td class="p-3">
                  <span class="px-2.5 py-1 text-xs rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                    {{ claim.status }}
                  </span>
                </td>
                <td class="p-3">
                  <span [class]="claim.score > 0.7 ? 'text-rose-400 font-bold' : 'text-slate-400'">
                    {{ claim.score }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `
})
export class DashboardComponent {
  recentClaims = signal([
    { id: 'CLM-88319', provider: 'PRV-1029 (Mayo Clinic)', amount: 14500.00, status: 'AUTO_APPROVED', score: 0.12 },
    { id: 'CLM-88320', provider: 'PRV-4491 (General Hospital)', amount: 8900.00, status: 'AUTO_APPROVED', score: 0.24 },
    { id: 'CLM-88321', provider: 'PRV-9912 (Metro Health)', amount: 24000.00, status: 'PENDING_AI_TRIAGE', score: 0.88 }
  ]);
}
