package saleson.shop.designateddonation.domain;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class DsgnCntrCnttImgDesc {
	
	// 이미지 아이디
	private long prjContentImgId;

	// 지정기부 아이디
	private long prjId;
	
	// 이미지 접근성 관련 설명
	private String imgDesc;
	
	// 이미지 순번
	private int imgSeq;
	
	// 최초등록자 ID
	private long frstRegisterId;
	
	// 최초 등록 시점
	private Timestamp frstRegistPnttm;
	
	public DsgnCntrCnttImgDesc() {};
}
