import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';
import { ContactsTableComponent, ContactsComponent } from "./index";

@NgModule({
    declarations: [ContactsTableComponent, ContactsComponent],
    imports: [
        RouterModule.forChild([
            { path: "", component: ContactsComponent }
        ])
    ],
})
export class ContactsModule { }