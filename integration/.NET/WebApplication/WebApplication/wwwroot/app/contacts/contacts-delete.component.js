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
const router_1 = require("@angular/router");
const contact_service_1 = require("./contact.service");
let ContactsDeleteComponent = class ContactsDeleteComponent {
    constructor(router, activatedRoute, contactService) {
        this.router = router;
        this.activatedRoute = activatedRoute;
        this.contactService = contactService;
    }
    ngOnInit() {
        this.activatedRoute.params.forEach((params) => {
            let id = +params["id"];
            this.contactService
                .get(id)
                .then(t => this.contact = t);
        });
    }
    onCancel() {
        this.router.navigate(["contacts"]);
    }
    onDelete(id) {
        this.contactService.delete(id).then(t => {
            this.router.navigate(["contacts"]);
        });
    }
};
ContactsDeleteComponent = __decorate([
    core_1.Component({
        templateUrl: "./app/contacts/contacts-delete.component.html",
        encapsulation: core_1.ViewEncapsulation.None
    }),
    __metadata("design:paramtypes", [router_1.Router, router_1.ActivatedRoute, contact_service_1.ContactService])
], ContactsDeleteComponent);
exports.ContactsDeleteComponent = ContactsDeleteComponent;
//# sourceMappingURL=contacts-delete.component.js.map