import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Event} from "../model/event";

@Injectable({
    providedIn: 'root'
})
export class EventsService {

    private apiUrl: string;

    onTaskAdded = new EventEmitter<Task>();

    constructor(private http: HttpClient) {
        this.apiUrl = '/api/events';
    }

    public getEvents(): Observable<Event[]> {
        return this.http.get<Event[]>('/api/simple/events'); // temporary API endpoint
    }

    public save(event: Event) {
        return this.http.post<Event>(this.apiUrl, event);
    }
}
