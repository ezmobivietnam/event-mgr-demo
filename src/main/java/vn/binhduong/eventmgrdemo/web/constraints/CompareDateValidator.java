package vn.binhduong.eventmgrdemo.web.constraints;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;

@Slf4j
public class CompareDateValidator implements ConstraintValidator<CompareDate, Object> {

    private String beforeFieldName;
    private String afterFieldName;

    @Override
    public void initialize(final CompareDate constraintAnnotation) {
        beforeFieldName = constraintAnnotation.before();
        afterFieldName = constraintAnnotation.after();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Field beforeDateField = value.getClass().getDeclaredField(beforeFieldName);
            beforeDateField.setAccessible(true);

            final Field afterDateField = value.getClass().getDeclaredField(afterFieldName);
            afterDateField.setAccessible(true);

            final ZonedDateTime beforeDate = (ZonedDateTime) beforeDateField.get(value);
            final ZonedDateTime afterDate = (ZonedDateTime) afterDateField.get(value);

            if (beforeDate == null || afterDate == null) {
                return true; // don't apply this constraint when one of two date is null
            } else { //both two data is not null
                return beforeDate.isBefore(afterDate);
            }
        } catch (final Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
