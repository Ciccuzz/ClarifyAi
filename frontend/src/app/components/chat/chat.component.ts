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

  // Dropdown
  showContextMenu = false;
  pageTitle = "Caricamento…";

  // Nuova variabile per la sessione con il backend
  private sessionId: string | null = null;

  ngOnInit() {
    this.initializeSession();
  }

  // -------------------------------------------------------
  //   AVVIO SESSIONE: RECUPERO DEL CONTESTO + API START
  // -------------------------------------------------------
  async initializeSession() {

    // 1) Richiede al content-script il contesto della pagina
    this.context.refresh();

    // 2) Attesa per la risposta del content-script
    setTimeout(async () => {

      const fullContext = this.context.getPageContext() || "";

      this.pageTitle = this.extractTitle(fullContext);

      // 3) Chiamiamo il backend per creare una sessione
      try {
        const res: any = await this.api.startSession(fullContext).toPromise();
        this.sessionId = res.sessionId;

        console.log("🔵 Sessione avviata:", this.sessionId);

      } catch (e) {
        console.error("Errore startSession:", e);
        this.messages.push({
          text: "Errore nel creare la sessione con il server.",
          from: "ai"
        });
      }

    }, 500);
  }

  // -------------------------------------------------------
  //   CHIUSURA DROPDOWN SE CLICCHI FUORI
  // -------------------------------------------------------
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

  // -------------------------------------------------------
  //   INVIO DEL MESSAGGIO
  // -------------------------------------------------------
  async sendMessage() {

    const txt = this.userInput.trim();
    if (!txt) return;

    if (!this.sessionId) {
      this.messages.push({
        text: "Errore: sessione non inizializzata.",
        from: "ai"
      });
      return;
    }

    // Mostriamo il messaggio dell’utente
    this.messages.push({ text: txt, from: "user" });
    this.userInput = "";

    // Mostriamo il loader
    this.isThinking = true;
    this.messages.push({ text: "Sto pensando…", from: "ai", temp: true });

    try {
      const res: any = await this.api.sendMessage(this.sessionId, txt).toPromise();

      // Rimuovi "sto pensando"
      this.messages = this.messages.filter(m => !m.temp);

      this.messages.push({
        text: res.response || res.result || "Non ho ricevuto risposta.",
        from: "ai"
      });

    } catch (e) {
      console.error("Errore nel messaggio:", e);

      this.messages = this.messages.filter(m => !m.temp);
      this.messages.push({
        text: "Errore di comunicazione col server.",
        from: "ai"
      });
    }

    this.isThinking = false;
  }
}
