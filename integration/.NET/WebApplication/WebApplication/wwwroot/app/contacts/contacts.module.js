"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
const core_1 = require("@angular/core");
const platform_browser_1 = require("@angular/platform-browser");
const router_1 = require("@angular/router");
const index_1 = require("./index");
const contact_service_1 = require("./contact.service");
const contacts_details_component_1 = require("./contacts-details.component");
const contacts_create_component_1 = require("./contacts-create.component");
const contacts_edit_component_1 = require("./contacts-edit.component");
const contacts_delete_component_1 = require("./contacts-delete.component");
const forms_1 = require("@angular/forms");
let ContactsModule = class ContactsModule {
};
ContactsModule = __decorate([
    core_1.NgModule({
        declarations: [
            index_1.ContactsTableComponent,
            contacts_delete_component_1.ContactsDeleteComponent,
            contacts_edit_component_1.ContactsEditComponent,
            contacts_create_component_1.ContactsCreateComponent,
            contacts_details_component_1.ContactsDetailsComponent,
            index_1.ContactsComponent
        ],
        imports: [
            platform_browser_1.BrowserModule,
            forms_1.ReactiveFormsModule,
            forms_1.FormsModule,
            router_1.RouterModule.forChild([
                { path: "contacts", component: index_1.ContactsComponent },
                { path: "contacts/:id/details", component: contacts_details_component_1.ContactsDetailsComponent },
                { path: "contacts/create", component: contacts_create_component_1.ContactsCreateComponent },
                { path: "contacts/:id/edit", component: contacts_edit_component_1.ContactsEditComponent },
                { path: "contacts/:id/delete", component: contacts_delete_component_1.ContactsDeleteComponent }
            ])
        ],
        providers: [contact_service_1.ContactService]
    })
], ContactsModule);
exports.ContactsModule = ContactsModule;
//# sourceMappingURL=contacts.module.js.map