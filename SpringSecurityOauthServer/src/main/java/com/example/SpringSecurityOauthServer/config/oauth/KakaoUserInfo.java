package com.example.SpringSecurityOauthServer.config.oauth;

import java.util.Map;

public class KakaoUserInfo implements OauthUserInfo{

    private final Map<String,Object> attributes;

    public KakaoUserInfo(Map<String,Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) ((Map<?, ?>) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map<?, ?>) attributes.get("properties")).get("nickname");
    }
}
