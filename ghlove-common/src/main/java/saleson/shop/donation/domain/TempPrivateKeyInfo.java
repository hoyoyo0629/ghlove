package saleson.shop.donation.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "G_TEMP_PRIVATE_KEY")
@Entity
@NoArgsConstructor
public class TempPrivateKeyInfo {
	
	// API 서버 복호화 키 임시 저장 테이블
	@Id
	private Long userId;
	
	@NotNull
	private String privateKey;
	
}
