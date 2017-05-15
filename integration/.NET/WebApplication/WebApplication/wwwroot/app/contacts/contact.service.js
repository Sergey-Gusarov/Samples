"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
const core_1 = require("@angular/core");
const index_1 = require("./index");
const http_1 = require("@angular/http");
const Observable_1 = require("rxjs/Observable");
require("rxjs/add/operator/toPromise");
let ContactService = class ContactService {
    constructor(http) {
        this.http = http;
        this.url = "api/contacts";
    }
    getAll() {
        let products = this.http.get(this.url)
            .toPromise()
            .then(this.extractContacts)
            .catch(this.handleError);
        return products;
    }
    get(id) {
        let product = this.http.get(this.url + "/" + id)
            .toPromise()
            .then(this.extractContact)
            .catch(this.handleError);
        return product;
    }
    add(contact) {
        return this.http.post(this.url, contact)
            .toPromise()
            .catch(this.handleError);
    }
    update(contact) {
        console.log(contact);
        return this.http.put(this.url + "/" + contact.Id, contact)
            .toPromise()
            .catch(this.handleError);
    }
    delete(id) {
        return this.http.delete(this.url + "/" + id)
            .toPromise()
            .catch(this.handleError);
    }
    extractContacts(response) {
        let res = response.json();
        let contacts = [];
        for (let i = 0; i < res.length; i++) {
            contacts.push(new index_1.Contact(res[i].id, res[i]._id, res[i].name, res[i].company, res[i].email, res[i].phone, res[i].interest));
        }
        return contacts;
    }
    extractContact(response) {
        let res = response.json();
        let contact = new index_1.Contact(res.id, res._id, res.name, res.company, res.email, res.phone, res.interest);
        return contact;
    }
    handleError(error) {
        let message = "";
        if (error instanceof http_1.Response) {
            let errorData = error.json().error || JSON.stringify(error.json());
            message = `${error.status} - ${error.statusText || ''} ${errorData}`;
        }
        else {
            message = error.message ? error.message : error.toString();
        }
        console.error(message);
        return Observable_1.Observable.throw(message);
    }
};
ContactService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [http_1.Http])
], ContactService);
exports.ContactService = ContactService;
//# sourceMappingURL=contact.service.js.map