package com.tpfinal;

public class Capataz implements Runnable{
    private Monitor monitor;
    private int t[];

    public Capataz(Monitor monitor, int[] t) {
        this.monitor=monitor;
        this.t = t;
    }

    @Override
    public void run() {
        while(monitor.getState()) {
            monitor.disparar(t);
        }
    }
}
