package com.example.SpringSecurityOauthServer.service;

import com.example.SpringSecurityOauthServer.entity.Member;
import com.example.SpringSecurityOauthServer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isExistUserIdProcess(String userId){
        return memberRepository.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Member findByUserIdProcess(String userId){
        Optional<Member> targetEntity = memberRepository.findByUserId(userId);
        return targetEntity.orElse(null);
    }

    public String joinProcess(Member newMember){
        if (isExistUserIdProcess(newMember.getUserId())){
            return "ID_DUPLICATED";
        }
        String encodePassword = passwordEncoder.encode(newMember.getPassword());
        newMember.setRoles("ROLE_USER");
        newMember.setPassword(encodePassword);
        newMember.setProvider("Form-Login");
        newMember.setProviderId("Form-Login"+newMember.getUserId());
        memberRepository.save(newMember);
        return "Join Success";
    }

    @Transactional
    public void joinOauthProcess(Member newMember) {
        String encodePassword = passwordEncoder.encode(newMember.getPassword());
        newMember.setPassword(encodePassword);
        memberRepository.save(newMember);
    }
}
