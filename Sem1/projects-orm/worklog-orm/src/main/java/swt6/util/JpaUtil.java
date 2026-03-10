package swt6.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;
import java.util.function.Function;

public class JpaUtil {

  private static EntityManagerFactory emFactory;

  public static synchronized EntityManagerFactory getEntityManagerFactory() {
    if (emFactory == null) {
        emFactory = Persistence.createEntityManagerFactory("WorkLogPU");
    }

    return emFactory;
  }

  public static synchronized void closeEntityManagerFactory() {
    if (emFactory != null) {
        emFactory.close();
        emFactory = null;
    }
  }

  public static EntityManager getTransactionalEntityManager() {
      var em = getEntityManagerFactory().createEntityManager();
      em.getTransaction().begin();

      return em;
  }

  public static void commit(EntityManager em) {
    var tx = em.getTransaction();
    if (tx.isActive()) {
        tx.commit();
    }
  }

  public static void rollback(EntityManager em) {
      var tx = em.getTransaction();
      if (tx.isActive()) {
          tx.rollback();
      }
  }

  public static void executeInTransaction(Consumer<EntityManager> callback) {
      try (var em = getTransactionalEntityManager()) {
          try {
              callback.accept(em);

              commit(em);
          } catch (Exception e) {
              rollback(em);

              throw e;
          }
      }
  }

    public static <T> T executeInTransaction(Function<EntityManager, T> callback) {
        try (var em = getTransactionalEntityManager()) {
            try {
                T res = callback.apply(em);

                commit(em);

                return res;
            } catch (Exception e) {
                rollback(em);

                throw e;
            }
        }
    }
}
