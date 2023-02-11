package com.Study.Board.Model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

    @NotEmpty(message ="아이디는 필수입니다")
    private String username;
    @NotEmpty(message ="비밀번호는 필수입니다")
    private String password1;

    @NotEmpty(message ="비밀번호 확인은 필수입니다")
    private String password2;

    @NotEmpty(message ="이메일은 필수입니다")
    private String email;

    @Builder.Default
    private List<PostDto> postDtoList = new ArrayList<>();

    @Builder.Default
    private List<CommentDto> commentDtoList = new ArrayList<>();

}
