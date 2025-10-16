package cookcloud.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "REVIEW")
public class Review implements Serializable {

	private static final long serialVersionUID = 418579298651982L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "REVIEW_ID")
	private Long reviewId;

	@Column(name = "REVIEW_CONTENT", columnDefinition = "NVARCHAR2(1000)", nullable = false)
	private String reviewContent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVIEW_INSERT_AT", nullable = false)
	private LocalDateTime reviewInsertAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVIEW_UPDATE_AT")
	private LocalDateTime reviewUpdateAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVIEW_DELETE_AT")
	private LocalDateTime reviewDeleteAt;

	@Column(name = "REVIEW_IS_DELETED", columnDefinition = "CHAR(1)", nullable = false)
	private String reviewIsDeleted;

	@Column(name = "RECIPE_ID", nullable = false)
	private Long recipeId;
	
	@ManyToOne
	@JoinColumn(name="RECIPE_ID", insertable = false, updatable = false)
	private Recipe recipe;

	@Column(name = "MEM_ID", columnDefinition = "VARCHAR2(20)", nullable = false)
	private String memId;
	
	@ManyToOne
	@JoinColumn(name="MEM_ID", insertable = false, updatable = false)
	private Member member;
	
	public String getMemNickname() {
        return member != null ? member.getMemNickname() : "알 수 없음";
    }

	@OneToMany(mappedBy = "review")
	private List<Report> reportList;
	
	@OneToMany(mappedBy = "review")
	private List<Likes> likestList;
	
}
