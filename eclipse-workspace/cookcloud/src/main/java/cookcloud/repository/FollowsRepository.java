package cookcloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cookcloud.entity.Follows;
import cookcloud.entity.FollowsId;
import cookcloud.entity.Member;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, FollowsId> {
	
	@Query("SELECT f FROM Follows f WHERE f.followIsFollowing ='Y' ORDER BY f.followerId DESC")
	List<Follows> findActiveFollows();
	
	@Query("SELECT f FROM Follows f WHERE f.followIsFollowing = 'Y' AND f.followerId = :memId ORDER BY f.followerId DESC")
	List<Follows> findByFollowerId(@Param("memId") String memId);

	@Query("SELECT f FROM Follows f WHERE f.followIsFollowing = 'Y' AND f.followingId = :memId ORDER BY f.followingId DESC")
	List<Follows> findByFollowingId(@Param("memId") String memId);
	
	@Query("SELECT f FROM Follows f WHERE f.followIsFollowing = 'Y' AND f.followingId = :followingId AND f.followerId = :followerId")
    Optional<Follows> findByFollowingIdAndFollowerId(@Param("followingId")String followingId, @Param("followerId")String followerId);

}
