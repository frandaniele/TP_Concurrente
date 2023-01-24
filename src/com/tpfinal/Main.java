package com.tpfinal;

public class Main {

    public static void main(String[] args) {
        final int[] pInvariantes = {3, 1, 3, 2, 1, 1, 1, 1, 2};

        final int[][] matrizIncidencia = {
              // T1 T10 T11  T2  T3  T4  T5  T6  T7  T8  T9
                {-1,  0,  1,  0,  0,  0,  0,  1,  0,  0,  0}, //P1
                { 0,  0,  0,  0,  0, -1,  1,  0,  0, -1,  1}, //P10
                { 0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -1}, //P11
                { 0,  1,  0,  0,  0,  0,  0,  0, -1,  0,  0}, //P12
                { 0,  0,  0,  0,  0,  0,  1, -1,  0,  0,  0}, //P13
                { 0,  0,  0,  0,  0,  0, -1,  1, -1,  1,  0}, //P14
                { 0,  0,  0,  0,  0,  0,  0,  0,  1, -1,  0}, //P15
                { 0,  0,  0,  0,  0,  0,  0,  0, -1,  1,  0}, //P16
                { 0,  0,  0,  0, -1,  1,  0,  0,  0,  0,  0}, //P17
                { 1,  0,  0, -1, -1,  0,  0,  0,  0,  0,  0}, //P2
                {-1,  0,  0,  1,  1,  0,  0,  0,  0,  0,  0}, //P3
                { 0,  0,  1, -1,  0,  0,  0,  0,  0,  0,  0}, //P4
                { 0,  0, -1,  1,  0,  0,  0,  0,  0,  0,  0}, //P5
                { 0,  0,  0,  0,  1, -1,  0,  0,  0,  0,  0}, //P6
                { 0,  1,  0,  0, -1,  1,  0,  0,  0,  0, -1}, //P7
                { 0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  1}, //P8
                { 0,  0,  0,  0,  0,  1, -1,  0,  0,  0,  0}  //P9
        };

        final int[] m0 = {3, 1, 0, 3, 0, 2, 0, 1, 1, 0, 1, 1, 0, 0, 2, 0, 0};
                        //P1 P10 P11 P12 P13 P14 P15 P16 P17 P2 P3 P4 P5 P6 P7 P8 P9

        final int[] tSensibilizadas0 = {1,0,0,0,0,0,0,0,1,0,0};

        final int[] t1 =  {1,0,0,0,0,0,0,0,0,0,0};
        final int[] t2 =  {0,0,0,1,0,0,0,0,0,0,0};
        final int[] t3 =  {0,0,0,0,1,0,0,0,0,0,0};
        final int[] t4 =  {0,0,0,0,0,1,0,0,0,0,0};
        final int[] t5 =  {0,0,0,0,0,0,1,0,0,0,0};
        final int[] t6 =  {0,0,0,0,0,0,0,1,0,0,0};
        final int[] t7 =  {0,0,0,0,0,0,0,0,1,0,0};
        final int[] t8 =  {0,0,0,0,0,0,0,0,0,1,0};
        final int[] t9 =  {0,0,0,0,0,0,0,0,0,0,1};
        final int[] t10 = {0,1,0,0,0,0,0,0,0,0,0};
        final int[] t11 = {0,0,1,0,0,0,0,0,0,0,0};

        //int[] temporizadas = {0,0,0,0,0,0,0,0,0,0,0};
        int[] temporizadas = {0,1,1,1,1,1,1,1,0,1,1};
        double[] alfas = {0,5,5,2,1.5,1,1,1,0,1,2};
        double[] betas = {0,10000,10000,10000,10000,10000,10000,10000,0,10000,10000};
        Tiempo tiempo = new Tiempo(temporizadas, alfas, betas);

        Log log = Log.getInstance();
        RedDePetri rdp = new RedDePetri(m0, pInvariantes, tSensibilizadas0, matrizIncidencia, log, tiempo);
        Politica politica = new Politica();
        Monitor monitor = new Monitor(rdp, politica, tiempo);

        Operario[] grupo1 = new Operario[3];
        Operario[] grupo2 = new Operario[3];

        int[][] tGrupo1 = {t3, t4, t5, t6};
        int[][] tGrupo2 = {t7, t8, t9, t10};

        for(int i = 0; i < grupo1.length; i++) {
            grupo1[i] = new Operario(monitor, tGrupo1);
            grupo2[i] = new Operario(monitor, tGrupo2);
        }

        int[][] tCapataz = {t1};
        int[][] tPasante = {t2, t11};

        Capataz capataz = new Capataz(monitor, tCapataz);
        Pasante pasante = new Pasante(monitor, tPasante);
        
        Thread[] threads = new Thread[8];
        threads[0] = new Thread(capataz, "CAPATAZ");
        threads[1] = new Thread(pasante, "PASANTE");

        for(int i = 2; i < 5; i++) {
            threads[i] = new Thread(grupo1[i-2], "TRABAJADOR " + (i-1));
            threads[i+3] = new Thread(grupo2[i-2], "TRABAJADOR " + (i+2));
        }

        for (Thread thread : threads) {
            if(thread != null)
                thread.start();
        }

        System.out.println("SE ARRANCO");

        try {
            threads[0].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n-----------------INVARIANTES-----------------------");
        
        monitor.getCuantosDeCada();

        log.writeFile();

        for (Thread thread : threads) {
            thread.interrupt();
        }
        
        System.out.println("-------------------MAIN END------------------------");
    }
}