package com.Study.Board.Service;

import com.Study.Board.Model.Comment;
import com.Study.Board.Model.CommentDto;
import com.Study.Board.Model.Enum.SearchType;
import com.Study.Board.Model.Post;
import com.Study.Board.Model.User;
import com.Study.Board.Repository.CommentRepository;
import com.Study.Board.Repository.PostRepository;
import com.Study.Board.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UtilService utilService;

    public void createComment(String username, Long postId, String content) {
        User user = userRepository.read(username);
        Post post = postRepository.read(postId);

        if(content.isEmpty() == false) {
            Comment comment = Comment.builder().build();
            comment.setContent(content);
            comment.setUser(user);
            comment.setPost(post);
            post.getCommentList().add(comment);
            commentRepository.save(comment);
        }
    }

    public List<CommentDto> readCommentList(Long postId) {
        List<Comment> commentList = commentRepository.readAll(postId);
        List<CommentDto> commentDtoList = new ArrayList<>();

        for(Comment comment : commentList) {
            commentDtoList.add(utilService.commentConvertCommentDto(comment));
        }

        return commentDtoList;
    }

    public void updateComment(Long commentId, String content) {
        Comment comment = commentRepository.read(commentId);
        comment.setContent(content);
    }
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.read(postId);
        Comment comment = post.getCommentList().get((int) (commentId - 1));
        commentRepository.delete(comment);
        post.getCommentList().remove((int)(commentId-1));
    }

    public void deleteCommentAll(Long postId) {
        Post post = postRepository.read(postId);
        post.getCommentList().clear();
        commentRepository.deleteAll(postId);
    }

    public List<CommentDto> searchCommentList(SearchType searchType, String searchText) {
        List<Comment> searchCommentList = commentRepository.search(searchType, searchText);
        List<CommentDto> searchCommentDtoList = new ArrayList<>();

        for(Comment searchComment : searchCommentList) {
            searchCommentDtoList.add(utilService.commentConvertCommentDto(searchComment));
        }

        return searchCommentDtoList;
    }
}
