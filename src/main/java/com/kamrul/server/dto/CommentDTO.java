package com.kamrul.server.dto;

import com.kamrul.server.services.verify.Verifiable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO implements Verifiable {
    private Long postId;
    private Long commentId;
    private String commentText;
}
