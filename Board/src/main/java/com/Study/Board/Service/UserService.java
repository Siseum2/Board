package com.Study.Board.Service;

import com.Study.Board.Model.User;
import com.Study.Board.Model.UserDto;
import com.Study.Board.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UtilService utilService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void createUser(UserDto userDto) {
        String encodingPassword = bCryptPasswordEncoder.encode(userDto.getPassword1());

        User user = User.builder().username(userDto.getUsername())
                .password(encodingPassword)
                .email(userDto.getEmail())
                .build();

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

    public boolean hasErrors(UserDto userDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return true;
        }

        if(utilService.isAlphaNumericOrAlpha(userDto.getUsername()) != true) {
            bindingResult.rejectValue("username", "usernameIsNotAlphaNumeric",
                    "아이디는 영문과 숫자로만 혼합하여 이루어져야 합니다.");

            return true;
        }

        if(!userDto.getPassword1().equals(userDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect"
                    , "2개의 패스워드가 일치하지 않습니다");

            return true;
        }

        if(hasUserName(userDto.getUsername()) == true) {
            bindingResult.rejectValue("username", "usernameDuplication",
                    "이미 존재하는 아이디 입니다");

            return true;
        }

        if(hasUserEmail(userDto.getEmail()) == true) {
            bindingResult.rejectValue("email", "emailDuplication",
                    "이미 존재하는 이메일 입니다");

            return true;
        }

        return false;
    }

}
