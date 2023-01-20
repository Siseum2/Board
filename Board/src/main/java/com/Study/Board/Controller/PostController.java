package com.Study.Board.Controller;

import com.Study.Board.Model.Post;
import com.Study.Board.Model.PostDto;
import com.Study.Board.Model.SearchDto;
import com.Study.Board.Service.CommentService;
import com.Study.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping({"/list",""})
    public String list(Model model, @RequestParam(required = false) String searchType, @RequestParam(required = false) String searchText,
    @RequestParam(value="page", defaultValue="1") int page) {

        Page<PostDto> postDtoPage = null;

        if(searchType == null) {
            postDtoPage = postService.readPostPage(page);
        }

        else {
            postDtoPage = postService.readPostPage(page, searchType, searchText);

            SearchDto searchDto = SearchDto.builder()
                    .searchType(searchType)
                    .searchText(searchText).build();
            model.addAttribute("searchDto", searchDto);
        }

        model.addAttribute("postDtoPage", postDtoPage);

        return "listPost";
    }


    @GetMapping("/post/{id}")
    public String readPost(Model model, @PathVariable Long id, @RequestParam(required = false) String act) {

        PostDto postDto = postService.readPost(id);
        model.addAttribute("postDto", postDto);

        if(act != null)
            return "createPost";

        return "readPost";
    }

    @GetMapping("/postForm")
    public String createPostForm() {
        return "createPost";
    }

    @PostMapping("/post")
    public String createPost(PostDto postDto) {
        Post post = Post.builder()
                .subject(postDto.getSubject())
                .content(postDto.getContent())
                .createdDate(postDto.getCreatedDate())
                .modifiedDate(postDto.getModifiedDate())
                .build();

        postService.createPost(post);

        return "redirect:/post/" + post.getId();
    }

    @PutMapping("/post/{postId}")
    public String modifyPost(PostDto postDto, @PathVariable Long postId) {

        postService.updatePost(postId, postDto.getSubject(), postDto.getSubject());

        return "redirect:/post/" + postId;
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        return "redirect:/list";
    }
}
