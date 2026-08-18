package saleson.shop.notice;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import saleson.api.common.exception.ApiException;
import saleson.common.Const;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.UserUtils;
import saleson.shop.community.CmntyServiceImpl;
import saleson.shop.community.doamin.CmntyFaqBbsDto;
import saleson.shop.community.doamin.CmntyFaqBbsFileDto;
import saleson.shop.notice.domain.SysNoticeSellerDto;
import saleson.shop.notice.domain.SysNoticeSellerFileDto;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;

import saleson.shop.user.UserMapper;


@Service("sysNoticeSellerService")
public class SysNoticeSellerServiceImpl extends EgovAbstractServiceImpl implements SysNoticeSellerService{

	private static final Logger log = LoggerFactory.getLogger(SysNoticeSellerServiceImpl.class);

	@Autowired
	SysNoticeSellerMapper sysNoticeSellerMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired SequenceService sequenceService;

	@Autowired
    private FileService fileService;

	@Autowired
    private FileStorage fileStorage;

	@Override
	public void insertNotice(SysNoticeSellerDto notice, MultipartFile[] detailImageFiles) {
		//등록자 아이디
		notice.setFrstCrtId(UserUtils.getUser().getUserId());
		if (null == notice.getNoticeFlag()) {
			notice.setNoticeFlag("N");
		}
		// 게시글 등록
		sysNoticeSellerMapper.insertNotice(notice);
		// 파일 업로드
		fileUploadHandlerSysNoticeSeller(notice, detailImageFiles);
	}


	//파일업로드
		public void fileUploadHandlerSysNoticeSeller(SysNoticeSellerDto notice, MultipartFile[] detailImageFiles) {

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
					                    .append(notice.getUploadPath())
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
					    SysNoticeSellerFileDto file = new SysNoticeSellerFileDto();

		                file.setNoticeId(notice.getNoticeId());
						file.setOrgnlAtchFileNm(fileName);
		                file.setAtchFileNm(saveFileName.toString());
		                file.setAtchFileExtnNm(extension);
		                file.setAtchFilePathNm(uploadPath.toString());
		                file.setAtchFileSz(multipartFile.getSize());
		                file.setFrstCrtId(UserUtils.getUser().getUserId());

		                sysNoticeSellerMapper.insertSysNoticeSellerFile(file);
					}
				}
			}
		}

	@Override
	public int getNoticeCount(SysNoticeSellerDto noticeParam) {

		return sysNoticeSellerMapper.getNoticeCount(noticeParam);
	}

	@Override
	public List<SysNoticeSellerDto> getNoticeList(SysNoticeSellerDto noticeParam) {

		return sysNoticeSellerMapper.getNoticeList(noticeParam);
	}

	@Override
	public SysNoticeSellerDto getNotice(int noticeId, Model model) {
		SysNoticeSellerDto detail = sysNoticeSellerMapper.getNotice(noticeId);
		if(detail == null) {
			return detail;
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

		model.addAttribute("fileList", getSysNoticeSellerFileList(noticeId));
		model.addAttribute("userId", UserUtils.getUser().getUserId());

		return detail;
	}

	@Override
	public List<SysNoticeSellerFileDto> getSysNoticeSellerFileList(int noticeId) {
		return sysNoticeSellerMapper.getSysNoticeSellerFileList(noticeId);
	}

	@Override
	public SysNoticeSellerFileDto getSysNoticeSellerFileDetail(long fileId) {
		return sysNoticeSellerMapper.getSysNoticeSellerFileDetail(fileId);
	}


	@Override
	public void updateSysNoticeSeller(SysNoticeSellerDto notice, MultipartFile[] detailImageFiles) {

		//notice.setCategoryTeam(notice.getCategoryTeam().replaceAll(",", "|"));
		//수정자 아이디
		long userId = UserUtils.getUser().getUserId();
		notice.setLastMdfcnId(userId);
		if (null == notice.getNoticeFlag()) {
			notice.setNoticeFlag("N");
		}

		// 게시글 수정
		sysNoticeSellerMapper.updateNotice(notice);

		// 파일 업로드
		fileUploadHandlerSysNoticeSeller(notice, detailImageFiles);
	}

	@Override
	public void deleteNotice(int noticeId) {
		long userId = UserUtils.getUser().getUserId();
		SysNoticeSellerDto detail = sysNoticeSellerMapper.getNotice(noticeId);
		if(detail == null) {
			 throw new ApiException("존재하지 않는 게시글입니다.");
		}

		SysNoticeSellerDto sysNoticeSellerDto = new SysNoticeSellerDto();
		sysNoticeSellerDto.setNoticeId(noticeId);
		sysNoticeSellerDto.setLastMdfcnId(userId);

		//게시글 첨부파일 삭제
		sysNoticeSellerMapper.deleteNoticeFileByNoticeId(sysNoticeSellerDto);

		//게시글 삭제
		sysNoticeSellerMapper.deleteNotice(sysNoticeSellerDto);
	}




	@Override
	public void deleteNoticeFileByFileId(long fileId) {

		long userId = UserUtils.getUser().getUserId();

		SysNoticeSellerFileDto sysNoticeSellerFileDto = new SysNoticeSellerFileDto();
		sysNoticeSellerFileDto.setFileId(fileId);
		sysNoticeSellerFileDto.setLastMdfcnId(userId);

		sysNoticeSellerMapper.deleteNoticeFileByFileId(sysNoticeSellerFileDto);
	}

	@Override
	public List<SysNoticeSellerDto> getFrontNoticeList(SysNoticeSellerDto noticeParam) {

		return sysNoticeSellerMapper.getFrontNoticeList(noticeParam);
	}

	@Override
	public int getFrontNoticeListCount(SysNoticeSellerDto noticeParam) {
		return sysNoticeSellerMapper.getFrontNoticeListCount(noticeParam);
	}

	@Override
	public void addHitCount(int noticeId) {

		sysNoticeSellerMapper.addHitCount(noticeId);
	}


	@Override
	public SysNoticeSellerDto getFrontNotice(int noticeId) {
		// TODO Auto-generated method stub
		return sysNoticeSellerMapper.getFrontNotice(noticeId);
	}
}
