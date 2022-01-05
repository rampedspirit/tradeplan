import { TestBed } from '@angular/core/testing';

import { ConditionNotificationService } from './condition-notification.service';

describe('ConditionNotificationService', () => {
  let service: ConditionNotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConditionNotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
