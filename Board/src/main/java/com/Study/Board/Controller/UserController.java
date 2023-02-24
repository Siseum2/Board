package com.Study.Board.Controller;

import com.Study.Board.Model.UserDto;
import com.Study.Board.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signUpForm(UserDto userDto) {
        return "signUpForm";
    }

    @PostMapping("/signup")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult) {

        if (userService.hasErrors(userDto, bindingResult)) {
            return "signUpForm";
        }

        try {
            userService.createUser(userDto);
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
