package cookcloud.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Entity
@Table(name = "ATTACHMENT")
public class Attachment implements Serializable{

	private static final long serialVersionUID = 165398361059033L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ATTACH_ID")
	private Long attachId;

	@Column(name="ATTACH_UPLOAD_NAME", columnDefinition = "NVARCHAR2(100)", nullable = false)
	private String attachUploadName;

	@Column(name="ATTACH_SERVER_NAME", columnDefinition = "NVARCHAR2(200)", nullable = false)
	private String attachServerName;

	@Column(name="ATTACH_SIZE", nullable = false)
	private Long attachSize;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ATTACH_INSERT_AT", nullable = false)
	private LocalDateTime attachInsertAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ATTACH_DELETE_AT")
	private LocalDateTime attachDeleteAt;

	@Column(name="ATTACH_IS_DELETED", columnDefinition = "CHAR(1)", nullable = false)
	private String attachIsDeleted;

	@Column(name="ATTACH_TYPE_CODE", nullable = false)
	private Long attachTypeCode;

	@Column(name="RECIPE_ID", nullable = false)
	private Long recipeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="RECIPE_ID", insertable = false, updatable = false)
	private Recipe recipe;

}
