import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContactTableComponent } from './index';

@NgModule({
    imports: [
        //RouterModule.forChild([
        //    { path: "", component: ContactTableComponent }
        //])
    ],
    exports: [
        //RouterModule
    ]
})
export class ContactsRoutingModule { }