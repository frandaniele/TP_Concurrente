package com.tpfinal;

public class RedDePetri {
    private int[] marcado;

    private final int[] pInvariantes = {3, 1, 3, 2, 1, 1, 1, 1, 2};

    private final int[][] matrizIncidencia = {
            {-1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {0,	0, 0, 0, 0,	-1, 1, 0, 0, -1, 1},
            {0,	0, 0, 0, 0,	0, 0, 0, 0,	1, -1},
            {0,	1, 0, 0, 0,	0, 0, 0, -1, 0, 0},
            {0,	0, 0, 0, 0,	0, 1, -1, 0, 0, 0},
            {0,	0, 0, 0, 0,	0, -1, 1, -1, 1, 0},
            {0,	0, 0, 0, 0,	0, 0, 0, 1,	-1, 0},
            {0,	0, 0, 0, 0,	0, 0, 0, -1, 1, 0},
            {0,	0, 0, 0, -1, 1, 0, 0, 0, 0, 0},
            {1,	0, 0, -1, -1, 0, 0, 0, 0, 0, 0},
            {-1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
            {0,	0, 1, -1, 0, 0, 0, 0, 0, 0, 0},
            {0,	0, -1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0,	0, 0, 0, 1,	-1, 0, 0, 0, 0, 0},
            {0,	1, 0, 0, -1, 1, 0, 0, 0, 0, -1},
            {0,	-1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0,	0, 0, 0, 0,	1, -1, 0, 0, 0, 0},
    };

    private final int[] m0 = {3, 1, 0, 3, 0, 2, 0, 1, 1, 0, 1, 1, 0, 0, 2, 0, 0};
                        //P1 P10 P11 P12 P13 P14 P15 P16 P17 P2 P3 P4 P5 P6 P7 P8 P9

    private int[] tSensibilizadas = {1,0,0,0,0,0,0,0,1,0,0};

    public RedDePetri(){
        marcado = m0;
    }

    public boolean disparar(int[] t){
        if(evaluarDisparo(t)){
            calcularNuevoEstado(t);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean evaluarDisparo(int[] t){
        int selector = -1;
        for(int i=0; i<t.length; i++){
            if(t[i] == 1){
                selector = i;
                break;
            }
        }

        if(selector != -1){
            if(tSensibilizadas[selector] == 1){
                return true;
            }
            else{
                return false;
            }
        }
        else {
            return false;
        }
    }

    private void calcularNuevoEstado(int[] t){
        int[] multiplicacion = new int[marcado.length];
        int[] suma = new int[marcado.length];

        for(int i=0; i<marcado.length; i++){
            for(int j=0; j<getCantidadTransiciones(); j++){
                multiplicacion[i] += matrizIncidencia[i][j] * t[j];
            }
        }

        for (int i=0; i<marcado.length; i++) {
            suma[i] = marcado[i] + multiplicacion[i];
        }

        marcado = suma;
    }

    public int[] getTransicionesSensibilizadas() {
        calcularSensibilizadas();
        /*System.out.printf("Transiciones sensibilizadas: ");
        for(int i=0; i<tSensibilizadas.length; i++){
            System.out.printf("%d-",tSensibilizadas[i]);
        }
        System.out.println();*/
        return tSensibilizadas;
    }

    private void calcularSensibilizadas(){
        for(int i=0; i<getCantidadTransiciones(); i++){
            tSensibilizadas[i] = 1;
            for (int j=0; j<marcado.length; j++){
                if(matrizIncidencia[j][i] == -1){
                    if(marcado[j] == 0){
                        tSensibilizadas[i] = 0;
                        break;
                    }
                }
            }
        }
    }

    private int getMarcadoPlaza(int P){
        int selector;

        if(P>1 && P<10){
            selector = P + 7;
        }
        else if(P==1){
            selector = 0;
        }
        else if(P>9 && P<18){
            selector = P - 9;
        }
        else{
            System.out.println("No hay plaza "+P);
            return -1;
        }

        return marcado[selector];
    }

    public boolean checkPInvariantes(){
        if(getMarcadoPlaza(1) + getMarcadoPlaza(2) + getMarcadoPlaza(5) + getMarcadoPlaza(6) + getMarcadoPlaza(9) + getMarcadoPlaza(13) != pInvariantes[0]){
            System.out.println("Inv 1: " + pInvariantes[0] + " Dio: " + (getMarcadoPlaza(1) + getMarcadoPlaza(2) + getMarcadoPlaza(5) + getMarcadoPlaza(6) + getMarcadoPlaza(9)));
            return false;
        }
        else if(getMarcadoPlaza(10) + getMarcadoPlaza(11) + getMarcadoPlaza(9) != pInvariantes[1]){
            System.out.println("Inv 2: " + pInvariantes[1] + " Dio: " + (getMarcadoPlaza(10) + getMarcadoPlaza(11) + getMarcadoPlaza(9)));
            return false;
        }
        else if(getMarcadoPlaza(11) + getMarcadoPlaza(12) + getMarcadoPlaza(15) + getMarcadoPlaza(8) != pInvariantes[2]){
            System.out.println("Inv 3: " + pInvariantes[2] + " Dio: " + (getMarcadoPlaza(11) + getMarcadoPlaza(12) + getMarcadoPlaza(15) + getMarcadoPlaza(8)));
            return false;
        }
        else if(getMarcadoPlaza(13) + getMarcadoPlaza(14) + getMarcadoPlaza(15) != pInvariantes[3]){
            System.out.println("Inv 4: " + pInvariantes[3] + " Dio: " + (getMarcadoPlaza(13) + getMarcadoPlaza(14) + getMarcadoPlaza(15)));
            return false;
        }
        else if(getMarcadoPlaza(15) + getMarcadoPlaza(16) != pInvariantes[4]){
            System.out.println("Inv 5: " + pInvariantes[4] + " Dio: " + (getMarcadoPlaza(15) + getMarcadoPlaza(16)));
            return false;
        }
        else if(getMarcadoPlaza(17) + getMarcadoPlaza(6) != pInvariantes[5]){
            System.out.println("Inv 6: " + pInvariantes[5] + " Dio: " + (getMarcadoPlaza(17) + getMarcadoPlaza(6)));
            return false;
        }
        else if(getMarcadoPlaza(2) + getMarcadoPlaza(3) != pInvariantes[6]){
            System.out.println("Inv 7: " + pInvariantes[6] + " Dio: " + (getMarcadoPlaza(2) + getMarcadoPlaza(3)));
            return false;
        }
        else if(getMarcadoPlaza(4) + getMarcadoPlaza(5) != pInvariantes[7]){
            System.out.println("Inv 8: " + pInvariantes[7] + " Dio: " + (getMarcadoPlaza(4) + getMarcadoPlaza(5)));
            return false;
        }
        else if(getMarcadoPlaza(6) + getMarcadoPlaza(7) + getMarcadoPlaza(8) != pInvariantes[8]){
            System.out.println("Inv 9: " + pInvariantes[8] + " Dio: " + (getMarcadoPlaza(6) + getMarcadoPlaza(7) + getMarcadoPlaza(8)));
            return false;
        }
        else{
            return true;
        }
    }

    public int getCantidadTransiciones(){
        return matrizIncidencia[0].length;
    }
}
