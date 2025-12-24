package com.quest.badminton.controller.views;

import com.quest.badminton.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class AuthVIewController {
    private final AuthService authService;

    @GetMapping("/register/confirm")
    public String confirmRegister(@RequestParam("token") String token,
                                  Model model)
    {
        authService.activeToken(token);
        return "register-confirm";
    }
}
