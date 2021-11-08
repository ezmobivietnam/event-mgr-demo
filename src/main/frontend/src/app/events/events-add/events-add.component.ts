import {Component, OnInit} from '@angular/core';
import {EventsService} from "../service/events.service";
import {Event} from "../model/event";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-events-add',
    templateUrl: './events-add.component.html',
    styleUrls: ['./events-add.component.less']
})
export class EventsAddComponent implements OnInit {

    UTC_FORMAT: string;
    event: Event;

    constructor(private eventService: EventsService, private datePipe: DatePipe) {
        this.UTC_FORMAT = 'yyyy-MM-ddThh:mm:ssZZZZZ';
        this.event = new Event();
    }

    ngOnInit(): void {
    }

    onSubmit() {
        // convert to UTC format before submit to server
        console.log('===========');
        console.log(this.event.startDate);
        this.event.startDate = this.datePipe.transform(this.event.startDate, this.UTC_FORMAT)
        console.log(this.event.startDate);
        console.log(this.event.endDate);
        this.event.endDate = this.datePipe.transform(this.event.endDate, this.UTC_FORMAT)
        console.log(this.event.endDate);
        console.log('===========');
        console.log(this.event);
        this.eventService.save(this.event).subscribe(result => {
            console.log(result)
            console.log(this.event)
            this.event = new Event();
        });
    }
}
