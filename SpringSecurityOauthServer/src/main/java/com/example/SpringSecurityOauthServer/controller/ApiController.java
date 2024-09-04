package com.example.SpringSecurityOauthServer.controller;

import com.example.SpringSecurityOauthServer.config.principal.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails userDetails){
        log.info(userDetails.getUsername());
        return "user";
    }

    @GetMapping("/manager")
    public String manager(@AuthenticationPrincipal PrincipalDetails userDetails){
        log.info(userDetails.getUsername());
        return "manager";
    }

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal PrincipalDetails userDetails){
        log.info(userDetails.getUsername());
        return "admin";
    }
}
