package com.kamrul.server.services.verify;

import com.kamrul.server.dto.CommentReplyDTO;
import com.kamrul.server.models.comment.CommentReply;
import com.kamrul.server.services.draftJS.DraftJSTextParsing;
import com.kamrul.server.services.verify.exception.VerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentReplyVerifier extends Verifications implements Verifier<CommentReplyDTO> {
    DraftJSTextParsing draftJSTextParsing;

    @Autowired
    public CommentReplyVerifier(DraftJSTextParsing draftJSTextParsing){
        this.draftJSTextParsing = draftJSTextParsing;
    }

    @Override
    public void verify(CommentReplyDTO commentReplyDTO) throws VerificationException {
        String commentReplyText= draftJSTextParsing.getAllText(commentReplyDTO.getCommentReplyText());
        verifyForPoliticalPost(commentReplyText);
    }
}
