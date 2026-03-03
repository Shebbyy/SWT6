package swt6.nonmodular.clients;

import swt6.nonmodular.beans.SimpleTimer;

public class TimerClient {
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    static void main() {
        SimpleTimer timer = new SimpleTimer(100, 10);

        timer.addTimerListener(te -> System.out.printf("Timer 1 expired: %d of %d%n", te.getTickCount(), te.getNoTicks()));

        timer.start();
        sleep(500);
        timer.stop();
        sleep(500);
        timer.start();
    }
}
