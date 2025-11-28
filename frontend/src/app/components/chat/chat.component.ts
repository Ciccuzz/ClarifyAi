import { Component, inject, HostListener } from '@angular/core';
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

  // ⭐ Dropdown state
  showContextMenu = false;
  pageTitle = "Caricamento…";

  ngOnInit() {
    this.refreshPageContext();
  }

  // Chiudi dropdown cliccando fuori
  @HostListener("document:click", ["$event"])
  handleOutside(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest(".context-wrapper")) {
      this.showContextMenu = false;
    }
  }

  toggleContextMenu(event: MouseEvent) {
    event.stopPropagation();
    this.showContextMenu = !this.showContextMenu;
  }

  private extractTitle(context: string): string {
    if (!context) return "Pagina sconosciuta";

    const firstSentence = context.split(".")[0];
    return firstSentence.split(" ").slice(0, 8).join(" ");
  }

  refreshPageContext() {
    this.context.refresh();

    setTimeout(() => {
      const ctx = this.context.getPageContext() || "";
      this.pageTitle = this.extractTitle(ctx);
    }, 500);

    this.showContextMenu = false;
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
}
