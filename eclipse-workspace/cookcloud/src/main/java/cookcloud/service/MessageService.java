package cookcloud.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cookcloud.entity.Follows;
import cookcloud.entity.Member;
import cookcloud.entity.Message;
import cookcloud.entity.Recipe;
import cookcloud.repository.FollowsRepository;
import cookcloud.repository.MessageRepository;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private FollowsService followsService;

	@Autowired
	private MemberService memberService;

	// 메시지 목록 조회
	public List<Message> getMessages(String memId) {
		return messageRepository.findByMemId(memId);
	}

	// 메시지 읽음 처리
	public void markMessageAsRead(Long messageId) {
		Message message = messageRepository.findById(messageId)
				.orElseThrow(() -> new RuntimeException("Message not found"));
		message.setMessageIsRead("Y");
		messageRepository.save(message);
	}

	// 메시지 삭제
	public void deleteMessage(Long messageId) {
		Message message = messageRepository.findById(messageId)
				.orElseThrow(() -> new RuntimeException("Message not found"));
		message.setMessageIsDeleted("Y");
		messageRepository.save(message);
	}

	// 공통 메시지 전송 메소드
	public void likeMessage(String sendToMemid, String nickname, String type) {
		String messageContent = "";
		if ("recipe".equals(type)) {
			messageContent = String.format("%s님이 회원님의 레시피를 좋아합니다.", nickname);
		} else if ("review".equals(type)) {
			messageContent = String.format("%s님이 회원님의 리뷰를 좋아합니다.", nickname);
		}
		insertMessage(sendToMemid, "좋아요 알림", messageContent, 32L);
	}

	public void followMessage(String sendToMemid, String nickname) {
		String messageContent = String.format("%s님이 회원님을 팔로우했습니다.", nickname);
		insertMessage(sendToMemid, "팔로우 알림", messageContent, 33L);
	}

	// 레시피 작성 시, 저장과 동시에 구독자에게 새 레시피 알림 전송
	public void notifyFollowersNewRecipe(String memId) {
		String memNickname = memberService.getMember(memId).get().getMemNickname();
		List<Member> followingMembers = followsService.getMyFollowings(memId);
		for (Member member : followingMembers) {
			String messageContent = String.format("%s님이 새 레시피를 작성했습니다.", memNickname);
			insertMessage(member.getMemId(), "새 레시피 알림", messageContent, 34L);
		}
	}

	@Transactional
	public boolean insertMessage(String sendToMemId, String messageTitle, String messageContent, Long messageCode) {
		try {
			Message message = new Message();
			message.setMessageTitle(messageTitle);
			message.setMessageContent(messageContent);
			message.setMessageSendAt(LocalDateTime.now());
			message.setMessageIsRead("N");
			message.setMessageIsDeleted("N");
			message.setMessageCode(messageCode);
			message.setMemId(sendToMemId);
			messageRepository.save(message);
			return true; // 전송성공
		} catch (Exception e) {
			return false; // 전송실패
		}
	}

}
