package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class PostFormDto {
    @NotEmpty(message ="제목은 필수입니다")
    private String subject;
    @NotEmpty(message ="내용은 필수입니다")
    private String content;

}
