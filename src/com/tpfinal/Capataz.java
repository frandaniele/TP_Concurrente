package com.tpfinal;

public class Capataz extends Trabajador implements Runnable{

    public Capataz(Monitor monitor, int[][] transiciones) {
        super.monitor = monitor;
        super.transiciones = transiciones;
    }

    @Override
    public void run() {
        while(monitor.seguirDisparando()) {
            super.disparos();
            if(Thread.interrupted())
                break;
        }
    }
}