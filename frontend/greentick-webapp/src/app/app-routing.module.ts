import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './components/auth.guard';
import { AuthComponent } from './components/auth/auth.component';
import { LoginComponent } from './components/auth/login/login.component';
import { ResetComponent } from './components/auth/reset/reset.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { VerifyComponent } from './components/auth/verify/verify.component';
import { ConditionListComponent } from './components/condition/condition-list/condition-list.component';
import { DocumentComponent } from './components/document/document.component';
import { FeedbackComponent } from './components/feedback/feedback.component';
import { FilterListComponent } from './components/filter/filter-list/filter-list.component';
import { MainComponent } from './components/main/main.component';
import { ScreenerListComponent } from './components/screener/screener-list/screener-list.component';
import { WatchlistListComponent } from './components/watchlist/watchlist-list/watchlist-list.component';

const routes: Routes = [
  {
    path: "",
    component: MainComponent,
    canActivate: [AuthGuard],
    children: [
      { path: "filter/list", component: FilterListComponent },
      { path: "condition/list", component: ConditionListComponent },
      { path: "screener/list", component: ScreenerListComponent },
      { path: "watchlist/list", component: WatchlistListComponent }
    ]
  },
  {
    path: "auth",
    component: AuthComponent,
    children: [
      { path: "login", component: LoginComponent },
      { path: "signup", component: SignupComponent },
      { path: "reset", component: ResetComponent },
      { path: "verify", component: VerifyComponent }
    ]
  },
  { path: "feedback", component: FeedbackComponent },
  { path: "doc", component: DocumentComponent },
  { path: "doc/:name", component: DocumentComponent },
  { path: "**", redirectTo: "" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
