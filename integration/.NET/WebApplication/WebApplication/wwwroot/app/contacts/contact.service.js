"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
const core_1 = require("@angular/core");
const index_1 = require("./index");
let contacts = [
    new index_1.Contact(1, "", "Stepan", "Biocad", "admin@biocad.com", "7800553535", "StoryCLM"),
    new index_1.Contact(2, "", "Vovan", "Cats and Dogs", "admin@biocad.com", "7800553535", "StoryCLM"),
    new index_1.Contact(3, "", "Voltran", "Voltrans", "admin@voltrans.com", "7800553535", "StoryCLM"),
    new index_1.Contact(4, "", "Vadim", "Geo", "admin@geo.com", "7800553535", "StoryCLM"),
    new index_1.Contact(5, "", "Anna", "J&J", "admin@j&j.com", "7800553535", "StoryCLM"),
    new index_1.Contact(6, "", "Milla", "Obor", "admin@biocad.com", "7800553535", "StoryCLM"),
];
let contactPromise = Promise.resolve(contacts);
let ContactService = class ContactService {
    getAll() {
        return contactPromise;
    }
    get(id) {
        return contactPromise.then(contacts => contacts.find(t => t.Id == id));
    }
    add(contact) {
        contacts.push(contact);
    }
    update(contact) {
        let index = contacts.findIndex(t => t.Id == contact.Id);
        contacts[index] = contact;
    }
    delete(id) {
        let con = contacts.find(t => t.Id == id);
        contacts.splice(contacts.indexOf(con), 1);
    }
};
ContactService = __decorate([
    core_1.Injectable()
], ContactService);
exports.ContactService = ContactService;
//# sourceMappingURL=contact.service.js.map