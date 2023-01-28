package com.tpfinal;

public class Politica {
    public Politica(){
    }
    
    public int decision(int[] candidatos){
        if(candidatos[0] == 1)//prioridad al conflicto
            return 0;
        else {
            for (int i = 4; i < 8; i++) 
                if (candidatos[i] == 1) //priorizo invariante trabajadores izquierdo
                   return i;

            for (int i = 2; i < 4; i++) 
                if (candidatos[i] == 1) //priorizo invariante pasante
                   return i;

            for (int i = 8; i < 11; i++) 
                if (candidatos[i] == 1) //invariante trabajadores derecho
                   return i;

            if (candidatos[1] == 1)
                return 1;
        }    

        return -1;
    }

    public int decisionCompleja(int[] candidatos, int[] cantInvariantes, int[] invQuePertenece) {
        int min = 1000;
        int minIndex = -1;
       
        if (candidatos[0] != 0) //prioridad al conflicto, pertenece a 2 invariantes
            return 0; 
    
        for (int i = 1; i < candidatos.length; i++) {
            if(candidatos[i] == 1) {
                if(cantInvariantes[invQuePertenece[i]] < min) {
                    min = cantInvariantes[invQuePertenece[i]];
                    minIndex = i;
                }
            }
        }
    
        return minIndex;
    }
}