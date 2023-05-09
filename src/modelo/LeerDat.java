package modelo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LeerDat {
    public static void main(String[] args) {
        String filePath = "src/Trayectos.dat";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            while (dataInputStream.available() > 0) {
                int number = dataInputStream.readInt();
                String word1 = new String(dataInputStream.readUTF().getBytes("ISO-8859-1"), "UTF-8");
                String word2 = new String(dataInputStream.readUTF().getBytes("ISO-8859-1"), "UTF-8");
                System.out.println("Number: " + number + ", Word 1: " + word1 + ", Word 2: " + word2);
            }
            dataInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}