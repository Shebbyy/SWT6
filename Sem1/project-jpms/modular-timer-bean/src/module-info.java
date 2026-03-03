import swt6.modular.beans.TimerProvider;
import swt6.modular.beans.impl.SimpleTimerProvider;

module swt.modular.beans {
    exports swt6.modular.beans;

    provides TimerProvider with SimpleTimerProvider; // Indirection, eg. no direct dependancy on client, rather interface, interface then integrated into client

    opens swt6.modular.beans.impl; // for reflection access
}