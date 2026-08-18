package saleson.shop.community.locgovdataboard;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;


import saleson.shop.databoard.domain.Databoard;
import saleson.shop.databoard.domain.DataboardFile;
import saleson.shop.databoard.support.DataboardParam;

@Mapper("locgovDataBoardMapperr")
public interface LocgovDataBoardMapper {

	/**
	 * 자료실 카운트
	 * @param databoardParam
	 * @return
	 */
	int getDataboardCount(DataboardParam databoardParam);

	/**
	 * 자료실 리스트(사용자)
	 * @param databoardParam
	 * @return
	 */
	List<Databoard> getDataboardList(DataboardParam databoardParam);

	/**
	 * 자료실 조회
	 * @param dataId
	 * @return
	 */
	Databoard getDataboard(int dataId);
	void insertDataboard(Databoard databoard);
	void insertDataboardFile(DataboardFile databoardFile);
	void updateDataboard(Databoard databoard);
	void deleteDataboard(int dataId);

	/**
	 * 조회수 추가
	 * @param dataId
	 */
	void addHitCount(int dataId);

	List<Databoard> getFrontDataboardList(DataboardParam databoardParam);

	int getFrontDataboardListCount(DataboardParam databoardParam);

	/**
	 * 카테고리 팀코드에 해당하는 자료실 목록을 가져옴. (메인, 팀 용)
	 * @param databoardParam
	 * @return
	 */
	List<Databoard> getFrontDataboardListByTeamCodes(DataboardParam databoardParam);

	int databoardListDelete(int dataId);

	/**
	 * 자료실 파일 목록 조회 (사용자)
	 * @param dataId
	 * @return
	 */
	List<DataboardFile> getFrontDataboardFileList(Integer dataId);

	/**
	 * 자료실 파일 조회
	 * @param dataFileId
	 * @return
	 */
	DataboardFile getFrontDataboardFileDetail(Integer dataFileId);

	void deleteDataboardFile(Integer dataFileId);

}
