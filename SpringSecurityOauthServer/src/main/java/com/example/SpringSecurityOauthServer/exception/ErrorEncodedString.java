package com.example.SpringSecurityOauthServer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorEncodedString {

    INVALID_CREDENTIALS("로그인하신 회원정보가 존재하지 않습니다."),
    FAIL_JOIN("회원가입에 실패하였습니다."),
    ID_DUPLICATED("아이디가 중복됩니다.");

    private final String message;
}
