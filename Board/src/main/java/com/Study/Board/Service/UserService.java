package com.Study.Board.Service;

import com.Study.Board.Model.User;
import com.Study.Board.Repository.CommentRepository;
import com.Study.Board.Repository.PostRepository;
import com.Study.Board.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final UtilService utilService;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User findUser(String username) {
        User user = userRepository.read(username);
        return user;
    }

    public boolean hasUserName(String username) {
        Long count = userRepository.exist(username);
        if(count > 0)
            return true;
        else
            return false;
    }

    public boolean hasUserEmail(String email) {
        Long count = userRepository.exist(email);
        if(count > 0)
            return true;
        else
            return false;
    }

}
