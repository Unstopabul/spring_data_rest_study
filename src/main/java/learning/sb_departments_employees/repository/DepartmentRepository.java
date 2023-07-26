package learning.sb_departments_employees.repository;

import learning.sb_departments_employees.model.Department;
import learning.sb_departments_employees.model.rest_projections.RestProjectionDepartment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@RepositoryRestResource(path = "deps", collectionResourceRel = "departs") // path - uri name, rel - json property name
//@RepositoryRestResource(excerptProjection = RestProjectionDepartment.class)   // set projection as default
public interface DepartmentRepository extends CrudRepository<Department, Integer>,
        PagingAndSortingRepository<Department, Integer> {
    // http://localhost:8080/departments?page=1&size=2&sort=headId,desc

    // http://localhost:8080/departments/search/findByParentId?parentId=1
    List<Department> findByParentId(int parentId);
}
