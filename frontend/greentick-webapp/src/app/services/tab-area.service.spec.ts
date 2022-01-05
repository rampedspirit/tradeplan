import { TestBed } from '@angular/core/testing';

import { TabAreaService } from './tab-area.service';

describe('TabAreaService', () => {
  let service: TabAreaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TabAreaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
