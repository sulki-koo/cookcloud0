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
@Table(name = "MESSAGE")
public class Message implements Serializable {

	private static final long serialVersionUID = 906024448710067L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MESSAGE_ID")
	private Long messageId;

	@Column(name = "MESSAGE_TITLE", columnDefinition = "NVARCHAR2(30)", nullable = false)
	private String messageTitle;

	@Column(name = "MESSAGE_CONTENT", columnDefinition = "NVARCHAR2(1000)", nullable = false)
	private String messageContent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MESSAGE_SEND_AT", nullable = false)
	private LocalDateTime messageSendAt;

	@Column(name = "MESSAGE_IS_READ", columnDefinition = "CHAR(1)", nullable = false)
	private String messageIsRead;

	@Column(name = "MESSAGE_IS_DELETED", columnDefinition = "CHAR(1)", nullable = false)
	private String messageIsDeleted;

	@Column(name = "MESSAGE_CODE", nullable = false)
	private Long messageCode;

	@Column(name = "MEM_ID", columnDefinition = "VARCHAR2(20)", nullable = false)
	private String memId;
	
	@ManyToOne
	@JoinColumn(name="MEM_ID", insertable = false, updatable = false)
	private Member member;

}
