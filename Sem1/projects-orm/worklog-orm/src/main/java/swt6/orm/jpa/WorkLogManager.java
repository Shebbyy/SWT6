package swt6.orm.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.TypedQuery;
import swt6.orm.domain.*;
import swt6.util.JpaUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

                if (emp.getAddress() != null) {
                    System.out.printf("    address: %s%n", emp.getAddress());
                }

                if (!emp.getPhones().isEmpty()) {
                    System.out.println("    phones:");

                    emp.getPhones().forEach((phone) -> System.out.printf("       %s%n", phone));
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

    private static Employee addPhones(Employee empl, String ...phones) {
        return executeInTransaction(em -> {
            var employee = em.merge(empl);

            for (String phone : phones) {
                employee.addPhone(phone);
            }

            return employee;
        });
    }

    private static <T> Optional<T> findAny(Class<T> entityClass) {
        return executeInTransaction(em -> {
            Optional<T> entity =
                    em.createQuery("select le from %s le".formatted(entityClass.getSimpleName()), entityClass).setMaxResults(1)
                            .getResultList().stream().findAny();
            return entity;
        });
    }

    private static void testFetchingStrategies() {
        // prepare: fetch valid ids for employee and logbookentry
        var anyEmpl  = findAny(Employee.class);
        var anyEntry = findAny(LogbookEntry.class);
        if (anyEmpl.isEmpty() || anyEntry.isEmpty()) return;

        System.out.println("############################################");

        executeInTransaction(em -> {
            Long entryId = anyEntry.get().getId();
            System.out.println("###> Fetching LogbookEntry ...");
            LogbookEntry entry = em.find(LogbookEntry.class, entryId);
            System.out.println("###> Fetched LogbookEntry");
            Employee empl1 = entry.getEmployee();
            System.out.println("###> Fetched associated Employee");
            System.out.println(empl1);
            System.out.println("###> Accessed associated Employee");
        });

        System.out.println("############################################");

        executeInTransaction(em -> {
            Long emplId = anyEmpl.get().getId();
            System.out.println("###> Fetching Employee ...");
            Employee empl2 = em.find(Employee.class, emplId);
            System.out.println("###> Fetched Employee");
            Set<LogbookEntry> entries = empl2.getLogbookEntries();
            System.out.println("###> Fetched associated entries");
            for (LogbookEntry e : entries)
                System.out.println("  " + e);
            System.out.println("###> Accessed associated entries");
        });

        System.out.println("############################################");
    }

    private static void listEntriesOfEmployee(Employee empl) {
        executeInTransaction(em -> {
            System.out.printf("Logbook entries of Employee %s (%d)%n", empl.getLastName(), empl.getId());

            TypedQuery<LogbookEntry> qry = em.createQuery("SELECT le FROM LogbookEntry le WHERE le.employee = :empl", LogbookEntry.class);

            qry.setParameter("empl", empl);

            qry.getResultList().forEach(System.out::println);
        });
    }

    private static void loadEmployeesWithEntries() {
        executeInTransaction(em -> {
            // Fetch join: Logbook Entries fetched with Employee, even if lazy loading is defined in mapping
            TypedQuery<Employee> qry = em.createQuery("SELECT em FROM Employee em JOIN FETCH em.logbookEntries", Employee.class);

            qry.getResultList().forEach((empl) -> {
                System.out.println(empl);

                empl.getLogbookEntries().forEach(le -> System.out.println("     " + le));
            });
        });
    }

    private static void listEntriesofEmployeeQueryDsl(Employee empl) {
        executeInTransaction(em -> {
            JPAQueryFactory queryFactory = new JPAQueryFactory(em);
            QLogbookEntry logbookEntry = QLogbookEntry.logbookEntry;

            // var 1
            //var query = queryFactory
            //        .select(logbookEntry)
            //        .from(logbookEntry)
            //        .where(logbookEntry.employee.eq(empl));
//
            //query.fetch().forEach(System.out::println);

            // var 2
            //var query = queryFactory
            //        .selectFrom(logbookEntry)
            //        .where(logbookEntry.employee.eq(empl));
            //query.fetch().forEach(System.out::println);

            // var 3 (projection + sort)
            var query = queryFactory
                    .select(logbookEntry.activity, logbookEntry.startTime, logbookEntry.endTime)
                    .from(logbookEntry)
                    .where(logbookEntry.employee.eq(empl))
                    .orderBy(logbookEntry.startTime.asc());

            query.fetch().forEach(tupel -> System.out.printf("%s: %s - %s%n",
                    tupel.get(logbookEntry.activity),
                    tupel.get(logbookEntry.startTime),
                    tupel.get(logbookEntry.endTime)));
        });
    }

    private static void listEmployeesWithMatchingActivitiesQueryDSL(String pattern) {
        executeInTransaction(em -> {
            var queryFactory = new JPAQueryFactory(em);
            var employee = QEmployee.employee;
            var logbookEntry = QLogbookEntry.logbookEntry;

            queryFactory.select(employee)
                    .from(employee)
                    .innerJoin(employee.logbookEntries, logbookEntry)
                    .on(logbookEntry.activity.like(pattern))
                    .fetch()
                    .forEach(System.out::println);
        });
    }

    public static void main(String[] args) {
        try {
            System.out.println("-------- Create Schema ----------");
            JpaUtil.getEntityManagerFactory();

            PermanentEmployee pe = new PermanentEmployee("Max", "Mustermann", LocalDate.of(2000, 1, 1));
            pe.setAddress(new Address("4232", "Hagenberg", "Hauptstraße 1"));
            pe.setSalary(5000.0);
            addPhones(pe, "1", "2", "3");
            Employee emp1 = pe;

            TemporaryEmployee te = new TemporaryEmployee("Maria", "Musterfrau", LocalDate.of(1999, 12, 31));
            te.setHourlyRate(50.0);
            te.setRenter("Acme");
            te.setStartDate(LocalDate.of(2026, 1, 1));
            te.setEndDate(LocalDate.of(2026, 12, 31));
            Employee emp2 = te;

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
            addLogbookEntries(emp1, entry1);
            addLogbookEntries(emp2, entry2, entry3);

            System.out.println("------- List Employees ----------");
            listEmployees();

            System.out.println("------- Test Fetching Strategies ----------");

            testFetchingStrategies();

            System.out.println("------- List Logbook Entries of Empl1 ----------");
            listEntriesOfEmployee(emp1);


            System.out.println("------- Load Employees with fetch join logbook entries ----------");
            loadEmployeesWithEntries();

            System.out.println("------- List Logbook Entries of Empl1 with QueryDSL ----------");
            listEntriesofEmployeeQueryDsl(emp1);

            System.out.println("------- List Employees with matching logbook entry QueryDSL ----------");
            listEmployeesWithMatchingActivitiesQueryDSL("%Test%");
        }
        finally {
            System.out.println("------- Closing Entity Manager Factory --------");
            JpaUtil.closeEntityManagerFactory();
        }
    }
}
