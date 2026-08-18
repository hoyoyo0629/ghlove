package saleson.shop.community.locgovdataboard;

import java.util.List;

import com.onlinepowers.framework.exception.UserException;

import saleson.shop.databoard.domain.Databoard;
import saleson.shop.databoard.domain.DataboardFile;
import saleson.shop.databoard.support.DataboardParam;

public interface LocgovDataBoardService {

	/**
	 * 자료실 카운트
	 * @param databoardParam
	 * @return
	 */
	public int getDataboardCount(DataboardParam databoardParam);

	/**
	 * 자료실 리스트(사용자)
	 * @param databoardParam
	 * @return
	 */
	public List<Databoard> getDataboardList(DataboardParam databoardParam);

	/**
	 * 자료실 조회
	 * @param dataId
	 * @return
	 */
	public Databoard getDataboard(int dataId);
	public void insertDataboard(Databoard databoard);
	public void updateDataboard(Databoard databoard);
	public void deleteDataboard(int dataId);
	public List<Databoard> getFrontDataboardList(DataboardParam databoardParam);

	public int getFrontDataboardListCount(DataboardParam databoardParam);

	/**
	 * 조회수 추가
	 * @param dataId
	 */
	public void addHitCount(int dataId);

	/**
	 * 카테고리 팀코드에 해당하는 공지사항 목록을 가져옴. (메인, 팀 용)
	 * @param databoardParam
	 * @return
	 */
	public List<Databoard> getFrontDataboardListByTeamCodes(DataboardParam databoardParam);

	public void deleteDataboardSeller(int databoardSellerId);
	/**
	 * 리스트 삭제
	 * @param databoardParam
	 * @return
	 */
	public String databoardListDelete(DataboardParam databoardParam) throws UserException;

	/**
	 * 자료실 상세 (사용자)
	 * @param dataId
	 * @return
	 */
	public Databoard getFrontDataboardDetail(Integer dataId);

	/**
	 * 자료실 파일 목록 조회 (사용자)
	 * @param dataId
	 * @return
	 */
	public List<DataboardFile> getFrontDataboardFileList(Integer dataId);

	/**
	 * 자료실 파일 조회
	 * @param dataFileId
	 * @return
	 */
	public DataboardFile getFrontDataboardFileDetail(Integer dataFileId);

	/**
	 * 첨부파일 삭제
	 * @param dataFileId
	 * @return
	 */
	public void deleteItemImageByItemId(Integer dataFileId);
}
