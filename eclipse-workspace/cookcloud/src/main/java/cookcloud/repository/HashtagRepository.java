package cookcloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cookcloud.entity.Hashtag;
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

	Optional<Hashtag> findByHashName(String hashName);
	
	@Query("SELECT h FROM Hashtag h ORDER BY h.hashUsageCount DESC, h.hashId DESC")
	List<Hashtag> findTopByUsageCount();
	
}
