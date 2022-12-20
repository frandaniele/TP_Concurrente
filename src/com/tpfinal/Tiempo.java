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

    public void actualizarQ(int index) {
        q[index] = System.currentTimeMillis() - tiempoDeSensibilizado[index];
    }

    public void sensibilizarTiempo(int index) {
        tiempoDeSensibilizado[index] = System.currentTimeMillis();
        q[index] = tiempoDeSensibilizado[index];
    }

    public void resetTiempo(int index) {
        tiempoDeSensibilizado[index] = 0;
        q[index] = 0;
    }

    public boolean enVentana(int index) {
        return q[index] > alfas[index] && q[index] <= betas[index];
    }

    public int calcularTiempoRestante(int index) {
        int dormir = (int)(alfas[index] - q[index]);
        return dormir > 0 ? dormir : 0;
    }

    public int[] getTransicionesTemporizadas() {
        return transicionesTemporizadas;
    }

    public void setTransicionesTemporizadas(int[] transicionesTemporizadas) {
        this.transicionesTemporizadas = transicionesTemporizadas;
    }

    public long[] getAlfas() {
        return alfas;
    }

    public void setAlfas(long[] alfas) {
        this.alfas = alfas;
    }

    public long[] getBetas() {
        return betas;
    }

    public void setBetas(long[] betas) {
        this.betas = betas;
    }

    public long[] getTiempoDeSensibilizado() {
        return tiempoDeSensibilizado;
    }

    public void setTiempoDeSensibilizado(long[] tiempoDeSensibilizado) {
        this.tiempoDeSensibilizado = tiempoDeSensibilizado;
    }

    public long[] getQ() {
        return q;
    }

    public void setQ(long[] q) {
        this.q = q;
    }
}