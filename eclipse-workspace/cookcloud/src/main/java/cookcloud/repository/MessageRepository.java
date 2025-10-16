package cookcloud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cookcloud.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{

	@Query("SELECT m FROM Message m WHERE m.memId = :memId AND m.messageIsDeleted = 'N' ORDER BY m.messageId DESC")
	List<Message> findByMemId(String memId);
	
}
