package swt6.orm.hibernate;

import org.hibernate.cfg.Configuration;
import swt6.orm.domain.Employee;
import swt6.util.HibernateUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        // Session factory expensive, Sessions itself cheap
        try(var session = HibernateUtil.getCurrentSession()) {
            var tx = session.beginTransaction();

            // persistance operations
            session.persist(employee); // save employee

            tx.commit();
        }
    }

  public static void main(String[] args) {
        HibernateUtil.getSessionFactory(); // Initialize at start of application for same performance on every action
    var    formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    var    in        = new BufferedReader(new InputStreamReader(System.in));
    String availCmds = "commands: quit, insert";

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
