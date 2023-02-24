package com.Study.Board.Service;

import com.Study.Board.Model.Comment;
import com.Study.Board.Model.CommentDto;
import com.Study.Board.Model.Post;
import com.Study.Board.Model.PostDto;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    public boolean isAlphaNumericOrAlpha(String str) {
        boolean isAlphaNumeric = str.matches("^[a-zA-Z0-9]+$");
        boolean isOnlyNumeric = str.matches("^[0-9]+$");

        return (isAlphaNumeric && !isOnlyNumeric);
    }

    public PostDto postConvertPostDto(Post post) {
        PostDto postDto = PostDto.builder()
                .id(post.getId())
                .subject(post.getSubject())
                .content(post.getContent())
                .username(post.getUser().getUsername())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();

        if(post.getCommentList().size() > 0) {
            for(Comment comment : post.getCommentList()) {
                CommentDto commentDto = commentConvertCommentDto(comment);
                postDto.getCommentDtoList().add(commentDto);
            }
        }

        return postDto;
    }

    public CommentDto commentConvertCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate())
                .postId(comment.getPost().getId())
                .build();

        return commentDto;
    }
}
