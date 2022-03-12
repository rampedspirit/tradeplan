import { TestBed } from '@angular/core/testing';

import { WatchlistNotificationService } from './watchlist-notification.service';

describe('WatchlistNotificationService', () => {
  let service: WatchlistNotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WatchlistNotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
