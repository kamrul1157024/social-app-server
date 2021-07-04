package com.kamrul.server.services;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileReading {

    public static String getAllText(String filePath)
    {
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(filePath)))
        {
            String jsonText="";
            String currentLine;
            while ((currentLine=bufferedReader.readLine())!=null)
            {
                jsonText+=currentLine;
            }

            return jsonText;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
