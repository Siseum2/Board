package com.Study.Board.Controller;

import com.Study.Board.Config.Auth.PrincipalDetail;
import com.Study.Board.Model.CommentDto;
import com.Study.Board.Model.PostDto;
import com.Study.Board.Service.CommentService;
import com.Study.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/comment/{postId}")
    public String createComment(Model model, @PathVariable Long postId, @AuthenticationPrincipal PrincipalDetail principalDetail,
                                @Valid CommentDto commentDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            PostDto postDto = postService.readPost(postId);
            model.addAttribute("postDto", postDto);
            return "readPost";
        }

        commentService.createComment(principalDetail.getUsername(), postId, commentDto.getContent());

        return "redirect:/" + postId;
    }
}
