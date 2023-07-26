package learning.sb_departments_employees.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import learning.sb_departments_employees.model.Department;
import learning.sb_departments_employees.model.Employee;
import learning.sb_departments_employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.io.IOException;

@Component
public class CustomRepositoryRestConfigurer implements RepositoryRestConfigurer {
    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    @Autowired
    public CustomRepositoryRestConfigurer(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.localValidatorFactoryBean = localValidatorFactoryBean;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//        config.setRepositoryDetectionStrategy(RepositoryDetectionStrategies.ANNOTATED);
//        config.setBasePath("/myapi");
        config.withEntityLookup() // uri becomes -> http://localhost:8080/employees/Oleg%20Smirnov
                .forRepository(EmployeeRepository.class)
                .withIdMapping(Employee::getName)
                .withLookup(EmployeeRepository::findByName);
    }

//    @Override
//    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
//        RepositoryRestConfigurer.super.configureValidatingRepositoryEventListener(validatingListener);
//        validatingListener.addValidator("beforeSave", localValidatorFactoryBean);
//    }

    //    @Override
//    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
//        objectMapper.registerModule(new SimpleModule("DepartmentsMapperModule")
//            .addSerializer(Department.class, new JsonSerializer<Department>() {
//                @Override
//                public void serialize(Department value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                    serializers.defaultSerializeField("IDDDD", value.getId(), gen);
//                    serializers.defaultSerializeField("Nnnname", value.getName(), gen);
//                }
//            })
//        );
//    }
}
