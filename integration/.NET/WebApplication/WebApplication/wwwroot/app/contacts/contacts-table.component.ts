import { Component, ViewEncapsulation, Input, OnInit, Inject, forwardRef } from "@angular/core";
import { Router } from '@angular/router';
import { Contact } from "./index";
import { ContactService } from "./contact.service";

@Component({
    selector: "contacts-table",
    templateUrl: "./app/contacts/contacts-table.component.html",
    encapsulation: ViewEncapsulation.None
})
export class ContactsTableComponent implements OnInit {

    @Input("tableName")
    name: string = "";

    public contacts: Array<Contact>;

    constructor(public router: Router, private contactsService: ContactService) {
    }

    ngOnInit() {
        this.contactsService.getAll().then(t => this.contacts = t);
    }

    onCreate() {
        this.router.navigate(["contacts", "create"]);
    }

    onDetails(selected: Contact) {
        this.router.navigate(["contacts", selected.Id, "details"]);
    }

    onEdit(selected: Contact) {
        this.router.navigate(["contacts", selected.Id, "edit"]);
    }

    onDelete(selected: Contact) {
        this.router.navigate(["contacts", selected.Id, "delete"]);
    }

}
