package swt6.modular.beans;

import swt6.modular.beans.impl.SimpleTimer;

public class TimerFactory {

    public static Timer createTimer(int interval, int noTicks) {
        return new SimpleTimer(interval, noTicks);
    }
}
