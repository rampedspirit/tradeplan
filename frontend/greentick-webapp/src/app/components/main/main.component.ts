import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditorService } from 'src/app/services/editor.service';
import { Tab, TabAreaService } from 'src/app/services/tab-area.service';
import { ConfirmationComponent } from '../common/confirmation/confirmation.component';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  isSmallScreen: boolean;

  constructor(public tabAreaService: TabAreaService, private dialog: MatDialog,
    private breakpointObserver: BreakpointObserver) {
    breakpointObserver.observe([
      Breakpoints.XSmall,
      Breakpoints.Small,
      Breakpoints.Medium,
      Breakpoints.Large,
      Breakpoints.XLarge,
    ]).subscribe(breakpointState => {
      this.isSmallScreen = breakpointState.breakpoints[Breakpoints.XSmall] || breakpointState.breakpoints[Breakpoints.Small];
    })
  }

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
