import { NgModule } from '@angular/core';
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

import { SignupComponent } from './components/auth/signup/signup.component';
import { LoginComponent } from './components/auth/login/login.component';
import { AuthComponent } from './components/auth/auth.component';
import { MainComponent } from './components/main/main.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { NgxSpinnerModule } from "ngx-spinner";

import { Amplify } from 'aws-amplify';
import { ResetComponent } from './components/auth/reset/reset.component';
import { VerifyComponent } from './components/auth/verify/verify.component';
import { FilterCreateComponent } from './components/filter/filter-create/filter-create.component';
import { FilterEditComponent } from './components/filter/filter-edit/filter-edit.component';
import { FilterListComponent } from './components/filter/filter-list/filter-list.component';
import { ApiModule } from './_gen';
import { HttpClientModule } from '@angular/common/http';
import { FilterNameSearchPipe } from './components/filter/filter-name-search.pipe';
import { MonacoWrapper } from './services/editor.service';
import { MonacoEditorModule } from 'ngx-monaco-editor';
import { ConfirmationComponent } from './components/common/confirmation/confirmation.component';
import { MessageComponent } from './components/common/message/message.component';

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
    MessageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ApiModule,
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
    MatSnackBarModule,
    MonacoEditorModule.forRoot({
      onMonacoLoad: AppModule.onMonacoLoad
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { 
  
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
    MonacoWrapper.setModelMarkers = monaco.editor.setModelMarkers

    MonacoWrapper.onEditorLoad();
  }
}
