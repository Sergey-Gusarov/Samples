import { Component } from "@angular/core";
import { Contact } from "./index";

@Component({
    selector: "contacts-table",
    templateUrl: "./app/contacts/contacts-table.component.html"
})
export class ContactsTableComponent {

    contacts: Array<Contact>;

    constructor() {

        this.contacts = [

            new Contact("Stepan", "Biocad", "admin@biocad.com", "+7800553535", "StoryCLM"),
            new Contact("Vovan", "Cats and Dogs", "admin@biocad.com", "+7800553535", "StoryCLM"),
            new Contact("Voltran", "Voltrans", "admin@voltrans.com", "+7800553535", "StoryCLM"),
            new Contact("Vadim", "Geo", "admin@geo.com", "+7800553535", "StoryCLM"),
            new Contact("Anna", "J&J", "admin@j&j.com", "+7800553535", "StoryCLM"),
            new Contact("Milla", "Obor", "admin@biocad.com", "+7800553535", "StoryCLM"),

        ];

    }

}