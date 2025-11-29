declare const chrome: any;

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ContextService {

  private lastContext = "";

  constructor() {
    // 🔹 L’IFRAME ASCOLTA LA RISPOSTA DAL BACKGROUND
    chrome.runtime.onMessage.addListener((msg: any) => {

      console.log("IFRAME RECEIVED:", msg);

      if (msg?.type === "CLARIFYAI_PAGE_CONTEXT_TO_IFRAME") {
        this.lastContext = msg.payload || "";
        console.log("CONTEXT SAVED:", this.lastContext);
      }
    });
  }

  // 🔹 L’IFRAME CHIEDE AL BACKGROUND DI PRENDERE IL CONTESTO
  refresh() {
    chrome.runtime.sendMessage({
      type: "CLARIFYAI_IFRAME_GET_CONTEXT"
    });
  }

  // 🔹 RESTITUISCE IL CONTENUTO SALVATO
  getPageContext() {
    return this.lastContext;
  }
}
