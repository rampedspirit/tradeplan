import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './components/auth.guard';
import { AuthComponent } from './components/auth/auth.component';
import { LoginComponent } from './components/auth/login/login.component';
import { ResetComponent } from './components/auth/reset/reset.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { VerifyComponent } from './components/auth/verify/verify.component';
import { FilterListComponent } from './components/filter/filter-list/filter-list.component';
import { MainComponent } from './components/main/main.component';

const routes: Routes = [
  {
    path: "",
    component: MainComponent,
    canActivate: [AuthGuard],
    children: [
      { path: "filter/list", component: FilterListComponent }
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
  { path: "**", redirectTo: "" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
