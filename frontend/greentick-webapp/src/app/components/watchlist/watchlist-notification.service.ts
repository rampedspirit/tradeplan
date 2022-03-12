import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { FilterResponse } from 'src/gen/filter';
import { WatchlistResponse } from 'src/gen/watchlist';

@Injectable({
  providedIn: 'root'
})
export class WatchlistNotificationService {

  public createSubject: Subject<WatchlistResponse>;
  public updateSubject: Subject<WatchlistResponse>;
  public deleteSubject: Subject<string>;

  constructor() {
    this.createSubject = new Subject<WatchlistResponse>();
    this.updateSubject = new Subject<WatchlistResponse>();
    this.deleteSubject = new Subject<string>();
  }

  public triggerCreateNotification(watchlist: WatchlistResponse) {
    this.createSubject.next(watchlist);
  }

  public triggerUpdateNotification(watchlist: WatchlistResponse) {
    this.updateSubject.next(watchlist);
  }

  public triggerDeleteNotification(id: string) {
    this.deleteSubject.next(id);
  }
}
