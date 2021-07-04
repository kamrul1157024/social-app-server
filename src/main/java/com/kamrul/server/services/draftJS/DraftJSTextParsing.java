package com.kamrul.server.services.draftJS;

import org.json.JSONArray;
import org.json.JSONObject;

public class DraftJSTextParsing {

    private JSONObject draftJSRaw;

    public DraftJSTextParsing(String draftJSRaw)
    {
        this.draftJSRaw= new JSONObject(draftJSRaw);
    }

    public String getAllText()
    {
        JSONArray blocks=  draftJSRaw.getJSONArray("blocks");
        String text="";
        for(int i=0;i<blocks.length();i++)
        {
            JSONObject jsonObject=blocks.getJSONObject(i);
            text+=jsonObject.get("text").toString();
        }
        return text;
    }

}
