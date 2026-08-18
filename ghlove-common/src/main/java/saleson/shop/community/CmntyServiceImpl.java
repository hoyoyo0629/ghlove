package saleson.shop.community;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;

import saleson.api.common.exception.ApiException;
import saleson.common.Const;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.UserUtils;
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


@Service("cmntyService")
public class CmntyServiceImpl implements CmntyService {

	private static final Logger log = LoggerFactory.getLogger(CmntyServiceImpl.class);

	@Autowired
	private CmntyMapper cmntyMapper;

	@Autowired
    private FileService fileService;

	@Autowired
    private FileStorage fileStorage;

	@Override
	public int countBbs(CmntyBbsRequestDto cmntyBbsRequestDto) {
		return cmntyMapper.countBbs(cmntyBbsRequestDto);
	}

	@Override
	public List<CmntyBbsDto> listBbs(CmntyBbsRequestDto cmntyBbsRequestDto) {
		return cmntyMapper.listBbs(cmntyBbsRequestDto);
	}

	@Override
	public void updateInqCnt(long bbsId) {
		cmntyMapper.updateInqCnt(bbsId);
	}

	@Override
	public CmntyBbsDto detailBbs(RequestContext requestContext, long bbsId, Model model, String type) {

		//조회수 증가
		cmntyMapper.updateInqCnt(bbsId);

		//댓글 가져오기
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

			//{ROLE_ADMIN_1:시스템주담당자, ROLE_ADMIN_2:시스템부담당자, ROLE_ADMIN_3:행안부주담당자, ROLE_ADMIN_4:행안부부담당자, ROLE_ADMIN_5:지자체주담당자, ROLE_ADMIN_6:지자체부담당자}
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority()) || "ROLE_ADMIN_3".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}

		long userId = requestContext.getUser().getUserId();

		CmntyBbsDto detail = cmntyMapper.detailBbs(bbsId);
		if(detail.getIsSecret().equalsIgnoreCase("Y")) { //게시글의 비밀글 여부가 Y 이면
			if( !(role.equalsIgnoreCase("ROLE_ADMIN_1") || role.equalsIgnoreCase("ROLE_ADMIN_2"))) { // 시스템 관리자는 확인 가능
				if(userId != detail.getFrstCrtId()) { //
					//throw new ApiException("비밀글은 작성자 본인만 확인가능합니다.");
					detail.setIsSecretYn("Y");
				} else { // 본인은 확인 가능
					detail.setIsSecretYn("N");
				}
			} else { // 관리자는 확인 가능
				detail.setIsSecretYn("N");
			}
		} else {
			detail.setIsSecretYn("N");
		}



		if(type.equals("detail")) {
			//댓글 가져오기
			List<CmntyCmntDto> bbsCmntList = cmntyMapper.bbsCmntList(bbsId);
			model.addAttribute("bbsCmntList", bbsCmntList);
			model.addAttribute("bbsCmntListCnt", bbsCmntList.size());
		}
		model.addAttribute("detail", detail);
		model.addAttribute("role",role);
		model.addAttribute("userId",user.getUserId());

		return detail;
	}


	@Override
	public List<CmntyCmntDto> bbsCmntList(long bbsId) {
		return cmntyMapper.bbsCmntList(bbsId);
	}

	@Override
	@Transactional
	public Integer deleteBbs(RequestContext requestContext, long bbsId) {
		long userId = requestContext.getUser().getUserId();
	    CmntyBbsDto detail= cmntyMapper.detailBbs(bbsId);
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 게시글만 삭제 가능합니다.");

		return cmntyMapper.deleteBbs(detail);
	}

	@Override
	@Transactional
	public Integer addCmnt(CmntyCmntDto cmntyCmntDto, RequestContext requestContext) {
		long userId = requestContext.getUser().getUserId();
		cmntyCmntDto.setFrstCrtId(userId);
		return cmntyMapper.addCmnt(cmntyCmntDto);
	}

	@Override
	@Transactional
	public Integer deleteCmnt(RequestContext requestContext, long cmntId) {
		long userId = requestContext.getUser().getUserId();
	    CmntyCmntDto detail= cmntyMapper.detailCmnt(cmntId);
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 댓글만 삭제 가능합니다.");

		return cmntyMapper.deleteCmnt(detail);
	}


	@Override
	@Transactional
	public Integer updateCmntCn(CmntyCmntDto cmntyCmntDto, RequestContext requestContext) {
		long userId = requestContext.getUser().getUserId();
	    CmntyCmntDto detail= cmntyMapper.detailCmnt(cmntyCmntDto.getCmntId());
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 댓글만 수정 가능합니다.");

		detail.setCmntCn(cmntyCmntDto.getCmntCn());
		return cmntyMapper.updateCmntCn(detail);
	}

	@Override
	public Integer addBbs(CmntyBbsDto cmntyBbsDto, RequestContext requestContext) throws UnsupportedEncodingException {
		String bbsCn = URLDecoder.decode(cmntyBbsDto.getBbsCn(), "UTF-8");
		cmntyBbsDto.setBbsCn(bbsCn);
		cmntyBbsDto.setFrstCrtId(requestContext.getUser().getUserId());
		return cmntyMapper.addBbs(cmntyBbsDto);
	}

	@Override
	@Transactional
	public Integer updateBbs(CmntyBbsDto cmntyBbsDto, RequestContext requestContext) throws UnsupportedEncodingException {
		long userId = requestContext.getUser().getUserId();
	    CmntyBbsDto detail= cmntyMapper.detailBbs(cmntyBbsDto.getBbsId());
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 게시글만 수정 가능합니다.");
		String bbsCn = URLDecoder.decode(cmntyBbsDto.getBbsCn(), "UTF-8");
		cmntyBbsDto.setBbsCn(bbsCn);

		return cmntyMapper.updateBbs(cmntyBbsDto);

	}

	@Override
	public int countRpstr(CmntyRpstrRequestDto cmntyRpstrRequestDto) {
		// 자료실 카운트
		return cmntyMapper.countRpstr(cmntyRpstrRequestDto);
	}

	@Override
	public List<CmntyRpstrDto> listRpstr(CmntyRpstrRequestDto cmntyRpstrRequestDto){
		// 자료실 목록
		return cmntyMapper.listRpstr(cmntyRpstrRequestDto);
	}

	@Override
	public void insertRpstr(CmntyRpstrDto cmntyRpstr, MultipartFile[] detailImageFiles) {

		//등록자 아이디
		cmntyRpstr.setFrstCrtId(UserUtils.getUser().getUserId());

		//공지여부 null 체크
		if( cmntyRpstr.getNoticeYn() == null ||cmntyRpstr.getNoticeYn().isBlank()) {
			cmntyRpstr.setNoticeYn("N");
		}

		// 자료실 등록
		cmntyMapper.insertRpstr(cmntyRpstr);

		// 자료실 currentId_value
//		cmntyRpstr.setRpstrId(cmntyMapper.getCurrentRpstrId());

		// 파일 업로드
		fileUploadHandler(cmntyRpstr,detailImageFiles);
	}

	@Override
	public void updateRpstrInqCnt(long rpstrId) {
		cmntyMapper.updateRpstrInqCnt(rpstrId);
	}

	@Override
	public CmntyRpstrDto getRpstrDetail(long rpstrId, Model model) {

		CmntyRpstrDto detail = cmntyMapper.getRpstrDetail(rpstrId);


		//권한 가져오기
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

			//	ROLE_ADMIN_1	시스템주담당자 / ROLE_ADMIN_2	시스템부담당자
			//	ROLE_ADMIN_3	행안부주담당자 / ROLE_ADMIN_4	행안부부담당자
			//	ROLE_ADMIN_5	지자체주담당자 / ROLE_ADMIN_6	지자체부담당자 / ROLE_ADMIN_10	지정기부담당자

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
			}
		}

		if(detail.getAtchFileSz() != null) {
			String filesize = "";
			long bytes = detail.getAtchFileSz();

			if(bytes < 1024) {
				filesize = String.valueOf(bytes) + "B";
			} else if (bytes >= 1024 && bytes <= 1024 * 1024) {
				filesize = String.valueOf(bytes/1024) + "KB";
			} else {
				filesize = String.valueOf(bytes/1024/1024) + "MB";
			}
			model.addAttribute("filesize", filesize);
		}

		model.addAttribute("fileList", getRpstrfileList(rpstrId));
		model.addAttribute("role", role);
		model.addAttribute("userId", UserUtils.getUser().getUserId());
		return detail;
	}

	@Override
	public List<CmntyFileDto> getRpstrfileList(long rpstrId) {
		return cmntyMapper.getRpstrfileList(rpstrId);
	}

	@Override
	public CmntyFileDto getRpstrFileDetail(long fileId) {
		return cmntyMapper.getRpstrFileDetail(fileId);
	}

	@Override
	public void deleteRpstr(long rpstrId) {

		long userId = UserUtils.getUser().getUserId();

		CmntyRpstrDto cmntyRpstr = new CmntyRpstrDto();
		cmntyRpstr.setRpstrId(rpstrId);
		cmntyRpstr.setLastMdfcnId(userId);

		cmntyMapper.deleteRpstr(cmntyRpstr);
	}

	@Override
	public void deleteFileByFileId(long fileId) {

		long userId = UserUtils.getUser().getUserId();

		CmntyFileDto cmntyFile = new CmntyFileDto();
		cmntyFile.setFileId(fileId);
		cmntyFile.setLastMdfcnId(userId);

		cmntyMapper.deleteFileByFileId(cmntyFile);
	}

	public void fileUploadHandler(CmntyRpstrDto cmntyRpstr, MultipartFile[] detailImageFiles) {

		if (detailImageFiles != null) {

			for (MultipartFile multipartFile : detailImageFiles) {
				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String fileName = multipartFile.getOriginalFilename();
					int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

					final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "hwpx", "zip", "7z"};

					boolean extenstion_check = false;

					for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
						if (extension.equals(AVAILABLE_EXTENSION[i])) {
							extenstion_check = true;
						}
					}

					StringBuilder saveFileName = new StringBuilder();
					String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
					saveFileName.setLength(0);
				    saveFileName.append(dateNanoTime);
				    saveFileName.append("_").append(multipartFile.getOriginalFilename());

				    // 1. 업로드 경로설정
				    StringBuilder uploadPath = new StringBuilder();

				    if (extenstion_check) {
						if (maxSize > multipartFile.getSize()) {

				            uploadPath
				                    .append(cmntyRpstr.getUploadPath())
				                    .append(File.separator);

							fileService.makeUploadPath(uploadPath.toString());

							// 2. 저장될 파일
							// 시큐어코딩 CWE-434 처리

							//대소문자 해제
							String fileToLowerCase= fileName.toLowerCase();

							if(fileToLowerCase.endsWith(".jpg")
									|| fileToLowerCase.endsWith(".jpeg")
									|| fileToLowerCase.endsWith(".gif")
									|| fileToLowerCase.endsWith(".bmp")
									|| fileToLowerCase.endsWith(".png")
									|| fileToLowerCase.endsWith(".doc")
									|| fileToLowerCase.endsWith(".docx")
									|| fileToLowerCase.endsWith(".xls")
									|| fileToLowerCase.endsWith(".xlsx")
									|| fileToLowerCase.endsWith(".ppt")
									|| fileToLowerCase.endsWith(".pptx")
									|| fileToLowerCase.endsWith(".pdf")
									|| fileToLowerCase.endsWith(".tif")
									|| fileToLowerCase.endsWith(".tiff")
									|| fileToLowerCase.endsWith(".hwp")
									|| fileToLowerCase.endsWith(".hwpx")
									|| fileToLowerCase.endsWith(".zip")
									|| fileToLowerCase.endsWith(".7z")
									) {

								File saveFile = new File(uploadPath + File.separator + saveFileName);

								try {
									fileStorage.upload(multipartFile.getBytes(), saveFile);
								} catch (IOException e) {
									log.error("ERROR: {}", e.getMessage(), e);
								}
							} else {
								throw new UserException("유효하지 않은 파일입니다.");
							}

						} else {
	                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
						}
					} else {
						throw new UserException("유효하지 않은 파일입니다.");
					}

				    // 자료실 File 등록
					CmntyFileDto file = new CmntyFileDto();

	                file.setRpstrId(cmntyRpstr.getRpstrId());
					file.setOrgnlAtchFileNm(fileName);
	                file.setAtchFileNm(saveFileName.toString());
	                file.setAtchFileExtnNm(extension);
	                file.setAtchFilePathNm(uploadPath.toString());
	                file.setAtchFileSz(multipartFile.getSize());
	                file.setFrstCrtId(UserUtils.getUser().getUserId());

	                cmntyMapper.insertRpstrFile(file);
				}
			}
		}
	}

	@Override
	public void updateRpstr(CmntyRpstrDto cmntyRpstr, MultipartFile[] detailImageFiles) {

		//수정자 아이디
		long userId = UserUtils.getUser().getUserId();
		cmntyRpstr.setLastMdfcnId(userId);

		//공지여부 null 체크
		if( cmntyRpstr.getNoticeYn() == null ||cmntyRpstr.getNoticeYn().isBlank()) {
			cmntyRpstr.setNoticeYn("N");
		}

		//자료실 수정
		cmntyMapper.updateRpstr(cmntyRpstr);

		// 파일 업로드
		fileUploadHandler(cmntyRpstr,detailImageFiles);

	}

	//게시글수 조회
	@Override
	public int countSrBbs(CmntySrBbsDto cmntySrBbsDto) {
		return cmntyMapper.countSrBbs(cmntySrBbsDto);
	}

	//sr게시글 목록 조회
	@Override
	public List<CmntySrBbsDto> listSrBbs(CmntySrBbsDto cmntySrBbsDto) {
		return cmntyMapper.listSrBbs(cmntySrBbsDto);
	}

	//sr게시글 등록
	@Override
	public void insertSrBbs(CmntySrBbsDto cmntySrBbsDto, MultipartFile[] detailImageFiles) {

		//등록자 아이디
		cmntySrBbsDto.setFrstCrtId(UserUtils.getUser().getUserId());

		// 게시글 등록
		cmntyMapper.insertSrBbs(cmntySrBbsDto);

		// 파일 업로드
		fileUploadHandlerSrBbs(cmntySrBbsDto, detailImageFiles);
	}


	//파일업로드
	public void fileUploadHandlerSrBbs(CmntySrBbsDto cmntySrBbsDto, MultipartFile[] detailImageFiles) {

		if (detailImageFiles != null) {

			for (MultipartFile multipartFile : detailImageFiles) {
				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String fileName = multipartFile.getOriginalFilename();
					int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

					final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "hwpx", "zip", "7z"};

					boolean extenstion_check = false;

					for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
						if (extension.equals(AVAILABLE_EXTENSION[i])) {
							extenstion_check = true;
						}
					}

					StringBuilder saveFileName = new StringBuilder();
					String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
					saveFileName.setLength(0);
				    saveFileName.append(dateNanoTime);
				    saveFileName.append("_").append(multipartFile.getOriginalFilename());

				    // 1. 업로드 경로설정
				    StringBuilder uploadPath = new StringBuilder();

				    if (extenstion_check) {
						if (maxSize > multipartFile.getSize()) {

				            uploadPath
				                    .append(cmntySrBbsDto.getUploadPath())
				                    .append(File.separator);

							fileService.makeUploadPath(uploadPath.toString());

							// 2. 저장될 파일
							// 시큐어코딩 CWE-434 처리

							//대소문자 해제
							String fileToLowerCase= fileName.toLowerCase();

							if(fileToLowerCase.endsWith(".jpg")
									|| fileToLowerCase.endsWith(".jpeg")
									|| fileToLowerCase.endsWith(".gif")
									|| fileToLowerCase.endsWith(".bmp")
									|| fileToLowerCase.endsWith(".png")
									|| fileToLowerCase.endsWith(".doc")
									|| fileToLowerCase.endsWith(".docx")
									|| fileToLowerCase.endsWith(".xls")
									|| fileToLowerCase.endsWith(".xlsx")
									|| fileToLowerCase.endsWith(".ppt")
									|| fileToLowerCase.endsWith(".pptx")
									|| fileToLowerCase.endsWith(".pdf")
									|| fileToLowerCase.endsWith(".tif")
									|| fileToLowerCase.endsWith(".tiff")
									|| fileToLowerCase.endsWith(".hwp")
									|| fileToLowerCase.endsWith(".hwpx")
									|| fileToLowerCase.endsWith(".zip")
									|| fileToLowerCase.endsWith(".7z")
									) {

								File saveFile = new File(uploadPath + File.separator + saveFileName);

								try {
									fileStorage.upload(multipartFile.getBytes(), saveFile);
								} catch (IOException e) {
									log.error("ERROR: {}", e.getMessage(), e);
								}
							} else {
								throw new UserException("유효하지 않은 파일입니다.");
							}

						} else {
	                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
						}
					} else {
						throw new UserException("유효하지 않은 파일입니다.");
					}

				    // 자료실 File 등록
				    CmntySrBbsFileDto file = new CmntySrBbsFileDto();

	                file.setBbsId(cmntySrBbsDto.getBbsId());
					file.setOrgnlAtchFileNm(fileName);
	                file.setAtchFileNm(saveFileName.toString());
	                file.setAtchFileExtnNm(extension);
	                file.setAtchFilePathNm(uploadPath.toString());
	                file.setAtchFileSz(multipartFile.getSize());
	                file.setFrstCrtId(UserUtils.getUser().getUserId());

	                cmntyMapper.insertSrBbsFile(file);
				}
			}
		}
	}


	//sr게시글 조회수 증가
	@Override
	public void updateSrBbsInqCnt(long bbsId) {
		cmntyMapper.updateSrBbsInqCnt(bbsId);
	}


	//sr게시글 상세 조회
	@Override
	public CmntySrBbsDto getSrBbsDetail(long bbsId, Model model) {

		CmntySrBbsDto detail = cmntyMapper.getSrBbsDetail(bbsId);

		//권한 가져오기
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

			//	ROLE_ADMIN_1	시스템주담당자 / ROLE_ADMIN_2	시스템부담당자
			//	ROLE_ADMIN_3	행안부주담당자 / ROLE_ADMIN_4	행안부부담당자
			//	ROLE_ADMIN_5	지자체주담당자 / ROLE_ADMIN_6	지자체부담당자 / ROLE_ADMIN_10	지정기부담당자

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
			}
		}
		detail.setIsSecretYn("N");
		//게시글의 비밀글 여부가 Y 이면
		if(detail.getIsSecret().equalsIgnoreCase("Y")) {
			detail.setIsSecretYn("Y");

			// 시스템 관리자는 확인 가능
			if((role.equalsIgnoreCase("ROLE_ADMIN_1") || role.equalsIgnoreCase("ROLE_ADMIN_2"))) {
				detail.setIsSecretYn("N");
			}

			// 본인 확인 가능
			if(user.getUserId() ==  detail.getFrstCrtId()) {
				detail.setIsSecretYn("N");
			}
		}

		//첨부파일 포맷
		if(detail.getAtchFileSz() != null) {
			String filesize = "";
			long bytes = detail.getAtchFileSz();

			if(bytes < 1024) {
				filesize = String.valueOf(bytes) + "B";
			} else if (bytes >= 1024 && bytes <= 1024 * 1024) {
				filesize = String.valueOf(bytes/1024) + "KB";
			} else {
				filesize = String.valueOf(bytes/1024/1024) + "MB";
			}
			model.addAttribute("filesize", filesize);
		}

		model.addAttribute("fileList", getSrBbsfileList(bbsId));
		model.addAttribute("role", role);
		model.addAttribute("userId", UserUtils.getUser().getUserId());
		return detail;
	}

	@Override
	public List<CmntySrBbsFileDto> getSrBbsfileList(long bbsId) {
		return cmntyMapper.getSrBbsfileList(bbsId);
	}


	@Override
	public CmntySrBbsFileDto getSrBbsFileDetail(long fileId) {
		return cmntyMapper.getSrBbsFileDetail(fileId);
	}

	@Override
	public void deleteSrBbsFileByFileId(long fileId) {

		long userId = UserUtils.getUser().getUserId();

		CmntySrBbsFileDto cmntySrBbsFileDto = new CmntySrBbsFileDto();
		cmntySrBbsFileDto.setFileId(fileId);
		cmntySrBbsFileDto.setLastMdfcnId(userId);

		cmntyMapper.deleteSrBbsFileByFileId(cmntySrBbsFileDto);
	}

	@Override
	public void updateSrBbs(CmntySrBbsDto cmntySrBbsDto, @RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles) {

		/*
		 * if(cmntySrBbsDto.getNoticeYn().isEmpty()) { cmntySrBbsDto.setNoticeYn("N"); }
		 *
		 * if(cmntySrBbsDto.getIsSecretYn().isEmpty()) { cmntySrBbsDto.setIsSecret("N");
		 * }
		 */
		//수정자 아이디
		long userId = UserUtils.getUser().getUserId();
		cmntySrBbsDto.setLastMdfcnId(userId);

		//게시글 수정
		cmntyMapper.updateSrBbs(cmntySrBbsDto);

		// 파일 업로드
		fileUploadHandlerSrBbs(cmntySrBbsDto ,detailImageFiles);

	}

	@Override
	@Transactional
	public void deleteSrBbs(long bbsId) {

		long userId = UserUtils.getUser().getUserId();

		CmntySrBbsDto detail= cmntyMapper.getSrBbsDetail(bbsId);
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 게시글만 삭제 가능합니다.");

		CmntySrBbsDto cmntySrBbsDto = new CmntySrBbsDto();
		cmntySrBbsDto.setBbsId(bbsId);
		cmntySrBbsDto.setLastMdfcnId(userId);

		//게시글 댓글 파일 삭제
		cmntyMapper.deleteSrBbsCmntFileByBbsId(cmntySrBbsDto);

		//게시글 댓글 삭제
		cmntyMapper.deleteSrBbsCmntByBbsId(cmntySrBbsDto);

		//게시글 첨부파일 삭제
		cmntyMapper.deleteSrBbsFileByBbsId(cmntySrBbsDto);

		//게시글 삭제
		cmntyMapper.deleteSrBbs(cmntySrBbsDto);

	}

	@Override
	public List<CmntySrBbsCmntDto> selectSrBbsCmntList(long bbsId) {
		return cmntyMapper.selectSrBbsCmntList(bbsId);
	}

	@Override
	public Integer addSrBbsCmnt(CmntySrBbsCmntDto cmntySrBbsCmntDto, RequestContext requestContext) {
		long userId = requestContext.getUser().getUserId();
		cmntySrBbsCmntDto.setFrstCrtId(userId);

		return cmntyMapper.addSrBbsCmnt(cmntySrBbsCmntDto);
	}

	@Override
	public Integer updateSrBbsCmnt(CmntySrBbsCmntDto cmntySrBbsCmntDto, RequestContext requestContext) {

		long userId = requestContext.getUser().getUserId();
		CmntySrBbsCmntDto detail= cmntyMapper.selectSrBbsCmntDetail(cmntySrBbsCmntDto.getCmntId());
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 댓글만 수정 가능합니다.");

		detail.setCmntCn(cmntySrBbsCmntDto.getCmntCn());

		return cmntyMapper.updateSrBbsCmnt(cmntySrBbsCmntDto);
	}

	@Override
	@Transactional
	public Integer deleteSrBbsCmnt(RequestContext requestContext, long cmntId) {

		long userId = requestContext.getUser().getUserId();
		CmntySrBbsCmntDto cmntySrBbsCmntDto = cmntyMapper.selectSrBbsCmntDetail(cmntId);
		if(userId != cmntySrBbsCmntDto.getFrstCrtId()) throw new ApiException("본인 댓글만 삭제 가능합니다.");
		//첨부 파일 삭제
		CmntySrBbsCmntFileDto cmntySrBbsCmntFileDto = new CmntySrBbsCmntFileDto();
		cmntySrBbsCmntFileDto.setCmntId(cmntId);
		cmntySrBbsCmntFileDto.setLastMdfcnId(userId);

		cmntyMapper.deleteSrBbsCmntFileByCmntId(cmntySrBbsCmntFileDto);

		//댓글 삭제
		return cmntyMapper.deleteSrBbsCmnt(cmntId);
	}

	//파일업로드
	public void fileUploadHandlerSrBbsCmnt(CmntySrBbsCmntDto cmntySrBbsCmntDto, MultipartFile[] detailImageFiles) {

			if (detailImageFiles != null) {

				for (MultipartFile multipartFile : detailImageFiles) {
					if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
						String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
						String fileName = multipartFile.getOriginalFilename();
						int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

						final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "hwpx", "zip", "7z"};

						boolean extenstion_check = false;

						for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
							if (extension.equals(AVAILABLE_EXTENSION[i])) {
								extenstion_check = true;
							}
						}

						StringBuilder saveFileName = new StringBuilder();
						String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
						saveFileName.setLength(0);
					    saveFileName.append(dateNanoTime);
					    saveFileName.append("_").append(multipartFile.getOriginalFilename());

					    // 1. 업로드 경로설정
					    StringBuilder uploadPath = new StringBuilder();

					    if (extenstion_check) {
							if (maxSize > multipartFile.getSize()) {

					            uploadPath
					                    .append(cmntySrBbsCmntDto.getUploadPath())
					                    .append(File.separator);

								fileService.makeUploadPath(uploadPath.toString());

								// 2. 저장될 파일
								// 시큐어코딩 CWE-434 처리

								//대소문자 해제
								String fileToLowerCase= fileName.toLowerCase();

								if(fileToLowerCase.endsWith(".jpg")
										|| fileToLowerCase.endsWith(".jpeg")
										|| fileToLowerCase.endsWith(".gif")
										|| fileToLowerCase.endsWith(".bmp")
										|| fileToLowerCase.endsWith(".png")
										|| fileToLowerCase.endsWith(".doc")
										|| fileToLowerCase.endsWith(".docx")
										|| fileToLowerCase.endsWith(".xls")
										|| fileToLowerCase.endsWith(".xlsx")
										|| fileToLowerCase.endsWith(".ppt")
										|| fileToLowerCase.endsWith(".pptx")
										|| fileToLowerCase.endsWith(".pdf")
										|| fileToLowerCase.endsWith(".tif")
										|| fileToLowerCase.endsWith(".tiff")
										|| fileToLowerCase.endsWith(".hwp")
										|| fileToLowerCase.endsWith(".hwpx")
										|| fileToLowerCase.endsWith(".zip")
										|| fileToLowerCase.endsWith(".7z")
										) {

									File saveFile = new File(uploadPath + File.separator + saveFileName);

									try {
										fileStorage.upload(multipartFile.getBytes(), saveFile);
									} catch (IOException e) {
										log.error("ERROR: {}", e.getMessage(), e);
									}
								} else {
									throw new UserException("유효하지 않은 파일입니다.");
								}

							} else {
		                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
							}
						} else {
							throw new UserException("유효하지 않은 파일입니다.");
						}

					    // 자료실 File 등록
					    CmntySrBbsCmntFileDto file = new CmntySrBbsCmntFileDto();

		                file.setCmntId(cmntySrBbsCmntDto.getCmntId());
						file.setOrgnlAtchFileNm(fileName);
		                file.setAtchFileNm(saveFileName.toString());
		                file.setAtchFileExtnNm(extension);
		                file.setAtchFilePathNm(uploadPath.toString());
		                file.setAtchFileSz(multipartFile.getSize());
		                file.setFrstCrtId(UserUtils.getUser().getUserId());

		                cmntyMapper.insertSrBbsCmntFile(file);
					}
				}
			}
		}

	@Override
	public List<CmntySrBbsCmntFileDto> getSrBbsCmntFileList(long cmntId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CmntySrBbsCmntFileDto getSrBbsCmntFileDetail(long fileId) {
		return cmntyMapper.getSrBbsCmntFileDetail(fileId);
	}

	@Override
	public void deleteSrBbsCmntFileByFileId(CmntySrBbsCmntFileDto cmntySrBbsCmntFileDto) {
		cmntyMapper.deleteSrBbsCmntFileByFileId(cmntySrBbsCmntFileDto);

	}

	@Override
	public void deleteSrBbsCmntFileByCmntId(CmntySrBbsCmntFileDto cmntySrBbsCmntFileDto) {
		cmntyMapper.deleteSrBbsCmntFileByCmntId(cmntySrBbsCmntFileDto);

	}

	@Override
	public void deleteSrBbsCmntByBbsId(CmntySrBbsDto cmntySrBbsDto) {
		cmntyMapper.deleteSrBbsCmntByBbsId(cmntySrBbsDto);
	}

	@Override
	public void deleteSrBbsCmntFileByBbsId(CmntySrBbsDto cmntySrBbsDto) {
		cmntyMapper.deleteSrBbsCmntFileByBbsId(cmntySrBbsDto);
	}







	//faq게시글수 조회
	@Override
	public int countFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto) {
		return cmntyMapper.countFaqBbs(cmntyFaqBbsDto);
	}

	//faq게시글 목록 조회
	@Override
	public List<CmntyFaqBbsDto> listFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto) {
		return cmntyMapper.listFaqBbs(cmntyFaqBbsDto);
	}

	//faq게시글 등록
	@Override
	public void insertFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto, MultipartFile[] detailImageFiles) {

		//등록자 아이디
		cmntyFaqBbsDto.setFrstCrtId(UserUtils.getUser().getUserId());

		// 게시글 등록
		cmntyMapper.insertFaqBbs(cmntyFaqBbsDto);

		// 파일 업로드
		fileUploadHandlerFaqBbs(cmntyFaqBbsDto, detailImageFiles);
	}


	//파일업로드
	public void fileUploadHandlerFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto, MultipartFile[] detailImageFiles) {

		if (detailImageFiles != null) {

			for (MultipartFile multipartFile : detailImageFiles) {
				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String fileName = multipartFile.getOriginalFilename();
					int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

					final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "hwpx", "zip", "7z"};

					boolean extenstion_check = false;

					for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
						if (extension.equals(AVAILABLE_EXTENSION[i])) {
							extenstion_check = true;
						}
					}

					StringBuilder saveFileName = new StringBuilder();
					String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
					saveFileName.setLength(0);
				    saveFileName.append(dateNanoTime);
				    saveFileName.append("_").append(multipartFile.getOriginalFilename());

				    // 1. 업로드 경로설정
				    StringBuilder uploadPath = new StringBuilder();

				    if (extenstion_check) {
						if (maxSize > multipartFile.getSize()) {

				            uploadPath
				                    .append(cmntyFaqBbsDto.getUploadPath())
				                    .append(File.separator);

							fileService.makeUploadPath(uploadPath.toString());

							// 2. 저장될 파일
							// 시큐어코딩 CWE-434 처리

							//대소문자 해제
							String fileToLowerCase= fileName.toLowerCase();

							if(fileToLowerCase.endsWith(".jpg")
									|| fileToLowerCase.endsWith(".jpeg")
									|| fileToLowerCase.endsWith(".gif")
									|| fileToLowerCase.endsWith(".bmp")
									|| fileToLowerCase.endsWith(".png")
									|| fileToLowerCase.endsWith(".doc")
									|| fileToLowerCase.endsWith(".docx")
									|| fileToLowerCase.endsWith(".xls")
									|| fileToLowerCase.endsWith(".xlsx")
									|| fileToLowerCase.endsWith(".ppt")
									|| fileToLowerCase.endsWith(".pptx")
									|| fileToLowerCase.endsWith(".pdf")
									|| fileToLowerCase.endsWith(".tif")
									|| fileToLowerCase.endsWith(".tiff")
									|| fileToLowerCase.endsWith(".hwp")
									|| fileToLowerCase.endsWith(".hwpx")
									|| fileToLowerCase.endsWith(".zip")
									|| fileToLowerCase.endsWith(".7z")
									) {

								File saveFile = new File(uploadPath + File.separator + saveFileName);

								try {
									fileStorage.upload(multipartFile.getBytes(), saveFile);
								} catch (IOException e) {
									log.error("ERROR: {}", e.getMessage(), e);
								}
							} else {
								throw new UserException("유효하지 않은 파일입니다.");
							}

						} else {
	                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
						}
					} else {
						throw new UserException("유효하지 않은 파일입니다.");
					}

				    // 자료실 File 등록
				    CmntyFaqBbsFileDto file = new CmntyFaqBbsFileDto();

	                file.setBbsId(cmntyFaqBbsDto.getBbsId());
					file.setOrgnlAtchFileNm(fileName);
	                file.setAtchFileNm(saveFileName.toString());
	                file.setAtchFileExtnNm(extension);
	                file.setAtchFilePathNm(uploadPath.toString());
	                file.setAtchFileSz(multipartFile.getSize());
	                file.setFrstCrtId(UserUtils.getUser().getUserId());

	                cmntyMapper.insertFaqBbsFile(file);
				}
			}
		}
	}


	//faq게시글 조회수 증가
	@Override
	public void updateFaqBbsInqCnt(long bbsId) {
		cmntyMapper.updateFaqBbsInqCnt(bbsId);
	}


	//faq게시글 상세 조회
	@Override
	public CmntyFaqBbsDto getFaqBbsDetail(long bbsId, Model model) {

		CmntyFaqBbsDto detail = cmntyMapper.getFaqBbsDetail(bbsId);
		if(detail == null) {
			return detail;
		}

		//권한 가져오기
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

			//	ROLE_ADMIN_1	시스템주담당자 / ROLE_ADMIN_2	시스템부담당자
			//	ROLE_ADMIN_3	행안부주담당자 / ROLE_ADMIN_4	행안부부담당자
			//	ROLE_ADMIN_5	지자체주담당자 / ROLE_ADMIN_6	지자체부담당자 / ROLE_ADMIN_10	지정기부담당자

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
			}
		}
		detail.setIsSecretYn("N");
		//게시글의 비밀글 여부가 Y 이면
		if(detail.getIsSecret().equalsIgnoreCase("Y")) {
			detail.setIsSecretYn("Y");

			// 시스템 관리자는 확인 가능
			if((role.equalsIgnoreCase("ROLE_ADMIN_1") || role.equalsIgnoreCase("ROLE_ADMIN_2"))) {
				detail.setIsSecretYn("N");
			}

			// 본인 확인 가능
			if(user.getUserId() ==  detail.getFrstCrtId()) {
				detail.setIsSecretYn("N");
			}
		}

		//첨부파일 포맷
		if(detail.getAtchFileSz() != null) {
			String filesize = "";
			long bytes = detail.getAtchFileSz();

			if(bytes < 1024) {
				filesize = String.valueOf(bytes) + "B";
			} else if (bytes >= 1024 && bytes <= 1024 * 1024) {
				filesize = String.valueOf(bytes/1024) + "KB";
			} else {
				filesize = String.valueOf(bytes/1024/1024) + "MB";
			}
			model.addAttribute("filesize", filesize);
		}

		model.addAttribute("fileList", getFaqBbsfileList(bbsId));
		model.addAttribute("role", role);
		model.addAttribute("userId", UserUtils.getUser().getUserId());
		return detail;
	}

	@Override
	public List<CmntyFaqBbsFileDto> getFaqBbsfileList(long bbsId) {
		return cmntyMapper.getFaqBbsfileList(bbsId);
	}


	@Override
	public CmntyFaqBbsFileDto getFaqBbsFileDetail(long fileId) {
		return cmntyMapper.getFaqBbsFileDetail(fileId);
	}

	@Override
	public void deleteFaqBbsFileByFileId(long fileId) {

		long userId = UserUtils.getUser().getUserId();

		CmntyFaqBbsFileDto cmntyFaqBbsFileDto = new CmntyFaqBbsFileDto();
		cmntyFaqBbsFileDto.setFileId(fileId);
		cmntyFaqBbsFileDto.setLastMdfcnId(userId);

		cmntyMapper.deleteFaqBbsFileByFileId(cmntyFaqBbsFileDto);
	}

	@Override
	public void updateFaqBbs(CmntyFaqBbsDto cmntyFaqBbsDto, @RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles) {

		/*
		 * if(cmntyFaqBbsDto.getNoticeYn().isEmpty()) { cmntyFaqBbsDto.setNoticeYn("N"); }
		 *
		 * if(cmntyFaqBbsDto.getIsSecretYn().isEmpty()) { cmntyFaqBbsDto.setIsSecret("N");
		 * }
		 */
		//수정자 아이디
		long userId = UserUtils.getUser().getUserId();
		cmntyFaqBbsDto.setLastMdfcnId(userId);

		//게시글 수정
		cmntyMapper.updateFaqBbs(cmntyFaqBbsDto);

		// 파일 업로드
		fileUploadHandlerFaqBbs(cmntyFaqBbsDto ,detailImageFiles);

	}

	@Override
	@Transactional
	public void deleteFaqBbs(long bbsId) {

		long userId = UserUtils.getUser().getUserId();


		CmntyFaqBbsDto detail = cmntyMapper.getFaqBbsDetail(bbsId);
		if(detail == null) throw new ApiException("존재하지 않는 게시글입니다.");

		CmntyFaqBbsDto cmntyFaqBbsDto = new CmntyFaqBbsDto();
		cmntyFaqBbsDto.setBbsId(bbsId);
		cmntyFaqBbsDto.setLastMdfcnId(userId);

		//게시글 댓글 파일 삭제
		cmntyMapper.deleteFaqBbsCmntFileByBbsId(cmntyFaqBbsDto);

		//게시글 댓글 삭제
		cmntyMapper.deleteFaqBbsCmntByBbsId(cmntyFaqBbsDto);

		//게시글 첨부파일 삭제
		cmntyMapper.deleteFaqBbsFileByBbsId(cmntyFaqBbsDto);

		//게시글 삭제
		cmntyMapper.deleteFaqBbs(cmntyFaqBbsDto);

	}

	@Override
	public List<CmntyFaqBbsCmntDto> selectFaqBbsCmntList(long bbsId) {
		return cmntyMapper.selectFaqBbsCmntList(bbsId);
	}

	@Override
	public Integer addFaqBbsCmnt(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, RequestContext requestContext) {
		long userId = requestContext.getUser().getUserId();
		cmntyFaqBbsCmntDto.setFrstCrtId(userId);

		return cmntyMapper.addFaqBbsCmnt(cmntyFaqBbsCmntDto);
	}

	@Override
	public Integer updateFaqBbsCmnt(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, RequestContext requestContext) {

		long userId = requestContext.getUser().getUserId();
		CmntyFaqBbsCmntDto detail= cmntyMapper.selectFaqBbsCmntDetail(cmntyFaqBbsCmntDto.getCmntId());
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 댓글만 수정 가능합니다.");

		detail.setCmntCn(cmntyFaqBbsCmntDto.getCmntCn());

		return cmntyMapper.updateFaqBbsCmnt(cmntyFaqBbsCmntDto);
	}

	@Override
	@Transactional
	public Integer deleteFaqBbsCmnt(RequestContext requestContext, long cmntId) {

		long userId = requestContext.getUser().getUserId();
		CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto = cmntyMapper.selectFaqBbsCmntDetail(cmntId);
		if(userId != cmntyFaqBbsCmntDto.getFrstCrtId()) throw new ApiException("본인 댓글만 삭제 가능합니다.");
		//첨부 파일 삭제
		CmntyFaqBbsCmntFileDto cmntyFaqBbsCmntFileDto = new CmntyFaqBbsCmntFileDto();
		cmntyFaqBbsCmntFileDto.setCmntId(cmntId);
		cmntyFaqBbsCmntFileDto.setLastMdfcnId(userId);

		cmntyMapper.deleteFaqBbsCmntFileByCmntId(cmntyFaqBbsCmntFileDto);

		//댓글 삭제
		return cmntyMapper.deleteFaqBbsCmnt(cmntId);
	}

	//파일업로드
	public void fileUploadHandlerFaqBbsCmnt(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, MultipartFile[] detailImageFiles) {

			if (detailImageFiles != null) {

				for (MultipartFile multipartFile : detailImageFiles) {
					if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
						String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
						String fileName = multipartFile.getOriginalFilename();
						int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

						final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "hwpx", "zip", "7z"};

						boolean extenstion_check = false;

						for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
							if (extension.equals(AVAILABLE_EXTENSION[i])) {
								extenstion_check = true;
							}
						}

						StringBuilder saveFileName = new StringBuilder();
						String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
						saveFileName.setLength(0);
					    saveFileName.append(dateNanoTime);
					    saveFileName.append("_").append(multipartFile.getOriginalFilename());

					    // 1. 업로드 경로설정
					    StringBuilder uploadPath = new StringBuilder();

					    if (extenstion_check) {
							if (maxSize > multipartFile.getSize()) {

					            uploadPath
					                    .append(cmntyFaqBbsCmntDto.getUploadPath())
					                    .append(File.separator);

								fileService.makeUploadPath(uploadPath.toString());

								// 2. 저장될 파일
								// 시큐어코딩 CWE-434 처리

								//대소문자 해제
								String fileToLowerCase= fileName.toLowerCase();

								if(fileToLowerCase.endsWith(".jpg")
										|| fileToLowerCase.endsWith(".jpeg")
										|| fileToLowerCase.endsWith(".gif")
										|| fileToLowerCase.endsWith(".bmp")
										|| fileToLowerCase.endsWith(".png")
										|| fileToLowerCase.endsWith(".doc")
										|| fileToLowerCase.endsWith(".docx")
										|| fileToLowerCase.endsWith(".xls")
										|| fileToLowerCase.endsWith(".xlsx")
										|| fileToLowerCase.endsWith(".ppt")
										|| fileToLowerCase.endsWith(".pptx")
										|| fileToLowerCase.endsWith(".pdf")
										|| fileToLowerCase.endsWith(".tif")
										|| fileToLowerCase.endsWith(".tiff")
										|| fileToLowerCase.endsWith(".hwp")
										|| fileToLowerCase.endsWith(".hwpx")
										|| fileToLowerCase.endsWith(".zip")
										|| fileToLowerCase.endsWith(".7z")
										) {

									File saveFile = new File(uploadPath + File.separator + saveFileName);

									try {
										fileStorage.upload(multipartFile.getBytes(), saveFile);
									} catch (IOException e) {
										log.error("ERROR: {}", e.getMessage(), e);
									}
								} else {
									throw new UserException("유효하지 않은 파일입니다.");
								}

							} else {
		                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
							}
						} else {
							throw new UserException("유효하지 않은 파일입니다.");
						}

					    // 자료실 File 등록
					    CmntyFaqBbsCmntFileDto file = new CmntyFaqBbsCmntFileDto();

		                file.setCmntId(cmntyFaqBbsCmntDto.getCmntId());
						file.setOrgnlAtchFileNm(fileName);
		                file.setAtchFileNm(saveFileName.toString());
		                file.setAtchFileExtnNm(extension);
		                file.setAtchFilePathNm(uploadPath.toString());
		                file.setAtchFileSz(multipartFile.getSize());
		                file.setFrstCrtId(UserUtils.getUser().getUserId());

		                cmntyMapper.insertFaqBbsCmntFile(file);
					}
				}
			}
		}

	@Override
	public List<CmntyFaqBbsCmntFileDto> getFaqBbsCmntFileList(long cmntId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CmntyFaqBbsCmntFileDto getFaqBbsCmntFileDetail(long fileId) {
		return cmntyMapper.getFaqBbsCmntFileDetail(fileId);
	}

	@Override
	public void deleteFaqBbsCmntFileByFileId(CmntyFaqBbsCmntFileDto cmntyFaqBbsCmntFileDto) {
		cmntyMapper.deleteFaqBbsCmntFileByFileId(cmntyFaqBbsCmntFileDto);

	}

	@Override
	public void deleteFaqBbsCmntFileByCmntId(CmntyFaqBbsCmntFileDto cmntyFaqBbsCmntFileDto) {
		cmntyMapper.deleteFaqBbsCmntFileByCmntId(cmntyFaqBbsCmntFileDto);

	}

	@Override
	public void deleteFaqBbsCmntByBbsId(CmntyFaqBbsDto cmntyFaqBbsDto) {
		cmntyMapper.deleteFaqBbsCmntByBbsId(cmntyFaqBbsDto);
	}

	@Override
	public void deleteFaqBbsCmntFileByBbsId(CmntyFaqBbsDto cmntyFaqBbsDto) {
		cmntyMapper.deleteFaqBbsCmntFileByBbsId(cmntyFaqBbsDto);
	}




	//오프라인담당자 sr게시글수 조회
	@Override
	public int countOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto) {
		return cmntyMapper.countOffSrBbs(cmntyOffSrBbsDto);
	}

	//오프라인담당자 sr게시글 목록 조회
	@Override
	public List<CmntyOffSrBbsDto> listOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto) {
		return cmntyMapper.listOffSrBbs(cmntyOffSrBbsDto);
	}

	//오프라인담당자 sr게시글 등록
	@Override
	public void insertOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto, MultipartFile[] detailImageFiles) {

		//등록자 아이디
		cmntyOffSrBbsDto.setFrstCrtId(UserUtils.getUser().getUserId());

		// 게시글 등록
		cmntyMapper.insertOffSrBbs(cmntyOffSrBbsDto);

		// 파일 업로드
		fileUploadHandlerOffSrBbs(cmntyOffSrBbsDto, detailImageFiles);
	}


	//파일업로드
	public void fileUploadHandlerOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto, MultipartFile[] detailImageFiles) {

		if (detailImageFiles != null) {

			for (MultipartFile multipartFile : detailImageFiles) {
				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String fileName = multipartFile.getOriginalFilename();
					int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

					final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "hwpx", "zip", "7z"};

					boolean extenstion_check = false;

					for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
						if (extension.equals(AVAILABLE_EXTENSION[i])) {
							extenstion_check = true;
						}
					}

					StringBuilder saveFileName = new StringBuilder();
					String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
					saveFileName.setLength(0);
				    saveFileName.append(dateNanoTime);
				    saveFileName.append("_").append(multipartFile.getOriginalFilename());

				    // 1. 업로드 경로설정
				    StringBuilder uploadPath = new StringBuilder();

				    if (extenstion_check) {
						if (maxSize > multipartFile.getSize()) {

				            uploadPath
				                    .append(cmntyOffSrBbsDto.getUploadPath())
				                    .append(File.separator);

							fileService.makeUploadPath(uploadPath.toString());

							// 2. 저장될 파일
							// 시큐어코딩 CWE-434 처리

							//대소문자 해제
							String fileToLowerCase= fileName.toLowerCase();

							if(fileToLowerCase.endsWith(".jpg")
									|| fileToLowerCase.endsWith(".jpeg")
									|| fileToLowerCase.endsWith(".gif")
									|| fileToLowerCase.endsWith(".bmp")
									|| fileToLowerCase.endsWith(".png")
									|| fileToLowerCase.endsWith(".doc")
									|| fileToLowerCase.endsWith(".docx")
									|| fileToLowerCase.endsWith(".xls")
									|| fileToLowerCase.endsWith(".xlsx")
									|| fileToLowerCase.endsWith(".ppt")
									|| fileToLowerCase.endsWith(".pptx")
									|| fileToLowerCase.endsWith(".pdf")
									|| fileToLowerCase.endsWith(".tif")
									|| fileToLowerCase.endsWith(".tiff")
									|| fileToLowerCase.endsWith(".hwp")
									|| fileToLowerCase.endsWith(".hwpx")
									|| fileToLowerCase.endsWith(".zip")
									|| fileToLowerCase.endsWith(".7z")
									) {

								File saveFile = new File(uploadPath + File.separator + saveFileName);

								try {
									fileStorage.upload(multipartFile.getBytes(), saveFile);
								} catch (IOException e) {
									log.error("ERROR: {}", e.getMessage(), e);
								}
							} else {
								throw new UserException("유효하지 않은 파일입니다.");
							}

						} else {
	                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
						}
					} else {
						throw new UserException("유효하지 않은 파일입니다.");
					}

				    // 자료실 File 등록
				    CmntyOffSrBbsFileDto file = new CmntyOffSrBbsFileDto();

	                file.setBbsId(cmntyOffSrBbsDto.getBbsId());
					file.setOrgnlAtchFileNm(fileName);
	                file.setAtchFileNm(saveFileName.toString());
	                file.setAtchFileExtnNm(extension);
	                file.setAtchFilePathNm(uploadPath.toString());
	                file.setAtchFileSz(multipartFile.getSize());
	                file.setFrstCrtId(UserUtils.getUser().getUserId());

	                cmntyMapper.insertOffSrBbsFile(file);
				}
			}
		}
	}


	//오프라인담당자 sr게시글 조회수 증가
	@Override
	public void updateOffSrBbsInqCnt(long bbsId) {
		cmntyMapper.updateOffSrBbsInqCnt(bbsId);
	}


	//오프라인담당자 sr게시글 상세 조회
	@Override
	public CmntyOffSrBbsDto getOffSrBbsDetail(long bbsId, Model model) {

		CmntyOffSrBbsDto detail = cmntyMapper.getOffSrBbsDetail(bbsId);
		if(detail == null) {
			return detail;
		}

		//권한 가져오기
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

			//	ROLE_ADMIN_1	시스템주담당자 / ROLE_ADMIN_2	시스템부담당자
			//	ROLE_ADMIN_3	행안부주담당자 / ROLE_ADMIN_4	행안부부담당자
			//	ROLE_ADMIN_5	지자체주담당자 / ROLE_ADMIN_6	지자체부담당자 / ROLE_ADMIN_10	지정기부담당자

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_7".equals(userRole.getAuthority()) || "ROLE_ADMIN_8".equals(userRole.getAuthority()))
			{
				role = userRole.getAuthority();
			}
		}
		detail.setIsSecretYn("N");
		//게시글의 비밀글 여부가 Y 이면
		if(detail.getIsSecret().equalsIgnoreCase("Y")) {
			detail.setIsSecretYn("Y");

			// 시스템 관리자는 확인 가능
			if((role.equalsIgnoreCase("ROLE_ADMIN_1") || role.equalsIgnoreCase("ROLE_ADMIN_2"))) {
				detail.setIsSecretYn("N");
			}

			// 본인 확인 가능
			if(user.getUserId() ==  detail.getFrstCrtId()) {
				detail.setIsSecretYn("N");
			}
		}

		//첨부파일 포맷
		if(detail.getAtchFileSz() != null) {
			String filesize = "";
			long bytes = detail.getAtchFileSz();

			if(bytes < 1024) {
				filesize = String.valueOf(bytes) + "B";
			} else if (bytes >= 1024 && bytes <= 1024 * 1024) {
				filesize = String.valueOf(bytes/1024) + "KB";
			} else {
				filesize = String.valueOf(bytes/1024/1024) + "MB";
			}
			model.addAttribute("filesize", filesize);
		}

		model.addAttribute("fileList", getOffSrBbsfileList(bbsId));
		model.addAttribute("role", role);
		model.addAttribute("userId", UserUtils.getUser().getUserId());
		return detail;
	}

	@Override
	public List<CmntyOffSrBbsFileDto> getOffSrBbsfileList(long bbsId) {
		return cmntyMapper.getOffSrBbsfileList(bbsId);
	}


	@Override
	public CmntyOffSrBbsFileDto getOffSrBbsFileDetail(long fileId) {
		return cmntyMapper.getOffSrBbsFileDetail(fileId);
	}

	@Override
	public void deleteOffSrBbsFileByFileId(long fileId) {

		long userId = UserUtils.getUser().getUserId();

		CmntyOffSrBbsFileDto cmntyOffSrBbsFileDto = new CmntyOffSrBbsFileDto();
		cmntyOffSrBbsFileDto.setFileId(fileId);
		cmntyOffSrBbsFileDto.setLastMdfcnId(userId);

		cmntyMapper.deleteOffSrBbsFileByFileId(cmntyOffSrBbsFileDto);
	}

	@Override
	public void updateOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto, @RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles) {

		/*
		 * if(cmntyOffSrBbsDto.getNoticeYn().isEmpty()) { cmntyOffSrBbsDto.setNoticeYn("N"); }
		 *
		 * if(cmntyOffSrBbsDto.getIsSecretYn().isEmpty()) { cmntyOffSrBbsDto.setIsSecret("N");
		 * }
		 */
		//수정자 아이디
		long userId = UserUtils.getUser().getUserId();
		cmntyOffSrBbsDto.setLastMdfcnId(userId);

		//게시글 수정
		cmntyMapper.updateOffSrBbs(cmntyOffSrBbsDto);

		// 파일 업로드
		fileUploadHandlerOffSrBbs(cmntyOffSrBbsDto ,detailImageFiles);

	}

	@Override
	@Transactional
	public void deleteOffSrBbs(long bbsId) {

		long userId = UserUtils.getUser().getUserId();


		CmntyOffSrBbsDto detail = cmntyMapper.getOffSrBbsDetail(bbsId);
		if(detail == null) throw new ApiException("존재하지 않는 게시글입니다.");

		CmntyOffSrBbsDto cmntyOffSrBbsDto = new CmntyOffSrBbsDto();
		cmntyOffSrBbsDto.setBbsId(bbsId);
		cmntyOffSrBbsDto.setLastMdfcnId(userId);

		//게시글 댓글 파일 삭제
		cmntyMapper.deleteOffSrBbsCmntFileByBbsId(cmntyOffSrBbsDto);

		//게시글 댓글 삭제
		cmntyMapper.deleteOffSrBbsCmntByBbsId(cmntyOffSrBbsDto);

		//게시글 첨부파일 삭제
		cmntyMapper.deleteOffSrBbsFileByBbsId(cmntyOffSrBbsDto);

		//게시글 삭제
		cmntyMapper.deleteOffSrBbs(cmntyOffSrBbsDto);

	}

	@Override
	public List<CmntyOffSrBbsCmntDto> selectOffSrBbsCmntList(long bbsId) {
		return cmntyMapper.selectOffSrBbsCmntList(bbsId);
	}

	@Override
	public Integer addOffSrBbsCmnt(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, RequestContext requestContext) {
		long userId = requestContext.getUser().getUserId();
		cmntyOffSrBbsCmntDto.setFrstCrtId(userId);

		return cmntyMapper.addOffSrBbsCmnt(cmntyOffSrBbsCmntDto);
	}

	@Override
	public Integer updateOffSrBbsCmnt(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, RequestContext requestContext) {

		long userId = requestContext.getUser().getUserId();
		CmntyOffSrBbsCmntDto detail= cmntyMapper.selectOffSrBbsCmntDetail(cmntyOffSrBbsCmntDto.getCmntId());
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 댓글만 수정 가능합니다.");

		detail.setCmntCn(cmntyOffSrBbsCmntDto.getCmntCn());

		return cmntyMapper.updateOffSrBbsCmnt(cmntyOffSrBbsCmntDto);
	}

	@Override
	@Transactional
	public Integer deleteOffSrBbsCmnt(RequestContext requestContext, long cmntId) {

		long userId = requestContext.getUser().getUserId();
		CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto = cmntyMapper.selectOffSrBbsCmntDetail(cmntId);
		if(userId != cmntyOffSrBbsCmntDto.getFrstCrtId()) throw new ApiException("본인 댓글만 삭제 가능합니다.");
		//첨부 파일 삭제
		CmntyOffSrBbsCmntFileDto cmntyOffSrBbsCmntFileDto = new CmntyOffSrBbsCmntFileDto();
		cmntyOffSrBbsCmntFileDto.setCmntId(cmntId);
		cmntyOffSrBbsCmntFileDto.setLastMdfcnId(userId);

		cmntyMapper.deleteOffSrBbsCmntFileByCmntId(cmntyOffSrBbsCmntFileDto);

		//댓글 삭제
		return cmntyMapper.deleteOffSrBbsCmnt(cmntId);
	}

	//파일업로드
	public void fileUploadHandlerOffSrBbsCmnt(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, MultipartFile[] detailImageFiles) {

			if (detailImageFiles != null) {

				for (MultipartFile multipartFile : detailImageFiles) {
					if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
						String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
						String fileName = multipartFile.getOriginalFilename();
						int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

						final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "hwpx", "zip", "7z"};

						boolean extenstion_check = false;

						for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
							if (extension.equals(AVAILABLE_EXTENSION[i])) {
								extenstion_check = true;
							}
						}

						StringBuilder saveFileName = new StringBuilder();
						String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
						saveFileName.setLength(0);
					    saveFileName.append(dateNanoTime);
					    saveFileName.append("_").append(multipartFile.getOriginalFilename());

					    // 1. 업로드 경로설정
					    StringBuilder uploadPath = new StringBuilder();

					    if (extenstion_check) {
							if (maxSize > multipartFile.getSize()) {

					            uploadPath
					                    .append(cmntyOffSrBbsCmntDto.getUploadPath())
					                    .append(File.separator);

								fileService.makeUploadPath(uploadPath.toString());

								// 2. 저장될 파일
								// 시큐어코딩 CWE-434 처리

								//대소문자 해제
								String fileToLowerCase= fileName.toLowerCase();

								if(fileToLowerCase.endsWith(".jpg")
										|| fileToLowerCase.endsWith(".jpeg")
										|| fileToLowerCase.endsWith(".gif")
										|| fileToLowerCase.endsWith(".bmp")
										|| fileToLowerCase.endsWith(".png")
										|| fileToLowerCase.endsWith(".doc")
										|| fileToLowerCase.endsWith(".docx")
										|| fileToLowerCase.endsWith(".xls")
										|| fileToLowerCase.endsWith(".xlsx")
										|| fileToLowerCase.endsWith(".ppt")
										|| fileToLowerCase.endsWith(".pptx")
										|| fileToLowerCase.endsWith(".pdf")
										|| fileToLowerCase.endsWith(".tif")
										|| fileToLowerCase.endsWith(".tiff")
										|| fileToLowerCase.endsWith(".hwp")
										|| fileToLowerCase.endsWith(".hwpx")
										|| fileToLowerCase.endsWith(".zip")
										|| fileToLowerCase.endsWith(".7z")
										) {

									File saveFile = new File(uploadPath + File.separator + saveFileName);

									try {
										fileStorage.upload(multipartFile.getBytes(), saveFile);
									} catch (IOException e) {
										log.error("ERROR: {}", e.getMessage(), e);
									}
								} else {
									throw new UserException("유효하지 않은 파일입니다.");
								}

							} else {
		                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
							}
						} else {
							throw new UserException("유효하지 않은 파일입니다.");
						}

					    // 자료실 File 등록
					    CmntyOffSrBbsCmntFileDto file = new CmntyOffSrBbsCmntFileDto();

		                file.setCmntId(cmntyOffSrBbsCmntDto.getCmntId());
						file.setOrgnlAtchFileNm(fileName);
		                file.setAtchFileNm(saveFileName.toString());
		                file.setAtchFileExtnNm(extension);
		                file.setAtchFilePathNm(uploadPath.toString());
		                file.setAtchFileSz(multipartFile.getSize());
		                file.setFrstCrtId(UserUtils.getUser().getUserId());

		                cmntyMapper.insertOffSrBbsCmntFile(file);
					}
				}
			}
		}

	@Override
	public List<CmntyOffSrBbsCmntFileDto> getOffSrBbsCmntFileList(long cmntId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CmntyOffSrBbsCmntFileDto getOffSrBbsCmntFileDetail(long fileId) {
		return cmntyMapper.getOffSrBbsCmntFileDetail(fileId);
	}

	@Override
	public void deleteOffSrBbsCmntFileByFileId(CmntyOffSrBbsCmntFileDto cmntyOffSrBbsCmntFileDto) {
		cmntyMapper.deleteOffSrBbsCmntFileByFileId(cmntyOffSrBbsCmntFileDto);

	}

	@Override
	public void deleteOffSrBbsCmntFileByCmntId(CmntyOffSrBbsCmntFileDto cmntyOffSrBbsCmntFileDto) {
		cmntyMapper.deleteOffSrBbsCmntFileByCmntId(cmntyOffSrBbsCmntFileDto);

	}

	@Override
	public void deleteOffSrBbsCmntByBbsId(CmntyOffSrBbsDto cmntyOffSrBbsDto) {
		cmntyMapper.deleteOffSrBbsCmntByBbsId(cmntyOffSrBbsDto);
	}

	@Override
	public void deleteOffSrBbsCmntFileByBbsId(CmntyOffSrBbsDto cmntyOffSrBbsDto) {
		cmntyMapper.deleteOffSrBbsCmntFileByBbsId(cmntyOffSrBbsDto);
	}


}
