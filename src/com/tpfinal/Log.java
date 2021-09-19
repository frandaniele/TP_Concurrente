package com.tpfinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.filechooser.FileSystemView;

public class Log {

    private File file;
    private FileWriter fichero;
    private PrintWriter pw;
    private BufferedReader br;

    public Log() {
        fichero = null;
        pw = null;
        br = null;
        try {
            file = new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/') + "/log.txt");
            fichero = new FileWriter(file);
            pw = new PrintWriter(fichero);
            br = new BufferedReader(new FileReader(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/') + "/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribir(int nDeTransicion){
        escribirLog(leerLog().append("T"+nDeTransicion));
    }

    private String leer() {
        try {
            String a = br.readLine();
            if(a != null){
                return a;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private StringBuffer leerLog() {
        StringBuffer info = new StringBuffer();
        abrir();
        info.append(leer());
        cerrar();
        return info;
    }

    private void escribirLog(StringBuffer texto) {
        fichero = null;
        pw=null;
        File file1 = new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/') + "/log1.txt");
        try {
            fichero = new FileWriter(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        abrir();
        pw.print(texto);
        cerrar();
        file.delete();
        file1.renameTo(file);
    }

    private void abrir() {
        try {
            pw = new PrintWriter(fichero);
            br = new BufferedReader(new FileReader(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replace('\\', '/') + "/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cerrar() {
        try {
            pw.close();
            br.close();
            fichero.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}