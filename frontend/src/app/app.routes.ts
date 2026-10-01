import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { FraudWorkbenchComponent } from './features/fraud-workbench/fraud-workbench.component';
import { ClaimSubmissionComponent } from './features/claim-submission/claim-submission.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'fraud-workbench', component: FraudWorkbenchComponent, canActivate: [authGuard] },
  { path: 'submit-claim', component: ClaimSubmissionComponent },
  { path: '**', redirectTo: 'dashboard' }
];
