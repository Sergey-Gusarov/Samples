import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { RouterModule, Routes, Route } from '@angular/router';
import { ContactsTableComponent, ContactsComponent } from "./index";
import { ContactService } from "./contact.service";
import { ContactsDetailsComponent } from "./contacts-details.component";
import { ContactsCreateComponent } from "./contacts-create.component";
import { ContactsEditComponent } from "./contacts-edit.component";
import { ContactsDeleteComponent } from "./contacts-delete.component";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from "@angular/http";

@NgModule({
    declarations: [
        ContactsTableComponent,
        ContactsDeleteComponent,
        ContactsEditComponent,
        ContactsCreateComponent,
        ContactsDetailsComponent,
        ContactsComponent
    ],
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        FormsModule,
        HttpModule,
        RouterModule.forChild([
            { path: "contacts", component: ContactsComponent },
            { path: "contacts/:id/details", component: ContactsDetailsComponent },
            { path: "contacts/create", component: ContactsCreateComponent },
            { path: "contacts/:id/edit", component: ContactsEditComponent },
            { path: "contacts/:id/delete", component: ContactsDeleteComponent }
        ])
    ],
    providers: [ContactService]
})
export class ContactsModule { }