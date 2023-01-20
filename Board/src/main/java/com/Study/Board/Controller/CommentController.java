package com.Study.Board.Controller;

import com.Study.Board.Model.CommentDto;
import com.Study.Board.Service.CommentService;
import com.Study.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/comment/{postId}")
    public String createComment(Model model, @PathVariable Long postId, CommentDto commentDto) {
        commentService.createComment(postId, commentDto.getContent());

        return "redirect:/post/" + postId;
    }
}
