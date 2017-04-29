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
const core_1 = require("@angular/core");
const router_1 = require("@angular/router");
const contact_service_1 = require("./contact.service");
const contact_1 = require("./contact");
const forms_1 = require("@angular/forms");
const custom_validators_1 = require("./custom-validators");
let ContactsEditComponent = class ContactsEditComponent {
    constructor(router, activatedRoute, contactService, fb) {
        this.router = router;
        this.activatedRoute = activatedRoute;
        this.contactService = contactService;
        this.fb = fb;
        this.formErrors = {
            "Name": "",
            "Company": "",
            "Email": "",
            "Phone": "",
            "Interest": ""
        };
        this.validationMessages = {
            "Name": {
                "required": "Обязательное поле.",
                "minlength": "Значение должно быть не менее 4х символов.",
                "maxlength": "Значение не должно быть больше 15 символов."
            },
            "Company": {
                "required": "Обязательное поле.",
                "minlength": "Значение должно быть не менее 3х символов.",
                "maxlength": "Значение не должно быть больше 15 символов."
            },
            "Email": {
                "required": "Обязательное поле.",
                "emailValidator": "Не правильный формат email адреса."
            },
            "Phone": {
                "required": "Обязательное поле.",
                "phoneValidator": "Не правильный формат телефонного номера."
            },
            "Interest": {
                "required": "Обязательное поле.",
                "minlength": "Значение должно быть не менее 3х символов.",
                "maxlength": "Значение не должно быть больше 15 символов."
            }
        };
    }
    ngOnInit() {
        this.buildForm();
        this.getContactFromRoute();
    }
    getContactFromRoute() {
        this.activatedRoute.params.forEach((params) => {
            let id = params["id"];
            if (id) {
                this.contactService
                    .get(id)
                    .then(t => {
                    this.contact = t;
                    this.contactForm.patchValue(this.contact);
                });
            }
            else {
                this.contact = new contact_1.Contact(-1, "", "TEST", "", "", "", "");
                this.contactForm.patchValue(this.contact);
            }
        });
    }
    buildForm() {
        this.contactForm = this.fb.group({
            "Name": ["", [
                    forms_1.Validators.required,
                    forms_1.Validators.minLength(4),
                    forms_1.Validators.maxLength(15)
                ]],
            "Company": ["", [
                    forms_1.Validators.required,
                    forms_1.Validators.minLength(3),
                    forms_1.Validators.maxLength(15)
                ]],
            "Email": ["", [
                    forms_1.Validators.required,
                    custom_validators_1.emailValidator
                ]],
            "Phone": ["", [
                    forms_1.Validators.required,
                    custom_validators_1.phoneValidator
                ]],
            "Interest": ["", [
                    forms_1.Validators.required,
                    forms_1.Validators.minLength(3),
                    forms_1.Validators.maxLength(15)
                ]]
        });
        this.contactForm.valueChanges.subscribe(data => this.onValueChange(data));
        this.onValueChange();
    }
    onValueChange(data) {
        if (!this.contactForm)
            return;
        let form = this.contactForm;
        for (let field in this.formErrors) {
            this.formErrors[field] = "";
            let control = form.get(field);
            if (control && control.dirty && !control.valid) {
                let message = this.validationMessages[field];
                for (let key in control.errors) {
                    this.formErrors[field] += message[key] + " ";
                }
            }
        }
    }
    onSubmit() {
        let con = new contact_1.Contact(this.contact.Id, this.contact.ExternalId, this.contactForm.value["Name"], this.contactForm.value["Company"], this.contactForm.value["Email"], this.contactForm.value["Phone"], this.contactForm.value["Interest"]);
        this.contactService.update(con).then(t => {
            this.contact = t;
            this.router.navigate(["contacts"]);
        });
    }
    onCancel() {
        this.router.navigate(["contacts"]);
    }
};
ContactsEditComponent = __decorate([
    core_1.Component({
        templateUrl: "./app/contacts/contacts-edit.component.html",
        encapsulation: core_1.ViewEncapsulation.None
    }),
    __metadata("design:paramtypes", [router_1.Router,
        router_1.ActivatedRoute,
        contact_service_1.ContactService,
        forms_1.FormBuilder])
], ContactsEditComponent);
exports.ContactsEditComponent = ContactsEditComponent;
//# sourceMappingURL=contacts-edit.component.js.map