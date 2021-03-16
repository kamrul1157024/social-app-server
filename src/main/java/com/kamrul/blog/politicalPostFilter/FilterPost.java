package com.kamrul.blog.politicalPostFilter;

import com.kamrul.blog.phoneticParser.avro.PhoneticParser;
import com.kamrul.blog.politicalPostFilter.models.LanguageProbability;
import com.kamrul.blog.politicalPostFilter.models.TextAnalyzingRequest;
import com.kamrul.blog.politicalPostFilter.models.TextAnalyzingResponse;
import com.kamrul.blog.politicalPostFilter.textProccessor.TextProcessor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FilterPost {

    @Value("${text-analyzer-server}")
    private String textAnalyzingServerURL;



    public LanguageProbability detectLanguage(String text)
    {
        String languageDetectionURL=textAnalyzingServerURL+"/detectLanguage/";

        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject textJsonObject= new JSONObject();
        textJsonObject.put("text",text);

        HttpEntity<String> requestHttpEntity=
                new HttpEntity<>(textJsonObject.toString(),httpHeaders);

        LanguageProbability probabilityResponse= restTemplate.postForObject(
                languageDetectionURL,
                requestHttpEntity,
                LanguageProbability.class
        );
        return  probabilityResponse;
    }

    public TextAnalyzingRequest getTextAnalyzingRequest(String text, LanguageProbability languageProbability)
    {
        String banglaSegment= TextProcessor.extractBanglaCharacters(text);
        String english= TextProcessor.extractEnglishCharacters(text);
        String banglishSegment = "";
        if(languageProbability.getBanglish()>=.3)
            banglishSegment=TextProcessor.extractBanglishInBangla(text);

        return new TextAnalyzingRequest(
                text,
                banglaSegment,
                banglishSegment,
                english
        );
    }

    public TextAnalyzingResponse isPoliticalStatement(String text)
    {
        String politicalPostDetectionURL=textAnalyzingServerURL+"/isPoliticalPost/";

        LanguageProbability languageProbability= detectLanguage(text);
        TextAnalyzingRequest textAnalyzingRequest=
                getTextAnalyzingRequest(text,languageProbability);

        RestTemplate restTemplate= new RestTemplate();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TextAnalyzingRequest> requestHttpEntity
                =new HttpEntity<>(textAnalyzingRequest, httpHeaders);

        TextAnalyzingResponse response= restTemplate.postForObject(
                politicalPostDetectionURL,
                requestHttpEntity,
                TextAnalyzingResponse.class
        );
        return response;
    }

}
