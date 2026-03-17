package swt6.spring.basics.ioc.logic;

import swt6.spring.basics.ioc.domain.Employee;

import java.util.List;

public interface WorkLogService {
    List<Employee> findAllEmployees();
    Employee findEmployeeById(Long id);
}
