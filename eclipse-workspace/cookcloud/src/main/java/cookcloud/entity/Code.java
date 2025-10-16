package cookcloud.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CODE")
@IdClass(CodeId.class)
public class Code implements Serializable {

	private static final long serialVersionUID = 332436933436524L;

	@Id
	@Column(name="PARENT_CODE")
	private Long parentCode;

	@Id
	@Column(name="CHILD_CODE")
	private Long childCode;

	@Column(name="CODE_NAME", columnDefinition = "NVARCHAR2(50)", nullable = false)
	private String codeName;

	@Column(name="CODE_DESCRIPTION", columnDefinition = "NVARCHAR2(1000)")
	private String codeDescription;

	@Column(name="CODE_REMARK", columnDefinition = "NVARCHAR2(500)")
	private String codeRemark;

}
