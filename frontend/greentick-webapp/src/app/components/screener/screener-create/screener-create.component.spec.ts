import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScreenerCreateComponent } from './screener-create.component';

describe('ScreenerCreateComponent', () => {
  let component: ScreenerCreateComponent;
  let fixture: ComponentFixture<ScreenerCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScreenerCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreenerCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
