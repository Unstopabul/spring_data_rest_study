package learning.sb_departments_employees.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "'name' can not be empty")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "'name' should contain only A-Z, a-z and space characters")
    private String name;
    @Column(name = "parent_id")
    @NotNull(message = "'parentId' can not be empty")
    private int parentId;
    @Column(name = "head_id")
    @NotNull(message = "'headId' can not be empty")
    private int headId;
    @OneToMany
    @JoinColumn(name = "dep_id")
//    @RestResource(path = "emppath", rel = "emprel")  // path - uri name, rel - json property name
    private List<Employee> employees;
}
