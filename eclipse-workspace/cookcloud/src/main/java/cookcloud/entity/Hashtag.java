package cookcloud.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "HASHTAG")
public class Hashtag implements Serializable {

	private static final long serialVersionUID = 250292794637521L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="HASH_ID")
	private Long hashId;

	@Column(name="HASH_NAME", columnDefinition = "NVARCHAR2(20)", nullable = false)
	private String hashName;

	@Column(name="HASH_CODE", nullable = false)	
	private Long hashtagCode;

	@Column(name="HASH_USAGE_COUNT", nullable = false)
	private Long hashUsageCount;
	
	public Hashtag(String hashName, Long hashtagCode, Long hashUsageCount) {
		this.hashName = hashName;
		this.hashtagCode = hashtagCode;
		this.hashUsageCount = hashUsageCount;
	}

	@OneToMany(mappedBy = "hashtag")
	private List<RecipeTag> recipeTagList;

}