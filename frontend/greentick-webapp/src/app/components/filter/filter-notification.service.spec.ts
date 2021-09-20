import { TestBed } from '@angular/core/testing';

import { FilterNotificationService } from './filter-notification.service';

describe('FilterNotificationService', () => {
  let service: FilterNotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FilterNotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
