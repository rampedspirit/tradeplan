import { TestBed } from '@angular/core/testing';

import { ScreenerNotificationService } from './screener-notification.service';

describe('ScreenerNotificationService', () => {
  let service: ScreenerNotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScreenerNotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
