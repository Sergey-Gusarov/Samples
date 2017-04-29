import { Component, ViewEncapsulation, OnInit } from "@angular/core";
import { Router, ActivatedRoute, Params } from '@angular/router';
import { ContactService } from "./contact.service";
import { Contact } from "./contact";
import { FormControl, FormGroup, FormBuilder, Validators } from "@angular/forms";
import { emailValidator, phoneValidator } from "./custom-validators";

interface IContactForm {
    name: string;
    company: string;
    email: string;
    phone: string;
    interest: string;
}

@Component({
    templateUrl: "./app/contacts/contacts-create.component.html",
        encapsulation: ViewEncapsulation.None
})
export class ContactsCreateComponent implements OnInit {

    contact: Contact;
    contactForm: FormGroup;

    constructor(private router: Router,
        private activatedRoute: ActivatedRoute,
        private contactService: ContactService,
        private fb: FormBuilder) {
    }

    formErrors = {
        "name": "",
        "company": "",
        "email": "",
        "phone": "",
        "interest": ""
    };

    validationMessages = {
        "name": {
            "required": "Обязательное поле.",
            "minlength": "Значение должно быть не менее 4х символов.",
            "maxlength": "Значение не должно быть больше 15 символов."
        },
        "company": {
            "required": "Обязательное поле.",
            "minlength": "Значение должно быть не менее 3х символов.",
            "maxlength": "Значение не должно быть больше 15 символов."
        },
        "email": {
            "required": "Обязательное поле.",
            "emailValidator": "Не правильный формат email адреса."
        },
        "phone": {
            "required": "Обязательное поле.",
            "phoneValidator": "Не правильный формат телефонного номера."
        },
        "interest": {
            "required": "Обязательное поле.",
            "minlength": "Значение должно быть не менее 3х символов.",
            "maxlength": "Значение не должно быть больше 15 символов."
        }
    };

    ngOnInit() {
        this.buildForm();
    }

    buildForm() {
        this.contactForm = this.fb.group({
            "name": [!this.contact ? "" : this.contact.Name, [
                Validators.required,
                Validators.minLength(4),
                Validators.maxLength(15)
            ]],
            "company": [!this.contact ? "" : this.contact.Company, [
                Validators.required,
                Validators.minLength(3),
                Validators.maxLength(15)
            ]],
            "email": [!this.contact ? "" : this.contact.Email, [
                Validators.required,
                emailValidator
            ]],
            "phone": [!this.contact ? "" : this.contact.Phone, [
                Validators.required,
                phoneValidator
            ]],
            "interest": [!this.contact ? "" : this.contact.Interest, [
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
        let con: Contact = new Contact(Math.floor(Math.random() * 600) + 1, "",
            this.contactForm.value["name"],
            this.contactForm.value["company"],
            this.contactForm.value["email"],
            this.contactForm.value["phone"],
            this.contactForm.value["interest"]);
        this.contactService.add(con).then(t => {
            this.contact = t;
            this.router.navigate(["contacts"]);
        });
    }

    onCancel() {
        this.router.navigate(["contacts"]);
    }

}