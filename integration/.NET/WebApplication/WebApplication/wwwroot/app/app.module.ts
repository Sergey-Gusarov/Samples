import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router"
import { AppRoutingModule } from './app-routing.module';
import { BrowserModule } from "@angular/platform-browser";
import { AppComponent } from "./app.component";
import { ContactsModule } from "./contacts/index";

@NgModule({
    imports: [
        BrowserModule,
        RouterModule.forRoot([
            { path: "", redirectTo: "/contacts", pathMatch: "full" },
            { path: "contacts", loadChildren: 'app/contacts/contacts.module#ContactsModule' }
        ]),
        ContactsModule
    ],
    declarations:
    [
        AppComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
    
}