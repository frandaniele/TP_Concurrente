package com.tpfinal;

public class Transicion {
    private int[] t;
    private int id;

    public Transicion(int[] t, int id){
        this.t = t;
        this.id = id;
    }

    public int[] getTransicion(){
        return t;
    }

    public int getId(){
        return id;
    }
}
