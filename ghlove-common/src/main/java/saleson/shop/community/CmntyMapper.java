package saleson.shop.community;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.community.doamin.CmntyBbsDto;
import saleson.shop.community.doamin.CmntyBbsRequestDto;
import saleson.shop.community.doamin.CmntyCmntDto;
import saleson.shop.community.doamin.CmntyFaqBbsCmntDto;
import saleson.shop.community.doamin.CmntyFaqBbsCmntFileDto;
import saleson.shop.community.doamin.CmntyFaqBbsDto;
import saleson.shop.community.doamin.CmntyFaqBbsFileDto;
import saleson.shop.community.doamin.CmntyFileDto;
import saleson.shop.community.doamin.CmntyOffSrBbsCmntDto;
import saleson.shop.community.doamin.CmntyOffSrBbsCmntFileDto;
import saleson.shop.community.doamin.CmntyOffSrBbsDto;
import saleson.shop.community.doamin.CmntyOffSrBbsFileDto;
import saleson.shop.community.doamin.CmntyRpstrDto;
import saleson.shop.community.doamin.CmntyRpstrRequestDto;
import saleson.shop.community.doamin.CmntySrBbsCmntDto;
import saleson.shop.community.doamin.CmntySrBbsCmntFileDto;
import saleson.shop.community.doamin.CmntySrBbsDto;
import saleson.shop.community.doamin.CmntySrBbsFileDto;

@Mapper("CmntyMapper")
public interface CmntyMapper {

	/**
	 * <pre>
	 * comment       : 조회수 증가
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	void updateInqCnt(long bbsId);

	/**
	 * <pre>
	 * comment       : 게시글 등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * void
	 */
	int addBbs(CmntyBbsDto cmntyBbsDto);

	/**
	 * <pre>
	 * comment       : 게시글 총 수
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @return
	 * int
	 */
	int countBbs(CmntyBbsRequestDto cmntyBbsRequestDto);

	/**
	 * <pre>
	 * comment       : 게시글 목록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @return
	 * List<CmntyBbsDto>
	 */
	List<CmntyBbsDto> listBbs(CmntyBbsRequestDto cmntyBbsRequestDto);

	/**
	 * <pre>
	 * comment       : 게시글 상세 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param bbsId
	 * @return
	 * CmntyBbsDto
	 */
	CmntyBbsDto detailBbs(long bbsId);

	/**
	 * <pre>
	 * comment       : 댓글 목록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param bbsId
	 * @return
	 * List<CmntyCmntDto>
	 */
	List<CmntyCmntDto> bbsCmntList(long bbsId);

	/**
	 * <pre>
	 * comment       : 게시글 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @return
	 * Integer
	 */
	Integer deleteBbs(CmntyBbsDto cmntyBbsDto);

	/**
	 * <pre>
	 * comment       : 댓글 상세
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param cmntId
	 * @return
	 * CmntyCmntDto
	 */
	CmntyCmntDto detailCmnt(long cmntId);

	/**
	 * <pre>
	 * comment       : 댓글 등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 24.
	 *
	 * </pre>
	 * @param cmntyCmntDto
	 * @return
	 * Integer
	 */
	Integer addCmnt(CmntyCmntDto cmntyCmntDto);

	/**
	 * <pre>
	 * comment       : 댓글 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param cmntyCmntDto
	 * @return
	 * Integer
	 */
	Integer deleteCmnt(CmntyCmntDto cmntyCmntDto);

	/**
	 * <pre>
	 * comment       : 댓글 내용 수정
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param cmntyCmntDto
	 * @return
	 * Integer
	 */
	Integer updateCmntCn(CmntyCmntDto cmntyCmntDto);

	/**
	 * <pre>
	 * comment       : 게시글 수정
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 25.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @return
	 * Integer
	 */
	Integer updateBbs(CmntyBbsDto cmntyBbsDto);

