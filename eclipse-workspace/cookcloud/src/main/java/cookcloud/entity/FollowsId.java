package cookcloud.entity;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowsId {

	private String followerId;
	private String followingId;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		FollowsId followsId = (FollowsId) obj;
		return Objects.equals(followerId, followsId.followerId) &&
				Objects.equals(followingId, followsId.followingId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(followerId, followingId);
	}
	
}
