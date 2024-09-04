package com.example.SpringSecurityOauthServer.config.oauth;

import java.util.Map;

public class GoogleUserInfo implements OauthUserInfo{

    private final Map<String,Object> attributes;

    public GoogleUserInfo(Map<String,Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