	/**
	 * <pre>
	 * comment       : 자료실 총 수
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 23.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * @return
	 * int
	 */
	int countRpstr(CmntyRpstrRequestDto cmntyRpstrRequestDto);

	/**
	 * <pre>
	 * comment       : 자료실 목록
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 23.
	 *
	 * </pre>
	 * @param CmntyRpstrRequestDto
	 * @return
	 * List<CmntyRpstrDto>
	 */
	List<CmntyRpstrDto> listRpstr(CmntyRpstrRequestDto cmntyRpstrRequestDto);

	/**
	 * <pre>
	 * comment       : 자료실 등록
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * @return
	 * Integer
	 */
	void insertRpstr(CmntyRpstrDto cmntyRpstr);

	/**
	 * <pre>
	 * comment       : 자료실 파일등록
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param CmntyFileDto
	 * @return
	 * Integer
	 */
	void insertRpstrFile(CmntyFileDto file);

	/**
	 * <pre>
	 * comment       : 현재 자료실  AUTO_INCREMENT 값 조회
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param
	 * @return
	 * long
	 */
	long getCurrentRpstrId();

	/**
	 * <pre>
	 * comment       : 자료실 조회수 증가
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param rpstrId
	 * @return
	 * Integer
	 */
	void updateRpstrInqCnt(long rpstrId);


	/**
	 * <pre>
	 * comment       : 자료실 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param rpstrId
	 * @return
	 * CmntyRpstrDto
	 */
	CmntyRpstrDto getRpstrDetail(long rpstrId);

	/**
	 * <pre>
	 * comment       : 자료실 첨부파일 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param
	 * @return fileId
	 * CmntyFileDto
	 */
	CmntyFileDto getRpstrFileDetail(long fileId);

	/**
	 * <pre>
	 * comment       : 자료실 상세 파일 목록
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param rpstrId
	 * @return
	 * CmntyFileDto
	 */
	List<CmntyFileDto> getRpstrfileList(long rpstrId);

	/**
	 * <pre>
	 * comment       : 자료실 글 삭제
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * @return
	 *
	 */
	void deleteRpstr(CmntyRpstrDto cmntyRpstr);

	/**
	 * <pre>
	 * comment       : 자료실 파일 삭제
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param CmntyFileDto
	 * @return
	 *
	 */
	void deleteFileByFileId(CmntyFileDto cmntyFileDto);

	/**
	 * <pre>
	 * comment       : 자료실 수정
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param CmntyFileDto
	 * @return
	 *
	 */
	void updateRpstr(CmntyRpstrDto cmntyRpstr);

	/**
	 *
	 * <pre>
	 * comment       : sr게시판 카운트
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param communityDto
	 * @return
	 * int
	 */
	public int countSrBbs(CmntySrBbsDto cmntySrBbsDto);

	/**
	 * <pre>
	 * comment       : sr게시판 게시글 목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param cmntySrBbsDto
	 * @return
	 * List<CmntySrBbsDto>
	 */
	List<CmntySrBbsDto> listSrBbs(CmntySrBbsDto cmntySrBbsDto);

	/**
	 * <pre>
	 * comment       : sr게시판 게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param cmntySrBbsDto
	 * @return
	 * int
	 */
	public int insertSrBbs(CmntySrBbsDto cmntySrBbsDto);

	/**
	 * <pre>
	 * comment       : sr게시판 게시글파일 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param cmntySrBbsDto
	 * @return
	 * int
	 */
	public int insertSrBbsFile(CmntySrBbsFileDto cmntySrBbsFileDto);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 조회수 증가
	 * preMethodName :
	 * author        :
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public void updateSrBbsInqCnt(long bbsId);


