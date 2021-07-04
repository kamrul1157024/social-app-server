package com.kamrul.server.services.verify;

import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.services.draftJS.DraftJSTextParsing;
import com.kamrul.server.services.verify.exception.VerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PostVerifier extends Verifications implements Verifier<PostDTO> {
    private DraftJSTextParsing draftJSTextParsing;

    @Autowired
    public PostVerifier(DraftJSTextParsing draftJSTextParsing){
        this.draftJSTextParsing = draftJSTextParsing;
    }

    @Override
    public void verify(PostDTO postDTO) throws VerificationException {
        String postText= draftJSTextParsing.getAllText(postDTO.getPostText());
        verifyForPoliticalPost(postText);
    }

}
