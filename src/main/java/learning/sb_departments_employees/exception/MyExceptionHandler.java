package learning.sb_departments_employees.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.core.ValidationErrors;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;

@ControllerAdvice
@Order(-1)  // to be ahead of other handlers
public class MyExceptionHandler {
    // Override default handling
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<?> handleNotFound(ResourceNotFoundException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }

    @Bean
    @Order(-1)  // To be ahead of the default exception translator
    // Convert ConstraintViolationException to RepositoryConstraintViolationException to allow for default REST exception
    // handling by org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler.handleRepositoryConstraintViolationException
    public PersistenceExceptionTranslator customPersistenceExceptionTranslator(
            ObjectFactory<PersistentEntities> persistentEntitiesFactory) {
        var validatorAdapter = new CustomValidatorBean() {
            public void processConstraintViolations(Set<ConstraintViolation<Object>> violations, Errors errors) {
                super.processConstraintViolations(violations, errors);
            }
        };

        return ex -> {
            Throwable exc = ex;
            while (!(exc instanceof ConstraintViolationException) && exc.getCause() != null) {
                exc = exc.getCause();
            }

            if (exc instanceof ConstraintViolationException constrExc) {
                @SuppressWarnings("unchecked")
                var violations = (Set<ConstraintViolation<Object>>)(Set<?>) constrExc.getConstraintViolations();
                if (violations != null && !violations.isEmpty()) {
                    // get entity from any violation - should be the same for all
                    Object entity = violations.iterator().next().getRootBean();
                    // taken from org.springframework.data.rest.core.event.ValidatingRepositoryEventListener.validate
                    Errors errors = new ValidationErrors(entity, persistentEntitiesFactory.getObject());
                    validatorAdapter.processConstraintViolations(violations, errors);
                    // taken from org.springframework.data.rest.core.event.ValidatingRepositoryEventListener.validate
                    if (errors.hasErrors()) {
                        return new RepositoryConstraintViolationException(errors);
                    }
                }
            }

            return null;
        };
    }
}
