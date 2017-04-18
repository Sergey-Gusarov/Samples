"use strict";
exports.rootRouterConfig = [
    { path: "", redirectTo: "home", pathMatch: "full" },
    { path: "home", loadChildren: 'app/contacts/contacts.module', }
];
//# sourceMappingURL=app.routes.js.map