package saleson.shop.community.locgfaq;


import java.util.List;

import com.onlinepowers.framework.exception.UserException;

import saleson.shop.community.doamin.CommunityDto;
import saleson.shop.community.doamin.LocgfaqDto;
import saleson.shop.databoard.support.DataboardParam;

public interface LocgFaqService {
	
	public int getlocFaqBoardCount(LocgfaqDto locgfaqDto);
	
	
	public List<LocgfaqDto> listLocgBoard(LocgfaqDto locgfaqDto);
	/**
	 * 
	 * <pre>
	 * comment       : 디테일 화면
	 * preMethodName : 
	 * author        : csh
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto
	 * @return 
	 * CommunityDto
	 */
	public LocgfaqDto deatilLocgBoard(LocgfaqDto locgfaqDto);
	/**
	 * 
	 * <pre>
	 * comment       : faq 등록
	 * preMethodName : 
	 * author        : csh
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto 
	 * void
	 */
	public void insertLocgBoard(LocgfaqDto locgfaqDto);
	/**	 * 
	 * <pre>
	 * comment       : faq 삭제
	 * preMethodName : 
	 * author        : csh
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto 
	 * void
	 */
	public void deleteLocgBoard(LocgfaqDto locgfaqDto);
	/**
	 * 	 * <pre>
	 * comment       : faq 업데이트 
	 * preMethodName : 
	 * author        : csh
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto 
	 * void
	 */
	public void updateLocgBoard(LocgfaqDto locgfaqDto);
	
	/**
	 * 
	 * <pre>
	 * comment       : 다중삭제
	 * preMethodName : 
	 * author        : csh
	 * date          : 2023. 9. 14.
	 *
	 * </pre>
	 * @param locgfaqDto
	 * @return
	 * @throws Exception 
	 * String
	 */
	public String databoardListDelete(LocgfaqDto locgfaqDto) throws UserException;
	
	/**
	 * 조회수 추가
	 * @param dataId
	 */
	public void addHitCount(int Id);
}
