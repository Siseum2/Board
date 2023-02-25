package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class PostFormDto {
    @NotBlank(message ="제목은 필수입니다")
    private String subject;
    @NotBlank(message ="내용은 필수입니다")
    private String content;

}
