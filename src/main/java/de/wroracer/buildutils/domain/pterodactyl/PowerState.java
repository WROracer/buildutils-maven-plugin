package de.wroracer.buildutils.domain.pterodactyl;

public class PowerState {
    public static final PowerState START = new PowerState("start");
    public static final PowerState STOP = new PowerState("stop");
    public static final PowerState RESTART = new PowerState("restart");
    public static final PowerState KILL = new PowerState("kill");


    private String signal;

    private PowerState(String signal) {
        this.signal = signal;
    }

    public String getSignal() {
        return signal;
    }
}
