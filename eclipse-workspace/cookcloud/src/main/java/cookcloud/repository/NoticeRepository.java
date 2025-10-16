package cookcloud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cookcloud.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>{
	
	@Query("SELECT n FROM Notice n WHERE n.noticeIsDeleted = 'N' ORDER BY n.noticeId DESC")
	List<Notice> findAllNotice();

}
