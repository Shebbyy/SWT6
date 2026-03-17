package swt6.spring.basics.aop.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TraceAdvice {

    @Before("execution(public * swt6.spring.basics.aop.logic..*find*(..))")
    public void traceBefore(JoinPoint jp) {
        String methodName = jp.getTarget().getClass().getName() + ":" + jp.getSignature().getName();
        System.out.println("----> " + methodName);
    }

    @After("execution(public * swt6.spring.basics.aop.logic..*find*(..))")
    public void traceAfter(JoinPoint jp) {
        String methodName = jp.getTarget().getClass().getName() + ":" + jp.getSignature().getName();
        System.out.println("<---- " + methodName);
    }

    @Around("execution(public * swt6.spring.basics.aop.logic..*find*(..))")
    public Object traceAround(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getTarget().getClass().getName() + ":" + jp.getSignature().getName();

        System.out.println("==> " + methodName);

        Object returnVal = jp.proceed();

        System.out.println("<== " + methodName);

        return returnVal;
    }


    @AfterThrowing(value = "execution(public * swt6.spring.basics.aop.logic..*find*(..))", throwing = "exception")
    public void traceException(JoinPoint jp, Throwable exception) {
        String methodName = jp.getTarget().getClass().getName() + ":" + jp.getSignature().getName();

        System.out.printf("%s throws exception: %s%n", methodName, exception);
    }
}
