package com.Study.Board;

import com.Study.Board.Model.Post;
import com.Study.Board.Service.CommentService;
import com.Study.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitClass {

    private final PostService postService;
    private final CommentService commentService;

    @PostConstruct
    public void init() {
        for(int i = 1; i <= 30; i++) {
            Post post = Post.builder().build();
            post.setSubject("제목" + i);
            post.setContent("내용" + i);
            postService.createPost(post);

            if(i>0) {
                commentService.createComment((long) i, "댓글" + i + i);
                commentService.createComment((long) i, "댓글" + i + (i + 1));
            }
        }
    }

}
