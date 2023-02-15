package com.Study.Board.Config.Auth;

import com.Study.Board.Model.User;
import com.Study.Board.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User principal;

        try {
            principal = userService.findUser(username);
        } catch(EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다");
        }

        return new PrincipalDetail(principal);
    }
}
