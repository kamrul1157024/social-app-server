package com.kamrul.server.phoneticParser.avro.autocorrect;

import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AutoCorrectLoader {

    private JSONObject jsonDict;

    @SneakyThrows
    public AutoCorrectLoader()
    {
         InputStream jsonDictInputStream=AutoCorrectLoader.class
                 .getClassLoader()
                 .getResourceAsStream("phoneticParser/autocorrect.json");

        try(BufferedReader bufferedReader
                    =new BufferedReader(
                            new InputStreamReader(jsonDictInputStream, StandardCharsets.UTF_8))
        ) {
            StringBuilder jsonDictString = new StringBuilder();
            String currentLine;
            while ((currentLine=bufferedReader.readLine())!=null)
            {
                jsonDictString.append(currentLine);
            }
            jsonDict=new JSONObject(jsonDictString.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public JSONObject getJsonDict()
    {
        return jsonDict;
    }


}
