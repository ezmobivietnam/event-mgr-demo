import {Component, OnInit} from '@angular/core';
import {EventsService} from "../service/events.service";
import {Event} from "../model/event";

@Component({
    selector: 'app-events-list',
    templateUrl: './events-list.component.html',
    styleUrls: ['./events-list.component.less']
})
export class EventsListComponent implements OnInit {

    events: Event[] = [];

    constructor(private eventService: EventsService) {
    }

    ngOnInit(): void {
        this.eventService.getEvents().subscribe(data => {
            this.events = data;
            console.log(data);
        });
    }

}
