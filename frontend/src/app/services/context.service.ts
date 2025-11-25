declare const chrome: any;

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ContextService {

  private lastContext = "";

  constructor() {
    chrome.runtime.onMessage.addListener((msg: any) => {
      if (msg?.type === "CLARIFYAI_PAGE_CONTEXT") {
        this.lastContext = msg.payload || "";
      }
    });
  }

  refresh() {
    chrome.runtime.sendMessage({ type: "CLARIFYAI_GET_CONTEXT" });
  }

  getPageContext() {
    return this.lastContext;
  }
}
