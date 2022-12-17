package com.tpfinal;

public class Politica {
    public Politica(){
    }

    public int decision(int[] candidatos){
        int n = -1;

        if(candidatos[4] == 1)
            n = 4;
        else if(candidatos[5] == 1)
            n = 5;
        else if(candidatos[6] == 1)
            n = 6;
        else if(candidatos[7] == 1)
            n = 7;
        else     
            for (int i = 0; i < candidatos.length; i++) {
                if (candidatos[i] > 0) {
                   n = i;
                   break;
                }
            }

        return n;
    }
}