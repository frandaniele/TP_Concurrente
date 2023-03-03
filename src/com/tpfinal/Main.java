package com.tpfinal;

public class Main {

    public static void main(String[] args) {
        RdPInit rdpValues = new RdPInit();

        Tiempo tiempo = new Tiempo(rdpValues);
        Log log = Log.getInstance();
        RedDePetri rdp = new RedDePetri(rdpValues, log, tiempo);
        Politica politica = new Politica();
        Monitor monitor = new Monitor(rdp, politica, tiempo);

        int[][] ts = rdpValues.getTransiciones();

        /*------- Creo los operarios -------*/
        Operario[] grupo1 = new Operario[3];
        Operario[] grupo2 = new Operario[3];

        int[][] tGrupo1 = {ts[2], ts[3], ts[4], ts[5]};//transiciones que van a 
        int[][] tGrupo2 = {ts[6], ts[7], ts[8], ts[9]};//disparar los grupos de operarios

        for(int i = 0; i < grupo1.length; i++) {
            grupo1[i] = new Operario(monitor, tGrupo1);
            grupo2[i] = new Operario(monitor, tGrupo2);
        }

        /*------- Creo capataz y pasante -------*/
        int[][] tCapataz = {ts[0]};//transicion que dispara el capataz
        int[][] tPasante = {ts[1], ts[10]};//transiciones que dispara el pasante

        Capataz capataz = new Capataz(monitor, tCapataz);
        Pasante pasante = new Pasante(monitor, tPasante);
        
        /*----------- Creacion de los hilos -----------*/
        Thread[] threads = new Thread[8];
        threads[0] = new Thread(capataz, "CAPATAZ");
        threads[1] = new Thread(pasante, "PASANTE");

        for(int i = 2; i < 5; i++) {
            threads[i] = new Thread(grupo1[i-2], "TRABAJADOR " + (i-1));
            threads[i+3] = new Thread(grupo2[i-2], "TRABAJADOR " + (i+2));
        }

        for (Thread thread : threads) {//inicio los hilos
            if(thread != null)
                thread.start();
        }

        double start_time = System.currentTimeMillis();
        System.out.println("Comenzo la jornada laboral.");

        double end_time = 0;
        try {
            threads[0].join(8000);//espero a que el capataz termine de ejecutar
            end_time = System.currentTimeMillis() - start_time;
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Thread thread : threads) {
            thread.interrupt();//si quedaron hilos durmiendo los interrumpo
        }

        System.out.println("\nFin de la jornada. Duracion:  " + String.format("%.2f", end_time/1000) + " segundos.");
      
        monitor.getResultados();//imprimo cuantos invariantes se dispararon

        log.writeFile();//escribo el archivo log.txt

        System.out.println("\n-------------------MAIN END------------------------");
    }
}