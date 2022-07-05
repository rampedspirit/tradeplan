import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatListOption } from '@angular/material/list';
import { NgxSpinnerService } from 'ngx-spinner';
import { Tab } from 'src/app/services/tab-area.service';
import { SymbolResponse, SymbolService, WatchlistService } from 'src/gen/watchlist';
import { ConfirmationComponent } from '../../common/confirmation/confirmation.component';
import { MessageComponent } from '../../common/message/message.component';
import { WatchlistNotificationService } from '../watchlist-notification.service';

@Component({
  selector: 'app-watchlist-edit',
  templateUrl: './watchlist-edit.component.html',
  styleUrls: ['./watchlist-edit.component.scss']
})
export class WatchlistEditComponent implements OnInit {

  fetchError: boolean;
  editWatchlistForm: FormGroup;

  symbols: SymbolResponse[];
  scripNames: string[];

  @Input()
  tab: Tab;

  get nameControl(): FormControl {
    return this.editWatchlistForm.get('name') as FormControl;
  }

  get descriptionControl(): FormControl {
    return this.editWatchlistForm.get('description') as FormControl;
  }

  constructor(private watchlistService: WatchlistService, private watchlistNotificationService: WatchlistNotificationService,
    private symbolService: SymbolService, private dialog: MatDialog, private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.refresh();
    this.refresh2();
  }

  refresh = () => {
    this.fetchError = false;
    this.spinner.show();
    this.watchlistService.getWatchlist(this.tab.id).subscribe(watchlist => {
      this.scripNames = watchlist.scripNames;
      this.editWatchlistForm = new FormGroup({
        name: new FormControl(watchlist.name, [Validators.required]),
        description: new FormControl(watchlist.description, [Validators.required]),
      });
      this.editWatchlistForm.valueChanges.subscribe(change => {
        this.tab.dirtyFlag = true;
      });
      this.spinner.hide();
    }, error => {
      this.fetchError = true;
      this.spinner.hide();
    });
  }

  refresh2 = () => {
    this.fetchError = false;
    this.spinner.show();
    this.symbolService.getAllSymbols().subscribe(allSymbols => {
      this.symbols = allSymbols;
      this.spinner.hide();
    }, error => {
      this.fetchError = true;
      this.spinner.hide();
    });
  }

  onSelectionChange(selectedOptions: MatListOption[]) {
    this.scripNames = selectedOptions.map(sel => sel.value);
    this.tab.dirtyFlag = true;
  }

  save = () => {
    //Validate fields
    Object.values(this.editWatchlistForm.controls).forEach(control => {
      control.markAllAsTouched();
    });

    if (this.editWatchlistForm.valid && this.scripNames.length > 0) {
      let name = this.editWatchlistForm.get('name')?.value;
      let description = this.editWatchlistForm.get('description')?.value;
      this.spinner.show();
      this.watchlistService.updateWatchlist(
        [{
          operation: 'REPLACE',
          property: 'NAME',
          value: name
        },
        {
          operation: 'REPLACE',
          property: 'DESCRIPTION',
          value: description
        },
        {
          operation: 'REPLACE',
          property: 'SCRIP_NAMES',
          value: this.scripNames.join(",")
        }], this.tab.id).subscribe(watchlist => {
          this.tab.title = watchlist.name;
          this.tab.dirtyFlag = false;
          this.watchlistNotificationService.triggerUpdateNotification(watchlist);
          this.spinner.hide();
        }, error => {
          this.spinner.hide();
          this.dialog.open(MessageComponent, {
            data: {
              type: "error",
              message: "Failed to save watchlist.",
              callbackText: "Retry",
              callback: this.save
            }
          });
        })
    }
  }

  delete = () => {
    const dialogRef = this.dialog.open(ConfirmationComponent, {
      data: {
        title: "Delete Watchlist",
        icon: "warning",
        message: "Are you sure to delete ?"
      }
    });

    dialogRef.afterClosed().subscribe(okToDelete => {
      if (okToDelete) {
        this.spinner.show();
        this.watchlistService.deleteWatchlist(this.tab.id).subscribe(watchlist => {
          this.spinner.hide();
          this.watchlistNotificationService.triggerDeleteNotification(watchlist.watchlistId);
        }, error => {
          this.dialog.open(MessageComponent, {
            data: {
              type: "error",
              message: "Failed to delete watchlist.",
              callbackText: "Retry",
              callback: this.delete
            }
          });
          this.spinner.hide();
        });
      }
    });
  }
}
