import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectionList, MatSelectionListChange } from '@angular/material/list';
import { NgxSpinnerService } from 'ngx-spinner';
import { TabAreaService, Tab } from 'src/app/services/tab-area.service';
import { WatchlistResponse, WatchlistService } from 'src/gen/watchlist';
import { WatchlistCreateComponent } from '../watchlist-create/watchlist-create.component';
import { WatchlistNotificationService } from '../watchlist-notification.service';

@Component({
  selector: 'app-watchlist-list',
  templateUrl: './watchlist-list.component.html',
  styleUrls: ['./watchlist-list.component.scss']
})
export class WatchlistListComponent implements OnInit {

  @ViewChild(MatSelectionList)
  watchlistList!: MatSelectionList;

  public watchlists: WatchlistResponse[];
  public fetchError = false;

  constructor(private watchlistService: WatchlistService, private watchlistNotificationService: WatchlistNotificationService,
    private tabAreaService: TabAreaService, private dialog: MatDialog, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.refresh();

    this.watchlistNotificationService.createSubject.subscribe(watchlist => {
      this.refresh();
      this.openTab(watchlist);
    });

    this.watchlistNotificationService.updateSubject.subscribe(watchlist => {
      this.refresh();
    });

    this.watchlistNotificationService.deleteSubject.subscribe(id => {
      this.refresh();
      this.tabAreaService.closeTab(id);
    });
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();

    this.watchlistService.getAllWatchlists().subscribe(watchlists => {
      this.watchlists = watchlists;
      this.spinner.hide();
    }, error => {
      this.spinner.hide();
      this.fetchError = true;
    });
  }

  openCreateWatchlistDialog = () => {
    const dialogRef = this.dialog.open(WatchlistCreateComponent, {
      minWidth: "30%"
    });
  }

  onSelectionChange(event: MatSelectionListChange) {
    this.watchlistList.deselectAll();
    let selectedWatchlist: WatchlistResponse = event.options[0].value;
    this.openTab(selectedWatchlist);
  }

  private openTab(watchlist: WatchlistResponse) {
    let tab: Tab = {
      id: watchlist.watchlistId,
      type: 'watchlist',
      title: watchlist.name,
      dirtyFlag: false
    }
    this.tabAreaService.openTab(tab);
  }

}
