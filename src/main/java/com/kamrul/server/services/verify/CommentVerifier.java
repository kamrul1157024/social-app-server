package com.kamrul.server.services.verify;

import com.kamrul.server.dto.CommentDTO;
import com.kamrul.server.services.draftJS.DraftJSTextParsing;
import com.kamrul.server.services.verify.exception.VerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentVerifier extends Verifications implements Verifier<CommentDTO>{
    DraftJSTextParsing draftJSTextParsing;

    @Autowired
    public CommentVerifier(DraftJSTextParsing draftJSTextParsing){
        this.draftJSTextParsing = draftJSTextParsing;
    }

    @Override
    public void verify(CommentDTO commentDTO) throws VerificationException {
        String commentText= draftJSTextParsing.getAllText(commentDTO.getCommentText());
        verifyForPoliticalPost(commentText);
    }
}
