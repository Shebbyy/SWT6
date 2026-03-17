package swt6.spring.basics.ioc.logic.xmlconfig;

import lombok.NoArgsConstructor;
import lombok.Setter;
import swt6.spring.basics.ioc.domain.Employee;
import swt6.spring.basics.ioc.logic.WorkLogService;
import swt6.spring.basics.ioc.util.Logger;

import java.util.*;

@NoArgsConstructor
public class WorkLogServiceImpl implements WorkLogService {
  private final Map<Long, Employee> employees = new HashMap<>();

  @Setter
  private Logger logger;

  private void init() {
    employees.put(1L, new Employee(1L, "Bill", "Gates"));
    employees.put(2L, new Employee(2L, "James", "Goslin"));
    employees.put(3L, new Employee(3L, "Bjarne", "Stroustrup"));
  }
  
  public WorkLogServiceImpl(Logger logger) {
      init();
      this.logger = logger;
  }


  @Override
  public Employee findEmployeeById(Long id) {
    var empl = employees.get(id);

    logger.log("findEmployeeById(%d) --> %s".formatted(id, (empl != null) ? empl : "null"));

    return empl;
  }

  @Override
  public List<Employee> findAllEmployees() {
      logger.log("findAllEmployees()");
      return new ArrayList<>(employees.values());
  }
}
