package com.kamrul.server.dto;

import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.utils.Converters;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedalDTO {

    private UserDTO user;
    private PostDTO post;
    private MedalType medalType;

    public MedalDTO(Medal medal) {
        this.user = Converters.convert(medal.getUser());
        this.post = Converters.convert(medal.getPost());
        this.medalType = medal.getMedalType();
    }
}
