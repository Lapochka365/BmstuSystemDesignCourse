package tigrbank.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimingAspect {

    @Around("@annotation(tigrbank.service.Timed)")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object result = joinPoint.proceed();
        long elapsed = System.nanoTime() - start;
        System.out.printf("  ⏱ [%s] выполнено за %.3f мс%n",
                joinPoint.getSignature().getName(), elapsed / 1_000_000.0);
        return result;
    }
}
