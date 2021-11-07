export class Event {
    id: number;
    name: string;
    description: string | null;
    startDate: Date;
    endDate: Date;
    lastUpdateDate: Date;

    constructor(id: number, name: string, description: string, startDate: Date, endDate: Date,  lastUpdateDate: Date){
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lastUpdateDate = lastUpdateDate;
    }
}
