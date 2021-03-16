package com.kamrul.blog.services.verify;

import com.kamrul.blog.models.comment.Comment;
import com.kamrul.blog.models.comment.CommentReply;
import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.services.FileReading;
import com.kamrul.blog.services.verify.exception.PoliticalPostException;
import com.kamrul.blog.services.verify.exception.VerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VerificationsTest {

    @Autowired
    Verifier<Post> postVerifier;
    @Autowired
    Verifier<Comment> commentVerifier;
    @Autowired
    Verifier<CommentReply> commentReplyVerifierVerifier;

    String politicalText;
    String nonPoliticalText;

    @BeforeEach
    void setUP()
    {
        String politicalPostPath="src/test/java/com/kamrul/blog/services/verify/politicalPost.json";
        String nonPoliticalPostPath="src/test/java/com/kamrul/blog/services/verify/nonPoliticalPost.json";

        politicalText= FileReading.getAllText(politicalPostPath);
        nonPoliticalText=FileReading.getAllText(nonPoliticalPostPath);

    }

    private void testPoliticalPost(String politicalText,String nonPoliticalText) throws VerificationException
    {
        Post post=new Post();
        post.setPostText(politicalText);
        assertThrows(PoliticalPostException.class,()->postVerifier.verify(post));
        post.setPostText(nonPoliticalText);
        assertDoesNotThrow(()->postVerifier.verify(post));

    }

    private void testPoliticalComment(String politicalText,String nonPoliticalText)
    {
        Comment comment=new Comment();
        comment.setCommentText(politicalText);
        assertThrows(PoliticalPostException.class,()->commentVerifier.verify(comment));
        comment.setCommentText(nonPoliticalText);
        assertDoesNotThrow(()->commentVerifier.verify(comment));

    }

    private void testPoliticalCommentReply(String politicalText,String nonPoliticalText)
    {
        CommentReply commentReply=new CommentReply();
        commentReply.setCommentReplyText(politicalText);
        assertThrows(PoliticalPostException.class,()->commentReplyVerifierVerifier.verify(commentReply));
        commentReply.setCommentReplyText(nonPoliticalText);
        assertDoesNotThrow(()->commentReplyVerifierVerifier.verify(commentReply));
    }

    @Test
    void verifyForPoliticalText() throws VerificationException {
       testPoliticalPost(politicalText,nonPoliticalText);
       testPoliticalComment(politicalText,nonPoliticalText);
       testPoliticalCommentReply(politicalText,nonPoliticalText);
    }
}