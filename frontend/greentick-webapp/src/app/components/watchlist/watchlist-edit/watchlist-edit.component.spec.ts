import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchlistEditComponent } from './watchlist-edit.component';

describe('WatchlistEditComponent', () => {
  let component: WatchlistEditComponent;
  let fixture: ComponentFixture<WatchlistEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WatchlistEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WatchlistEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
