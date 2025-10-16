package cookcloud.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeTagId implements Serializable {

	private Long recipeId;
	private Long hashId;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RecipeTagId recipeTagId = (RecipeTagId) obj;
		return Objects.equals(recipeId, recipeTagId.recipeId) && Objects.equals(hashId, recipeTagId.hashId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(recipeId, hashId);
	}

}
