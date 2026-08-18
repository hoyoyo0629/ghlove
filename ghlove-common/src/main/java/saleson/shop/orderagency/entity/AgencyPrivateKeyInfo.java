package saleson.shop.orderagency.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "G_AGENCY_PRIVATE_KEY")
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)				// 엔티티 객체 데이터 변경 감지2
public class AgencyPrivateKeyInfo {
	
	// API 서버 복호화 키 임시 저장 테이블
	@Id
	@NotNull
	@Column(length = 50)
	private String userSessionId;
	
	// 개인키(복호화키)
	@NotNull
	@Column(length = 5000)
	private String privateKey;
	
	// 공개키(암호화키)
	@NotNull
	@Column(length = 5000)
	private String publicKey;
	
	@CreatedDate		// 엔티티 객체 데이터 변경 감지하여 insert 시 자동으로 값 세팅
	private Timestamp frstRegDt;
	
}
