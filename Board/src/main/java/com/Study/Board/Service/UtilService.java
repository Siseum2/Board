package com.Study.Board.Service;

import com.Study.Board.Model.Comment;
import com.Study.Board.Model.CommentDto;
import com.Study.Board.Model.Post;
import com.Study.Board.Model.PostDto;
import org.springframework.stereotype.Service;

@Service
public class UtilService {
    public PostDto postConvertPostDto(Post post) {
        PostDto postDto = PostDto.builder()
                .id(post.getId())
                .subject(post.getSubject())
                .content(post.getContent())
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
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate())
                .postId(comment.getPost().getId())
                .build();

        return commentDto;
    }
}
