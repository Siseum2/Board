package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotEmpty(message ="댓글 내용은 필수입니다")
    private String content;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long postId;
}
