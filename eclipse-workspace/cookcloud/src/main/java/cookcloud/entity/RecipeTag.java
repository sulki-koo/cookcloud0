package cookcloud.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "RECIPETAG")
@IdClass(RecipeTagId.class)
public class RecipeTag implements Serializable {

	private static final long serialVersionUID = 472551661700793L;

	@Id
	@Column(name="RECIPE_ID")
	private Long recipeId;

	@Id
	@Column(name="HASH_ID")
	private Long hashId;
	
	public RecipeTag(Long recipeId, Long hashId) {
		this.recipeId = recipeId;
		this.hashId = hashId;
	}

	@ManyToOne
	@JoinColumn(name="RECIPE_ID", insertable = false, updatable = false)
	private Recipe recipe;
	
	@ManyToOne
	@JoinColumn(name="HASH_ID", insertable = false, updatable = false)
	private Hashtag hashtag;
	
	public String getHashtagName() {
        return hashtag != null ? hashtag.getHashName() : "알 수 없음";
    }

}