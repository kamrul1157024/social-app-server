package com.kamrul.blog.services.verify;

import com.kamrul.blog.politicalPostFilter.FilterPost;
import com.kamrul.blog.politicalPostFilter.models.TextAnalyzingResponse;
import com.kamrul.blog.services.draftJS.DraftJSTextParsing;
import com.kamrul.blog.services.verify.exception.PoliticalPostException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Verifications{

    @Autowired
    private FilterPost filterPost;

    public void verifyForPoliticalPost(String text) throws PoliticalPostException
    {
        DraftJSTextParsing draftJSPostText= new DraftJSTextParsing(text);

        String postText= draftJSPostText.getAllText();
        TextAnalyzingResponse textAnalyzingResponse=
                filterPost.isPoliticalStatement(postText);
        if (textAnalyzingResponse.getProbabilityOfBeingPolitical()>=.5)
            throw new PoliticalPostException();
    }

}
