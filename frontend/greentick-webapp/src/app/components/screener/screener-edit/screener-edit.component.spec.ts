import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScreenerEditComponent } from './screener-edit.component';

describe('ScreenerEditComponent', () => {
  let component: ScreenerEditComponent;
  let fixture: ComponentFixture<ScreenerEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScreenerEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreenerEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
