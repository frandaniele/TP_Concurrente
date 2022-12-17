package com.tpfinal;

public class Operario extends Trabajador implements Runnable {

    public Operario(Monitor monitor, int[][] transiciones) {
        super.monitor = monitor;
        super.transiciones = transiciones;
    }

    @Override
    public void run() {
        while(monitor.seguirDisparando()) {
            super.disparos();
            monitor.addInvariante(transiciones[transiciones.length - 1]);
        }
    }
}