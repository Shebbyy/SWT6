package swt6.nonmodular.beans;

import java.util.EventObject;

public class TimerEvent extends EventObject {

    private final int tickCount;
    private final int noTicks;

    public TimerEvent(Object source, int tickCount, int noTicks) {
        super(source);

        this.tickCount = tickCount;
        this.noTicks   = noTicks;
    }

    public int getTickCount() {
        return tickCount;
    }

    public int getNoTicks() {
        return noTicks;
    }

}
