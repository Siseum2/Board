package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PostDto {
    private Long id;
    @NotBlank(message ="제목은 필수입니다")
    private String subject;
    @NotBlank(message ="내용은 필수입니다")
    private String content;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    @Builder.Default
    private List<CommentDto> commentDtoList = new ArrayList<>();

}
