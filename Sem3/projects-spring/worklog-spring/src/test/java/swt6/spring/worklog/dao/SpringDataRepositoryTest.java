package swt6.spring.worklog.dao;

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
public class SpringDataRepositoryTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Test
  @Transactional
  public void testSave() {
    Employee empl1 =
      new Employee("Josef", "Himmelbauer", LocalDate.of(1950, 1, 1));
    Employee empl2 = new Employee("Karl", "Malden", LocalDate.of(1940, 5, 3));

    printTitle("insert employee", 60, '-');
    empl1 = employeeRepository.save(empl1);
    empl2 = employeeRepository.save(empl2);
    employeeRepository.flush();

    printTitle("update employee", 60, '-');
    empl1.setLastName("Himmelbauer-Huber");
    empl1 = employeeRepository.save(empl1);
  }

  @Test
  @Transactional
  public void testFind() {
    printTitle("findById", 60, '-');
    Optional<Employee> empl3 = employeeRepository.findById(1L);
    System.out.printf(
      "empl1 = " + empl3.map(Employee::toString).orElse("<not found>"));

    printTitle("findAll", 60, '-');
    employeeRepository.findAll().forEach(System.out::println);
  }

  @Test
  public void testQueries() {
      testSave();

      printTitle("findByLastName", 60, '-');

      Optional<Employee> empl1 = employeeRepository.findByLastName("Malden");
      System.out.println(
              "empl1 = " + empl1.map(Employee::toString).orElse("<not found>"));

      printTitle("findByLastNameContaining", 60, '-');

      employeeRepository.findByLastNameContaining("a").forEach(System.out::println);

      printTitle("findOlderThan", 60, '-');
      employeeRepository.findOlderThan(LocalDate.of(1949, 1, 1)).forEach(System.out::println);
  }
}

