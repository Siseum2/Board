package com.Study.Board;

import com.Study.Board.Model.PostDto;
import com.Study.Board.Model.UserDto;
import com.Study.Board.Service.CommentService;
import com.Study.Board.Service.PostService;
import com.Study.Board.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitClass {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @PostConstruct
    public void init() {

        UserDto userDto = UserDto.builder()
                .username("user")
                .password1("1234")
                .email("user@google.com")
                .build();

        userService.createUser(userDto);

        for(int i = 1; i <= 24; i++) {
            PostDto postDto = PostDto.builder().build();
            postDto.setSubject("제목" + i);
            postDto.setContent("내용" + i);
            postService.createPost("user", postDto);

            if(i>0) {
                commentService.createComment("user", (long) i, "댓글" + i + i);
                commentService.createComment("user", (long) i, "댓글" + i + (i + 1));
            }
        }
    }

}

