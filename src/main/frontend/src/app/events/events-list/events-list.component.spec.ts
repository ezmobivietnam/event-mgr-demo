import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventsListComponent} from './events-list.component';
import {EventsService} from "../service/events.service";
import {Event} from "../model/event";
import {of} from "rxjs";

describe('EventsListComponent', () => {
    let component: EventsListComponent;
    let fixture: ComponentFixture<EventsListComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [EventsListComponent],
            providers: [
                {provide: EventsService, useValue: jasmine.createSpyObj('EventsService', ['getEvents', 'save'])}
            ]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(EventsListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
