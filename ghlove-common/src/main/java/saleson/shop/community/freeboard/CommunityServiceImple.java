package saleson.shop.community.freeboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.community.doamin.CommunityDto;


@Service("communityService")
public class CommunityServiceImple implements CommunityService {

	private static final Logger log = LoggerFactory.getLogger(CommunityServiceImple.class);

	@Autowired
	private CommunityMapper communityMapper;

	//
	@Override
	public int getFreeBoardCount(CommunityDto communityDto) {
		// TODO Auto-generated method stub
		return communityMapper.countFreeBoard(communityDto);
	}

	@Override
	public List<CommunityDto> listFreeBoard(CommunityDto communityDto) {
		// TODO Auto-generated method stub
		return communityMapper.listFreeBoard(communityDto);
	}

	@Override
	public CommunityDto deatilFreeBoard(CommunityDto communityDto) {
		// TODO Auto-generated method stub
		return communityMapper.deatilFreeBoard(communityDto);
	}

	@Override
	public void insertFreeBoard(CommunityDto communityDto) {
		communityMapper.insertFreeBoard(communityDto);

	}

	@Override
	public void deleteFreeBoard(CommunityDto communityDto) {
		communityMapper.deleteFreeBoard(communityDto);

	}

	@Override
	public void updateFreeBoard(CommunityDto communityDto) {
		communityMapper.updateFreeBoard(communityDto);

	}

	@Override
	public void addHitCount(int Id) {
		communityMapper.addHitCount(Id);

	}

}
