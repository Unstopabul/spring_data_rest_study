package learning.sb_departments_employees.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Entity(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "'name' can not be empty")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "'name' should contain only A-Z, a-z and space characters")
    private String name;
    @Column(name = "dep_id")
    @NotNull(message = "'depId' can not be empty")
    private int depId;
    @Column(name = "email")
    @NotNull(message = "'email' can not be empty")
    @Email(message = "'email' should be valid")
    private String email;
    @Column(name = "salary")
    @NotNull(message = "'salary' can not be empty")
    private double salary;

    public Employee() {
    }

    public Employee(String name, int depId, String email, double salary) {
        this.name = name;
        this.depId = depId;
        this.email = email;
        this.salary = salary;
    }
}
