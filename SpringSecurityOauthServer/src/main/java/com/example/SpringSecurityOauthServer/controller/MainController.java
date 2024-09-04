package com.example.SpringSecurityOauthServer.controller;

import com.example.SpringSecurityOauthServer.entity.Member;
import com.example.SpringSecurityOauthServer.exception.ErrorEncodedString;
import com.example.SpringSecurityOauthServer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping("/public/login")
    public String loginPage(
            @RequestParam(value="errorMessage",required=false) String errorMessage,
            @RequestParam(value = "error", required = false, defaultValue = "false") boolean error,
            Model model){
        if (error) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "login.html";
    }

    @GetMapping("/public/main")
    public String index(){
        return "main.html";
    }

    @GetMapping("/public/join")
    public String join(
            @RequestParam(value="errorMessage",required=false) String errorMessage,
            @RequestParam(value = "error", required = false, defaultValue = "false") boolean error,
            Model model){
        if (error) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "join.html";
    }

    @GetMapping("/public/except")
    @ResponseBody
    public String except(){
        return "Not Access This Page";
    }

    @PostMapping("/auth/join")
    public String joinProc(Member member){
        String result = memberService.joinProcess(member);
        String errorMessage;
        if (result.equals("Join Success")){
            return "redirect:/public/login";
        }else if (result.equals("ID_DUPLICATED")){
            errorMessage = URLEncoder.encode(ErrorEncodedString.ID_DUPLICATED.getMessage(), StandardCharsets.UTF_8);
        }else{
            errorMessage = URLEncoder.encode(ErrorEncodedString.FAIL_JOIN.getMessage(), StandardCharsets.UTF_8);
        }
        return "redirect:/public/join?error=true&errorMessage="+errorMessage;
    }
}
