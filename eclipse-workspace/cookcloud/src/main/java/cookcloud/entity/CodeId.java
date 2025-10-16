package cookcloud.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeId implements Serializable {

	private Long parentCode;
	private Long childCode;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		CodeId codeId = (CodeId) obj;
		return Objects.equals(parentCode, codeId.parentCode) &&
				Objects.equals(childCode, codeId.childCode);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(parentCode, childCode);
	}
	
}
