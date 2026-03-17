package swt6.spring.basics.ioc.logic.factorybased;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import swt6.spring.basics.ioc.domain.Employee;
import swt6.spring.basics.ioc.util.Logger;
import swt6.spring.basics.ioc.util.LoggerFactory;

public class WorkLogServiceImpl {
  private Logger logger;
  private Map<Long, Employee> employees = new HashMap<>();

  private void initLogger() {
    Properties props = new Properties();
   
    try {
      ClassLoader cl = this.getClass().getClassLoader();
      props.load(cl.getResourceAsStream(
        "swt6/spring/basics/ioc/worklog.properties"));
    }
    catch (IOException e) {
      e.printStackTrace();
    }


    String type = props.getProperty("loggerType", "consoleLogger");
    logger = LoggerFactory.getLogger(type);
  }
  
  private void init() {
    employees.put(1L, new Employee(1L, "Bill", "Gates"));
    employees.put(2L, new Employee(2L, "James", "Goslin"));
    employees.put(3L, new Employee(3L, "Bjarne", "Stroustrup"));
  }
  
  public WorkLogServiceImpl() {
    initLogger();
    init();
  }


  public Employee findEmployeeById(Long id) {
    var empl = employees.get(id);

    logger.log("findEmployeeById(%d) --> %s".formatted(id, (empl != null) ? empl : "null"));

    return empl;
  }

  public List<Employee> findAllEmployees() {
      logger.log("findAllEmployees()");
    return new ArrayList<Employee>(employees.values());
  }
}
