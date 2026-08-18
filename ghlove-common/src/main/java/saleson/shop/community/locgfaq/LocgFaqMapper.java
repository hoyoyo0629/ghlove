package saleson.shop.community.locgfaq;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.community.doamin.LocgfaqDto;

@Mapper("locgFaqMapper")
public interface LocgFaqMapper {
	int getlocFaqBoardCount(LocgfaqDto locgfaqDto);
	
	List<LocgfaqDto> listLocgBoard(LocgfaqDto locgfaqDto);
	
	LocgfaqDto deatilLocgBoard(LocgfaqDto locgfaqDto);
	
	void insertLocgBoard(LocgfaqDto locgfaqDto);
	
	void deleteLocgBoard(LocgfaqDto locgfaqDto);
	
	void updateLocgBoard(LocgfaqDto locgfaqDto);
	
	int databoardListDelete(int Id);

	void addHitCount(int Id);
}	
