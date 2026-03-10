package swt6.orm.jpa;

import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.util.JpaUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static swt6.util.JpaUtil.executeInTransaction;

public class WorkLogManager {

    // var1
    //private static void insertEmployee(Employee emp) {
    //    // persistenceUnit from persistence.xml
    //    try (var emFactory = Persistence.createEntityManagerFactory("WorkLogPU")) {
    //        var em = emFactory.createEntityManager();
//
    //        EntityTransaction tx = em.getTransaction();
//
    //        try {
    //            tx.begin();
//
    //            em.persist(emp);
//
    //            tx.commit();
    //        } catch (Exception e) {
    //            if (tx.isActive()) {
    //                tx.rollback();
    //            }
//
    //            throw e;
    //        }
    //    }
    //}

    // v2
    // on call emp still transient
    //private static void insertEmployee(Employee emp) {
    //    try (var em = JpaUtil.getTransactionalEntityManager()) {
    //        try {
    //            // emp now persistent
    //            em.persist(emp);
//
    //            JpaUtil.commit(em);
    //            // emp now stored/detached
    //        } catch (Exception e) {
    //            JpaUtil.rollback(em);
//
    //            throw e;
    //        }
    //    }
    //}

    // v3
    private static void insertEmployee(Employee emp) {
        executeInTransaction((em) -> {
            em.persist(emp);
        });
    }


    // v4
    private static <T> void insertEntity(T emp) {
        executeInTransaction((em) -> {
            em.persist(emp);
        });
    }

    private static <T> T saveEntity(T emp) {
        return executeInTransaction((em) -> {
            // if elem with same id exists -> Update/merge, otherwise create; Upsert essentially
            return em.merge(emp);
        });
    }

    private static void listEmployees() {
        executeInTransaction(em -> {
            var fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            List<Employee> employeeList = em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();

            employeeList.forEach((emp) -> {
                System.out.println(emp);

                if (!emp.getLogbookEntries().isEmpty()) {
                    System.out.println("    logbookEntries:");

                    emp.getLogbookEntries().forEach((logEntry) -> {
                        System.out.printf("       %s: %s - %s%n", logEntry.getActivity(), logEntry.getStartTime().format(fmt), logEntry.getEndTime().format(fmt));
                    });
                }
            });
        });
    }

    private static Employee addLogbookEntries(Employee empl, LogbookEntry ...entries) {
        return executeInTransaction(em -> {
            var employee = em.merge(empl);

            for (LogbookEntry entry : entries) {
                employee.addLogbookEntry(entry);
            }

            return employee;
        });
    }

    public static void main(String[] args) {
        try {
            System.out.println("-------- Create Schema ----------");
            JpaUtil.getEntityManagerFactory();

            Employee emp1 = new Employee("Max", "Mustermann", LocalDate.of(2000, 1, 1));
            Employee emp2 = new Employee("Maria", "Musterfrau", LocalDate.of(1999, 12, 31));

            LogbookEntry entry1 = new LogbookEntry("Analyse",
                    LocalDateTime.of(2026, 3, 9, 8, 15),
                    LocalDateTime.of(2026, 3, 9, 10, 15)
            );

            LogbookEntry entry2 = new LogbookEntry("Implementation",
                    LocalDateTime.of(2026, 3, 9, 8, 15),
                    LocalDateTime.of(2026, 3, 9, 17, 30)
            );

            LogbookEntry entry3 = new LogbookEntry("Testing",
                    LocalDateTime.of(2026, 3, 9, 12, 15),
                    LocalDateTime.of(2026, 3, 9, 18, 15)
            );

            System.out.println("------- Insert Entities ---------");
            //insertEmployee(emp1);

            System.out.println("------- Save Entity ----------");
            emp1 = saveEntity(emp1);
            emp2 = saveEntity(emp2);

            System.out.println("------- Add Logbook Entries ----------");
            emp1 = addLogbookEntries(emp1, entry1);
            emp2 = addLogbookEntries(emp2, entry2, entry3);

            System.out.println("------- List Employees ----------");
            listEmployees();
        }
        finally {
            System.out.println("------- Closing Entity Manager Factory --------");
            JpaUtil.closeEntityManagerFactory();
        }
    }
}
