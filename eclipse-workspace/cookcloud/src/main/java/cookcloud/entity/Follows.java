package cookcloud.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "FOLLOWS")
@IdClass(FollowsId.class)
public class Follows implements Serializable {

	private static final long serialVersionUID = 896895357863219L;

	@Id
	@Column(name = "FOLLOWER_ID", columnDefinition = "VARCHAR2(20)")
	private String followerId;

	@ManyToOne
	@JoinColumn(name = "FOLLOWER_ID", insertable = false, updatable = false)
	private Member follower;

	@Id
	@Column(name = "FOLLOWING_ID", columnDefinition = "VARCHAR2(20)")
	private String followingId;

	@ManyToOne
	@JoinColumn(name = "FOLLOWING_ID", insertable = false, updatable = false)
	private Member following;

	@Column(name = "FOLLOW_IS_FOLLOWING", columnDefinition = "CHAR(1)")
	private String followIsFollowing;

}
