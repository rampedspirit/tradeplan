import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { FeedbackComponent } from './components/feedback/feedback.component';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(public authService: AuthService, private router: Router, private dialog: MatDialog) { }

  logout() {
    this.authService.logout();
    this.router.navigateByUrl("/auth");
  }

  openFeedback() {
    const dialogRef = this.dialog.open(FeedbackComponent, {
      minWidth: "30%"
    });
  }
}
