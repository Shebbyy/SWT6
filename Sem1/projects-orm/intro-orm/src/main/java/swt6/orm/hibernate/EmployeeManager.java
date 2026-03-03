package swt6.orm.hibernate;

import org.hibernate.cfg.Configuration;
import swt6.orm.domain.Employee;
import swt6.util.HibernateUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmployeeManager {

  static String promptFor(BufferedReader in, String prompt) {
    System.out.print(prompt + "> ");
    System.out.flush();
    try {
      return in.readLine();
    }
    catch (Exception e) {
      return promptFor(in, prompt);
    }
  }

  // v1
  /**public static void saveEmployee(Employee employee) {
      // Session factory expensive, Sessions itself cheap
      try(var factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
          var session = factory.openSession()) {
          var tx = session.beginTransaction();

          // persistance operations
          session.persist(employee); // save employee

          tx.commit();
      } // factory.close() / session.close()
  }**/

    // v2
    // not thread safe, due to sharing session
    /**public static void saveEmployee(Employee employee) {
        // Session factory expensive, Sessions itself cheap
        try(var factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            var session = factory.getCurrentSession()) {
            var tx = session.beginTransaction();

            // persistance operations
            session.persist(employee); // save employee

            tx.commit();
        }
    }**/


    // v3
    // Connection Management cleanup
    public static void saveEmployee(Employee employee) {
        try(var session = HibernateUtil.getCurrentSession()) {
            var tx = session.beginTransaction();

            session.persist(employee);

            tx.commit();
        }
    }

    private static List<Employee> getAllEmployees() {
        try(var session = HibernateUtil.getCurrentSession()) {
            var tx = session.beginTransaction();

            // Select from JavaClass Order By ClassField
            var l = session.createQuery("SELECT e FROM Employee e ORDER BY lastName", Employee.class).getResultList();

            tx.commit();

            return l;
        }
    }

    private static boolean updateEmployee(long id, String firstName, String lastName, LocalDate dob) {
        try(var session = HibernateUtil.getCurrentSession()) {
            var tx = session.beginTransaction();

            // Select from JavaClass Order By ClassField
            var em = session.find(Employee.class, id);

            if (em != null) {
                em.setFirstName(firstName);
                em.setLastName(lastName);
                em.setDateOfBirth(dob);
            }

            tx.commit();

            return em != null;
        }
    }

    private static Employee findEmployeeById(long id) {
        try(var session = HibernateUtil.getCurrentSession()) {
            var tx = session.beginTransaction();

            // Select from JavaClass Order By ClassField
            var em = session.find(Employee.class, id);

            tx.commit();

            return em;
        }
    }

  public static void main(String[] args) {
        HibernateUtil.getSessionFactory(); // Initialize at start of application for same performance on every action
    var    formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    var    in        = new BufferedReader(new InputStreamReader(System.in));
    String availCmds = "commands: quit, insert, list, findById, update";

    System.out.println("Hibernate Employee Admin");
    System.out.println(availCmds);
    String userCmd = promptFor(in, "");

    try {
      while (!userCmd.equals("quit")) {
        try {
          switch (userCmd) {
            case "insert" -> saveEmployee(new Employee(
                    promptFor(in, "firstName"),
                    promptFor(in, "lastName"),
                    LocalDate.parse(promptFor(in, "DOB (dd.mm.yyyy)"), formatter)
            ));

            case "list" -> {
                for (var employee : getAllEmployees()) {
                    System.out.println(employee);
                }
            }
            case "findById" -> System.out.println(findEmployeeById(Long.parseLong(promptFor(in, "id"))));

            case "update" -> {
                var id = Long.parseLong(promptFor(in, "id"));

                if (updateEmployee(
                        id,
                        promptFor(in, "firstName"),
                        promptFor(in, "lastName"),
                        LocalDate.parse(promptFor(in, "DOB (dd.mm.yyyy)"), formatter))
                ) {
                    System.out.println("Update successful");
                    System.out.println(findEmployeeById(id));
                } else {
                    System.out.println("Update failed");
                }
            }


            default -> {
              System.out.println("ERROR: invalid command");
              break;
            }
          } // switch
        } // try
        catch (NumberFormatException ignored) {
          System.out.println("ERROR: Cannot parse integer");
        }
          catch (DateTimeParseException ignored) {
          System.out.println("ERROR: Cannot parse date");
        }

        System.out.println(availCmds);
        userCmd = promptFor(in, "");
      } // while
    } // try
    catch (Exception ex) {
      ex.printStackTrace();
    } // catch
    finally {
        HibernateUtil.closeSessionFactory();
    }
  }
}
