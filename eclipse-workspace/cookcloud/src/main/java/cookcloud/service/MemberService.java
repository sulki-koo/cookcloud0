package cookcloud.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cookcloud.entity.Member;
import cookcloud.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public List<Member> getMemberList() {
		return memberRepository.findAllNotDeleted();
	}

	public Optional<Member> getMember(String memId) {
		return memberRepository.findByIdAndNotDeleted(memId);
	}

	public Optional<Member> findByMemNickname(String memNickname) {
		Optional<Member> opMember = memberRepository.findByMemNickname(memNickname);
		return opMember;
	}

	@Transactional
	public void insertMember(Member member) {
		member.setMemPassword(passwordEncoder.encode(member.getMemPassword()));
		member.setMemInsertAt(LocalDateTime.now()); // 가입 날짜
		member.setRoleCode(22L); // 회원
		member.setMemStatusCode(11L); // 정상 상태
		memberRepository.save(member);
	}

	public boolean isDuplicateId(String memId) {
		boolean idExists = getMember(memId).isPresent();
		return idExists;
	}

	public boolean isDuplicateNickname(String memNickname) {
		boolean nicknameExists = findByMemNickname(memNickname).isPresent();
		return nicknameExists;
	}

	// 이메일과 전화번호로 회원 찾기
	public String findID(String memEmail, String memPhone) {
		// 이메일과 전화번호로 회원을 찾아 Member 객체를 반환
		Member member = memberRepository.findByMemEmailAndMemPhone(memEmail, memPhone).orElse(null);

		// Member가 null이 아니면 아이디를 반환, null이면 null 반환
		return (member != null) ? member.getMemId() : null;
	}

	// 아이디 *로 마스킹
	public String maskMemberId(String memId) {
		// ID가 null이거나 길이가 3자 미만인 경우는 처리하지 않음 (회원가입 시 이미 3자 이상 보장됨)
		if (memId == null || memId.length() < 3) {
			return memId; // ID가 null이거나 길이가 3자 미만이면 그대로 반환
		}

		// 앞의 3자리는 그대로 두고 나머지는 *로 마스킹
		String visiblePart = memId.substring(0, 3); // 앞의 3자리는 그대로
		String maskedPart = "*".repeat(memId.length() - 3); // 나머지 문자는 *로 마스킹

		return visiblePart + maskedPart;
	}

	// 비밀번호 찾기
	public String findPassword(String memId, String memEmail, String memPhone) {
		Member member = memberRepository.findByMemIdAndMemEmailAndMemPhone(memId, memEmail, memPhone).orElse(null);
		return (member != null) ? member.getMemId() : "일치하는 회원 정보가 없습니다.";
	}

	// 비밀번호 변경
	public void updatePass(String memId, String memPassword) {
        Optional<Member> findMember = memberRepository.findById(memId);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            member.setMemPassword(passwordEncoder.encode(memPassword)); // 서비스에서 인코딩
            memberRepository.save(member);
        }
    }

	// 회원 목록 조회
	public List<Member> getMembersAdmin() {
		return memberRepository.findAll();
	}

	// 회원 상태와 역할 업데이트
	public void updateMemberStatusAndRole(String memId, Long memStatusCode, Long roleCode) {
		// 특정 회원 조회
		Member member = memberRepository.findById(memId).orElse(null);
		if (member != null) {
			// 상태코드와 등급코드 업데이트
			member.setMemStatusCode(memStatusCode);
			member.setRoleCode(roleCode);
			// 수정된 회원 정보 저장
			memberRepository.save(member);
		}
	}

	@Transactional
	public Member updateMember(Member member) {
		Member findMember = getMember(member.getMemId()).get();
		findMember.setMemName(member.getMemName());
		findMember.setMemNickname(member.getMemNickname());
		findMember.setMemEmail(member.getMemEmail());
		findMember.setMemPhone(member.getMemPhone());
		return memberRepository.save(findMember);
	}

	@Transactional
	public void deleteMember(String memId) {
		Member findMember = getMember(memId).get();
		findMember.setMemDeleteAt(LocalDateTime.now()); // 탈퇴일
		findMember.setMemStatusCode(13L); // 탈퇴 회원
		memberRepository.save(findMember);
	}

}
