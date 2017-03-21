import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { BrowserModule } from "@angular/platform-browser";
import { AppComponent } from "./app.component";
import { WebHooksModule, routs } from "./web-hooks/index";

@NgModule({
    imports: [
        BrowserModule,
        WebHooksModule,
        RouterModule.forRoot(routs)
    ],
    declarations: [AppComponent],
    bootstrap: [AppComponent]
})
export class AppModule { }