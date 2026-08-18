package saleson.shop.report.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CallFindRequestDto {

	public String callDate;
	public Long frst_register_id;
	public LocalDateTime frst_regist_pnttm;

}

