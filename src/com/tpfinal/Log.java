package com.tpfinal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log {
    private String output;
    private static Log instance;

    public Log() {
        this.output = "";
    }

    public static Log getInstance() {
        if(instance == null) 
            instance = new Log();

        return instance;
    }

    public void escribirLog(String s) {
        output += s;
    }

    public void writeFile() { 
        FileWriter fichero = null;

        try {
            fichero = new FileWriter("log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(fichero);
            pw.println(this.output);
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 

        try {
            fichero.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}