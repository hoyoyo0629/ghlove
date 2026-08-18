package saleson.shop.maintenance;

import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface MaintenanceService {

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
	 *
	 * <pre>
	 * comment       : 운영유지보수 sr게시판 리스트
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param cmntySrBbsDto
	 * @return
	 * List<CmntySrBbsDto>
	 */
	public List<MaintenanceDto> listMaintenance(MaintenanceDto maintenanceDto);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void insertMaintenance(MaintenanceDto maintenanceDto, MultipartFile[] detailImageFiles);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 조회수 증가
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param rpstrId
	 * void
	 */
	public void updateMaintenanceInqCnt(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 상세조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public MaintenanceDto getMaintenanceDetail(long bbsId, Model model);

	/**
	 * 	 * <pre>
	 * comment       : 운영유지보수 sr게시글 상세 파일목록
	 * preMethodName :
	 * author        : ucubepym
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
	 * author        : ucubepym
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
	public void deleteMaintenanceFileByFileId(long fileId);


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
	public void updateMaintenance(MaintenanceDto maintenanceDto, MultipartFile[] detailImageFiles);

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
	public void deleteMaintenance(long bbsId);


	/**
	 * 스트리밍 엑셀다운로드
	 *
	 * @param	orderParam
	 * @throws	Exception
	 */
	public SXSSFWorkbook streamAllMaintenanceData(MaintenanceDto maintenanceDto);


}
