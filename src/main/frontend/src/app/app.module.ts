import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { EventsComponent } from './events/events.component';
import { EventsAddComponent } from './events/events-add/events-add.component';
import { EventsListComponent } from './events/events-list/events-list.component';

@NgModule({
  declarations: [
    AppComponent,
    EventsComponent,
    EventsAddComponent,
    EventsListComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
