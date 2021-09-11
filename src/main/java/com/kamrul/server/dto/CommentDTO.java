package com.kamrul.server.dto;

import com.kamrul.server.models.comment.CommentForType;
import com.kamrul.server.services.verify.Verifiable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO implements Verifiable {
    private Long commentId;
    private String commentText;
    private Long commentFor;
    private CommentForType commentForType;
}
