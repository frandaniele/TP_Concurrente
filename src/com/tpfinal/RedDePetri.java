package com.tpfinal;

public class RedDePetri {
    private int[] marcado;

    private final int[] pInvariantes;

    private final int[][] matrizIncidencia;

    private int[] tSensibilizadas;

    private Log log;

    private Tiempo tiempo;

    public RedDePetri(int[] m0, int[] invariantesPlaza, int[] tSensibilizadas, int[][] matriz, Log log, Tiempo tiempo) {
        this.marcado = m0;
        this.pInvariantes = invariantesPlaza;
        this.tSensibilizadas = tSensibilizadas;
        this.matrizIncidencia = matriz;
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

            System.out.println(Thread.currentThread().getName() + " disparo T" + transicion);
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
        calcularSensibilizadas();
    }

    /**
     * @return vector de transiciones sensibilizadas
     */
    public int[] getTransicionesSensibilizadas() {
        calcularSensibilizadas();

        return tSensibilizadas;
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

        for(int i = 0; i < aux.length; i++) {
            if(aux[i] == 0 && tSensibilizadas[i] == 1){
                tiempo.sensibilizarTiempo(i);
            }
            else if(aux[i] == 1 && tSensibilizadas[i] == 0){
                tiempo.resetTiempo(i);
            }
            else if(aux[i] == 1 && tSensibilizadas[i] == 1){
                tiempo.actualizarQ(i);
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
        if(getMarcadoPlaza(1) + getMarcadoPlaza(2) + getMarcadoPlaza(5) + getMarcadoPlaza(6) + getMarcadoPlaza(9) + getMarcadoPlaza(13) != pInvariantes[0])
            System.out.println("Inv 1: " + pInvariantes[0] + " Dio: " + (getMarcadoPlaza(1) + getMarcadoPlaza(2) + getMarcadoPlaza(5) + getMarcadoPlaza(6) + getMarcadoPlaza(9)));
        else if(getMarcadoPlaza(10) + getMarcadoPlaza(11) + getMarcadoPlaza(9) != pInvariantes[1])
            System.out.println("Inv 2: " + pInvariantes[1] + " Dio: " + (getMarcadoPlaza(10) + getMarcadoPlaza(11) + getMarcadoPlaza(9)));
        else if(getMarcadoPlaza(11) + getMarcadoPlaza(12) + getMarcadoPlaza(15) + getMarcadoPlaza(8) != pInvariantes[2])
            System.out.println("Inv 3: " + pInvariantes[2] + " Dio: " + (getMarcadoPlaza(11) + getMarcadoPlaza(12) + getMarcadoPlaza(15) + getMarcadoPlaza(8)));
        else if(getMarcadoPlaza(13) + getMarcadoPlaza(14) + getMarcadoPlaza(15) != pInvariantes[3])
            System.out.println("Inv 4: " + pInvariantes[3] + " Dio: " + (getMarcadoPlaza(13) + getMarcadoPlaza(14) + getMarcadoPlaza(15)));
        else if(getMarcadoPlaza(15) + getMarcadoPlaza(16) != pInvariantes[4])
            System.out.println("Inv 5: " + pInvariantes[4] + " Dio: " + (getMarcadoPlaza(15) + getMarcadoPlaza(16)));
        else if(getMarcadoPlaza(17) + getMarcadoPlaza(6) != pInvariantes[5])
            System.out.println("Inv 6: " + pInvariantes[5] + " Dio: " + (getMarcadoPlaza(17) + getMarcadoPlaza(6)));
        else if(getMarcadoPlaza(2) + getMarcadoPlaza(3) != pInvariantes[6])
            System.out.println("Inv 7: " + pInvariantes[6] + " Dio: " + (getMarcadoPlaza(2) + getMarcadoPlaza(3)));
        else if(getMarcadoPlaza(4) + getMarcadoPlaza(5) != pInvariantes[7])
            System.out.println("Inv 8: " + pInvariantes[7] + " Dio: " + (getMarcadoPlaza(4) + getMarcadoPlaza(5)));
        else if(getMarcadoPlaza(6) + getMarcadoPlaza(7) + getMarcadoPlaza(8) != pInvariantes[8])
            System.out.println("Inv 9: " + pInvariantes[8] + " Dio: " + (getMarcadoPlaza(6) + getMarcadoPlaza(7) + getMarcadoPlaza(8)));
        else
            return true;

        return false;
    }

    public int getCantidadTransiciones(){
        return matrizIncidencia[0].length;
    }
}