package com.tpfinal;

import java.util.concurrent.Semaphore;

public class Monitor {
    private Semaphore mutexMonitor;
    private Semaphore[] colas;
    private RedDePetri rdp;
    private Politica politica;
    private Tiempo tiempo;
    private int nTransicionesRed;
    private int[] tInvariantes = {0, 0, 0};

    public Monitor(RedDePetri rdp, Politica politica, Tiempo tiempo) {
        this.rdp = rdp;
        this.tiempo = tiempo;
        this.politica = politica;
        mutexMonitor = new Semaphore(1, true);
        
        nTransicionesRed = rdp.getCantidadTransiciones();
        colas = new Semaphore[nTransicionesRed];
        for (int i = 0; i < colas.length; i++) {
            colas[i] = new Semaphore(0, true);
        }
    }

    /**
     * obtengo acceso al monitor o quedo en la cola de entrada. 
     * luego intento disparar, si pude se cambio el estado de la red
     * si hay alguien en cola de condicion despierto segun la politica y suelto el monitor
     * sino pude disparar me voy a dormir (cola condicion o espero un tiempo) y cuando despierto vuelvo a intentar disparar
     * @param t
     */
    public void disparar(int[] t) {
        try {
            mutexMonitor.acquire();

            System.out.println(Thread.currentThread().getName() + " va a intentar disparar");

            boolean dispare = rdp.disparar(t);

            boolean desperte = signal();

            if (!desperte) // si desperte, el hilo despertado se encarga de liberar el mutex
                mutexMonitor.release();

            if (!dispare) {
                if (rdp.estaSensibilizada(t)) {//debo esperar hasta cumplir el tiempo alfa
                    dormir(t);
                    disparar(t);//cuando me despierto vuelvo a entrar al metodo del monitor
                }
                else {
                    colaCondicion(t);
                    rdp.disparar(t);//cuando me despiertan significa que puedo disparar instantaneamente
                    mutexMonitor.release();//libero el mutex
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }

    /**
     * segun la politica despierto a algun hilo (o no)
     * y suelto el mutex del monitor
     */
    private boolean signal() {
        //int aDespertar = politica.decision(obtenerVectorM());
        int aDespertar = politica.decisionCompleja(obtenerVectorM(), tInvariantes, rdp.getmapaTransicionesInvariantes());

        System.out.println(Thread.currentThread().getName() + " vino aDespertar");
                
        if (aDespertar != -1) {
            System.out.println(Thread.currentThread().getName() + " desperto condicion " + aDespertar);
            colas[aDespertar].release();
            return true;
        }
        else
            System.out.println(Thread.currentThread().getName() + " no desperto a nadie");

        return false;
    }

    /*
     * segun la transicion que intente disparar voy a "dormir"
     * a su cola de condicion
     */
    private void colaCondicion(int[] t) {
        for (int i = 0; i < t.length; i++) {
            if (t[i] == 1) {
                try {
                    System.out.println(Thread.currentThread().getName() + " vino a esperar por condicion " + i);
                    colas[i].acquire();
                } catch (InterruptedException e) {
                    System.out.println("\nEl fin de la jornada laboral encontró a " + Thread.currentThread().getName() + " esperando condicion.");
                    System.exit(0);
                }
                return;//salgo del for
            }
        }
    }

    /**
     * cuando un hilo quiere disparar una transicion sensibilizada
     * pero debe esperar un tiempo para llegar a su alfa
     * calcula el tiempo y se pone a dormir
     * @param t
     */
    private void dormir(int[] t) {
        for (int i = 0; i < t.length; i++) {
            if (t[i] == 1) {
                try {
                    if(tiempo.getTiempoDeSensibilizado()[i] != 0) {//ya fue sensibilizada, estoy esperando entrar en ventana
                        double aDormir = tiempo.calcularTiempoRestante(i);                        
                        if(aDormir > 0){
                            System.out.println(Thread.currentThread().getName() + " vino a esperar para entrar en su ventana temporal");
                            Thread.sleep((long)aDormir);
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("\nEl fin de la jornada laboral encontró a " + Thread.currentThread().getName() + " durmiendo.");
                    System.exit(0);
                }
                return;//salgo del for
            }
        }
    }

    /**
     * segun las transiciones que pueden dispararse
     * y los hilos en colas de condicion
     * obtengo un vector
     * @return vector and logico entre tSens y colas
     */
    private int[] obtenerVectorM() {
        int[] sensibilizadas = rdp.getTransicionesSensibilizadas();
        int[] quienesEstan = new int[nTransicionesRed];
        
        for (int i = 0; i < colas.length; i++) {
            quienesEstan[i] = 0;
            if (colas[i].hasQueuedThreads()) 
                if (tiempo.getTransicionesTemporizadas()[i] == 1 && tiempo.enVentana(i) || tiempo.getTransicionesTemporizadas()[i] == 0)//si es instantanea, o la transicion cumplio su alfa
                    quienesEstan[i] = 1;
        }
            
        int[] m = new int[nTransicionesRed];
        
        for (int i = 0; i < m.length; i++){
            m[i] = sensibilizadas[i] & quienesEstan[i];
        }

        return m;
    }
    
    /**
     * segun el vector que representa la transicion a disparar
     * obtengo el num de transicion que representa en el modelo
     * @param t
     * @return num de transicion
     */
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

    /**
     * detecto si se disparo una transicion que
     * completa un invariante y lo sumo al total
     * @param t
     */
    public synchronized void addInvariante(int[] t) {
        int tr = traducirTransicion(t);
        if (tr == 6) 
            tInvariantes[0]++;
        else if (tr == 10) 
            tInvariantes[1]++;
        else if (tr == 11) 
            tInvariantes[2]++;
    }

    /**
     * indica a los hilos si seguir disparando transiciones
     * segun la cantidad de invariantes completados deseados
     * @return
     */
    public boolean seguirDisparando(){
        return (tInvariantes[0] + tInvariantes[1] + tInvariantes[2]) <= 993;
    }

    /**
     * imprime la cant de c/ invariante de transicion completado
     */
    public void getCuantosDeCada(){
        double total = tInvariantes[0] + tInvariantes[1] + tInvariantes[2];
        double porcentaje0 = tInvariantes[0] / total;
        double porcentaje1 = tInvariantes[1] / total;
        double porcentaje2 = tInvariantes[2] / total;

        System.out.println("Invariante que termina en T6: " + tInvariantes[0] + " (" + String.format("%.2f", porcentaje0 * 100) + "%)");
        System.out.println("Invariante que termina en T10: " + tInvariantes[1] + " (" + String.format("%.2f", porcentaje1 * 100) + "%)");
        System.out.println("Invariante que termina en T11: " + tInvariantes[2] + " (" + String.format("%.2f", porcentaje2 * 100) + "%)");
    }
}