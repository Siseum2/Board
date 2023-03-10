package com.Study.Board.Service;

import com.Study.Board.Model.Enum.SearchType;
import com.Study.Board.Model.Post;
import com.Study.Board.Model.PostDto;
import com.Study.Board.Model.User;
import com.Study.Board.Repository.CommentRepository;
import com.Study.Board.Repository.PostRepository;
import com.Study.Board.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UtilService utilService;

    public Post createPost(String username, PostDto postDto) {
        Post post = Post.builder()
                .subject(postDto.getSubject())
                .content(postDto.getContent())
                .build();

        User user = userRepository.read(username);
        post.setUser(user);
        postRepository.save(post);

        return post;
    }

    public PostDto readPost(Long postId) {
        Post post = postRepository.read(postId);
        PostDto postDto = utilService.postConvertPostDto(post);
        return postDto;
    }

    public Page<PostDto> searchPostPage(int page) {
        Long total = postRepository.counts();
        int startPage = 0;
        int remainPageCount = 10;

        startPage = (int) (total - page * 10);
        if(startPage < 0) {
            remainPageCount = remainPageCount + startPage;
            startPage = 0;
        }

        List<Post> postList = postRepository.readPostPage(startPage, remainPageCount);
        List<PostDto> postDtoList = new ArrayList<>();
        Collections.reverse(postList);

        for(Post post : postList) {
            postDtoList.add(utilService.postConvertPostDto(post));
        }

        Pageable pageable = PageRequest.of(page-1 ,10);
        PageImpl<PostDto> postDtoPage = new PageImpl<>(postDtoList, pageable, total);
        return postDtoPage;
    }

    public Page<PostDto> searchPostPage(int page, SearchType searchType, String search) {
        Long total = postRepository.counts(searchType, search);
        int startPage = 0;
        int pageCount = 10;

        startPage = (int) (total - page * 10);
        if(startPage < 0) {
            pageCount = pageCount + startPage;
            startPage = 0;
        }

        List<Post> postList = postRepository.readPostPage(startPage, pageCount, searchType, search);
        List<PostDto> postDtoList = new ArrayList<>();
        Collections.reverse(postList);

        for(Post post : postList) {
            postDtoList.add(utilService.postConvertPostDto(post));
        }

        Pageable pageable = PageRequest.of(page-1 ,10);
        PageImpl<PostDto> postDtoPage = new PageImpl<>(postDtoList, pageable, total);
        return postDtoPage;
    }


    public List<PostDto> readPostAll() {
        List<Post> postList = postRepository.readAll();
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post post : postList) {
            postDtoList.add(utilService.postConvertPostDto(post));
        }

       return postDtoList;
    }

    public void updatePost(Long postId, String subject, String content) {
        Post post = postRepository.read(postId);
        post.setSubject(subject);
        post.setContent(content);
    }

    public void deletePost(Long postId) {
        commentRepository.deleteAll(postId);
        postRepository.delete(postId);
    }

}
