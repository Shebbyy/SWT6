package swt6.spring.basics.ioc;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import swt6.spring.basics.hello.GreetingService;
import swt6.spring.basics.ioc.logic.WorkLogService;
import swt6.spring.basics.ioc.logic.factorybased.WorkLogServiceImpl;

public class IocTest {

  @Test
  public void testFactoryApproach() {
      WorkLogServiceImpl workLog = new WorkLogServiceImpl();

      workLog.findAllEmployees();

      workLog.findEmployeeById(3L);
  }

  @Test
  public void testXmlConfig() {
      try (AbstractApplicationContext factory = new ClassPathXmlApplicationContext("swt6/spring/basics/ioc/applicationcontext-xml-config.xml")) {
          WorkLogService service = factory.getBean("workLogService-constructor-injection", WorkLogService.class);

          service.findAllEmployees();

          service.findEmployeeById(3L);
      }
  }

  @Test
  public void testJavaConfig() {
  }
}
