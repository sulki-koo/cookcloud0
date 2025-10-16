package cookcloud.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "NOTICE")
public class Notice implements Serializable {

	private static final long serialVersionUID = 101468578680264L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="NOTICE_ID")
	private Long noticeId;

	@Column(name="NOTICE_TITLE", columnDefinition = "NVARCHAR2(50)", nullable = false)
	private String noticeTitle;

	@Column(name="NOTICE_CONTENT", columnDefinition = "NVARCHAR2(2000)", nullable = false)
	private String noticeContent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NOTICE_INSERT_AT", nullable = false)
	private LocalDateTime noticeInsertAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NOTICE_UPDATE_AT")
	private LocalDateTime noticeUpdateAt;

	@Column(name="NOTICE_IS_DELETED", columnDefinition = "CHAR(1)", nullable = false)
	private String noticeIsDeleted;

	@Column(name="NOTICE_BOARD_CODE", nullable = false)
	private Long noticeBoardCode;

	@Column(name="NOTICE_ANSWER_NAME", columnDefinition = "NVARCHAR2(10)", nullable = false)
	private String noticeAnswerName;

}
