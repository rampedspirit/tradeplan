import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ConditionDetailedResponse } from 'src/gen/condition';

@Injectable({
  providedIn: 'root'
})
export class ConditionNotificationService {

  public createSubject: Subject<ConditionDetailedResponse>;
  public updateSubject: Subject<ConditionDetailedResponse>;
  public deleteSubject: Subject<string>;

  constructor() {
    this.createSubject = new Subject<ConditionDetailedResponse>();
    this.updateSubject = new Subject<ConditionDetailedResponse>();
    this.deleteSubject = new Subject<string>();
  }

  public triggerCreateNotification(condition: ConditionDetailedResponse) {
    this.createSubject.next(condition);
  }

  public triggerUpdateNotification(condition: ConditionDetailedResponse) {
    this.updateSubject.next(condition);
  }

  public triggerDeleteNotification(id: string) {
    this.deleteSubject.next(id);
  }
}
