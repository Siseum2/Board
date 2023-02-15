package com.Study.Board.Controller;

import com.Study.Board.Service.CommentService;
import com.Study.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/home")
    public @ResponseBody String homeController() {
        return "Hello";
    }


}
