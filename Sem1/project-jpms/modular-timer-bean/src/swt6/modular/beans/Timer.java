package swt6.modular.beans;

public interface Timer {
    boolean isRunning();

    void addTimerListener(TimerListener l);

    void removeTimerListener(TimerListener l);

    void start();

    void stop();
}
