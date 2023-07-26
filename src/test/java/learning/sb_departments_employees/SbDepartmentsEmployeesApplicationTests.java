package learning.sb_departments_employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.sb_departments_employees.model.Department;
import learning.sb_departments_employees.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@WebMvcTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SbDepartmentsEmployeesApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ForTests forTests;

    @Test
    void findAllDeps() throws Exception {
        this.mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name", hasSize(3)))
                .andExpect(jsonPath("$._embedded.departments[2].name", is("Kotlin")));
    }

    @Test
    void findDepById() throws Exception {
        this.mockMvc.perform(get("/departments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Java")))
                .andExpect(jsonPath("$.headId", is(2)));
    }

    @Test
//    @Transactional
    void persCtxWithoutTransaction() throws Exception {
        Optional<Department> dep1 = departmentRepository.findById(1);
        Optional<Department> dep2 = departmentRepository.findById(1);
        assertSame(dep1.get(), dep2.get());
    }

    @Test
//    @Transactional
    void checkCommitAfterSet() throws Exception {
        forTests.setDepName(1, "NuName");
        Optional<Department> dep = departmentRepository.findById(1);
        assertEquals(dep.get().getName(), "NuName");
    }

    @Test
    void test1_2() {
        Set<Object> set = new TreeSet<>();
        set.add(5.5);
        set.add(6.7f);
    }

    @Test
    void createDep() throws Exception {
        Department department = Department.builder().name("REST").parentId(3).headId(4).build();

        this.mockMvc.perform(
                post("/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(department))
        ).andExpect(status().isCreated());

        this.mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name", hasSize(4)))
                .andExpect(jsonPath("$._embedded.departments[3].name", is("REST")));
    }

    @Test
    void updateDep() throws Exception {
        this.mockMvc.perform(
                patch("/departments/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"SpringJava\"}")
        ).andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/departments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("SpringJava")))
                .andExpect(jsonPath("$.headId", is(2)));
    }

    @Test
    void deleteDep() throws Exception {
        Department department = Department.builder().name("REST").parentId(3).headId(4).build();

        this.mockMvc.perform(
                post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department))
        );

        this.mockMvc.perform(
                delete("/departments/4")
        ).andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name", hasSize(3)));
    }

    @Test
    void findEmpByIdError() throws Exception {
        this.mockMvc.perform(get("/employees/7"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDepValidationError() throws Exception {
        Department department = Department.builder().name("REST89").parentId(3).headId(4).build();

        this.mockMvc.perform(
                post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].defaultMessage", containsString("should contain only A-Z")));
    }

    @Test
    void contextLoads() {
    }

}

@Component
class ForTests {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    void setDepName(int id, String name) throws Exception {
        Optional<Department> dep1 = departmentRepository.findById(id);
        dep1.get().setName(name);
    }
}
