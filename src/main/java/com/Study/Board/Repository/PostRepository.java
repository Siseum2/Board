package com.Study.Board.Repository;

import com.Study.Board.Model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Post read(Long postId) {
        Post post = em.createQuery("select distinct p from Post p left join fetch p.commentList where p.id = :id", Post.class)
                .setParameter("id", postId)
                .getSingleResult();

        return post;
    }

    public List<Post> readPage(int page, int pageCount) {
        List<Post> postList = em.createQuery("select p from Post p", Post.class)
                .setFirstResult(page)
                .setMaxResults(pageCount)
                .getResultList();

        return postList;
    }

    public List<Post> readPage(int page, int pageCount, String searchType, String searchText) {
        List<Post> searchPost = null;
        if(searchType.equals("Subject")) {
            searchPost = em.createQuery("select distinct p from Post p where p.subject like :searchSubject", Post.class)
                    .setParameter("searchSubject", "%" + searchText + "%")
                    .setFirstResult(page)
                    .setMaxResults(pageCount)
                    .getResultList();
        }

        else if(searchType.equals("SubjectContent")) {
            searchPost = em.createQuery("select distinct p from Post p " +
                            "where p.subject like :searchSubject or p.content like :searchContent", Post.class)
                    .setParameter("searchSubject", "%" + searchText + "%")
                    .setParameter("searchContent", "%" +  searchText + "%")
                    .setFirstResult(page)
                    .setMaxResults(pageCount)
                    .getResultList();
        }

        else if(searchType.equals("Comment")) {
            searchPost = em.createQuery("select distinct p from Post p left join p.commentList c " +
                            "where c.content like :searchText", Post.class)
                    .setParameter("searchText", "%" + searchText + "%")
                    .setFirstResult(page)
                    .setMaxResults(pageCount)
                    .getResultList();

            int a=0;
        }

        return searchPost;
    }

    public List<Post> readAll() {
        List<Post> postList = em.createQuery(
                        "select distinct p from Post p left join fetch p.commentList", Post.class)
                .getResultList();

        return postList;
    }

    public void delete(Long id) {
        em.createQuery("delete from Post p where p.id = :id")
                .setParameter("id", id)
                .executeUpdate();

        em.clear();
    }

    public List<Post> search(String searchType, String searchText) {
        List<Post> searchPost = null;
        if(searchType.equals("제목")) {
             searchPost = em.createQuery("select distinct p from Post p where p.subject like :searchSubject", Post.class)
                    .setParameter("searchSubject", "%" + searchText + "%")
                    .getResultList();
        }

        else if(searchType.equals("제목+내용")) {
            searchPost = em.createQuery("select distinct p from Post p " +
                            "where p.subject like :searchSubject or p.content like :searchContent", Post.class)
                    .setParameter("searchSubject", "%" + searchText + "%")
                    .setParameter("searchContent", "%" +  searchText + "%")
                    .getResultList();
        }

        return searchPost;
    }

    public Long counts() {
        Long count = em.createQuery("select COUNT(p) from Post p", Long.class)
                .getSingleResult();

        return count;
    }

    public Long counts(String searchType, String searchText) {
        Long count = 0L;

        if(searchType.equals("Subject")) {
            count = em.createQuery("select COUNT(p) from Post p " +
                            "where p.subject like :searchSubject", Long.class)
                    .setParameter("searchSubject", "%" + searchText + "%")
                    .getSingleResult();
        }

        else if(searchType.equals("SubjectContent")) {
            count = em.createQuery("select COUNT(distinct p) from Post p " +
                            "where p.subject like :searchSubject or p.content like :searchContent", Long.class)
                    .setParameter("searchSubject", "%" + searchText + "%")
                    .setParameter("searchContent", "%" + searchText + "%")
                    .getSingleResult();
        }

        else if(searchType.equals("Comment")) {
            count = em.createQuery("select COUNT(distinct p) from Post p left join p.commentList c " +
                            "where c.content like :searchContent", Long.class)
                    .setParameter("searchContent", "%" + searchText + "%")
                    .getSingleResult();
        }

        return count;
    }
}