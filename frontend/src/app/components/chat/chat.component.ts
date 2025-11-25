import { Component, inject } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Message } from '../../models/message.model';
import { ApiService } from '../../services/api.service';
import { ContextService } from '../../services/context.service';
import { MessageComponent } from '../messages/message.component';

@Component({
  selector: 'clarify-chat',
  standalone: true,
  imports: [NgFor, NgIf, FormsModule, MessageComponent],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {

  userInput = "";
  messages: Message[] = [
    { text: "Ciao! Sono qui per aiutarti.", from: "ai" }
  ];

  private api = inject(ApiService);
  private context = inject(ContextService);

  isThinking = false;

  ngOnInit() {
    this.context.refresh();
  }

  async sendMessage() {
    const txt = this.userInput.trim();
    if (!txt) return;

    this.messages.push({ text: txt, from: "user" });
    this.userInput = "";

    this.isThinking = true;
    this.messages.push({ text: "Sto pensando…", from: "ai", temp: true });

    const pageText = this.context.getPageContext();

    try {
      const res = await this.api.sendText(txt, pageText).toPromise();

      this.messages = this.messages.filter(m => !m.temp);

      this.messages.push({
        text: res.response || res.result || "Non ho ricevuto risposta.",
        from: "ai"
      });

    } catch {
      this.messages = this.messages.filter(m => !m.temp);
      this.messages.push({ text: "Errore di comunicazione col server.", from: "ai" });
    }

    this.isThinking = false;
  }

  refreshPageContext() {
    this.context.refresh();
    this.messages.push({ from: "ai", text: "Contesto aggiornato." });
  }
}
