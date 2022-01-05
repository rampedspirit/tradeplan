import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface ConfirmationInfo {
  title: string,
  icon: string,
  message: string
}

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.scss']
})
export class ConfirmationComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: ConfirmationInfo) { }

  ngOnInit(): void {
  }

}
