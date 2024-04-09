package learning.websocket.controller;

import learning.websocket.converter.UserConverter;
import learning.websocket.dto.UserDto;
import learning.websocket.entity.User;
import learning.websocket.repository.UserRepository;
import learning.websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class UserController {

    private final UserService userService;

    @GetMapping("/loginForm")
    public String loginForm() {return "login/loginForm";}

    @GetMapping("/signInForm")
    public String signInForm() {
        return "login/signInForm";
    }

    @PostMapping("/signInForm/process")
    public String signInProcess(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "password") String password) {

        userService.signIn(userId, name, password);
        return "redirect:/login/loginForm";
    }
}
