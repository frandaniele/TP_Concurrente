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

    public Monitor(RedDePetri rdp, Politica politica, Log log){
        this.rdp = rdp;
        this.politica = politica;
        this.log = log;
        nTransicionesRed = rdp.getCantidadTransiciones();
        mutexMonitor = new Semaphore(1, true);
        colas = new Semaphore[nTransicionesRed];
        for(int i=0; i<colas.length; i++){
            colas[i] = new Semaphore(0, true);
        }
        tInvariantes = new int[3];
        for(int i=0; i<tInvariantes.length; i++){
            tInvariantes[i] = 0;
        }
        invariantesCompletados = 0;
    }

    public void disparar(int[] t){
        try {
            mutexMonitor.acquire();
            //System.out.printf("%s entro al monitor\n", Thread.currentThread().getName());
            while(true){
                int transicion = -1;
                for (int i = 0; i < t.length; i++) {
                    if (t[i] == 1) {
                        if (i == 1 || i == 2) {
                            transicion = i + 9;
                        } else if (i > 2 && i < 11) {
                            transicion = i - 1;
                        } else if (i == 0) {
                            transicion = i + 1;
                        } else {
                            throw new IllegalStateException("Valor inesperado: " + i);
                        }
                        break;
                    }
                }
                //System.out.printf("%s va a intentar disparar la transicion %d\n", Thread.currentThread().getName(), transicion);
                if(rdp.disparar(t)){
                    //System.out.printf("%s pudo disparar\n", Thread.currentThread().getName());
                    if (transicion == 6 || transicion == 10 || transicion == 11) {
                        //System.out.println("Se finalizo el invariante que termina con T" + transicion);
                        invariantesCompletados++;
                        //System.out.println("Invariantes completados: " + invariantesCompletados);
                        if(transicion == 6){
                            tInvariantes[0]++;
                        }
                        else if(transicion == 10){
                            tInvariantes[1]++;
                        }
                        else if(transicion == 11){
                            tInvariantes[2]++;
                        }
                    }

                    if (!rdp.checkPInvariantes()) {
                        throw new IllegalStateException("Se violó algún invariante de plaza");
                    }

                    int[] sensibilizadas = rdp.getTransicionesSensibilizadas();
                    int[] quienesEstan = new int[nTransicionesRed];

                    for(int i=0; i<colas.length; i++){
                        if (colas[i].hasQueuedThreads()){
                            quienesEstan[i] = 1;
                        }
                        else{
                            quienesEstan[i] = 0;
                        }
                    }

                    int[] m = new int[nTransicionesRed];

                    for(int i=0; i<m.length; i++){
                        m[i] = sensibilizadas[i] & quienesEstan[i];
                    }

                    int aDespertar = politica.decision(m);

                    if(aDespertar != -1){
                        System.out.printf("vector sensibilizadas: ");
                        for(int i=0; i<sensibilizadas.length; i++){
                            System.out.printf("%d-",sensibilizadas[i]);
                        }
                        System.out.println();
                        System.out.printf("vector colas: ");
                        for(int i=0; i<quienesEstan.length; i++){
                            System.out.printf("%d-",quienesEstan[i]);
                        }
                        System.out.println();
                        System.out.printf("vector candidatos: ");
                        for(int i=0; i<m.length; i++){
                            System.out.printf("%d-",m[i]);
                        }
                        System.out.println();
                        System.out.println(Thread.currentThread().getName() + "antes de despertar a uno por politica ");
                        verColas();
                        if(colas[aDespertar].hasQueuedThreads()){
                            //System.out.printf("%s desperto a alguien en colas[%d]\n", Thread.currentThread().getName(), aDespertar);
                            colas[aDespertar].release();
                            System.out.println(Thread.currentThread().getName() + "despues de despertar [" + aDespertar +"]");
                            verColas();
                        }
                    }
                    break;
                }
                else{
                    //System.out.printf("%s no pudo disparar y suelta el monitor\n", Thread.currentThread().getName());
                    System.out.println("-------------------------------------------------------------");
                    dormir(t);
                    mutexMonitor.acquire();
                    //System.out.printf("%s entro al monitor\n", Thread.currentThread().getName());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            /*System.out.println(Thread.currentThread().getName() + "antes de despertar varios ");
            verColas();
            for(int i=0; i<colas.length; i++) {
                if (colas[i].hasQueuedThreads()) {
                    //System.out.printf("%s desperto a alguien en cola[%d]\n", Thread.currentThread().getName(), i);
                    colas[i].release();
                }
            }
            System.out.println(Thread.currentThread().getName() + "despues de despertar varios");
            verColas();*/
            //System.out.printf("%s va a salir del monitor\n", Thread.currentThread().getName());
            System.out.println("-------------------------------------------------------------");
            mutexMonitor.release();
        }
    }

    public boolean getState(){
        if(getInvariantesCompletados()<=995){
            return true;
        }
        else{
            return false;
        }
    }

    public void getCuantosDeCada(){
        System.out.println("Invariante que termina en T6: " + tInvariantes[0]);
        System.out.println("Invariante que termina en T10: " + tInvariantes[1]);
        System.out.println("Invariante que termina en T11: " + tInvariantes[2]);
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
        mutexMonitor.release();
        for (int i = 0; i < t.length; i++) {
            if (t[i] == 1) {
                try {
                    //System.out.printf("%s entrando a cola de condicion %d\n", Thread.currentThread().getName(), i);
                    colas[i].acquire();
                    //System.out.printf("%s saliendo de cola de condicion %d\n", Thread.currentThread().getName(), i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
