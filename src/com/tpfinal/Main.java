package com.tpfinal;

public class Main {

    public static void main(String[] args) {
        Trabajador[] grupo1 = new Trabajador[3];
        Trabajador[] grupo2 = new Trabajador[3];
        Thread[] threads = new Thread[8];

        int[] t1 = {1,0,0,0,0,0,0,0,0,0,0};
        int[] t2 = {0,0,0,1,0,0,0,0,0,0,0};
        int[] t3 = {0,0,0,0,1,0,0,0,0,0,0};
        int[] t4 = {0,0,0,0,0,1,0,0,0,0,0};
        int[] t5 = {0,0,0,0,0,0,1,0,0,0,0};
        int[] t6 = {0,0,0,0,0,0,0,1,0,0,0};
        int[] t7 = {0,0,0,0,0,0,0,0,1,0,0};
        int[] t8 = {0,0,0,0,0,0,0,0,0,1,0};
        int[] t9 = {0,0,0,0,0,0,0,0,0,0,1};
        int[] t10 = {0,1,0,0,0,0,0,0,0,0,0};
        int[] t11 = {0,0,1,0,0,0,0,0,0,0,0};

        Log log = Log.getInstance();
        RedDePetri rdp = new RedDePetri();
        Politica politica = new Politica(rdp);
        Monitor monitor = new Monitor(rdp, politica, log);

        Capataz capataz = new Capataz(monitor, t1);
        Pasante pasante = new Pasante(monitor, t2, t11);

        threads[0] = new Thread(capataz, "CAPATAZ");
        threads[1] = new Thread(pasante, "PASANTE");

        for(int i=0; i<grupo1.length; i++){
            grupo1[i] = new Trabajador(monitor, t3, t4, t5, t6);
            grupo2[i] = new Trabajador(monitor, t7, t8, t9, t10);
        }

        for(int i=2; i<5; i++){
            threads[i] = new Thread(grupo1[i-2], "TRABAJADOR " + (i-1));
            threads[i+3] = new Thread(grupo2[i-2], "TRABAJADOR " + (i+2));
        }

        for (Thread thread : threads) {
            if(thread != null){
                thread.start();
            }
        }

        System.out.println("SE ARRANCO");

        /*for (Thread thread : threads) {
            try {
                if(thread != null){
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

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

        /*try {
            threads[0].join(); //son los dos hilos que sacan de las plazas IDLE (1era t de los invariantes)
            threads[2].join(); //"CAPATAZ 1" y "CAPATAZ 3". Esperamos que finalicen 1000 invariantes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        log.writeFile();

        System.out.println("-----------------MAIN END-----------------------");
    }
}
