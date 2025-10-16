package cookcloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cookcloud.entity.Member;
import cookcloud.repository.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(memId)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + memId));

        if (member.getMemStatusCode() == 13L) {
            throw new DisabledException("탈퇴한 회원은 로그인할 수 없습니다.");
        }
        
        return User.builder()
            .username(member.getMemId())
            .password(member.getMemPassword())  // DB에 저장된 암호화된 비밀번호 사용
            .roles("USER")  // 기본 역할 설정 (실제 사용 안 해도 됨)
            .build();
    }
    
}
