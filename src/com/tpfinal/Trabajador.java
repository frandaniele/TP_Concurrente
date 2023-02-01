package com.tpfinal;

import java.util.concurrent.TimeUnit;

public class Trabajador {
    protected int[][] transiciones;
    protected Monitor monitor;
    
    /**
     * dispara una a una las transiciones que le corresponden
     * descansa 1 ms entre ellas y chequea si debe
     * interrumpirse por haberse completado 1000 invariantes
     */
    public void disparos() {
        for(int[] t : transiciones) {
            if(Thread.interrupted())
                return;
            
            try {
                monitor.disparar(t);
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}