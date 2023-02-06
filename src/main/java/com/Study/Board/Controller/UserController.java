package com.Study.Board.Controller;

import com.Study.Board.Model.*;
import com.Study.Board.Service.CommentService;
import com.Study.Board.Service.PostService;
import com.Study.Board.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/signup")
    public String signUpForm(UserDto userDto) {
        return "signUpForm";
    }

    @PostMapping("/signup")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "signUpForm";
        }

        if(!userDto.getPassword1().equals(userDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect"
                    , "2개의 패스워드가 일치하지 않습니다");

            return "signupForm";
        }

        if(userService.hasUserName(userDto.getUsername()) == true) {
            bindingResult.rejectValue("username", "usernameDuplication",
                    "이미 존재하는 아이디 입니다");

            return "signupForm";
        }

        if(userService.hasUserEmail(userDto.getEmail()) == true) {
            bindingResult.rejectValue("email", "emailDuplication",
                    "이미 존재하는 이메일 입니다");

            return "signupForm";
        }


        String password = bCryptPasswordEncoder.encode(userDto.getPassword1());

        User user = User.builder().username(userDto.getUsername())
                .password(password)
                .email(userDto.getEmail())
                .build();

        try {
            userService.createUser(user);
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signUpFailed", e.getMessage());
            return "signupForm";
        }

        return "redirect:/list";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "loginForm";
    }
}
