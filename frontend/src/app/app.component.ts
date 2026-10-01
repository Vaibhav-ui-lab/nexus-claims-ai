import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="min-h-screen flex flex-col">
      <nav class="bg-slate-900 border-b border-slate-800 px-6 py-4 flex justify-between items-center">
        <div class="flex items-center space-x-3">
          <div class="w-8 h-8 rounded-lg bg-sky-500 flex items-center justify-center font-bold text-slate-950">N</div>
          <span class="text-xl font-bold tracking-tight text-white">NexusClaims <span class="text-sky-400">AI</span></span>
        </div>
        <div class="flex space-x-6">
          <a routerLink="/dashboard" routerLinkActive="text-sky-400 border-b-2 border-sky-400" class="text-slate-300 hover:text-white py-1 transition font-medium">Dashboard</a>
          <a routerLink="/fraud-workbench" routerLinkActive="text-sky-400 border-b-2 border-sky-400" class="text-slate-300 hover:text-white py-1 transition font-medium">Fraud Workbench</a>
          <a routerLink="/submit-claim" routerLinkActive="text-sky-400 border-b-2 border-sky-400" class="text-slate-300 hover:text-white py-1 transition font-medium">Submit Claim</a>
        </div>
      </nav>
      <main class="flex-1">
        <router-outlet></router-outlet>
      </main>
    </div>
  `
})
export class AppComponent {
  title = 'NexusClaims AI';
}
