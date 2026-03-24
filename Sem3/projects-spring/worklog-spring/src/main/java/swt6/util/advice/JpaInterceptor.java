package swt6.util.advice;

import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import swt6.util.JpaUtil;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class JpaInterceptor {

  @Getter
  private final EntityManagerFactory entityManagerFactory;

  @Around("@within(swt6.util.annotation.ViewModel)")
  public Object holdEntityManager(ProceedingJoinPoint pjp) throws Throwable {
    if (entityManagerFactory == null)
      throw new IllegalArgumentException("Property 'entityManagerFactory' is required");

    boolean participate = false;
    if (TransactionSynchronizationManager.hasResource(entityManagerFactory))
      participate = true;
    else {
      log.trace("Opening EntityManager");
      JpaUtil.openEntityManager(entityManagerFactory);
    }

    try {
      return pjp.proceed(); // delegates to method of target class.
    }
    finally {
      if (!participate) {
        JpaUtil.closeEntityManager(entityManagerFactory);
        log.trace("Closed EntityManager");
      }
    }
  }
}