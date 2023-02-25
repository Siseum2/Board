package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

    @NotBlank(message ="아이디는 필수입니다")
    private String username;
    @NotBlank(message ="비밀번호는 필수입니다")
    private String password1;

    @NotBlank(message ="비밀번호 확인은 필수입니다")
    private String password2;

    @NotBlank(message ="이메일은 필수입니다")
    private String email;

    @Builder.Default
    private List<PostDto> postDtoList = new ArrayList<>();

    @Builder.Default
    private List<CommentDto> commentDtoList = new ArrayList<>();

}
