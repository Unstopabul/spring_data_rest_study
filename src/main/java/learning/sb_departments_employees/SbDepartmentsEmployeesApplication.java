package learning.sb_departments_employees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.convert.converter.Converter;


@SpringBootApplication
public class SbDepartmentsEmployeesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbDepartmentsEmployeesApplication.class, args);
    }

//    @Bean
//    public HibernatePropertiesCustomizer getHiberPropCust(ObjectFactory<PersistentEntities> persistentEntitiesFactory) {
//        return hibernateProperties -> {
////            hibernateProperties.put(AvailableSettings.JAKARTA_VALIDATION_FACTORY, getLocalValidatorFactoryBean());
//            hibernateProperties.put(AvailableSettings.JAKARTA_VALIDATION_FACTORY, getCustomValidatorFactory(persistentEntitiesFactory));
////                hibernateProperties.put(AvailableSettings.JAKARTA_VALIDATION_MODE, "NONE");
//        };
//    }
}