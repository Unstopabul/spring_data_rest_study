package learning.sb_departments_employees.model.custom;

import learning.sb_departments_employees.model.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class DataOutputCustomizer {
    @Bean   // Used mainly to add custom links. Called when handling return value: after controller and before serialization
    public RepresentationModelProcessor<EntityModel<Employee>> employeeProcessor(EntityLinks links) {
        return new RepresentationModelProcessor<EntityModel<Employee>>() {
            @Override
            public EntityModel<Employee> process(EntityModel<Employee> model) {
                var link = links.linkToCollectionResource(Employee.class);
                model.add(link.withRel("added-link-modelProcessor"));
                return model;
            }
        };

//        return model -> {
//
//        };
    }

    @Bean
    public ConversionService customConversionService() {
        return new ConversionService() {
            @Override
            public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
                return false;
            }

            @Override
            public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
                return false;
            }

            @Override
            public <T> T convert(Object source, Class<T> targetType) {
                return null;
            }

            @Override
            public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
                return null;
            }
        };
    }

    @Bean
    public Converter<Employee, EntityModel<Employee>> customEmployeeConverter() {
//        return source -> {
//            return EntityModel.of(source);
//        };
        return new EmployeeConverter();
    }

    static final class EmployeeConverter implements Converter<Employee, EntityModel<Employee>> {
        @Override
        public EntityModel<Employee> convert(Employee source) {
            return EntityModel.of(source);
        }
    }
}
