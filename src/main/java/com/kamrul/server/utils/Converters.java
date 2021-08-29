package com.kamrul.server.utils;

import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.dto.UserDTO;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;


public class Converters {


    public static UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        userDTO.setDateOfBirth(user.getDateOfBirth());

        if(user.getEmailVisible())
            userDTO.setEmail(user.getEmail());

        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setCity(user.getCity());
        userDTO.setCountry(user.getCountry());
        userDTO.setEmailVerified(user.getEmailVerified());
        userDTO.setEmailVisible(user.getEmailVisible());
        userDTO.setGender(user.getGender());
        userDTO.setTotalNumberOfFollower(user.getTotalNumberOfFollower());
        userDTO.setTotalNumberOfUserFollowed(user.getTotalNumberOfUserFollowed());
        userDTO.setUserDescription(user.getUserDescription());
        userDTO.setDeleted(user.getDeleted());
        return userDTO;
    }


    public static User convert( UserDTO userDTO,User user)
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
        if(userDTO.getUserDescription()!=null)
            user.setUserDescription(userDTO.getUserDescription());
        return user;
    }

    public static PostDTO convert(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setUser(post.getUser());
        postDTO.setPostId(post.getPostId());
        postDTO.setPostTitle(post.getPostTitle());
        postDTO.setPostText(post.getPostText());
        postDTO.setCreationDate(post.getCreationDate());
        postDTO.setDraft(post.getDraft());
        postDTO.setTotalBronze(post.getTotalBronze());
        postDTO.setTotalSilver(post.getTotalSilver());
        postDTO.setTotalGold(post.getTotalGold());
        postDTO.setTags(post.getTags());
        return postDTO;
    }

    public static Post convert(PostDTO postDTO,Post post)
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
