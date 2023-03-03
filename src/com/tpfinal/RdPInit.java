package com.tpfinal;

public class RdPInit {
    /*------- Vectores para invariantes de plaza -------*/
    private final int[] pInvariantesVal = {3, 1, 3, 2, 1, 1, 1, 1, 2};
    private final int[][] pInvariantesPlazas = {
        {1, 2, 5, 6, 9, 13}, 
        {9, 10, 11}, 
        {8, 11, 12, 15}, 
        {13, 14, 15}, 
        {15, 16}, 
        {6, 17}, 
        {2, 3}, 
        {4, 5}, 
        {6, 7, 8},
    };
    
    private final int[][] matrizIncidencia = {
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
    
    private final int[] m0 = {3, 1, 0, 3, 0, 2, 0, 1, 1, 0, 1, 1, 0, 0, 2, 0, 0};
                        //P1 P10 P11 P12 P13 P14 P15 P16 P17 P2 P3 P4 P5 P6 P7 P8 P9
    
    private final int[] tSensibilizadas0 = {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
                                        // T1 T10 T11 T2 T3 T4 T5 T6 T7 T8 T9
    
    /*------- Vectores de las transiciones -------*/
    private final int[][] transiciones = {
        {1,0,0,0,0,0,0,0,0,0,0}, //T1
        {0,0,0,1,0,0,0,0,0,0,0}, //T2
        {0,0,0,0,1,0,0,0,0,0,0}, //T3
        {0,0,0,0,0,1,0,0,0,0,0}, //T4
        {0,0,0,0,0,0,1,0,0,0,0}, //T5
        {0,0,0,0,0,0,0,1,0,0,0}, //T6
        {0,0,0,0,0,0,0,0,1,0,0}, //T7
        {0,0,0,0,0,0,0,0,0,1,0}, //T8
        {0,0,0,0,0,0,0,0,0,0,1}, //T9
        {0,1,0,0,0,0,0,0,0,0,0}, //T10
        {0,0,1,0,0,0,0,0,0,0,0}  //T11
    };
    
    /*  -1: comparte invariante (t1)
    0: invariante que termina con t6
    1: invariante que termina con t10
    2: invariante que termina con t11   */
    private final int[] transicionAInvariante = {-1,1,2,2,0,0,0,0,1,1,1};
    
    /*-------- Vectores para transiciones temporizadas --------*/
    private final int[] temporizadas = {0,1,1,1,1,1,1,1,0,1,1};
    private final double[] alfas = {0,1,3,4,1,1,1,1,0,1,2};
    private final double[] betas = {0,10000,10000,10000,10000,10000,10000,10000,0,10000,10000};

    public int[] getpInvariantesVal() {
        return pInvariantesVal;
    }

    public int[][] getpInvariantesPlazas() {
        return pInvariantesPlazas;
    }

    public int[][] getMatrizIncidencia() {
        return matrizIncidencia;
    }

    public int[] getM0() {
        return m0;
    }

    public int[] gettSensibilizadas0() {
        return tSensibilizadas0;
    }

    public int[][] getTransiciones() {
        return transiciones;
    }

    public int[] getTransicionAInvariante() {
        return transicionAInvariante;
    }

    public int[] getTemporizadas() {
        return temporizadas;
    }

    public double[] getAlfas() {
        return alfas;
    }

    public double[] getBetas() {
        return betas;
    }    
}
