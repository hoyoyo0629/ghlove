package saleson.shop.community.locgfaq;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.exception.UserException;

import saleson.shop.community.doamin.LocgfaqDto;



@Service("locgFaqService")
public class LocgFaqServiceImple implements LocgFaqService {
	

	@Autowired
	private LocgFaqMapper locgFaqMapper;

	@Override
	public int getlocFaqBoardCount(LocgfaqDto locgfaqDto) {
		
		return locgFaqMapper.getlocFaqBoardCount(locgfaqDto);
	}

	@Override
	public List<LocgfaqDto> listLocgBoard(LocgfaqDto locgfaqDto) {
		
		return locgFaqMapper.listLocgBoard(locgfaqDto);
	}

	@Override
	public void insertLocgBoard(LocgfaqDto locgfaqDto) {
		locgFaqMapper.insertLocgBoard(locgfaqDto);
		
	}
	
	@Override
	public LocgfaqDto deatilLocgBoard(LocgfaqDto locgfaqDto) {
		
		return locgFaqMapper.deatilLocgBoard(locgfaqDto);
	}

	@Override
	public void deleteLocgBoard(LocgfaqDto locgfaqDto) {
		locgFaqMapper.deleteLocgBoard(locgfaqDto);
		
	}

	@Override
	public void updateLocgBoard(LocgfaqDto locgfaqDto) {
		locgFaqMapper.updateLocgBoard(locgfaqDto);
		
	}

	@Override
	public String databoardListDelete(LocgfaqDto locgfaqDto) throws UserException {
		String code = "FAIL";

		if(locgfaqDto.getDataboardList() != null && locgfaqDto.getDataboardList().size() > 0) {
			int nResult = 0;


			for (String databoardListId : locgfaqDto.getDataboardList()) {
				int dataId = Integer.parseInt(databoardListId);
				nResult += locgFaqMapper.databoardListDelete(dataId);

			}

			if (nResult == locgfaqDto.getDataboardList().size()) {
				code = "SUCC";
			} else {
				throw new UserException();
			}
		}
		return code;
	}

	@Override
	public void addHitCount(int Id) {
		locgFaqMapper.addHitCount(Id);
		
	}

	

}
