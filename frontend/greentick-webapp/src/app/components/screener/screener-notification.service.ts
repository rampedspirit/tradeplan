import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ScreenerResponse } from 'src/gen/screener';

@Injectable({
  providedIn: 'root'
})
export class ScreenerNotificationService {

  public createSubject: Subject<ScreenerResponse>;
  public updateSubject: Subject<ScreenerResponse>;
  public deleteSubject: Subject<string>;

  constructor() {
    this.createSubject = new Subject<ScreenerResponse>();
    this.updateSubject = new Subject<ScreenerResponse>();
    this.deleteSubject = new Subject<string>();
  }

  public triggerCreateNotification(screener: ScreenerResponse) {
    this.createSubject.next(screener);
  }

  public triggerUpdateNotification(screener: ScreenerResponse) {
    this.updateSubject.next(screener);
  }

  public triggerDeleteNotification(id: string) {
    this.deleteSubject.next(id);
  }
}
