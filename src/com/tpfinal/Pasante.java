package com.tpfinal;

public class Pasante implements Runnable{
    private Monitor monitor;
    private int t1[];
    private int t2[];

    public Pasante(Monitor monitor, int[] t1, int[] t2) {
        this.monitor = monitor;
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public void run() {
        while(monitor.getState()) {
            monitor.disparar(t1);
            monitor.disparar(t2);
        }
    }
}