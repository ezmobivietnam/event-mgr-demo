import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventsAddComponent} from './events-add.component';
import {EventsService} from "../service/events.service";
import {DatePipe} from "@angular/common";

describe('EventsAddComponent', () => {
    let component: EventsAddComponent;
    let fixture: ComponentFixture<EventsAddComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [EventsAddComponent],
            providers: [
                {provide: EventsService, useValue: jasmine.createSpyObj('EventsService', ['getEvents', 'save'])},
                {provide: DatePipe, useValue: jasmine.createSpyObj('DatePipe', ['transform'])}
            ]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(EventsAddComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
