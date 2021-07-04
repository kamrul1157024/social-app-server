package com.kamrul.server.services.verify;

import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.services.verify.exception.VerificationException;
import org.springframework.stereotype.Service;

@Service
public class CommentVerifier
        extends Verifications
        implements Verifier<Comment>{

    @Override
    public void verify(Comment comment)
            throws VerificationException {
        verifyForPoliticalPost(comment.getCommentText());
    }
}
