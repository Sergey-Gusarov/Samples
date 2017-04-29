import { Injectable } from "@angular/core";
import { Contact } from "./index";
import { Http, Response } from "@angular/http";
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class ContactService {

    private url = "api/contacts";

    constructor(private http: Http) { }

    getAll(): Promise<Contact[]> {
        let products = this.http.get(this.url)
            .toPromise()
            .then(this.extractContacts)
            .catch(this.handleError);

        return products;
    }

    get(id: number): Promise<Contact> {
        let product = this.http.get(this.url + "/" + id)
            .toPromise()
            .then(this.extractContact)
            .catch(this.handleError);

        return product;
    }

    add(contact: Contact) {
        return this.http.post(this.url, contact)
            .toPromise()
            .catch(this.handleError);
    }

    update(contact: Contact) {
        console.log(contact);
        return this.http.put(this.url + "/" + contact.Id, contact)
            .toPromise()
            .catch(this.handleError);
    }

    delete(id: number) {
        return this.http.delete(this.url + "/" + id)
            .toPromise()
            .catch(this.handleError);
    }

    private extractContacts(response: Response) {
        let res = response.json();
        let contacts: Contact[] = [];
        for (let i = 0; i < res.length; i++) {
            contacts.push(new Contact(res[i].id, res[i].externalId, res[i].name, res[i].company, res[i].email, res[i].phone, res[i].interest));
        }
        return contacts;
    }

    private extractContact(response: Response) {
        let res = response.json();
        let contact = new Contact(res.id, res.externalId, res.name, res.company, res.email, res.phone, res.interest);
        return contact;
    }

    private handleError(error: any): any {
        let message = "";

        if (error instanceof Response) {
            let errorData = error.json().error || JSON.stringify(error.json());
            message = `${error.status} - ${error.statusText || ''} ${errorData}`
        } else {
            message = error.message ? error.message : error.toString();
        }

        console.error(message);
        return Observable.throw(message);
    }

}