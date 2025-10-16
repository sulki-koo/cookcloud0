package cookcloud.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cookcloud.entity.Notice;
import cookcloud.repository.NoticeRepository;
import jakarta.transaction.Transactional;

@Service
public class NoticeService {

	@Autowired
	private NoticeRepository noticeRepository;

	public List<Notice> getNotices() {
		return noticeRepository.findAllNotice();
	}

	// 모든 공지사항 조회 (삭제 여부와 상관없이 모두 조회)
	public List<Notice> getNoticeAdmin() {
		return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "noticeId"));
	}

	@Transactional
	public void insertNotice(Notice notice) {
		notice.setNoticeInsertAt(LocalDateTime.now());
		notice.setNoticeBoardCode(42L); // 기본 값
		notice.setNoticeIsDeleted("N");
		noticeRepository.save(notice);
	}

	@Transactional
	public boolean updateNotice(Notice notice) {
		try {
			Notice findNotice = noticeRepository.findById(notice.getNoticeId())
					.orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));
			findNotice.setNoticeTitle(notice.getNoticeTitle());
			findNotice.setNoticeContent(notice.getNoticeContent());
			findNotice.setNoticeUpdateAt(LocalDateTime.now());
			noticeRepository.save(findNotice);
			return true; // 전송성공
		} catch (Exception e) {
			return false; // 전송실패
		}
	}

	@Transactional
	public boolean deleteNotice(Long noticeId) {
		try {
			Notice findNotice = noticeRepository.findById(noticeId)
					.orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));
			findNotice.setNoticeIsDeleted("Y");
			noticeRepository.save(findNotice);
			return true; // 전송성공
		} catch (Exception e) {
			return false; // 전송실패
		}
	}

}
