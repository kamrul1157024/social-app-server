package com.kamrul.blog.utils;

import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.dto.UserDTO;
import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.User;


public class Converters {


    public static UserDTO convert(UserDTO userDTO, User user)
    {
        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setEmail(user.getEmail());
        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setCity(user.getCity());
        userDTO.setCountry(user.getCountry());
        userDTO.setIsEmailVerified(userDTO.getEmailVerified());
        userDTO.setEmailVisible(user.getEmailVisible());
        userDTO.setGender(user.getGender());
        return userDTO;
    }


    public static User convert(User user, UserDTO userDTO)
    {

        if(userDTO.getUserName()!=null)
            user.setUserName(userDTO.getUserName());

        if(userDTO.getFirstName()!=null)
            user.setFirstName(userDTO.getFirstName());

        if(userDTO.getLastName()!=null)
            user.setLastName(userDTO.getLastName());

        if(userDTO.getEmail()!=null)
            user.setEmail(userDTO.getEmail());

        if(userDTO.getDateOfBirth()!=null)
            user.setDateOfBirth(userDTO.getDateOfBirth());

        if(userDTO.getCountry()!=null)
            user.setCountry(userDTO.getCountry());

        if(userDTO.getCity()!=null)
            user.setCity(userDTO.getCity());

        if(userDTO.getEmailVisible()!=null)
            user.setEmailVisible(userDTO.getEmailVisible());

        if(userDTO.getEmailVerified()!=null)
            user.setEmailVerified(userDTO.getEmailVerified());

        if(userDTO.getProfilePicture()!=null)
            user.setProfilePicture(userDTO.getProfilePicture());
        if(userDTO.getGender()!=null)
            user.setGender(userDTO.getGender());
        return user;
    }

    public static PostDTO convert(PostDTO postDTO,Post post)
    {
        postDTO.setUser(post.getUser());
        postDTO.setPostId(post.getPostId());
        postDTO.setPostTitle(post.getPostTitle());
        postDTO.setPostText(post.getPostText());
        postDTO.setComments(post.getComments());
        postDTO.setCreationDate(post.getCreationDate());
        postDTO.setDraft(post.isDraft());
        postDTO.setTotalUpVotes(post.getTotalUpVotes());
        postDTO.setTags(post.getTags());
        return postDTO;
    }

    public static Post convert(Post post,PostDTO postDTO)
    {
        if(postDTO.getUser()!=null)
            post.setUser(postDTO.getUser());
        if(postDTO.getPostTitle()!=null)
            post.setPostTitle(postDTO.getPostTitle());
        if(postDTO.getPostText()!=null)
            post.setPostText(postDTO.getPostText());
        if(postDTO.getDraft()!=null)
            post.setDraft(postDTO.getDraft());
        if(postDTO.getTags()!=null)
            post.setTags(postDTO.getTags());
        return post;
    }

}
