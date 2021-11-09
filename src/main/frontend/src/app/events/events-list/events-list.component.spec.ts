import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventsListComponent} from './events-list.component';
import {EventsService} from "../service/events.service";
import {Event} from "../model/event";
import {Observable, of} from "rxjs";


class MockEventsService extends EventsService {
    override getEvents(): Observable<Event[]> {
        return of([]);
    }


    override save(event: Event): Observable<any> {
        return of(1);
    }
}

describe('EventsListComponent', () => {
    let component: EventsListComponent;
    let fixture: ComponentFixture<EventsListComponent>;
    let mockHttpClient = jasmine.createSpyObj('HttpClient', ['post', 'get'])
    let mocService = new MockEventsService(mockHttpClient);

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [EventsListComponent],
            providers: [
                // {provide: EventsService, useValue: jasmine.createSpyObj('EventsService', ['getEvents', 'save'])}
                {provide: EventsService, useValue: mocService}
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
