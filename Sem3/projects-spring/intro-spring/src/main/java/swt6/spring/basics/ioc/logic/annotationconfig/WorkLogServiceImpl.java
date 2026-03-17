package swt6.spring.basics.ioc.logic.annotationconfig;

import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import swt6.spring.basics.ioc.domain.Employee;
import swt6.spring.basics.ioc.logic.WorkLogService;
import swt6.spring.basics.ioc.util.Log;
import swt6.spring.basics.ioc.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Service("workLogService")
public class WorkLogServiceImpl implements WorkLogService {
  private final Map<Long, Employee> employees = new HashMap<>();

  // var 2 setter injection, when using lombok
  // @Setter(onMethod_ = @Autowired)
  @Setter
  private Logger logger;

    @org.springframework.beans.factory.annotation.Autowired
    public WorkLogServiceImpl(@Log(Log.Type.CONSOLE) Logger logger) {
        init();
        this.logger = logger;
    }

  private void init() {
    employees.put(1L, new Employee(1L, "Bill", "Gates"));
    employees.put(2L, new Employee(2L, "James", "Goslin"));
    employees.put(3L, new Employee(3L, "Bjarne", "Stroustrup"));
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
