package com.tpfinal;

public class Trabajador implements Runnable {
    private Monitor monitor;
    private int t1[];
    private int t2[];
    private int t3[];
    private int t4[];

    public Trabajador(Monitor monitor, int[] t1, int[] t2, int[] t3, int[] t4) {
        this.monitor = monitor;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
    }

    @Override
    public void run() {
        while (monitor.getState()) {
            monitor.disparar(t1);
            monitor.disparar(t2);
            monitor.disparar(t3);
            monitor.disparar(t4);
        }
    }
}