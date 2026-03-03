package swt6.nonmodular.beans;

import java.util.EventListener;

@FunctionalInterface
public interface TimerListener extends EventListener {
    void expired(TimerEvent e);
}
