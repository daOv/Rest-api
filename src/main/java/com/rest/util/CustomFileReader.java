package com.rest.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CustomFileReader {

    public static String readFile (String fileName) throws IOException {
        FileReader fr = new FileReader("./src/main/resources/proba.json");
        BufferedReader br = new BufferedReader(fr);
        String string="";
        int i;
       while(br.read()!=-1){
           string += br.readLine();
       }
        br.close();
        fr.close();

        return string;
    }
}
