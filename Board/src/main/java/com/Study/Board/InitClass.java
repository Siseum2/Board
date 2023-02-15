package com.Study.Board;

import com.Study.Board.Model.Post;
import com.Study.Board.Model.User;
import com.Study.Board.Service.CommentService;
import com.Study.Board.Service.PostService;
import com.Study.Board.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitClass {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {

        User user = User.builder()
                .username("user")
                .password(bCryptPasswordEncoder.encode("1234"))
                .email("user@google.com")
                .build();

        userService.createUser(user);

        for(int i = 1; i <= 24; i++) {
            Post post = Post.builder().build();
            post.setSubject("제목" + i);
            post.setContent("내용" + i);
            postService.createPost("user", post);

            if(i>0) {
                commentService.createComment("user", (long) i, "댓글" + i + i);
                commentService.createComment("user", (long) i, "댓글" + i + (i + 1));
            }
        }
    }

}

