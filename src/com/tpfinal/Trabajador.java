package com.tpfinal;

import java.util.concurrent.TimeUnit;

public abstract class Trabajador {
    protected int[][] transiciones;
    protected Monitor monitor;
    
    public int[][] getTransiciones() {
        return transiciones;
    }
    
    public void setTransiciones(int[][] transiciones) {
        this.transiciones = transiciones;
    }
    
    public Monitor getMonitor() {
        return monitor;
    }
    
    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }
    
    public void disparos() {
        for(int[] t : transiciones) {
            if(Thread.interrupted())
                break;
            
            try {
                monitor.disparar(t);
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}