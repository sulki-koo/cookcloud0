package cookcloud.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@AllArgsConstructor
@Entity
@Table(name = "LIKES")
public class Likes implements Serializable {

	private static final long serialVersionUID = 258904516221807L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "LIKE_ID")
	private Long likeId;

	@Column(name = "LIKE_IS_LIKED", columnDefinition = "CHAR(1)", nullable = false)
	private String likeIsLiked;

	@Column(name = "MEM_ID", columnDefinition = "VARCHAR2(20)", nullable = false)
	private String memId;
	
	@ManyToOne
    @JoinColumn(name = "MEM_ID", insertable = false, updatable = false)
    private Member member;

	@Column(name = "REVIEW_ID")
	private Long reviewId;
	
	@ManyToOne
	@JoinColumn(name = "REVIEW_ID", insertable = false, updatable = false)
	private Review review;
	
	@Column(name = "RECIPE_ID")
	private Long recipeId;
	
	@ManyToOne
    @JoinColumn(name = "RECIPE_ID", insertable = false, updatable = false)
    private Recipe recipe;

}
