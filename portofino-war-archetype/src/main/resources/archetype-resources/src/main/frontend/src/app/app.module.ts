import {Component, NgModule, Optional} from '@angular/core';
import {
  PortofinoModule, PortofinoUpstairsModule, AuthenticationService,
  NotificationService, MatSnackBarNotificationService, NOTIFICATION_HANDLERS,
  Page, PortofinoComponent, PortofinoService, SidenavService,
  CrudComponent, CustomPageComponent, TextPageComponent,
  UpstairsComponent,
  PageLayout
} from "portofino";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatRadioModule } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatSortModule } from "@angular/material/sort";
import { MatTableModule } from "@angular/material/table";
import { MatToolbarModule } from "@angular/material/toolbar";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {QuillModule} from "ngx-quill";
import {HttpClientModule, HttpClient} from "@angular/common/http";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatMomentDateModule} from "@angular/material-moment-adapter";
import {FileInputAccessorModule} from "file-input-accessor";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {registerLocaleData} from "@angular/common";

//Customize for your app's locales, if any. As of version 5.1.3, Portofino is translated to Spanish and Italian.
//Additional translations are welcome!
import localeEs from "@angular/common/locales/es";
import localeIt from "@angular/common/locales/it";

registerLocaleData(localeEs);
registerLocaleData(localeIt);

@Component({
  selector: 'app-root',
  template: `<portofino-app appTitle="Portofino Application" apiRoot="http://localhost:8080/api/"></portofino-app>`
})
export class AppComponent {}

@Component({
  selector: 'portofino-welcome',
  template: `
    <portofino-page-layout [page]="this">
      <ng-template #content>
        <p>Welcome to Portofino 5. This is your new, empty application.</p>
        <p>
          Use the navigation button
          <button title="{{ 'Navigation' | translate }}" type="button" mat-icon-button
                  (click)="sidenav.toggle()">
            <mat-icon aria-label="Side nav toggle icon">menu</mat-icon>
          </button>
          to explore the pages.
        </p>
        <p>
          Initially, the application has the user admin/admin built in.
          You can use that to <a [routerLink]="portofino.upstairsLink + '/wizard'">run the wizard</a>,
          connect to your database, and build a complete application from it in a few clicks. Please refer to the
          <a href="https://github.com/ManyDesigns/Portofino/wiki/Getting-started-with-Portofino-5">getting started page</a>.
        </p>
        <p>
          The wizard is one of the tools that can be found in the administration section
          <a [routerLink]="portofino.upstairsLink">"upstairs"</a> (link in the toolbar).
          The "upstairs" section is optional and can be disabled in production, leaving only the "downstairs" floor,
          i.e., the application.
          <small>"Upstairs" and "downstairs" are historical references to Portofino 3, which used the same model-driven interface
            both for the application and for the application's model (the metamodel).</small>
        </p>
        <h3>API</h3>
        <p>
          Your application exposes a REST API which depends on the resource-actions that it's made of (when you create
          a page, a corresponding resource-action is created for you).
          You can download <a [href]="portofino.apiRoot">the documentation of the REST API</a> in OpenAPI (aka Swagger)
          format. This is machine-readable, so you have the option of generating a client automatically in many
          programming languages.
          Note that currently the documentation is cached, and if you change the structure of the application, you'll
          have to restart it in order to see the updated documentation.
        </p>
        <h3>I want to know more</h3>
        <p>
          You can find additional documentation in the <a href="https://github.com/ManyDesigns/Portofino/wiki">wiki</a>
          and in the <a href="https://portofino.manydesigns.com/en/docs">documentation center</a>, which was written for
          Portofino 4.x, but is in many cases still applicable.
        </p>
      </ng-template>
    </portofino-page-layout>`
})
@PortofinoComponent({ name: 'welcome' })
export class WelcomeComponent extends Page {
  constructor(
    portofino: PortofinoService, http: HttpClient, router: Router,
    @Optional() route: ActivatedRoute, authenticationService: AuthenticationService,
    notificationService: NotificationService, translate: TranslateService,
    public sidenav: SidenavService) {
    super(portofino, http, router, route, authenticationService, notificationService, translate);
  }
}

@NgModule({
  declarations: [AppComponent, WelcomeComponent],
  providers: [
    { provide: NOTIFICATION_HANDLERS, useClass: MatSnackBarNotificationService, multi: true }
  ],
  imports: [
    RouterModule.forRoot([...PortofinoModule.defaultRoutes()], PortofinoModule.defaultRouterConfig()),
    PortofinoModule, PortofinoUpstairsModule,
    BrowserModule, BrowserAnimationsModule, FlexLayoutModule, FormsModule, HttpClientModule, ReactiveFormsModule,
    MatAutocompleteModule, MatButtonModule, MatCheckboxModule, MatDatepickerModule, MatDialogModule, MatFormFieldModule,
    MatIconModule, MatInputModule, MatMenuModule, MatPaginatorModule, MatRadioModule, MatSelectModule, MatSidenavModule,
    MatSnackBarModule, MatSortModule, MatTableModule, MatToolbarModule, MatMomentDateModule,
    FileInputAccessorModule, QuillModule.forRoot(), TranslateModule.forRoot()],
  bootstrap: [AppComponent]
})
export class AppModule {
  // It's necessary to spell the components used in the application here, otherwise Angular (Ivy) tree-shakes them.
  // See https://github.com/angular/angular/issues/33715#issuecomment-617606494 and
  // https://github.com/angular/angular/issues/35314#issuecomment-584821399
  static entryComponents = [
    CrudComponent, CustomPageComponent, TextPageComponent, UpstairsComponent, WelcomeComponent ];
}
