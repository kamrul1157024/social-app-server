package com.kamrul.blog.services.verify;

import com.kamrul.blog.models.comment.CommentReply;
import com.kamrul.blog.services.verify.exception.VerificationException;
import org.springframework.stereotype.Service;

@Service
public class CommentReplyVerifier
        extends Verifications
        implements Verifier<CommentReply> {
    @Override
    public void verify(CommentReply commentReply) throws VerificationException {
        verifyForPoliticalPost(commentReply.getCommentReplyText());
    }
}
