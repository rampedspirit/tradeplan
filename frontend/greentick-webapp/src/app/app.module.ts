import { NgModule, NgZone } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';

import { SignupComponent } from './components/auth/signup/signup.component';
import { LoginComponent } from './components/auth/login/login.component';
import { AuthComponent } from './components/auth/auth.component';
import { MainComponent } from './components/main/main.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatBadgeModule } from '@angular/material/badge';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSidenavModule } from '@angular/material/sidenav';
import { NgxSpinnerModule } from "ngx-spinner";
import { MatTableModule } from '@angular/material/table';
import { MatDatepickerModule } from '@angular/material/datepicker';

import { Amplify } from 'aws-amplify';
import { ResetComponent } from './components/auth/reset/reset.component';
import { VerifyComponent } from './components/auth/verify/verify.component';
import { FilterCreateComponent } from './components/filter/filter-create/filter-create.component';
import { FilterEditComponent } from './components/filter/filter-edit/filter-edit.component';
import { FilterListComponent } from './components/filter/filter-list/filter-list.component';
import { HttpClientModule } from '@angular/common/http';
import { FilterNameSearchPipe } from './components/filter/filter-name-search.pipe';
import { MonacoWrapper } from './services/editor.service';
import { MonacoEditorModule } from 'ngx-monaco-editor';
import { ConfirmationComponent } from './components/common/confirmation/confirmation.component';
import { MessageComponent } from './components/common/message/message.component';
import { environment } from 'src/environments/environment';

import { ApiModule as FilterApiModule } from 'src/gen/filter';
import { BASE_PATH as FILTER_API_BASE_PATH } from 'src/gen/filter';

import { ApiModule as ConditionApiModule } from 'src/gen/condition';
import { BASE_PATH as CONDITION_API_BASE_PATH } from 'src/gen/condition';

import { ApiModule as ScreenerApiModule } from 'src/gen/screener';
import { BASE_PATH as SCREENER_API_BASE_PATH } from 'src/gen/screener';

import { ConditionListComponent } from './components/condition/condition-list/condition-list.component';
import { ConditionCreateComponent } from './components/condition/condition-create/condition-create.component';
import { ConditionEditComponent } from './components/condition/condition-edit/condition-edit.component';
import { ConditionNameSearchPipe } from './components/condition/condition-name-search.pipe';
import { ScreenerListComponent } from './components/screener/screener-list/screener-list.component';
import { ScreenerCreateComponent } from './components/screener/screener-create/screener-create.component';
import { ScreenerEditComponent } from './components/screener/screener-edit/screener-edit.component';
import { ScreenerNameSearchPipe } from './components/screener/screener-name-search.pipe';
import { Router } from '@angular/router';
import { DocumentComponent } from './components/document/document.component';
import { ScreenerExecutableCreateComponent } from './components/screener/screener-executable-create/screener-executable-create.component';
import { MatNativeDateModule } from '@angular/material/core';
import { DatePipe } from '@angular/common';
import { ScreenerExecutableResultComponent } from './components/screener/screener-executable-result/screener-executable-result.component';
import { ConditionResultComponent } from './components/condition/condition-result/condition-result.component';

Amplify.configure({
  Auth: {
    mandatorySignIn: true,
    region: 'ap-south-1',
    userPoolId: 'ap-south-1_XbmXK37O2',
    userPoolWebClientId: '2h4d41bhrr467dtg341j90q652',
    authenticationFlowType: 'USER_PASSWORD_AUTH'
  }
});

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    MainComponent,
    AuthComponent,
    ResetComponent,
    VerifyComponent,
    FilterListComponent,
    FilterCreateComponent,
    FilterEditComponent,
    FilterNameSearchPipe,
    ConfirmationComponent,
    MessageComponent,
    ConditionListComponent,
    ConditionCreateComponent,
    ConditionEditComponent,
    ConditionNameSearchPipe,
    ScreenerListComponent,
    ScreenerCreateComponent,
    ScreenerEditComponent,
    ScreenerNameSearchPipe,
    DocumentComponent,
    ScreenerExecutableCreateComponent,
    ScreenerExecutableResultComponent,
    ConditionResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FilterApiModule,
    ConditionApiModule,
    ScreenerApiModule,
    HttpClientModule,
    MatCardModule,
    MatButtonModule,
    FlexLayoutModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatMenuModule,
    NgxSpinnerModule,
    MatDividerModule,
    MatTooltipModule,
    MatListModule,
    MatTabsModule,
    MatDialogModule,
    MatButtonToggleModule,
    MatBadgeModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatSidenavModule,
    MatTableModule,
    MatDatepickerModule,
    MatNativeDateModule,

    MonacoEditorModule.forRoot({
      onMonacoLoad: AppModule.onMonacoLoad
    })
  ],
  providers: [{
    provide: FILTER_API_BASE_PATH,
    useValue: environment.filterApiBasePath
  },
  {
    provide: CONDITION_API_BASE_PATH,
    useValue: environment.conditionApiBasePath
  },
  {
    provide: SCREENER_API_BASE_PATH,
    useValue: environment.screenerApiBasePath
  }, DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule {

  private static NGZONE: NgZone;
  private static ROUTER: Router;

  constructor(private ngZone: NgZone, private router: Router) {
    AppModule.NGZONE = ngZone;
    AppModule.ROUTER = router;
  }

  /**
   * Initialize the Monaco Editor Configurations
   */
  static onMonacoLoad() {
    let monaco: any = (<any>window).monaco;
    MonacoWrapper.register = monaco.languages.register;
    MonacoWrapper.setLanguageConfiguration = monaco.languages.setLanguageConfiguration;
    MonacoWrapper.setMonarchTokensProvider = monaco.languages.setMonarchTokensProvider;
    MonacoWrapper.registerDocumentFormattingEditProvider = monaco.languages.registerDocumentFormattingEditProvider;
    MonacoWrapper.registerCompletionItemProvider = monaco.languages.registerCompletionItemProvider;
    MonacoWrapper.registerHoverProvider = monaco.languages.registerHoverProvider;
    MonacoWrapper.defineTheme = monaco.editor.defineTheme;
    MonacoWrapper.setModelMarkers = monaco.editor.setModelMarkers;

    MonacoWrapper.onEditorLoad();

    monaco.editor.registerCommand("open-documentation", (ctx: any, args: any) => AppModule.openDocumentation(ctx, args));
  }

  static openDocumentation(ctx: any, args: any) {
    if (args.type == "function") {
      let baseUrl = window.location.href.replace(AppModule.ROUTER.url, '');
      window.open(baseUrl + "/doc/" + args.name, '_blank');
    }
  }
}
