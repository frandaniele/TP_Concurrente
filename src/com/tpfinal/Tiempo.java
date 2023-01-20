package com.tpfinal;

public class Tiempo {
    private int[] transicionesTemporizadas;
    private long[] tiempoDeSensibilizado;
    private long[] alfas;
    private long[] betas;
    private long[] q;

    public Tiempo(int[] transicionesTemporizadas, long[] alfas, long[] betas) {
        this.transicionesTemporizadas = transicionesTemporizadas;
        this.alfas = alfas;
        this.betas = betas;
        
        q = new long[transicionesTemporizadas.length];
        tiempoDeSensibilizado = new long[transicionesTemporizadas.length];
        for(int i = 0; i < q.length; i++) {
            q[i] = 0;
            tiempoDeSensibilizado[i] = 0;
        }
    }

    /**
     * actualiza la posicion indicada por index
     * del vector q de tiempo de las transiciones 
     * con el tiempo que paso desde que sensibilizo
     * @param index
     */
    public void actualizarQ(int index) {
        q[index] = System.currentTimeMillis() - tiempoDeSensibilizado[index];
    }

    /**
     * marco el tiempo en que se sensibilizo 
     * la transicion correspondiente a index
     * @param index
     */
    public void sensibilizarTiempo(int index) {
        tiempoDeSensibilizado[index] = System.currentTimeMillis();
        q[index] = tiempoDeSensibilizado[index];
    }

    /**
     * testea si una transicion se encuentra en
     * su ventana de disparo
     * @param index
     * @return true si el tiempo que paso desde
     * el sensibilizado de la transicion
     * esta entre los limites de disparo, 
     * false en caso contrario
     */
    public boolean enVentana(int index) {
        return q[index] > alfas[index] && q[index] <= betas[index];
    }

    /**
     * calcula cuanto tiempo le falta
     * a una transicion para poder dispararse
     * @param index
     * @return el tiempo restante para alcanzar el alfa de tiempo
     */
    public int calcularTiempoRestante(int index) {
        int dormir = (int)(alfas[index] - q[index]);
        return dormir > 0 ? dormir : 0;
    }

    /*
     * resetea los vectores de tiempo
     * de una transicion
     */
    public void resetTiempo(int index) {
        tiempoDeSensibilizado[index] = 0;
        q[index] = 0;
    }

    public int[] getTransicionesTemporizadas() {
        return transicionesTemporizadas;
    }

    public long[] getTiempoDeSensibilizado() {
        return tiempoDeSensibilizado;
    }
}