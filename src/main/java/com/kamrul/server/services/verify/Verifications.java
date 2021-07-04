package com.kamrul.server.services.verify;

import com.kamrul.server.politicalPostFilter.FilterPost;
import com.kamrul.server.politicalPostFilter.models.TextAnalyzingResponse;
import com.kamrul.server.services.draftJS.DraftJSTextParsing;
import com.kamrul.server.services.verify.exception.PoliticalPostException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Verifications{
    @Autowired
    private FilterPost filterPost;

    public void verifyForPoliticalPost(String text) throws PoliticalPostException {
        TextAnalyzingResponse textAnalyzingResponse= filterPost.isPoliticalStatement(text);
        if (textAnalyzingResponse.getProbabilityOfBeingPolitical()>=.5) throw new PoliticalPostException();
    }
}
