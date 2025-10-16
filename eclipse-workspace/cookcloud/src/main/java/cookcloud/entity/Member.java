package cookcloud.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "MEMBER")
public class Member implements Serializable {

	private static final long serialVersionUID = 958674608281975L;

	@Id
	@Column(name = "MEM_ID", columnDefinition = "VARCHAR2(20)")
	private String memId;

	@Column(name = "MEM_PASSWORD", columnDefinition = "VARCHAR2(255)", nullable = false)
	private String memPassword;

	@Column(name = "MEM_NAME", columnDefinition = "NVARCHAR2(50)", nullable = false)
	private String memName;

	@Column(name = "MEM_NICKNAME", unique = true, columnDefinition = "NVARCHAR2(10)", nullable = false)
	private String memNickname;

	@Column(name = "MEM_EMAIL", columnDefinition = "VARCHAR2(50)", nullable = false)
	private String memEmail;

	@Column(name = "MEM_PHONE", columnDefinition = "VARCHAR2(20)", nullable = false)
	private String memPhone;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MEM_INSERT_AT", nullable = false)
	private LocalDateTime memInsertAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MEM_DELETE_AT")
	private LocalDateTime memDeleteAt;

	@Column(name = "ROLE_CODE", nullable = false)
	private Long roleCode;

	@Column(name = "MEM_STATUS_CODE", nullable = false)
	private Long memStatusCode;

	@OneToMany(mappedBy = "member")
	private List<Recipe> recipeList;

	@OneToMany(mappedBy = "follower")
	private List<Follows> followsList;

	@OneToMany(mappedBy = "following")
	private List<Follows> followingList;

	@OneToMany(mappedBy = "member")
	private List<Likes> likesList;

	@OneToMany(mappedBy = "member")
	private List<Review> reviewList;

	@OneToMany(mappedBy = "member")
	private List<Message> messageList;

	@OneToMany(mappedBy = "member")
	private List<Report> reportList;

}