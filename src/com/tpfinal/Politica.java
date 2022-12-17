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
}