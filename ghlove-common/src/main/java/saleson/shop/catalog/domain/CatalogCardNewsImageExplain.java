package saleson.shop.catalog.domain;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.common.utils.UserUtils;

@Data
@NoArgsConstructor
public class CatalogCardNewsImageExplain {
	
	private long cardNewsId;
	
	private int imgSeq;
	
	private String imgDesc;
	
	private long frstRegisterId;
	
	private Timestamp frstRegistPnttm;
	
}
