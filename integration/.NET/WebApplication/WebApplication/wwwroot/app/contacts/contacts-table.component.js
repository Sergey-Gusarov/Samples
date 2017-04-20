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
let ContactsTableComponent = class ContactsTableComponent {
    constructor() {
        this.contacts = [
            new index_1.Contact("Stepan", "Biocad", "admin@biocad.com", "+7800553535", "StoryCLM"),
            new index_1.Contact("Vovan", "Cats and Dogs", "admin@biocad.com", "+7800553535", "StoryCLM"),
            new index_1.Contact("Voltran", "Voltrans", "admin@voltrans.com", "+7800553535", "StoryCLM"),
            new index_1.Contact("Vadim", "Geo", "admin@geo.com", "+7800553535", "StoryCLM"),
            new index_1.Contact("Anna", "J&J", "admin@j&j.com", "+7800553535", "StoryCLM"),
            new index_1.Contact("Milla", "Obor", "admin@biocad.com", "+7800553535", "StoryCLM"),
        ];
    }
};
ContactsTableComponent = __decorate([
    core_1.Component({
        selector: "contacts-table",
        templateUrl: "./app/contacts/contacts-table.component.html"
    }),
    __metadata("design:paramtypes", [])
], ContactsTableComponent);
exports.ContactsTableComponent = ContactsTableComponent;
//# sourceMappingURL=contacts-table.component.js.map