package com.Study.Board.Controller;

import com.Study.Board.Config.Auth.PrincipalDetail;
import com.Study.Board.Model.CommentDto;
import com.Study.Board.Model.Enum.SearchType;
import com.Study.Board.Model.Post;
import com.Study.Board.Model.PostDto;
import com.Study.Board.Model.SearchDto;
import com.Study.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping({"/list",""})
    public String list(Model model, @RequestParam(required = false, value="searchType") SearchType searchType,
                       @RequestParam(required = false, value="searchText") String searchText,
                       @RequestParam(value="page", defaultValue="1") int page) {

        Page<PostDto> postDtoPage = null;

        if(searchType == null) {
            postDtoPage = postService.searchPostPage(page);
        }

        else {
            postDtoPage = postService.searchPostPage(page, searchType, searchText);

            SearchDto searchDto = SearchDto.builder()
                    .searchType(searchType)
                    .searchText(searchText).build();
            model.addAttribute("searchDto", searchDto);
        }

        model.addAttribute("postDtoPage", postDtoPage);

        return "listPost";
    }


    @GetMapping("/{id}")
    public String readPost(Model model, @PathVariable Long id, @RequestParam(required = false) String act, CommentDto commentDto
            ,@AuthenticationPrincipal PrincipalDetail principalDetail) {

        PostDto postDto = postService.readPost(id);
        model.addAttribute("postDto", postDto);

        if(principalDetail == null || !principalDetail.getUsername().equals(postDto.getUsername()))
            model.addAttribute("isAuthority", false);
        else
            model.addAttribute("isAuthority", true);

        if(act != null)
            return "createPost";

        return "readPost";
    }

    @GetMapping("/postForm")
    public String PostForm(PostDto postDto) {
        return "createPost";
    }

    @PostMapping("/post")
    public String createPost(@Valid PostDto postDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        if(bindingResult.hasErrors()) {
            return "createPost";
        }

        Post post = postService.createPost(principalDetail.getUsername(), postDto);

        return "redirect:/" + post.getId();
    }

    @PutMapping("/post/{postId}")
    public String modifyPost(@PathVariable Long postId, @Valid PostDto postDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "createPost";
        }

        postService.updatePost(postId, postDto.getSubject(), postDto.getContent());

        return "redirect:/" + postId;
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        return "redirect:/list";
    }
}
