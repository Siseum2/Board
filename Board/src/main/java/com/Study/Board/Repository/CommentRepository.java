package com.Study.Board.Repository;

import com.Study.Board.Model.Comment;
import com.Study.Board.Model.Enum.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment read(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        return comment;
    }
    public List<Comment> readAll(Long postId) {
        List<Comment> commentList = em.createQuery("select c from Comment c where c.post.id = :id", Comment.class)
                .setParameter("id", postId)
                .getResultList();

        return commentList;
    }

    public void delete(Comment comment) {
        em.remove(comment);
        em.flush();
        em.clear();
    }
    public void deleteAll(Long postId) {
        em.createQuery("delete from Comment c where c.post.id = :id")
                .setParameter("id", postId)
                .executeUpdate();
        em.clear();
    }

    public List<Comment> search(SearchType searchType, String searchText) {
        List<Comment> searchComment = null;
        if(searchType.equals(SearchType.COMMENT)) {
            searchComment = em.createQuery("select c from Comment c where c.content like :search", Comment.class)
                    .setParameter("search", "%" + searchText + "%")
                    .getResultList();
        }

        return searchComment;
    }

}
