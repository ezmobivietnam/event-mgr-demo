import {Component, OnInit} from '@angular/core';
import {EventsService} from "../service/events.service";
import {Event} from "../model/event";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-events-list',
    templateUrl: './events-list.component.html',
    styleUrls: ['./events-list.component.less']
})
export class EventsListComponent implements OnInit {

    events: Event[] = [];
    date : string = "";

    constructor(private eventService: EventsService, private datePipe: DatePipe) {
    }

    ngOnInit(): void {
        this.eventService.getEvents().subscribe(data => {
            this.events = data;
            console.log(data);
        });
    }

}
