/*
 * Salesforce DTO generated by camel-salesforce-maven-plugin
 * Generated on: Thu Apr 06 14:36:58 MSK 2017
 */
package ru.breffi.entityTypeConverterImpl.employee.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Salesforce Enumeration DTO for picklist Field
 */
public enum Employee__History_FieldEnum {

    // BF_Emloyee_SignedAgreement__c
    BF_EMLOYEE_SIGNEDAGREEMENT__C("BF_Emloyee_SignedAgreement__c"),
    // BF_Emloyee_category__c
    BF_EMLOYEE_CATEGORY__C("BF_Emloyee_category__c"),
    // BF_Emloyee_company__c
    BF_EMLOYEE_COMPANY__C("BF_Emloyee_company__c"),
    // BF_Emloyee_email__c
    BF_EMLOYEE_EMAIL__C("BF_Emloyee_email__c"),
    // BF_Emloyee_name__c
    BF_EMLOYEE_NAME__C("BF_Emloyee_name__c"),
    // BF_Emloyee_patronymic__c
    BF_EMLOYEE_PATRONYMIC__C("BF_Emloyee_patronymic__c"),
    // BF_Emloyee_position__c
    BF_EMLOYEE_POSITION__C("BF_Emloyee_position__c"),
    // BF_Emloyee_socialnetwork__c
    BF_EMLOYEE_SOCIALNETWORK__C("BF_Emloyee_socialnetwork__c"),
    // BF_Emloyee_speciality__c
    BF_EMLOYEE_SPECIALITY__C("BF_Emloyee_speciality__c"),
    // BF_Emloyee_tel__c
    BF_EMLOYEE_TEL__C("BF_Emloyee_tel__c"),
    // BF_Emloyee_typeofemployee__c
    BF_EMLOYEE_TYPEOFEMPLOYEE__C("BF_Emloyee_typeofemployee__c"),
    // BF_TelemarketComment__c
    BF_TELEMARKETCOMMENT__C("BF_TelemarketComment__c"),
    // Name
    NAME("Name"),
    // created
    CREATED("created"),
    // feedEvent
    FEEDEVENT("feedEvent"),
    // locked
    LOCKED("locked"),
    // ownerAccepted
    OWNERACCEPTED("ownerAccepted"),
    // ownerAssignment
    OWNERASSIGNMENT("ownerAssignment"),
    // unlocked
    UNLOCKED("unlocked");

    final String value;

    private Employee__History_FieldEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static Employee__History_FieldEnum fromValue(String value) {
        for (Employee__History_FieldEnum e : Employee__History_FieldEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
