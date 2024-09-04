package com.example.SpringSecurityOauthServer.config.oauth;

public interface OauthUserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
