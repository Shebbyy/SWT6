package swt6.spring.worklog.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
