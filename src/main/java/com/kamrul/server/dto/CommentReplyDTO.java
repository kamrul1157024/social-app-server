package com.kamrul.server.dto;

import com.kamrul.server.services.verify.Verifiable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyDTO implements Verifiable {
    private Long commentId;
    private Long commentReplyId;
    private String commentReplyText;
}
