import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Event} from "../model/event";

@Injectable({
    providedIn: 'root'
})
export class EventsService {

    onTaskAdded = new EventEmitter<Task>();

    constructor(private http: HttpClient) {
    }

    public getEvents(): Observable<Event[]> {
        return this.http.get<Event[]>('/api/simple/events');
    }
}
