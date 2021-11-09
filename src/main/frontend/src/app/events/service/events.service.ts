import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, retry, throwError} from "rxjs";
import {Event} from "../model/event";

@Injectable({
    providedIn: 'root'
})
export class EventsService {

    apiUrl: string;

    onEventAdded = new EventEmitter<Event>();

    constructor(private http: HttpClient) {
        this.apiUrl = '/api/events';
    }

    public getEvents(): Observable<Event[]> {
        // return this.http.get<Event[]>('/api/simple/events'); // temporary API endpoint
        return this.http.get<Event[]>('/api/simple/events').pipe(
            retry(1),
            catchError(this.handleError)
        );
    }

    public save(event: Event) {
        return this.http.post<Event>(this.apiUrl, event).pipe(
            retry(1),
            catchError(this.handleError)
        );
    }

    private handleError(error: HttpErrorResponse) {
        // console.log("error log start");
        // console.log(error);

        let errorMsg = error.message;
        if (error.message) {
            if (error.status == 504) {//504 (Gateway Timeout)
                // do nothing because the two object error.message and error.error is similar
            } else {
                let details = "";
                for (const x in error.error) {
                    details += '\n';
                    details += x + ": " + error.error[x];
                }
                errorMsg += details;
            }
        }

        // console.log(errorMsg);
        // console.log("error log end");
        window.alert(errorMsg);
        return throwError('Error occurs from server')
    }

    private isString(value: any) {
        return typeof value === 'string' || value instanceof String;
    }

}
