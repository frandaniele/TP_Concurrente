package com.tpfinal;

import java.util.concurrent.Semaphore;

public class Monitor {
    private Semaphore mutexMonitor;
    private Semaphore[] colas;
    private RedDePetri rdp;
    private Politica politica;
    private Log log;
    private int invariantesCompletados;
    private int nTransicionesRed;
    private int[] tInvariantes;

    public Monitor(RedDePetri rdp, Politica politica, Log log) {
        this.rdp = rdp;
        this.politica = politica;
        this.log = log;
        mutexMonitor = new Semaphore(1, true);
        
        nTransicionesRed = rdp.getCantidadTransiciones();
        colas = new Semaphore[nTransicionesRed];
        for(int i = 0; i < colas.length; i++) {
            colas[i] = new Semaphore(0, true);
        }

        tInvariantes = new int[3];
        for(int i = 0; i < tInvariantes.length; i++) {
            tInvariantes[i] = 0;
        }
        invariantesCompletados = 0;
    }

    public void disparar(int[] t) {
        try {
            mutexMonitor.acquire();

            int transicion = traducirTransicion(t);
            if(transicion == -1) 
                throw new IllegalStateException("Valor inesperado");
            
            if(rdp.disparar(t)){
                log.escribirLog("T" + transicion + "-");
                
                invariantesCompletados += sumarInvariante(transicion);
                
                if (!rdp.checkPInvariantes()) 
                    throw new IllegalStateException("Se violó algún invariante de plaza");
                
                int aDespertar = politica.decision(obtenerVectorM());
                
                if(aDespertar != -1){
                    verColas();
                    if(colas[aDespertar].hasQueuedThreads()){
                        colas[aDespertar].release();
                        System.out.println(Thread.currentThread().getName() + "despues de despertar [" + aDespertar +"]");
                        verColas();
                    }
                }
                
                mutexMonitor.release();
            }
            else{
                mutexMonitor.release();
                dormir(t);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }

    private int traducirTransicion(int t[]) {
        for (int i = 0; i < t.length; i++) {
            if (t[i] == 1) {
                if (i == 1 || i == 2) 
                    return i + 9;
                else if (i > 2 && i < 11) 
                    return i - 1;
                else if (i == 0) 
                    return i + 1;
            }
        }

        return -1;
    }

    private int[] obtenerVectorM() {
        int[] sensibilizadas = rdp.getTransicionesSensibilizadas();
        int[] quienesEstan = new int[nTransicionesRed];
        
        for(int i = 0; i < colas.length; i++) {
            if (colas[i].hasQueuedThreads()) 
                quienesEstan[i] = 1;
            else 
                quienesEstan[i] = 0;
        }
            
        int[] m = new int[nTransicionesRed];
        
        for(int i = 0; i < m.length; i++){
            m[i] = sensibilizadas[i] & quienesEstan[i];
        }

        return m;
    }
    
    private int sumarInvariante(int transicion) {
        if (transicion == 6 || transicion == 10 || transicion == 11) {
            if(transicion == 6) 
                tInvariantes[0]++;
            else if(transicion == 10) 
                tInvariantes[1]++;
            else if(transicion == 11) 
                tInvariantes[2]++;

            return 1;//sumo 1 invariante
        }

        return 0;//no sumo invariante
    }

    private int getInvariantesCompletados(){
        return invariantesCompletados;
    }

    private void verColas(){
        System.out.printf("Colas: ");
        for(int i=0; i<colas.length; i++){
            System.out.printf("%d-",colas[i].getQueueLength());
        }
        System.out.println();
    }

    private void dormir(int[] t){
        for (int i = 0; i < t.length; i++) {
            if (t[i] == 1) {
                try {
                    colas[i].acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public boolean getState(){
        return getInvariantesCompletados()<=995;
    }

    public void getCuantosDeCada(){
        System.out.println("Invariante que termina en T6: " + tInvariantes[0]);
        System.out.println("Invariante que termina en T10: " + tInvariantes[1]);
        System.out.println("Invariante que termina en T11: " + tInvariantes[2]);
    }
}