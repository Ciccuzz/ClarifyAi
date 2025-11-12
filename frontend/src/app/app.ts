import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { ApiService, PromptRequest } from './services/api';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, NgIf],
  template: `
    <div class="container">
      <h1>ClarifyAI 🧠</h1>

      <form (ngSubmit)="sendPrompt()" class="form">
        <label>Text:</label>
        <textarea
          [(ngModel)]="prompt.text"
          name="text"
          rows="3"
          placeholder="Enter your text here..."
          required>
        </textarea>

        <label>Action:</label>
        <select [(ngModel)]="prompt.action" name="action">
                  <option value="">-- Select action --</option>
                  <option value="SUMMARY">Summary</option>
                  <option value="SIMPLIFY">Simplify</option>
                  <option value="TRANSLATE_IT">Translate-it</option>
                  <option value="TRANSLATE_EN">Translate-en</option>
                </select>

        <label>Length:</label>
        <select [(ngModel)]="prompt.length" name="length">
          <option value="">-- Select length --</option>
          <option value="VERY_SHORT">Very Short</option>
          <option value="SHORT">Short</option>
          <option value="MEDIUM">Medium</option>
          <option value="LONG">Long</option>
          <option value="EXTRA_LONG">Extra Long</option>
          <option value="TRANSLATION">Translation Length</option>
          <option value="PERSONALIZED">Personalized(choose the number of max words)</option>
        </select>

        <div *ngIf="prompt.length === 'PERSONALIZED'">
          <label>Max words:</label>
          <input
            type="number"
            [(ngModel)]="prompt.maxWords"
            name="maxWords"
            placeholder="Es: 200" />
        </div>

        <div class="submit-row">
          <button type="submit" [disabled]="isLoading">
            {{ isLoading ? 'Sending...' : 'Send Prompt' }}
          </button>

          <div *ngIf="isLoading" class="spinner-inline">
            <div class="spinner"></div>
            <span>Generating response...</span><
          </div>
        </div>
      </form>

      <div *ngIf="risposta" class="risposta">
        <h3>Response:</h3>
        <p>{{ risposta }}</p>
      </div>
    </div>


  `,
  styles: [`
    .container {
      max-width: 600px;
      margin: 40px auto;
      padding: 24px;
      border-radius: 12px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
      background: #fafafa;
      font-family: Arial, sans-serif;
    }
    textarea, input, select {
      width: 100%;
      margin-bottom: 12px;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 6px;
      font-size: 14px;
    }
    button {
      background: #007bff;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 6px;
      cursor: pointer;
    }
    button:hover { background: #0056b3; }
    .risposta {
      background: #e7f3ff;
      padding: 16px;
      border-radius: 8px;
      margin-top: 20px;
      white-space: pre-line;
    }
  `]
})
export class AppComponent {
  prompt: PromptRequest = {
    text: '',
    action: '',
    length: '',
    maxWords: null
  };

  risposta: string | null = null;
  isLoading = false;

  constructor(private api: ApiService) {}

    sendPrompt() {
      if (this.isLoading) return;
      this.isLoading = true;
      this.risposta = null;

      this.api.sendPrompt(this.prompt).subscribe({
        next: (res: any) => {
          this.risposta = res.result || res.response || 'Nessuna risposta ricevuta';
          this.isLoading = false;
        },
        error: (err: any) => {
          console.error('Errore API:', err);
          this.risposta = 'Errore nel server.';
          this.isLoading = false;
        }
      });
    }
  }
