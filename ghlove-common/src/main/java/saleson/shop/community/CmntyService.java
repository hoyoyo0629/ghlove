package saleson.shop.community;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.context.RequestContext;

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

public interface CmntyService {


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
	public int countBbs(CmntyBbsRequestDto cmntyBbsRequestDto);

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
	public List<CmntyBbsDto> listBbs(CmntyBbsRequestDto cmntyBbsRequestDto);
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
	public CmntyBbsDto detailBbs(RequestContext requestContext, long bbsId, Model model, String type);

	/**
	 * <pre>
	 * comment       : 조회수 증가
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 25.
	 *
	 * </pre>
	 * @param Id
	 * void
	 */
	public void updateInqCnt(long Id);

	/**
	 * <pre>
	 * comment       : 코멘트 목록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 17.
	 *
	 * </pre>
	 * @param bbsId
	 * @return
	 * List<CmntyCmntDto>
	 */
	public List<CmntyCmntDto> bbsCmntList(long bbsId);

	/**
	 * <pre>
	 * comment       : 댓글 등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 24.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * Integer
	 */
	public Integer addCmnt(CmntyCmntDto cmntyCmntDto, RequestContext requestContext);

	/**
	 * <pre>
	 * comment       : 게시글 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public Integer deleteBbs(RequestContext requestContext, long bbsId);

	/**
	 * <pre>
	 * comment       : 댓글 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * Integer
	 */
	public Integer deleteCmnt(RequestContext requestContext, long cmntId);


	/**
	 * <pre>
	 * comment       : 댓글 수정
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * Integer
	 */
	public Integer updateCmntCn(CmntyCmntDto cmntyCmntDto, RequestContext requestContext);

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
	public Integer addBbs(CmntyBbsDto cmntyBbsDto, RequestContext requestContext) throws UnsupportedEncodingException;

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
	public Integer updateBbs(CmntyBbsDto cmntyBbsDto, RequestContext requestContext) throws UnsupportedEncodingException;

	/**
	 * 	 * <pre>
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
	public int countRpstr(CmntyRpstrRequestDto cmntyRpstrRequestDto);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 목록
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 23.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 *  @return
	 * List<CmntyRpstrDto>
	 */
	public List<CmntyRpstrDto> listRpstr(CmntyRpstrRequestDto cmntyRpstrRequestDto);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 등록
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void insertRpstr(CmntyRpstrDto cmntyRpstr, MultipartFile[] detailImageFiles);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 조회수 증가
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param rpstrId
	 * void
	 */
	public void updateRpstrInqCnt(long rpstrId);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param rpstrId
	 * void
	 */
	public CmntyRpstrDto getRpstrDetail(long rpstrId, Model model);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 상세 파일목록
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param rpstrId
	 * void
	 */
	public List<CmntyFileDto> getRpstrfileList(long rpstrId);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 첨부파일 상세조회
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public CmntyFileDto getRpstrFileDetail(long fileId);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 삭제
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param rpstrId
	 * void
	 */
	public void deleteRpstr(long rpstrId);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 파일삭제
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteFileByFileId(long fileId);

