import { AbstractControl, ValidatorFn } from "@angular/forms";

export function emailValidator(control: AbstractControl): { [key: string]: any } {

    let emailRegex = /[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}/i;
    let value = control.value;

    let result = emailRegex.test(value);

    if (result) {
        return null;
    } else {
        return { "emailValidator": { value } }
    }
}

export function phoneValidator(control: AbstractControl): { [key: string]: any } {

    let emailRegex = /^(\+\d{1,3}[- ]?)?\d{10}$/i;
    let value = control.value;

    let result = emailRegex.test(value);

    if (result) {
        return null;
    } else {
        return { "phoneValidator": { value } }
    }
}

export function rangeValidator(minValue: number, maxValue: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
        let value = control.value;
        let numValue: number = +value;

        if (isNaN(numValue)) {
            return { "rangeValidator": { value } };
        }
        else if (numValue < minValue || numValue > maxValue) {
            return { "rangeValidator": { value } };
        }
        else {
            return null;
        }
    }
}

