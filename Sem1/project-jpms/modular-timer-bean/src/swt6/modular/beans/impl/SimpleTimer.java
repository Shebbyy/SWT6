package swt6.modular.beans.impl;

import swt6.modular.beans.Timer;
import swt6.modular.beans.TimerEvent;
import swt6.modular.beans.TimerListener;

import java.util.ArrayList;
import java.util.List;

public class SimpleTimer implements Timer {
    private int noTicks ;
    private int interval;
    private boolean stopTimer = false;
    private Thread tickerThread = null;
    private final List<TimerListener> listeners = new ArrayList<>();

    public SimpleTimer(int interval, int noTicks) {
        this.noTicks = noTicks;
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    private void setInterval(int interval) {
        this.interval = interval;
    }

    public int getNoTicks() {
        return noTicks;
    }

    private void setNoTicks(int noTicks) {
        this.noTicks = noTicks;
    }

    @Override
    public boolean isRunning() {
        return tickerThread != null;
    }

    @Override
    public void addTimerListener(TimerListener l) {
        this.listeners.add(l);
    }

    @Override
    public void removeTimerListener(TimerListener l) {
        this.listeners.remove(l);
    }

    @Override
    public void start() {
        if (isRunning()) {
            throw new IllegalStateException("Cannot start - Timer already running");
        }

        int interval = getInterval();
        int noTicks  = getNoTicks();

        tickerThread = new Thread(() -> {
            int tickCount = 0;
            while (!stopTimer && tickCount < noTicks) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) { // ignore
                }

                if (!stopTimer) {
                    tickCount++;

                    fireEvent(new TimerEvent(this, tickCount, noTicks));
                }
            }

            stopTimer    = false;
            tickerThread = null;
        });

        tickerThread.start();
    }

    @Override
    public void stop() {
        stopTimer = true;
    }

    private void fireEvent(TimerEvent te) {
        listeners.forEach((l) -> {
            l.expired(te);
        });
    }
}
