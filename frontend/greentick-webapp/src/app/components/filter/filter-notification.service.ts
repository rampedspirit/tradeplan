import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { FilterResponse } from 'src/gen/filter';

@Injectable({
  providedIn: 'root'
})
export class FilterNotificationService {

  public createSubject: Subject<FilterResponse>;
  public updateSubject: Subject<FilterResponse>;
  public deleteSubject: Subject<string>;

  constructor() {
    this.createSubject = new Subject<FilterResponse>();
    this.updateSubject = new Subject<FilterResponse>();
    this.deleteSubject = new Subject<string>();
  }

  public triggerCreateNotification(filter: FilterResponse) {
    this.createSubject.next(filter);
  }

  public triggerUpdateNotification(filter: FilterResponse) {
    this.updateSubject.next(filter);
  }

  public triggerDeleteNotification(id: string) {
    this.deleteSubject.next(id);
  }
}
