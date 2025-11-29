import { Component, Input } from '@angular/core';
import { NgClass } from '@angular/common';
import { Message } from '../../models/message.model';

@Component({
  selector: 'app-message',
  standalone: true,
  imports: [NgClass],
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent {
  @Input() msg!: Message;
}
