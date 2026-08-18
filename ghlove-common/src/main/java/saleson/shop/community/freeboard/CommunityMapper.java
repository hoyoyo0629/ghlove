package saleson.shop.community.freeboard;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.community.doamin.CommunityDto;

@Mapper("communityMapper")
public interface CommunityMapper {	

	void insertFreeBoard(CommunityDto communityDto);
	
	int countFreeBoard(CommunityDto communityDto);
	
	List<CommunityDto> listFreeBoard(CommunityDto communityDto);
	
	CommunityDto deatilFreeBoard(CommunityDto communityDto);
	
	void deleteFreeBoard(CommunityDto communityDto);
	
	void updateFreeBoard(CommunityDto communityDto);
	
	int databoardListDelete(int dataId);	

	void addHitCount(int Id);
}
