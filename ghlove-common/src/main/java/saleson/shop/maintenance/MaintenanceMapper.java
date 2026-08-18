package saleson.shop.maintenance;

import java.util.List;
import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("MaintenanceMapper")
public interface MaintenanceMapper {
	/**
	 *
	 * <pre>
	 * comment       : 운영유지보수 sr게시판 카운트
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param communityDto
	 * @return
	 * int
	 */
	public int countMaintenance(MaintenanceDto maintenanceDto);

	/**
	 * <pre>
	 * comment       : 운영유지보수 sr게시판 게시글 목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param cmntySrBbsDto
	 * @return
	 * List<CmntySrBbsDto>
	 */
	List<MaintenanceDto> listMaintenance(MaintenanceDto maintenanceDto);

	/**
	 * <pre>
	 * comment       : 운영유지보수 sr게시판 게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param cmntySrBbsDto
	 * @return
	 * int
	 */
	public int insertMaintenance(MaintenanceDto maintenanceDto);

	/**
	 * <pre>
	 * comment       : 운영유지보수 sr게시판 게시글파일 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param cmntySrBbsDto
	 * @return
	 * int
	 */
	public int insertMaintenanceFile(MaintenanceFileDto maintenanceFileDto);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 조회수 증가
	 * preMethodName :
	 * author        :
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public void updateMaintenanceInqCnt(long bbsId);


	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public MaintenanceDto getMaintenanceDetail(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 상세 파일목록
	 * preMethodName :
	 * author        :
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public List<MaintenanceFileDto> getMaintenancefileList(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 첨부파일 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public MaintenanceFileDto getMaintenanceFileDetail(long fileId);

	/**
	 * 	 * <pre>
	 * comment       :  운영유지보수 sr게시글 첨부파일삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteMaintenanceFileByFileId(MaintenanceFileDto maintenanceFileDto);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param CmntyRpstrDto, MultipartFile[] detailImageFiles
	 * void
	 */
	public int updateMaintenance(MaintenanceDto maintenanceDto);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteMaintenance(MaintenanceDto maintenanceDto);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 삭제(첨부파일삭제처리)
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteMaintenanceFileByBbsId(MaintenanceDto maintenanceDto);


}
