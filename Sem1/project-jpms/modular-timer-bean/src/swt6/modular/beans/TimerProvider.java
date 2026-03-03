package swt6.modular.beans;

public interface TimerProvider {
    double timerResolution();
    Timer createTimer(int interval, int numTicks);
}
