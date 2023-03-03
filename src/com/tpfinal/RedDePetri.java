package com.tpfinal;

import java.util.Arrays;

public class RedDePetri {
    private int[] marcado;

    private final int[] pInvariantesVal;
    private final int[][] pInvariantesPlazas;

    private final int[][] matrizIncidencia;
    
    private int[] tSensibilizadas;

    private final int[] mapaTransicionesInvariantes;

    private Log log;

    private Tiempo tiempo;

    public RedDePetri(RdPInit rdpValues, Log log, Tiempo tiempo) {
        this.marcado = rdpValues.getM0();
        this.pInvariantesVal = rdpValues.getpInvariantesVal();
        this.pInvariantesPlazas = rdpValues.getpInvariantesPlazas();
        this.tSensibilizadas = rdpValues.gettSensibilizadas0();
        this.matrizIncidencia = rdpValues.getMatrizIncidencia();
        this.mapaTransicionesInvariantes = rdpValues.getTransicionAInvariante();
        this.log = log;
        this.tiempo = tiempo;
    }
    
    /**
     * se fija si es posible disparar la transicion 
     * en ese caso cambia de estado a la red y
     * chequea los invariantes de plaza y
     * escribe en el log la transicion que se disparo
     * @param t
     * @return true si disparo la transicion, false si no
     */
    public boolean disparar(int[] t){
        if(evaluarDisparo(t)){
            calcularNuevoEstado(t);                
            if (!checkPInvariantes()) 
                throw new IllegalStateException("Se violó algún invariante de plaza");

            int transicion = traducirTransicion(t);
            if(transicion == -1) 
                throw new IllegalStateException("Valor inesperado");

         //   System.out.println(Thread.currentThread().getName() + " disparo T" + transicion);
            log.escribirLog("T" + transicion + "-");
                
            return true;
        }
            
        return false;
    }

    /**
     * chequea si la transicion a disparar 
     * se encuentra en el vector de las sensibilizadas
     * @param t
     * @return true si puedo disparar la transicion, false si no
     */
    private boolean evaluarDisparo(int[] t){
        calcularSensibilizadas();

        int selector = -1;
        for(int i = 0; i < t.length; i++){
            if(t[i] == 1){
                selector = i;
                break;
            }
        }

        if(selector != -1) {
            if(tiempo.getTransicionesTemporizadas()[selector] == 1) //es temporizada
                return tSensibilizadas[selector] == 1 && tiempo.enVentana(selector);
            else
                return tSensibilizadas[selector] == 1;//es instantanea
        }
        else 
            return false;
    }

    /**
     * segun la ecuacion de estado de la red
     * calcula el nuevo estado (marcado) de la misma
     * despues de disparar la transicion t
     * @param t
     */
    private void calcularNuevoEstado(int[] t){
        int[] multiplicacion = new int[marcado.length];
        int[] suma = new int[marcado.length];

        for(int i = 0; i < marcado.length; i++)
            for(int j = 0; j < getCantidadTransiciones(); j++)
                multiplicacion[i] += matrizIncidencia[i][j] * t[j];

        for (int i = 0; i < marcado.length; i++) 
            suma[i] = marcado[i] + multiplicacion[i];

        marcado = suma;
    }

    /**
     * @return vector de transiciones sensibilizadas
     */
    public int[] getTransicionesSensibilizadas() {
        calcularSensibilizadas();

        return tSensibilizadas;
    }

    /**
     * chequea si una transicion esta sensibilizada
     * @param transicion
     * @return true si la transicion esta sensibilizada , false si no
     */
    public boolean estaSensibilizada(int[] transicion) {
        for (int i = 0; i < transicion.length; i++) {
            if (transicion[i] == 1)
                return tSensibilizadas[i] == 1;
        }

        return false;
    }

    /**
     * segun la matriz de incidencia 
     * y el marcado actual de la red
     * calcula y actualiza el vector de las 
     * transiciones sensibilizadas
     */
    private void calcularSensibilizadas(){
        int[] aux = tSensibilizadas.clone();
        for(int i = 0; i < getCantidadTransiciones(); i++){
            tSensibilizadas[i] = 1;
            
            for (int j = 0; j < marcado.length; j++){
                if(matrizIncidencia[j][i] == -1){
                    if(marcado[j] == 0){
                        tSensibilizadas[i] = 0;
                        break;
                    }
                }
            }
        }

        double now = System.currentTimeMillis();
        for(int i = 0; i < aux.length; i++) {
            if(aux[i] == 0 && tSensibilizadas[i] == 1){
                tiempo.sensibilizarTiempo(i, now);
            }
            else if(aux[i] == 1 && tSensibilizadas[i] == 0){
                tiempo.resetTiempo(i);
            }
            else if(aux[i] == 1 && tSensibilizadas[i] == 1){
                tiempo.actualizarQ(i, now);
            }
        }
    }

    private int traducirTransicion(int t[]) {
        for (int i = 0; i < t.length; i++) {
            if (t[i] == 1) {
                if (i == 1 || i == 2) 
                    return i + 9;
                else if (i > 2 && i < 11) 
                    return i - 1;
                else if (i == 0) 
                    return i + 1;
            }
        }

        return -1;
    }

    /**
     * @param P
     * @return marcado de la plaza P
     */
    private int getMarcadoPlaza(int P){
        int selector;

        if(P>1 && P<10)
            selector = P + 7;
        else if(P==1)
            selector = 0;
        else if(P>9 && P<18)
            selector = P - 9;
        else{
            System.out.println("No hay plaza " + P);
            return -1;
        }

        return marcado[selector];
    }

    /**
     * calcula el valor de los invariantes de plaza
     * de la red
     * @return true si no se violo ningun invariante, false si si
     */
    public boolean checkPInvariantes(){
        for (int i = 0; i < pInvariantesPlazas.length; i++) {
            if(getSumaInvariante(pInvariantesPlazas[i]) != pInvariantesVal[i]) {
                System.out.println("Inv " + Arrays.toString(pInvariantesPlazas[i]) + ": Debia dar " + pInvariantesVal[i] + " y dio " + getSumaInvariante(pInvariantesPlazas[i]));                
                return false;
            }
        }
        
        return true;
    }

    private int getSumaInvariante(int[] plazas) {
        int suma = 0;
        for(int plaza : plazas)
            suma += getMarcadoPlaza(plaza);

        return suma;
    }

    public int getCantidadTransiciones(){
        return matrizIncidencia[0].length;
    }

    public int[] getmapaTransicionesInvariantes() {
        return mapaTransicionesInvariantes;
    }
}