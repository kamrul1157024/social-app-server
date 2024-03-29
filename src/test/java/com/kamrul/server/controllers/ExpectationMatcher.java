package com.kamrul.server.controllers;

import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExpectationMatcher {
    public static ResultActions expectToBeUser(ResultActions response, User user) throws Exception{
        ResultActions currentExpectation = response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.city").value(user.getCity()))
                .andExpect(jsonPath("$.country").value(user.getCountry()))
                .andExpect(jsonPath("$.gender").value(user.getGender()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.userDescription").value(user.getUserDescription()));
        if(user.getEmailVisible()){
            currentExpectation = currentExpectation.andExpect(jsonPath("$.email").value(user.getEmail()));
        }
        return currentExpectation;
    }

    public static ResultActions expectToBePost(ResultActions response, Post post) throws Exception {
        return response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getPostId()))
                .andExpect(jsonPath("$.postTitle").value(post.getPostTitle()))
                .andExpect(jsonPath("$.postText").value(post.getPostText()))
                .andExpect(jsonPath("$.draft").value(false))
                .andExpect(jsonPath("$.user.userId").value(post.getUser().getUserId()));
    }

    public static ResultActions expectToBeComment(ResultActions response, Comment comment,String ...startingJsonPath) throws Exception{
        String jsonPath = "";
        if (startingJsonPath.length>0) {
            jsonPath = startingJsonPath[0];
        }
        ResultActions currentExpectation =  response
                .andExpect(jsonPath("$."+jsonPath+"commentText").value(comment.getCommentText()))
                .andExpect(jsonPath("$."+jsonPath+"creationDate").isString())
                .andExpect(jsonPath("$."+jsonPath+"updated").value(comment.getUpdated()))
                .andExpect(jsonPath("$."+jsonPath+"replyTo").value(comment.getReplyTo()))
                .andExpect(jsonPath("$."+jsonPath+"user.userId").value(comment.getUser().getUserId()));
        if (comment.getCommentId()!=null){
            currentExpectation
                    .andExpect(jsonPath("$."+jsonPath+"commentId").value(comment.getCommentId()));
        }
        return currentExpectation;
    }
}
