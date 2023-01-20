package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String subject;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    @Builder.Default
    private List<CommentDto> commentDtoList = new ArrayList<>();

}
