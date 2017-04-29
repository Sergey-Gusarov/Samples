import { Component, ViewEncapsulation, OnInit } from "@angular/core";
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ContactService } from "./contact.service";
import { Contact } from "./contact";

@Component({
    templateUrl: "./app/contacts/contacts-details.component.html",
        encapsulation: ViewEncapsulation.None
})
export class ContactsDetailsComponent implements OnInit {

    public contact: Contact;

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private contactService: ContactService) {
    }

    ngOnInit() {
        this.activatedRoute.params.forEach((params: Params) => {
            let id = +params["id"]; 
            this.contactService
                .get(id) 
                .then(t => this.contact = t); 
        });
    }

    onCancel() {
        this.router.navigate(["contacts"]);
    }

}