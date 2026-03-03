package swt6.modular.clients;

import swt6.modular.beans.Timer;
import swt6.modular.beans.TimerFactory;

public class TimerClient {
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    static void main() {
        Timer timer = TimerFactory.createTimer(100, 10);

        timer.addTimerListener(te -> System.out.printf("Timer 1 expired: %d of %d%n", te.getTickCount(), te.getNoTicks()));

        timer.start();
        sleep(500);
        timer.stop();
        sleep(500);
        timer.start();
    }
}
