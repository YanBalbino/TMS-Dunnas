package com.dunnas.tms.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/tickets";
    }
}
