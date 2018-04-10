package com.project.profee.handler;

import com.project.profee.dto.error.ErrorResponse;
import com.project.profee.dto.error.ValidationError;
import com.project.profee.exception.PaymentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private final static String REQUEST_BODY_EMPTY_ERROR_MESSAGE = "Request body should not be empty";
    private final static String SERVER_ERROR_MESSAGE = "Service unavailable, we are fixing it. Please try again later";
    private final static String UNSUPPORTED_MEDIA_TYPE_MESSAGE = "Supporting only JSON media type";

    private final MessageSource messageSource;

    @Autowired
    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError handleValidationErrors(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }

    @ExceptionHandler({PaymentNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleEntityNotFoundException(PaymentNotFoundException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ErrorResponse handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleEmptyRequestBody(HttpMessageNotReadableException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(REQUEST_BODY_EMPTY_ERROR_MESSAGE);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ErrorResponse handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(UNSUPPORTED_MEDIA_TYPE_MESSAGE);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleServerError(Exception ex) {
        log.error(ex.getMessage());
        return new ErrorResponse(SERVER_ERROR_MESSAGE);
    }

    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
        ValidationError validationError = new ValidationError();
        fieldErrors.forEach((fieldError -> {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            validationError.addFieldError(fieldError.getField(), localizedErrorMessage);
        }));
        return validationError;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(fieldError, currentLocale);
    }
}
