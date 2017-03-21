import { WebHooksTableComponent } from "./index";

export const routs = [
    { path: "web-hooks", component: WebHooksTableComponent },
    { path: "", redirectTo: "web-hooks", pathMatch: "full" }
];