package ru.t1.apupynin.clientms.dto;

import ru.t1.apupynin.clientms.enums.DocumentType;

import java.time.LocalDate;

public class RegistrationRequest {
    public String clientId;
    public String login;
    public String password;
    public String email;
    public String firstName;
    public String middleName;
    public String lastName;
    public LocalDate dateOfBirth;
    public DocumentType documentType;
    public String documentId;
    public String documentPrefix;
    public String documentSuffix;
}


