package learning.websocket.controller;

import learning.websocket.config.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails == null) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin", true);
        }
        return "home";
    }
}
