import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScreenerListComponent } from './screener-list.component';

describe('ScreenerListComponent', () => {
  let component: ScreenerListComponent;
  let fixture: ComponentFixture<ScreenerListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScreenerListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreenerListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
