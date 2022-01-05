import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Filter } from 'src/gen/filter';

@Injectable({
  providedIn: 'root'
})
export class FilterNotificationService {

  public createSubject: Subject<Filter>;
  public updateSubject: Subject<Filter>;
  public deleteSubject: Subject<string>;

  constructor() {
    this.createSubject = new Subject<Filter>();
    this.updateSubject = new Subject<Filter>();
    this.deleteSubject = new Subject<string>();
  }

  public triggerCreateNotification(filter: Filter) {
    this.createSubject.next(filter);
  }

  public triggerUpdateNotification(filter: Filter) {
    this.updateSubject.next(filter);
  }

  public triggerDeleteNotification(id: string) {
    this.deleteSubject.next(id);
  }
}
