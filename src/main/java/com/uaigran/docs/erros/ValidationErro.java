package com.uaigran.docs.erros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErro extends ErrorMessage {
    private List<FieldValidationError>  errors;
}
