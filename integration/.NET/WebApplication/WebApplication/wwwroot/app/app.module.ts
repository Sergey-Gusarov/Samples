import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router"
import { rootRouterConfig } from './app.routes';
import { BrowserModule } from "@angular/platform-browser";
import { AppComponent } from "./app.component";
import { ContactsModule } from "./contacts/index";

@NgModule({
    imports: [
        BrowserModule,
        RouterModule.forRoot(rootRouterConfig, { useHash: false }),
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