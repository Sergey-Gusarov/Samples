import { Component, ViewEncapsulation, OnInit } from "@angular/core";
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ContactService } from "./contact.service";
import { Contact } from "./contact";
import { FormControl, FormGroup, FormBuilder, Validators } from "@angular/forms";
import { emailValidator, phoneValidator } from "./custom-validators";
import { Observable } from 'rxjs/Observable';

interface IContactForm {
    name: string;
    company: string;
    email: string;
    phone: string;
    interest: string;
}

@Component({
    templateUrl: "./app/contacts/contacts-edit.component.html",
        encapsulation: ViewEncapsulation.None
})
export class ContactsEditComponent implements OnInit {

    contact: Contact;
    contactForm: FormGroup;

    constructor(private router: Router,
        private activatedRoute: ActivatedRoute,
        private contactService: ContactService,
        private fb: FormBuilder) {
    }

    formErrors = {
        "Name": "",
        "Company": "",
        "Email": "",
        "Phone": "",
        "Interest": ""
    };

    validationMessages = {
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

    ngOnInit() {
        this.buildForm();
        this.getContactFromRoute();
    }

    private getContactFromRoute() {
        this.activatedRoute.params.forEach((params: Params) => {
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
                this.contact = new Contact(-1, "", "TEST", "", "", "", "");
                this.contactForm.patchValue(this.contact);
            }
        });
    }

    buildForm() {
        this.contactForm = this.fb.group({
            "Name": ["", [
                Validators.required,
                Validators.minLength(4),
                Validators.maxLength(15)
            ]],
            "Company": ["", [
                Validators.required,
                Validators.minLength(3),
                Validators.maxLength(15)
            ]],
            "Email": ["", [
                Validators.required,
                emailValidator
            ]],
            "Phone": ["", [
                Validators.required,
                phoneValidator
            ]],
            "Interest": ["", [
                Validators.required,
                Validators.minLength(3),
                Validators.maxLength(15)
            ]]
        });

        this.contactForm.valueChanges.subscribe(data => this.onValueChange(data));
        this.onValueChange();
    }

    onValueChange(data?: any) {
        if (!this.contactForm) return;
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
        let con: Contact = new Contact(this.contact.Id, this.contact._id,
            this.contactForm.value["Name"],
            this.contactForm.value["Company"],
            this.contactForm.value["Email"],
            this.contactForm.value["Phone"],
            this.contactForm.value["Interest"]);

        this.contactService.update(con).then(t => {
            this.contact = t;
            this.router.navigate(["contacts"]);
        });
    }

    onCancel() {
        this.router.navigate(["contacts"]);
    }

}