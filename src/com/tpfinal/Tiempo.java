package com.tpfinal;

public class Tiempo {
    private int[] transicionesTemporizadas;
    private double[] tiempoDeSensibilizado;
    private double[] alfas;
    private double[] betas;
    private double[] q;
    private double[] tiempoInvariantes;

    public Tiempo(int[] transicionesTemporizadas, double[] alfas, double[] betas) {
        this.transicionesTemporizadas = transicionesTemporizadas;
        this.alfas = alfas;
        this.betas = betas;
        
        q = new double[transicionesTemporizadas.length];
        tiempoDeSensibilizado = new double[transicionesTemporizadas.length];
        for(int i = 0; i < q.length; i++) {
            q[i] = 0;
            tiempoDeSensibilizado[i] = 0;
        }

        tiempoInvariantes = new double[3];
    }

    /**
     * actualiza la posicion indicada por index
     * del vector q de tiempo de las transiciones 
     * con el tiempo que paso desde que sensibilizo
     * @param index
     */
    public void actualizarQ(int index, double time) {
        q[index] = time - tiempoDeSensibilizado[index];
    }

    /**
     * marco el tiempo en que se sensibilizo 
     * la transicion correspondiente a index
     * @param index
     */
    public void sensibilizarTiempo(int index, double time) {
        tiempoDeSensibilizado[index] = time;
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
        return q[index] >= alfas[index] && q[index] <= betas[index];
    }

    /**
     * calcula cuanto tiempo le falta
     * a una transicion para poder dispararse
     * @param index
     * @return el tiempo restante para alcanzar el alfa de tiempo
     */
    public double calcularTiempoRestante(int index) {
        double dormir = alfas[index] - q[index];
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

    public double[] getTiempoMinInvariantes() {
        tiempoInvariantes[0] = (alfas[4] + alfas[5] + alfas[6] + alfas[7])/1000;
        tiempoInvariantes[1] = (alfas[1] + alfas[9] + alfas[10])/1000;
        tiempoInvariantes[2] = (alfas[2] + alfas[3])/1000;
        
        return tiempoInvariantes;
    }

    public int[] getTransicionesTemporizadas() {
        return transicionesTemporizadas;
    }

    public double[] getTiempoDeSensibilizado() {
        return tiempoDeSensibilizado;
    }
}