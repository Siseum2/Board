package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class CommentFormDto {

    @NotBlank(message ="내용은 필수입니다")
    private String content;

}
