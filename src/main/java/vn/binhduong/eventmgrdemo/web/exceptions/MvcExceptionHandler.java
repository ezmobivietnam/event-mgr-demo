package vn.binhduong.eventmgrdemo.web.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by ezmobivietnam on 2021-01-04.
 */
@Slf4j
@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> beanValidationError(ConstraintViolationException exc) {
        log.error("Bean validation error", exc);
        Map<String, String> errors = new HashMap<>(exc.getConstraintViolations().size());
        for (ConstraintViolation<?> violation : exc.getConstraintViolations()) {
            String key = violation.getRootBeanClass().getName() + "." + violation.getPropertyPath();
            errors.put(key, violation.getMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }


    /**
     * Method parameters that are decorated with the @PathVariable annotation can be of any simple type such as int,
     * long, Date... Spring automatically converts to the appropriate type and throws a TypeMismatchException if the
     * type is not correct.
     *
     * @param exc
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<List<String>> methodArgumentTypeMismatchError(MethodArgumentTypeMismatchException exc) {
        log.error("Method argument type mismatch error", exc);
        return ResponseEntity.badRequest().body(List.of(exc.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodValidationError(MethodArgumentNotValidException exc) {
        log.error("Method argument validation error", exc);
        Map<String, String> errors = new HashMap<>();
        exc.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                errors.put(error.getCode(), error.getDefaultMessage());
            }
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HttpStatus> dataNotFoundError(NoSuchElementException exc) {
        log.error("Data not found error", exc);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> databaseIntegrityError(DataIntegrityViolationException exc) {
        log.error("Data integrity constraint error", exc);
        Map<String, String> errors = new HashMap<>();
        errors.put("DataIntegrityViolation", exc.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<List<String>> databaseError(Exception exc) {
        log.error("Database error", exc);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of(exc.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> methodValidationError(HttpMessageNotReadableException exc) {
        log.error("JSON parse error", exc);
        Map<String, String> errors = new HashMap<>();
        errors.put("JsonParseError", exc.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<String>> internalServerError(Exception exc) {
        log.error("Unexpected error", exc);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of(exc.getMessage()));
    }
}
