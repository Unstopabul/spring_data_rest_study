package learning.sb_departments_employees.model.rest_projections;

import learning.sb_departments_employees.model.Department;
import learning.sb_departments_employees.model.Employee;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

// http://localhost:8080/departments?projection=withIdAndEmplInline
@Projection(name = "withIdAndEmplInline", types = Department.class)
public interface RestProjectionDepartment {
    int getId();
    String getName();
    List<Employee> getEmployees();
}
