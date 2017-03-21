import { Component } from "@angular/core";
import { Profile } from "../index";

@Component({
    moduleId: module.id,
    templateUrl: "web-hooks-table.component.html",
    styleUrls: ["web-hooks-table.component.css"]
})
export class WebHooksTableComponent {

    profiles: Array<Profile>;

    constructor() {
        this.profiles = [
            { Age: 32, Created: Date.now(), Gender: true, Name: "Dima", Rating: 33.34 },
            { Age: 33, Created: Date.now(), Gender: true, Name: "Taras", Rating: 333.34 },
            { Age: 34, Created: Date.now(), Gender: false, Name: "Stas", Rating: 133.334 },
            { Age: 25, Created: Date.now(), Gender: true, Name: "Dima", Rating: 33.343 },
            { Age: 56, Created: Date.now(), Gender: true, Name: "Dima", Rating: 23.34 },
            { Age: 37, Created: Date.now(), Gender: true, Name: "Dima", Rating: 53.334 },
            { Age: 48, Created: Date.now(), Gender: true, Name: "Opel", Rating: 63.34 }
        ];
    }
    

}