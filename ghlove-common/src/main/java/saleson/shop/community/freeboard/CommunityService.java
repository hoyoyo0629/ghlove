package saleson.shop.community.freeboard;

import java.util.List;

import saleson.shop.community.doamin.CommunityDto;
import saleson.shop.community.doamin.LocgfaqDto;

public interface CommunityService {


	/**
	 *
	 * <pre>
	 * comment       : 자유게시판 카운트
	 * preMethodName :
	 * author        : rhkdqhr90
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto
	 * @return
	 * int
	 */
	public int getFreeBoardCount( CommunityDto communityDto);

	/**
	 *
	 * <pre>
	 * comment       : 자유게시판 리스트
	 * preMethodName :
	 * author        : rhkdqhr90
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto
	 * @return
	 * List<CommunityDto>
	 */
	public List<CommunityDto> listFreeBoard(CommunityDto communityDto);
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
	public CommunityDto deatilFreeBoard(CommunityDto communityDto);
	/**
	 *
	 * <pre>
	 * comment       : 자유게시판 등록
	 * preMethodName :
	 * author        : csh
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto
	 * void
	 */
	public void insertFreeBoard(CommunityDto communityDto);
	/**	 *
	 * <pre>
	 * comment       : 게시물 삭제
	 * preMethodName :
	 * author        : csh
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto
	 * void
	 */
	public void deleteFreeBoard(CommunityDto communityDto);
	/**
	 * 	 * <pre>
	 * comment       : 업데이트 게시판
	 * preMethodName :
	 * author        : csh
	 * date          : 2023. 9. 4.
	 *
	 * </pre>
	 * @param communityDto
	 * void
	 */
	public void updateFreeBoard(CommunityDto communityDto);

	/**
	 * 조회수 추가
	 * @param dataId
	 */
	public void addHitCount(int Id);

}
