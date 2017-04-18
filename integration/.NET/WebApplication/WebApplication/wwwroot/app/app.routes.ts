import { Routes } from "@angular/router";
import { ContactTableComponent } from "./contacts/index";

export const rootRouterConfig: Routes = [
    { path: "", redirectTo: "home", pathMatch: "full" },
    { path: "home", loadChildren: 'app/contacts/contacts.module', }
];