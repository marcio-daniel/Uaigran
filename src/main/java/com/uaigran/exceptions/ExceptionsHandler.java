package com.uaigran.exceptions;

import com.uaigran.docs.erros.ErrorMessage;
import com.uaigran.docs.erros.FieldValidationError;
import com.uaigran.docs.erros.ValidationErro;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ErrorMessage> fileProcessingException(FileProcessingException exception){
        var errorMessage = ErrorMessage
                .builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessage> conflictException (ConflictException exception){
        var errorMessage = ErrorMessage
                .builder()
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorMessage> internalServerException(InternalServerException exception){
        var errorMessage = ErrorMessage
                .builder()
                .message(exception.getMessage())
                .build();

        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> notFoundException(BadRequestException exception){
        var errorMessage = ErrorMessage
                .builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorMessage> unauthorizedException(UnauthorizedException exception){
        var errorMessage = ErrorMessage
                .builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ){
        var validationError = new ValidationErro();
        List<FieldValidationError> fieldErrorList = this.mapBindExceptionToFieldError(exception);

        validationError.setMessage("Um ou mais campos inválidos");
        validationError.setErrors(fieldErrorList);

        return this.handleExceptionInternal(exception, validationError, headers, HttpStatus.BAD_REQUEST, request);
    }

    private List<FieldValidationError> mapBindExceptionToFieldError(BindException exception) {
        List<FieldValidationError> fieldErrorsList = new ArrayList<FieldValidationError>();

        for(ObjectError error : exception.getBindingResult().getAllErrors()) {
            var fieldName = ((FieldError) error).getField();
            var errorDescription = this.messageSource.getMessage(error, LocaleContextHolder.getLocale());

            var fieldValidationError = new FieldValidationError();

            fieldValidationError.setNameField(fieldName);
            fieldValidationError.setErrorMessage(errorDescription);

            fieldErrorsList.add(fieldValidationError);
        }
        return fieldErrorsList;
    }


}
