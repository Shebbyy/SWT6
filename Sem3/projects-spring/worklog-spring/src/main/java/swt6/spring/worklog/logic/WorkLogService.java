package swt6.spring.worklog.logic;

import java.util.List;
import java.util.Optional;

import swt6.spring.worklog.domain.Employee;

public interface WorkLogService {
  public Employee               syncEmployee(Employee employee);
  public Optional<Employee>     findEmployeeById(Long id);
  public List<Employee>         findAllEmployees();
}
