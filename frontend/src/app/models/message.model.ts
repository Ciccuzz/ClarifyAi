export interface Message {
  text: string;
  from: 'user' | 'ai';
  temp?: boolean;
}
