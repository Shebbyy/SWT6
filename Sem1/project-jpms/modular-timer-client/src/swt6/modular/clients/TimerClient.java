package swt6.modular.clients;

import swt6.modular.beans.Timer;
import swt6.modular.beans.TimerFactory;
import swt6.modular.beans.TimerProvider;

import java.util.Optional;
import java.util.ServiceLoader;

public class TimerClient {
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    private static void testTimer() {
        Timer timer = TimerFactory.createTimer(100, 10);

        timer.addTimerListener(te -> System.out.printf("Timer 1 expired: %d of %d%n", te.getTickCount(), te.getNoTicks()));

        timer.start();
        sleep(500);
        timer.stop();
    }

    private static Optional<Timer> getBestTimer(int interval, int noTicks) {
        ServiceLoader<TimerProvider> serviceLoader = ServiceLoader.load(TimerProvider.class);

        double minResolution = Double.MAX_VALUE;
        TimerProvider minProvider = null;

        for (TimerProvider provider : serviceLoader) {
            if (provider.timerResolution() < minResolution) {
                minProvider = provider;
                minResolution = provider.timerResolution();
            }
        }

        return minProvider == null ?
                Optional.empty() :
                Optional.of(minProvider.createTimer(interval, noTicks));
    }

    private static void testTimerProvider() {
        getBestTimer(100, 10).ifPresent(timer -> {
            timer.addTimerListener(te -> System.out.printf("timer expired: %d/%d%n",
                    te.getTickCount(), te.getNoTicks()));
            timer.start();
            sleep(500);
            timer.stop();
            sleep(150);
        });
    }

    private static void testReflection() {
        getBestTimer(100, 10).ifPresent(timer -> {
            try {
                System.out.printf("Fields of %s object %n", timer.getClass().getName());
                var fields = timer.getClass().getDeclaredFields();
                for (var field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(timer);
                    System.out.printf("  %s -> %s%n", field.getName(), value);
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    static void main() {
        System.out.printf("=========== testTimer ============%n");
        testTimer();

        System.out.printf("======= testTimerProvider ========%n");
        testTimerProvider();

        System.out.printf("========= testReflection =========%n");
        testReflection();
    }
}
