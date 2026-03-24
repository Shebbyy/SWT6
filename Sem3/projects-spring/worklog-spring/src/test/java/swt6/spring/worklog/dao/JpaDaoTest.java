package swt6.spring.worklog.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import swt6.spring.worklog.config.JpaConfig1;
import swt6.spring.worklog.domain.Employee;

import java.time.LocalDate;
import java.util.Optional;

import static swt6.util.PrintUtil.printTitle;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaConfig1.class)
public class JpaDaoTest {

  @Autowired
  private EntityManagerFactory emFactory;

  @Autowired
  private EmployeeDao employeeDao;

  @Test
  @Transactional
  public void testInsertUpdate() {
    Employee empl1 =
      new Employee("Josef", "Himmelbauer", LocalDate.of(1950, 1, 1));

    printTitle("insert/update employee", 60, '-');

    employeeDao.insert(empl1);
    empl1.setFirstName("Kevin");
    empl1 = employeeDao.merge(empl1);
  }

  @Test
  @Transactional
  public void testFindById() {
    printTitle("find employee", 60, '-');
    Optional<Employee> empl = employeeDao.findById(1L);
    System.out.printf(
      "empl = " + empl.map(Employee::toString).orElse("<not found>"));

    empl = employeeDao.findById(100L);
    System.out.printf(
      "empl = " + empl.map(Employee::toString).orElse("<not found>"));
  }

  @Test
  @Transactional
  public void testFindAll() {
    printTitle("find all employees", 60, '-');
    employeeDao.findAll().forEach(System.out::println);
  }
}

