package ru.romanow.services.order.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.romanow.services.order.exceptions.CreateOrderException;
import ru.romanow.services.order.exceptions.EntityProcessException;
import ru.romanow.services.order.exceptions.WarrantyProcessingException;
import ru.romanow.services.order.model.ErrorResponse;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody ErrorResponse handleBadRequest(MethodArgumentNotValidException exception) {
        String validationErrors = prepareValidationErrors(exception.getBindingResult().getFieldErrors());
        if (logger.isDebugEnabled()) {
            logger.debug("Bad Request: {}", validationErrors);
        }
        return new ErrorResponse(validationErrors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public @ResponseBody ErrorResponse handleNotFound(RuntimeException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ EntityProcessException.class, WarrantyProcessingException.class, CreateOrderException.class})
    public @ResponseBody ErrorResponse conflict(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorResponse handleException(Exception exception) {
        logger.error("", exception);
        return new ErrorResponse(exception.getMessage());
    }

    private String prepareValidationErrors(List<FieldError> errors) {
        return errors.stream()
                .map(err -> "Field " + err.getField() + " has wrong value: [" + err.getDefaultMessage() + "]")
                .collect(Collectors.joining(";"));
    }
}
