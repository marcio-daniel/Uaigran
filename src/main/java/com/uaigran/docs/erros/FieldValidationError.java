package com.uaigran.docs.erros;

import lombok.Data;

@Data
public class FieldValidationError {
    private String nameField;
    private String errorMessage;
}
