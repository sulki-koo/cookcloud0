package cookcloud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cookcloud.entity.Attachment;
public interface AttachmentRepository extends JpaRepository<Attachment, Long>{
	
	@Query("SELECT a FROM Attachment a WHERE a.recipeId = :recipeId AND a.attachIsDeleted='N'")
	List<Attachment> findByRecipeIdAndAttachIsDeleted(@Param("recipeId") Long recipeId);
	
}
