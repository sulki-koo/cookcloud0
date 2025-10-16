package cookcloud.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "RECIPE")
public class Recipe implements Serializable {

	private static final long serialVersionUID = 562040398472513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RECIPE_ID")
	private Long recipeId;

	@Column(name = "RECIPE_TITLE", columnDefinition = "NVARCHAR2(100)", nullable = false)
	private String recipeTitle;

	@Column(name = "RECIPE_CONTENT", columnDefinition = "NVARCHAR2(2000)", nullable = false)
	private String recipeContent;

	@Column(name = "RECIPE_VIEW_COUNT", nullable = false)
	private Long recipeViewCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECIPE_INSERT_AT", nullable = false)
	private LocalDateTime recipeInsertAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECIPE_UPDATE_AT")
	private LocalDateTime recipeUpdateAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECIPE_DELETE_AT")
	private LocalDateTime recipeDeleteAt;

	@Column(name = "RECIPE_IS_DELETED", columnDefinition = "CHAR(1)", nullable = false)
	private String recipeIsDeleted;

	@Column(name = "RECIPE_CODE", nullable = false)
	private Long recipeCode;

	@Column(name = "RECIPE_BOARD_CODE", nullable = false)
	private Long recipeBoardCode;

	@Column(name = "LIKERANK")
	private Long likeRank;

	@Column(name = "MEM_ID", columnDefinition = "VARCHAR2(20)", nullable = false)
	private String memId;

	public Recipe(Long recipeId, String recipeTitle, String recipeContent, Long recipeViewCount,
			LocalDateTime recipeInsertAt, LocalDateTime recipeUpdateAt, LocalDateTime recipeDeleteAt,
			String recipeIsDeleted, Long recipeCode, Long recipeBoardCode, Long likeRank, String memId) {
		this.recipeId = recipeId;
		this.recipeTitle = recipeTitle;
		this.recipeContent = recipeContent;
		this.recipeViewCount = recipeViewCount;
		this.recipeInsertAt = recipeInsertAt;
		this.recipeUpdateAt = recipeUpdateAt;
		this.recipeDeleteAt = recipeDeleteAt;
		this.recipeIsDeleted = recipeIsDeleted;
		this.recipeCode = recipeCode;
		this.recipeBoardCode = recipeBoardCode;
		this.likeRank = likeRank;
		this.memId = memId;
	}

	@ManyToOne
	@JoinColumn(name = "MEM_ID", insertable = false, updatable = false)
	private Member member;

	public String getMemNickname() {
		return member != null ? member.getMemNickname() : "알 수 없음";
	}

	@OneToMany(mappedBy = "recipe")
	private List<Review> reviewList;

	@OneToMany(mappedBy = "recipe")
	private List<Report> reportList;

	@OneToMany(mappedBy = "recipe")
	private List<Likes> likesList;
	
	public int getLikeCount() {
	    return likesList != null ? likesList.size() : 0;
	}
	
	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RecipeTag> recipeTagList = new ArrayList<>();

	@OneToMany(mappedBy = "recipe")
	private List<Attachment> attachList;

	@Transient
	@JsonIgnore
	private String imageUrl;

	public String getImageUrl() {
		if (attachList != null && !attachList.isEmpty()) {
			return "/img/recipe/" + attachList.get(0).getAttachServerName();
		}
		return "/img/recipe/default-image.jpg"; // 기본 이미지
	}
	
	@Transient
	@JsonIgnore
	private String mediaType;

	public String getMediaType() {
	    if (attachList != null && !attachList.isEmpty()) {
	        String fileName = attachList.get(0).getAttachServerName().toLowerCase();
	        if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mov")) {
	            return "video";
	        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif")) {
	            return "image";
	        }
	    }
	    return "none";
	}


}