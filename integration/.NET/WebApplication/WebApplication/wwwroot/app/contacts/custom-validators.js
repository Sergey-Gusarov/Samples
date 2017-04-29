"use strict";
function emailValidator(control) {
    let emailRegex = /[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}/i;
    let value = control.value;
    let result = emailRegex.test(value);
    if (result) {
        return null;
    }
    else {
        return { "emailValidator": { value } };
    }
}
exports.emailValidator = emailValidator;
function phoneValidator(control) {
    let emailRegex = /^(\+\d{1,3}[- ]?)?\d{10}$/i;
    let value = control.value;
    let result = emailRegex.test(value);
    if (result) {
        return null;
    }
    else {
        return { "phoneValidator": { value } };
    }
}
exports.phoneValidator = phoneValidator;
function rangeValidator(minValue, maxValue) {
    return (control) => {
        let value = control.value;
        let numValue = +value;
        if (isNaN(numValue)) {
            return { "rangeValidator": { value } };
        }
        else if (numValue < minValue || numValue > maxValue) {
            return { "rangeValidator": { value } };
        }
        else {
            return null;
        }
    };
}
exports.rangeValidator = rangeValidator;
//# sourceMappingURL=custom-validators.js.map