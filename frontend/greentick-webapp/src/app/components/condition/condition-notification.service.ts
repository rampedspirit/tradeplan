import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Condition } from 'src/gen/condition';

@Injectable({
  providedIn: 'root'
})
export class ConditionNotificationService {

  public createSubject: Subject<Condition>;
  public updateSubject: Subject<Condition>;
  public deleteSubject: Subject<string>;

  constructor() {
    this.createSubject = new Subject<Condition>();
    this.updateSubject = new Subject<Condition>();
    this.deleteSubject = new Subject<string>();
  }

  public triggerCreateNotification(condition: Condition) {
    this.createSubject.next(condition);
  }

  public triggerUpdateNotification(condition: Condition) {
    this.updateSubject.next(condition);
  }

  public triggerDeleteNotification(id: string) {
    this.deleteSubject.next(id);
  }
}
