package com.kamrul.server.services.draftJS;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class DraftJSTextParsing {

    @SneakyThrows
    public String getAllText(String draftRawJson) {
        JSONObject draftJSRaw= new JSONObject(draftRawJson);
        JSONArray blocks=  draftJSRaw.getJSONArray("blocks");
        String text="";
        for(int i=0;i<blocks.length();i++) {
            text+=blocks.getJSONObject(i).getString("text");
        }
        return text;
    }

}
