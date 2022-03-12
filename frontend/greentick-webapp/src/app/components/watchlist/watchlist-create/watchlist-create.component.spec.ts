import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchlistCreateComponent } from './watchlist-create.component';

describe('WatchlistCreateComponent', () => {
  let component: WatchlistCreateComponent;
  let fixture: ComponentFixture<WatchlistCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WatchlistCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WatchlistCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
