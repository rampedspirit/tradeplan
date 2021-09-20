import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Tab, TabAreaService } from 'src/app/services/tab-area.service';
import { Filter } from 'src/app/_gen';
import { ConfirmationComponent } from '../common/confirmation/confirmation.component';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  constructor(public tabAreaService: TabAreaService, private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  closeTab(tab: Tab) {
    if (tab.dirtyFlag) {
      const dialogRef = this.dialog.open(ConfirmationComponent, {
        data: {
          title: "Close Tab [ " + tab.title + " ]",
          icon: "warning",
          message: "This tab contains unsaved changes. You will lose the changes if you continue."
        }
      });

      dialogRef.afterClosed().subscribe(okToClose => {
        if (okToClose) {
          this.tabAreaService.closeTab(tab.id);
        }
      });
    }
    else {
      this.tabAreaService.closeTab(tab.id);
    }
  }
}
