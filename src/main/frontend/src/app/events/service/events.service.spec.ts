import { TestBed } from '@angular/core/testing';

import { EventsService } from './events.service';
import {HttpClient} from "@angular/common/http";
import createSpy = jasmine.createSpy;

describe('EventsService', () => {
  let service: EventsService;

  beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [
            {provide: HttpClient, useValue: jasmine.createSpyObj('HttpClient', ['post', 'get'])}
        ]
    });
    service = TestBed.inject(EventsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
