"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var WebHooksTableComponent = (function () {
    function WebHooksTableComponent() {
        this.profiles = [
            { Age: 32, Created: Date.now(), Gender: true, Name: "Dima", Rating: 33.34 },
            { Age: 33, Created: Date.now(), Gender: true, Name: "Taras", Rating: 333.34 },
            { Age: 34, Created: Date.now(), Gender: false, Name: "Stas", Rating: 133.334 },
            { Age: 25, Created: Date.now(), Gender: true, Name: "Dima", Rating: 33.343 },
            { Age: 56, Created: Date.now(), Gender: true, Name: "Dima", Rating: 23.34 },
            { Age: 37, Created: Date.now(), Gender: true, Name: "Dima", Rating: 53.334 },
            { Age: 48, Created: Date.now(), Gender: true, Name: "Opel", Rating: 63.34 }
        ];
    }
    WebHooksTableComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            templateUrl: "web-hooks-table.component.html",
            styleUrls: ["web-hooks-table.component.css"]
        }), 
        __metadata('design:paramtypes', [])
    ], WebHooksTableComponent);
    return WebHooksTableComponent;
}());
exports.WebHooksTableComponent = WebHooksTableComponent;
//# sourceMappingURL=web-hooks-table.component.js.map