import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { EventsComponent } from './events/events.component';
import { EventsAddComponent } from './events/events-add/events-add.component';
import { EventsListComponent } from './events/events-list/events-list.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {EventsService} from "./events/service/events.service";
import {DatePipe} from "@angular/common";

@NgModule({
  declarations: [
    AppComponent,
    EventsComponent,
    EventsAddComponent,
    EventsListComponent
  ],
  imports: [
    BrowserModule,
      HttpClientModule,
      FormsModule
  ],
  providers: [FormsModule, EventsService, DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
