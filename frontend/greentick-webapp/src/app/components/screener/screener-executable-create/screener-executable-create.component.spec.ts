import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScreenerExecutableCreateComponent } from './screener-executable-create.component';

describe('ScreenerExecutableCreateComponent', () => {
  let component: ScreenerExecutableCreateComponent;
  let fixture: ComponentFixture<ScreenerExecutableCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScreenerExecutableCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreenerExecutableCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
