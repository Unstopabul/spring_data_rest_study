package learning.sb_departments_employees.repository;

import learning.sb_departments_employees.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    // http://localhost:8080/employees/search/salaryGreaterThanEqual?salary=500&sort=depId,desc&sort=email
    @RestResource(path = "salaryGreaterThanEqual", rel = "salaryGreaterThanEqual")
    List<Employee> findBySalaryGreaterThanEqual(double salary, Sort sort);

    // http://localhost:8080/employees/search/findByDepIdIn?depId=1,3
    // Slice works as List, no additional info in response
    Page<Employee> findByDepIdIn(Collection<Integer> depId, Pageable pageable);

    Optional<Employee> findByName(String name);
}