	/**
	 * 	 * <pre>
	 * comment       : 자료실 수정
	 * preMethodName :
	 * author        :
	 * date          : 2024. 7. 22.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void updateRpstr(CmntyRpstrDto cmntyRpstr, MultipartFile[] detailImageFiles);

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
	 *
	 * <pre>
	 * comment       : sr게시판 리스트
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param cmntySrBbsDto
	 * @return
	 * List<CmntySrBbsDto>
	 */
	public List<CmntySrBbsDto> listSrBbs(CmntySrBbsDto cmntySrBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void insertSrBbs(CmntySrBbsDto cmntySrBbsDto, MultipartFile[] detailImageFiles);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 조회수 증가
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param rpstrId
	 * void
	 */
	public void updateSrBbsInqCnt(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 상세조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public CmntySrBbsDto getSrBbsDetail(long bbsId, Model model);

	/**
	 * 	 * <pre>
	 * comment       : sr게시글 상세 파일목록
	 * preMethodName :
	 * author        : ucubepym
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
	 * author        : ucubepym
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
	public void deleteSrBbsFileByFileId(long fileId);


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
	public void updateSrBbs(CmntySrBbsDto cmntySrBbsDto, MultipartFile[] detailImageFiles);

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
	public void deleteSrBbs(long bbsId);

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
	public Integer addSrBbsCmnt(CmntySrBbsCmntDto cmntySrBbsCmntDto, RequestContext requestContext);

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
	public Integer updateSrBbsCmnt(CmntySrBbsCmntDto cmntySrBbsCmntDto, RequestContext requestContext);

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
	public Integer deleteSrBbsCmnt(RequestContext requestContext, long cmntId);


	/**
	 * <pre>
	 * comment       : 댓글 파일 저장
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param CmntySrBbsCmntDto
	 * @param MultipartFile
	 * @return
	 * Integer
	 */
	public void fileUploadHandlerSrBbsCmnt(CmntySrBbsCmntDto cmntySrBbsCmntDto, MultipartFile[] detailImageFiles);

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
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param communityDto
	 * @return
	 * int
	 */
	public int countFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 *
	 * <pre>
	 * comment       : faq게시판 리스트
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param cmntyFaqBbsDto
	 * @return
	 * List<CmntyFaqBbsDto>
	 */
	public List<CmntyFaqBbsDto> listFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void insertFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto, MultipartFile[] detailImageFiles);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 조회수 증가
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param rpstrId
	 * void
	 */
	public void updateFaqBbsInqCnt(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 상세조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public CmntyFaqBbsDto getFaqBbsDetail(long bbsId, Model model);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 상세 파일목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
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
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
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
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param fileId
	 * void
	 */
	public void deleteFaqBbsFileByFileId(long fileId);


	/**
	 * 	 * <pre>
	 * comment       : faq게시글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param CmntyRpstrDto, MultipartFile[] detailImageFiles
	 * void
	 */
	public void updateFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto, MultipartFile[] detailImageFiles);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void deleteFaqBbs(long bbsId);

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
	 * List<CmntyFaqBbsCmntDto>
	 */
	public List<CmntyFaqBbsCmntDto> selectFaqBbsCmntList(long bbsId);

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
	public Integer addFaqBbsCmnt(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, RequestContext requestContext);

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
	public Integer updateFaqBbsCmnt(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, RequestContext requestContext);

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
	public Integer deleteFaqBbsCmnt(RequestContext requestContext, long cmntId);


	/**
	 * <pre>
	 * comment       : 댓글 파일 저장
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 17.
	 *
	 * </pre>
	 * @param CmntyFaqBbsCmntDto
	 * @param MultipartFile
	 * @return
	 * Integer
	 */
	public void fileUploadHandlerFaqBbsCmnt(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, MultipartFile[] detailImageFiles);

	/**
	 * 	 * <pre>
	 * comment       : faq게시글 댓글 상세 파일목록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 19.
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
	 * date          : 2025. 9. 19.
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
	 * date          : 2025. 9. 19.
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
	 * date          : 2025. 9. 22.
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
	 * date          : 2025. 9. 22.
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
	 * date          : 2025. 9. 22.
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
	 *
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 리스트
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyOffSrBbsDto
	 * @return
	 * List<CmntyOffSrBbsDto>
	 */
	public List<CmntyOffSrBbsDto> listOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param CmntyRpstrDto
	 * void
	 */
	public void insertOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto, MultipartFile[] detailImageFiles);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 조회수 증가
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param rpstrId
	 * void
	 */
	public void updateOffSrBbsInqCnt(long bbsId);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 상세조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param bbsId
	 * void
	 */
	public CmntyOffSrBbsDto getOffSrBbsDetail(long bbsId, Model model);

	/**
	 * 	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 상세 파일목록
	 * preMethodName :
	 * author        : ucubepym
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
	 * author        : ucubepym
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
	public void deleteOffSrBbsFileByFileId(long fileId);


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
	public void updateOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto, MultipartFile[] detailImageFiles);

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
	public void deleteOffSrBbs(long bbsId);

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
	public Integer addOffSrBbsCmnt(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, RequestContext requestContext);

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
	public Integer updateOffSrBbsCmnt(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, RequestContext requestContext);

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
	public Integer deleteOffSrBbsCmnt(RequestContext requestContext, long cmntId);


	/**
	 * <pre>
	 * comment       : 댓글 파일 저장
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param CmntyOffSrBbsCmntDto
	 * @param MultipartFile
	 * @return
	 * Integer
	 */
	public void fileUploadHandlerOffSrBbsCmnt(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, MultipartFile[] detailImageFiles);

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
