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
@Entity
@Table(name = "RECIPELOG")
public class RecipeLog implements Serializable {
	
	private static final long serialVersionUID = 650207203242951L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="LOGGING_ID")
	private Long loggingId;

	@Column(name="LOGGING_ACTION", columnDefinition = "VARCHAR2(20)", nullable = false)
	private String loggingAction;

	@Column(name="LOGGING_INSERT_AT", nullable = false)
	private LocalDateTime loggingInsertAt;

	@Column(name="RECIPE_ID", nullable = false)
	private Long recipeId;
	
	@ManyToOne
	@JoinColumn(name="RECIPE_ID", insertable = false, updatable = false)
	private Recipe recipe;

}


