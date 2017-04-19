import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';
import { ContactTableComponent, ContactsRoutingModule } from "./index";

@NgModule({
    declarations: [ContactTableComponent],
    imports: [
        RouterModule.forChild([
            { path: "", component: ContactTableComponent }
        ])
    ],
})
export class ContactsModule { }