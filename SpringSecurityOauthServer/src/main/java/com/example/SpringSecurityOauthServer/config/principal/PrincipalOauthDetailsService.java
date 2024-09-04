package com.example.SpringSecurityOauthServer.config.principal;

import com.example.SpringSecurityOauthServer.config.oauth.GoogleUserInfo;
import com.example.SpringSecurityOauthServer.config.oauth.NaverUserInfo;
import com.example.SpringSecurityOauthServer.config.oauth.OauthUserInfo;
import com.example.SpringSecurityOauthServer.entity.Member;
import com.example.SpringSecurityOauthServer.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauthDetailsService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OauthUserInfo oauth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            oauth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }

        assert oauth2UserInfo != null;
        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String email = oauth2UserInfo.getEmail();
        String name = oauth2UserInfo.getName();
        String userId = provider+"_"+providerId;

        Member checkMember = memberService.findByUserIdProcess(userId);
        if (checkMember == null){
            // Oauth 로그인 강제 회원가입 진행
            checkMember = Member.builder()
                    .name(name)
                    .userId(userId)
                    .password("")
                    .email(email)
                    .roles("ROLE_USER")
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberService.joinOauthProcess(checkMember);
        }
        return new PrincipalDetails(checkMember,oAuth2User.getAttributes());
    }
}
