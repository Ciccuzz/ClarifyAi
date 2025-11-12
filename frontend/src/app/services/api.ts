import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PromptRequest {
  text: string;
  action: string;
  length: string;
  maxWords: number | null;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api'; // ✅ backend Spring Boot

  constructor(private http: HttpClient) {}

  sendPrompt(prompt: PromptRequest): Observable<any> {
    return this.http.post(this.apiUrl, prompt);
  }
}
