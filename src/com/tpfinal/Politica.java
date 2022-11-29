package com.tpfinal;

public class Politica {
    public Politica(){
    }

    public int decision(int[] candidatos){
        int n = -1;
        System.out.printf("%s vino a decidir\n", Thread.currentThread().getName());

        for (int i = 0; i < candidatos.length; i++) {
            if (candidatos[i] > 0) {
               n = i;
               break;
            }
        }

        if(candidatos[7] > 0)
            n = 7;
        if(candidatos[6] > 0)
            n = 6;
        if(candidatos[5] > 0)
            n = 5;
        if(candidatos[4] > 0)
            n = 4;

        System.out.printf("%s decidio %d \n", Thread.currentThread().getName(), n);
        return n;
    }
}