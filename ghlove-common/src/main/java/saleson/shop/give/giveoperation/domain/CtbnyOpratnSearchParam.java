package saleson.shop.give.giveoperation.domain;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;
import saleson.common.configuration.SalesonProperty;

@Data
@Builder
public class CtbnyOpratnSearchParam {
	
	private String shLocgovCode;
	private Long shRegistSn;
	
}
