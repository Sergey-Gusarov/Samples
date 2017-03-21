import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { WebHooksTableComponent } from "./index";

@NgModule({
    imports: [CommonModule],
    declarations: [WebHooksTableComponent],
    exports: [WebHooksTableComponent]
})
export class WebHooksModule { }