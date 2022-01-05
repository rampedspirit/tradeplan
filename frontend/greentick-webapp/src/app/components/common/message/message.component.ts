import { Component, Inject, Input, OnInit, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface MessageInfo {
  type: string
  message: string
  callbackText: string;
  callback: () => void;
}

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent {

  icon: string;
  backgroundColor: string;

  @Input()
  set type(value: string) {
    if (value == 'info') {
      this.icon = "info_outline";
      this.backgroundColor = '#99cc33';
    } else if (value == 'warn') {
      this.icon = "warning";
      this.backgroundColor = '#ffcc00';
    } else {
      this.icon = "error_outline";
      this.backgroundColor = '#ff9966';
    }
  }

  @Input()
  message: string;

  @Input()
  callbackText: string;

  @Input()
  callback: () => void;

  constructor(@Optional() public dialogRef: MatDialogRef<MessageComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: MessageInfo) {
    if (data) {
      this.type = data.type;
      this.message = data.message;
      this.callbackText = data.callbackText;
      this.callback = data.callback;
    }
  }

  executeCallback() {
    this.callback();
    if (this.dialogRef) {
      this.dialogRef.close();
    }
  }
}
