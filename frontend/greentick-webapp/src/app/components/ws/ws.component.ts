import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';

interface IMethodRequest {
  method: string;
  symbols: string[];
}

@Component({
  selector: 'app-ws',
  templateUrl: './ws.component.html',
  styleUrls: ['./ws.component.scss']
})
export class WsComponent implements OnInit {

  private webSocket: WebSocket;

  connectForm: FormGroup;

  error: string;

  payload: string;
  response: string;

  allMethods: IMethodRequest[];
  selectedMethod: IMethodRequest;

  get nameControl(): FormControl {
    return this.connectForm.get('name') as FormControl;
  }

  get passwordControl(): FormControl {
    return this.connectForm.get('password') as FormControl;
  }

  constructor(private spinner: NgxSpinnerService) {
    this.allMethods = [];

    this.allMethods.push({
      method: "addsymbol",
      symbols: ["5PAISA", "ADANIENT", "SOBHA"]
    });

    this.allMethods.push({
      method: "removesymbol",
      symbols: ["ADANIENT", "SOBHA"]
    });
  }

  ngOnInit(): void {
    this.reset();
    this.connectForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      password: new FormControl(null, [Validators.required])
    });
  }

  doLogin() {
    this.reset();

    let name = this.connectForm.get('name')?.value;
    let password = this.connectForm.get('password')?.value;
    let url: string = 'ws://localhost:5006?user=' + name + '&password=' + password;

    this.webSocket = new WebSocket(url);
    this.webSocket.onopen = (e) => this.onConnectionOpen(e);
    this.webSocket.onclose = (e) => this.onConnectionClose(e);
    this.webSocket.onerror = (e) => this.onError(e);
    this.webSocket.onmessage = (e) => this.onMessage(e);
  }

  doLogout() {
    if (this.webSocket != null) {
      let logoutRequest: IMethodRequest = {
        method: "logout",
        symbols: []
      };
      this.webSocket.send(JSON.stringify(logoutRequest));
    }
  }

  doDisconnect() {
    if (this.webSocket != null) {
      this.webSocket.close();
    }
  }

  doSend() {
    this.reset();
    if (this.webSocket != null) {
      this.webSocket.send(this.payload);
    }
  }

  private reset() {
    this.error = null;
    this.response = null;
    this.spinner.hide();
  }

  private onConnectionOpen(event: Event) {
    console.log("On Connection Open : " + event);
  }

  private onConnectionClose(event: Event) {
    console.log("On Connection Close : " + event);

    this.webSocket.onopen = null;
    this.webSocket.onclose = null;
    this.webSocket.onerror = null;
    this.webSocket.onmessage = null;
    this.webSocket = null;
  }

  private onError(event: Event) {
    console.log("Error : " + event);
  }

  private onMessage(e: Event) {
    let responseData = JSON.parse((e as MessageEvent).data);
    this.response = JSON.stringify(responseData, null, 2);
  }

  public onMethodSelectionChange() {
    this.payload = JSON.stringify(this.selectedMethod, null, 2);
  }
}