	/**
	 * 	 * <pre>
	 * comment       : sr게시글 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public CmntySrBbsDto getSrBbsDetail(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 상세 파일목록
	 * preMethodName :
	 * author        :
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public List<CmntySrBbsFileDto> getSrBbsfileList(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 첨부파일 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public CmntySrBbsFileDto getSrBbsFileDetail(long fileId);

	/**
	 * 	 * <pre>
	 * comment       :  sr게시글 첨부파일삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteSrBbsFileByFileId(CmntySrBbsFileDto cmntySrBbsFileDto);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param CmntyRpstrDto, MultipartFile[] detailImageFiles
	 * void
	 */
	public int updateSrBbs(CmntySrBbsDto cmntySrBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteSrBbs(CmntySrBbsDto cmntySrBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 삭제(첨부파일삭제처리)
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteSrBbsFileByBbsId(CmntySrBbsDto cmntySrBbsDto);

	/**
	 * <pre>
	 * comment       : 댓글 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param bbsId
	 * @return
	 * List<CmntySrBbsCmntDto>
	 */
	public List<CmntySrBbsCmntDto> selectSrBbsCmntList(long bbsId);

	/**
	 * <pre>
	 * comment       : 댓글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * Integer
	 */
	public Integer addSrBbsCmnt(CmntySrBbsCmntDto cmntySrBbsCmntDto);

	/**
	 * <pre>
	 * comment       : 댓글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * Integer
	 */
	public Integer updateSrBbsCmnt(CmntySrBbsCmntDto cmntySrBbsCmntDto);

	/**
	 * <pre>
	 * comment       : 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * Integer
	 */
	public Integer deleteSrBbsCmnt(long cmntId);

	/**
	 * <pre>
	 * comment       : 댓글 상세
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param cmntId
	 * @return
	 * CmntyCmntDto
	 */
	public CmntySrBbsCmntDto selectSrBbsCmntDetail(long cmntId);



	/**
	 * <pre>
	 * comment       : sr게시판 게시글 댓글 파일 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param CmntySrBbsCmntFileDto
	 * @return
	 * int
	 */
	public int insertSrBbsCmntFile(CmntySrBbsCmntFileDto cmntySrBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 댓글 상세 파일목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 19.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public List<CmntySrBbsCmntFileDto> getSrBbsCmntFileList(long cmntId);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 댓글 첨부파일 상세조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 19.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public CmntySrBbsCmntFileDto getSrBbsCmntFileDetail(long fileId);

	/**
	 * 	 * <pre>
	 * comment       :  sr게시글 댓글 첨부파일삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 19.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteSrBbsCmntFileByFileId(CmntySrBbsCmntFileDto cmntySrBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       :  sr게시글 댓글 삭제 시 첨부파일전체삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 22.
	 *
	 * </pre>
	 * @param cmntId
	 * void
	 */
	public void deleteSrBbsCmntFileByCmntId(CmntySrBbsCmntFileDto cmntySrBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       :  sr게시글 게시글 삭제 시 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 22.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteSrBbsCmntByBbsId(CmntySrBbsDto cmntySrBbsDto);

	/**
	 * 	 * <pre>
	 * comment       :  sr게시글 댓글 삭제 시 댓글 첨부파일전체삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 22.
	 *
	 * </pre>
	 * @param cmntId
	 * void
	 */
	public void deleteSrBbsCmntFileByBbsId(CmntySrBbsDto cmntySrBbsDto);




	/**
	 *
	 * <pre>
	 * comment       : faq게시판 카운트
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 12.
	 *
	 * </pre>
	 * @param communityDto
	 * @return
	 * int
	 */
	public int countFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 * <pre>
	 * comment       : faq게시판 게시글 목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 12.
	 *
	 * </pre>
	 * @param cmntyFaqBbsDto
	 * @return
	 * List<CmntyFaqBbsDto>
	 */
	List<CmntyFaqBbsDto> listFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 * <pre>
	 * comment       : faq게시판 게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 15.
	 *
	 * </pre>
	 * @param cmntyFaqBbsDto
	 * @return
	 * int
	 */
	public int insertFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 * <pre>
	 * comment       : faq게시판 게시글파일 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 15.
	 *
	 * </pre>
	 * @param cmntyFaqBbsDto
	 * @return
	 * int
	 */
	public int insertFaqBbsFile(CmntyFaqBbsFileDto cmntyFaqBbsFileDto);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 조회수 증가
	 * preMethodName :
	 * author        :
	 * date          : 2026. 5. 15.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public void updateFaqBbsInqCnt(long bbsId);


	/**
	 * 	 * <pre>
	 * comment       : faq게시글 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public CmntyFaqBbsDto getFaqBbsDetail(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 상세 파일목록
	 * preMethodName :
	 * author        :
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public List<CmntyFaqBbsFileDto> getFaqBbsfileList(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 첨부파일 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public CmntyFaqBbsFileDto getFaqBbsFileDetail(long fileId);

	/**
	 * 	 * <pre>
	 * comment       :  faq게시글 첨부파일삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteFaqBbsFileByFileId(CmntyFaqBbsFileDto cmntyFaqBbsFileDto);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param CmntyRpstrDto, MultipartFile[] detailImageFiles
	 * void
	 */
	public int updateFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 17.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 삭제(첨부파일삭제처리)
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 17.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteFaqBbsFileByBbsId(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 * <pre>
	 * comment       : 댓글 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 17.
	 *
	 * </pre>
	 * @param bbsId
	 * @return
	 * List<CmntyFaqBbsCmntDto>
	 */
	public List<CmntyFaqBbsCmntDto> selectFaqBbsCmntList(long bbsId);

	/**
	 * <pre>
	 * comment       : 댓글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 17.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * Integer
	 */
	public Integer addFaqBbsCmnt(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto);

	/**
	 * <pre>
	 * comment       : 댓글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 17.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * Integer
	 */
	public Integer updateFaqBbsCmnt(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto);

	/**
	 * <pre>
	 * comment       : 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 17.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * Integer
	 */
	public Integer deleteFaqBbsCmnt(long cmntId);

	/**
	 * <pre>
	 * comment       : 댓글 상세
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 17.
	 *
	 * </pre>
	 * @param cmntId
	 * @return
	 * CmntyCmntDto
	 */
	public CmntyFaqBbsCmntDto selectFaqBbsCmntDetail(long cmntId);



	/**
	 * <pre>
	 * comment       : faq게시판 게시글 댓글 파일 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 17.
	 *
	 * </pre>
	 * @param CmntyFaqBbsCmntFileDto
	 * @return
	 * int
	 */
	public int insertFaqBbsCmntFile(CmntyFaqBbsCmntFileDto cmntyFaqBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 댓글 상세 파일목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 19.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public List<CmntyFaqBbsCmntFileDto> getFaqBbsCmntFileList(long cmntId);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 댓글 첨부파일 상세조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 19.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public CmntyFaqBbsCmntFileDto getFaqBbsCmntFileDetail(long fileId);

	/**
	 * 	 * <pre>
	 * comment       :  faq게시글 댓글 첨부파일삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 19.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteFaqBbsCmntFileByFileId(CmntyFaqBbsCmntFileDto cmntyFaqBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       :  faq게시글 댓글 삭제 시 첨부파일전체삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 22.
	 *
	 * </pre>
	 * @param cmntId
	 * void
	 */
	public void deleteFaqBbsCmntFileByCmntId(CmntyFaqBbsCmntFileDto cmntyFaqBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       :  faq게시글 게시글 삭제 시 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 22.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteFaqBbsCmntByBbsId(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 * 	 * <pre>
	 * comment       :  faq게시글 댓글 삭제 시 댓글 첨부파일전체삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 22.
	 *
	 * </pre>
	 * @param cmntId
	 * void
	 */
	public void deleteFaqBbsCmntFileByBbsId(CmntyFaqBbsDto cmntyFaqBbsDto);


	/**
	 *
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 카운트
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param communityDto
	 * @return
	 * int
	 */
	public int countOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto);

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 게시글 목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyOffSrBbsDto
	 * @return
	 * List<CmntyOffSrBbsDto>
	 */
	List<CmntyOffSrBbsDto> listOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto);

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyOffSrBbsDto
	 * @return
	 * int
	 */
	public int insertOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto);

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 게시글파일 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyOffSrBbsDto
	 * @return
	 * int
	 */
	public int insertOffSrBbsFile(CmntyOffSrBbsFileDto cmntyOffSrBbsFileDto);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 조회수 증가
	 * preMethodName :
	 * author        :
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public void updateOffSrBbsInqCnt(long bbsId);


	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public CmntyOffSrBbsDto getOffSrBbsDetail(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 상세 파일목록
	 * preMethodName :
	 * author        :
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public List<CmntyOffSrBbsFileDto> getOffSrBbsfileList(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 첨부파일 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public CmntyOffSrBbsFileDto getOffSrBbsFileDetail(long fileId);

	/**
	 * 	 * <pre>
	 * comment       :  오프라인담당자 sr게시글 첨부파일삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteOffSrBbsFileByFileId(CmntyOffSrBbsFileDto cmntyOffSrBbsFileDto);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param CmntyRpstrDto, MultipartFile[] detailImageFiles
	 * void
	 */
	public int updateOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 삭제(첨부파일삭제처리)
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteOffSrBbsFileByBbsId(CmntyOffSrBbsDto cmntyOffSrBbsDto);

	/**
	 * <pre>
	 * comment       : 댓글 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param bbsId
	 * @return
	 * List<CmntyOffSrBbsCmntDto>
	 */
	public List<CmntyOffSrBbsCmntDto> selectOffSrBbsCmntList(long bbsId);

	/**
	 * <pre>
	 * comment       : 댓글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * Integer
	 */
	public Integer addOffSrBbsCmnt(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto);

	/**
	 * <pre>
	 * comment       : 댓글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * Integer
	 */
	public Integer updateOffSrBbsCmnt(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto);

	/**
	 * <pre>
	 * comment       : 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * Integer
	 */
	public Integer deleteOffSrBbsCmnt(long cmntId);

	/**
	 * <pre>
	 * comment       : 댓글 상세
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntId
	 * @return
	 * CmntyCmntDto
	 */
	public CmntyOffSrBbsCmntDto selectOffSrBbsCmntDetail(long cmntId);



	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 게시글 댓글 파일 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param CmntyOffSrBbsCmntFileDto
	 * @return
	 * int
	 */
	public int insertOffSrBbsCmntFile(CmntyOffSrBbsCmntFileDto cmntyOffSrBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 댓글 상세 파일목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public List<CmntyOffSrBbsCmntFileDto> getOffSrBbsCmntFileList(long cmntId);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 댓글 첨부파일 상세조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public CmntyOffSrBbsCmntFileDto getOffSrBbsCmntFileDetail(long fileId);

	/**
	 * 	 * <pre>
	 * comment       :  오프라인담당자 sr게시글 댓글 첨부파일삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteOffSrBbsCmntFileByFileId(CmntyOffSrBbsCmntFileDto cmntyOffSrBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       :  오프라인담당자 sr게시글 댓글 삭제 시 첨부파일전체삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntId
	 * void
	 */
	public void deleteOffSrBbsCmntFileByCmntId(CmntyOffSrBbsCmntFileDto cmntyOffSrBbsCmntFileDto);

	/**
	 * 	 * <pre>
	 * comment       :  오프라인담당자 sr게시글 게시글 삭제 시 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteOffSrBbsCmntByBbsId(CmntyOffSrBbsDto cmntyOffSrBbsDto);

	/**
	 * 	 * <pre>
	 * comment       :  오프라인담당자 sr게시글 댓글 삭제 시 댓글 첨부파일전체삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntId
	 * void
	 */
	public void deleteOffSrBbsCmntFileByBbsId(CmntyOffSrBbsDto cmntyOffSrBbsDto);
}
