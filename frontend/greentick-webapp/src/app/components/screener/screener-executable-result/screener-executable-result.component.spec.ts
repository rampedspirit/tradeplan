import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScreenerExecutableResultComponent } from './screener-executable-result.component';

describe('ScreenerExecutableResultComponent', () => {
  let component: ScreenerExecutableResultComponent;
  let fixture: ComponentFixture<ScreenerExecutableResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScreenerExecutableResultComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreenerExecutableResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
