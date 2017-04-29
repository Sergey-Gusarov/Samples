import { Injectable } from "@angular/core";
import { Contact } from "./index";
import { Observable } from 'rxjs/Observable';

let contacts = [
    new Contact(1, "", "Stepan", "Biocad", "admin@biocad.com", "7800553535", "StoryCLM"),
    new Contact(2, "", "Vovan", "Cats and Dogs", "admin@biocad.com", "7800553535", "StoryCLM"),
    new Contact(3, "", "Voltran", "Voltrans", "admin@voltrans.com", "7800553535", "StoryCLM"),
    new Contact(4, "", "Vadim", "Geo", "admin@geo.com", "7800553535", "StoryCLM"),
    new Contact(5, "", "Anna", "J&J", "admin@j&j.com", "7800553535", "StoryCLM"),
    new Contact(6, "", "Milla", "Obor", "admin@biocad.com", "7800553535", "StoryCLM"),
];

let contactPromise = Promise.resolve(contacts);

@Injectable()
export class ContactService {

    getAll(): Promise<Contact[]> {
        return contactPromise;
    }

    get(id: number): Promise<Contact> {
        return contactPromise.then(contacts => contacts.find(t => t.Id == id));
    }

    add(contact: Contact) {
        contacts.push(contact);
    }

    update(contact: Contact) {
        let index = contacts.findIndex(t => t.Id == contact.Id);
        contacts[index] = contact;
    }

    delete(id: number) {
        let con = contacts.find(t => t.Id == id);
        contacts.splice(contacts.indexOf(con), 1);
    }

}