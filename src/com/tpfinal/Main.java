package com.tpfinal;

public class Main {

    public static void main(String[] args) {
        final int[] pInvariantes = {3, 1, 3, 2, 1, 1, 1, 1, 2};

        final int[][] matrizIncidencia = {
                {-1,  0,  1,  0,  0,  0,  0,  1,  0,  0,  0},
                { 0,  0,  0,  0,  0, -1,  1,  0,  0, -1,  1},
                { 0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -1},
                { 0,  1,  0,  0,  0,  0,  0,  0, -1,  0,  0},
                { 0,  0,  0,  0,  0,  0,  1, -1,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0, -1,  1, -1,  1,  0},
                { 0,  0,  0,  0,  0,  0,  0,  0,  1, -1,  0},
                { 0,  0,  0,  0,  0,  0,  0,  0, -1,  1,  0},
                { 0,  0,  0,  0, -1,  1,  0,  0,  0,  0,  0},
                { 1,  0,  0, -1, -1,  0,  0,  0,  0,  0,  0},
                {-1,  0,  0,  1,  1,  0,  0,  0,  0,  0,  0},
                { 0,  0,  1, -1,  0,  0,  0,  0,  0,  0,  0},
                { 0,  0, -1,  1,  0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  1, -1,  0,  0,  0,  0,  0},
                { 0,  1,  0,  0, -1,  1,  0,  0,  0,  0, -1},
                { 0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  1},
                { 0,  0,  0,  0,  0,  1, -1,  0,  0,  0,  0},
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

        Log log = Log.getInstance();
        RedDePetri rdp = new RedDePetri(m0, pInvariantes, tSensibilizadas0, matrizIncidencia);
        Politica politica = new Politica();
        Monitor monitor = new Monitor(rdp, politica, log);

        Trabajador[] grupo1 = new Trabajador[3];
        Trabajador[] grupo2 = new Trabajador[3];

        for(int i = 0; i < grupo1.length; i++){
            grupo1[i] = new Trabajador(monitor, t3, t4, t5, t6);
            grupo2[i] = new Trabajador(monitor, t7, t8, t9, t10);
        }

        Capataz capataz = new Capataz(monitor, t1);
        Pasante pasante = new Pasante(monitor, t2, t11);
        
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
        
        monitor.getCuantosDeCada();

        log.writeFile();

        System.out.println("-----------------MAIN END-----------------------");
    }
}