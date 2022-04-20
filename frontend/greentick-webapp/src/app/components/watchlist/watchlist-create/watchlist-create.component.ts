import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { WatchlistService } from 'src/gen/watchlist';
import { FilterCreateComponent } from '../../filter/filter-create/filter-create.component';
import { WatchlistNotificationService } from '../watchlist-notification.service';

@Component({
  selector: 'app-watchlist-create',
  templateUrl: './watchlist-create.component.html',
  styleUrls: ['./watchlist-create.component.scss']
})
export class WatchlistCreateComponent implements OnInit {

  isValidating: boolean;

  createWatchlistForm: FormGroup;
  createError: boolean;

  get nameControl(): FormControl {
    return this.createWatchlistForm.get('name') as FormControl;
  }

  constructor(public dialogRef: MatDialogRef<FilterCreateComponent>, private watchlistService: WatchlistService,
    private watchlistNotificationService: WatchlistNotificationService, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.createWatchlistForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
    });
  }

  submit = () => {
    this.validateAllFields();
    if (this.createWatchlistForm.valid) {
      let name = this.createWatchlistForm.get('name')?.value;
      this.createWatchlist(name);
    }
  }

  private validateAllFields() {
    Object.values(this.createWatchlistForm.controls).forEach(control => {
      control.markAllAsTouched();
    });
  }

  private createWatchlist(name: string) {
    this.spinner.show();
    this.createError = false;
    this.watchlistService.createWatchlist({
      name: name
    }).subscribe(filter => {
      this.dialogRef.close();
      this.watchlistNotificationService.triggerCreateNotification(filter);
      this.spinner.hide();
    }, error => {
      this.createError = true;
      this.spinner.hide();
    });
  }
}
