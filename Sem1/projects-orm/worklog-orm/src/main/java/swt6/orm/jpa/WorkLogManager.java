package swt6.orm.jpa;

import swt6.orm.domain.Employee;
import swt6.util.JpaUtil;

import java.time.LocalDate;
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
            List<Employee> employeeList = em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();

            employeeList.forEach(System.out::println);
        });
    }

    public static void main(String[] args) {
        try {
            System.out.println("-------- Create Schema ----------");
            JpaUtil.getEntityManagerFactory();

            Employee emp1 = new Employee("Max", "Mustermann", LocalDate.of(2000, 1, 1));
            Employee emp2 = new Employee("Maria", "Musterfrau", LocalDate.of(1999, 12, 31));

            System.out.println("------- Insert Entities ---------");
            //insertEmployee(emp1);

            System.out.println("------- Save Entity ----------");
            emp1 = saveEntity(emp1);
            emp2 = saveEntity(emp2);

            System.out.println("------- List Employees ----------");
            listEmployees();
        }
        finally {
            System.out.println("------- Closing Entity Manager Factory --------");
            JpaUtil.closeEntityManagerFactory();
        }
    }
}
