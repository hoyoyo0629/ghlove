package saleson.shop.maintenance;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;

import saleson.api.common.exception.ApiException;
import saleson.common.Const;
import saleson.common.file.ExcelCellStyleUtils;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.UserUtils;
import saleson.shop.order.domain.OrderList;
import saleson.shop.order.support.OrderParam;


@Service("maintenanceService")
public class MaintenanceServiceImpl implements MaintenanceService {

	private static final Logger log = LoggerFactory.getLogger(MaintenanceServiceImpl.class);

	@Autowired
	private MaintenanceMapper maintenanceMapper;

	@Autowired
    private FileService fileService;

	@Autowired
    private FileStorage fileStorage;


	//게시글수 조회
	@Override
	public int countMaintenance(MaintenanceDto maintenanceDto) {
		return maintenanceMapper.countMaintenance(maintenanceDto);
	}

	//운영유지보수 sr게시글 목록 조회
	@Override
	public List<MaintenanceDto> listMaintenance(MaintenanceDto maintenanceDto) {
		List<MaintenanceDto> list = maintenanceMapper.listMaintenance(maintenanceDto);
		for (MaintenanceDto dto : list) {
			if(dto.getBbsCn() != null) {
				String cleanText = dto.getBbsCn();
				//html 처리
				cleanText = cleanText.replaceAll("(?i)<br\\s*/?>", "\n")
									.replaceAll("(?i)</?div[^>]*>", "\n")
									.replaceAll("(?i)</?p[^>]*>", "\n");

				cleanText = cleanText.replaceAll("<[^>]*>", "")
									.replaceAll("&nbsp;", " ")
									.replaceAll("&lt;", "<")
									.replaceAll("&gt;", ">")
									.replaceAll("&amp;", "&");

				cleanText = cleanText.replaceAll("[ \t]+\n", "\n").replaceAll("\n[ \t]+", "\n");
				cleanText = cleanText.replaceAll("[ \t\r]+", " ").trim();

				if(cleanText.length() > 100) {
					cleanText = cleanText.substring(0, 100) + "...";
				}
				dto.setBbsCn(cleanText);
			}
			if(dto.getProcessCn() != null) {
				String cleanText = dto.getProcessCn();
				//html 처리
				cleanText = cleanText.replaceAll("(?i)<br\\s*/?>", "\n")
									.replaceAll("(?i)</?div[^>]*>", "\n")
									.replaceAll("(?i)</?p[^>]*>", "\n");

				cleanText = cleanText.replaceAll("<[^>]*>", "")
									.replaceAll("&nbsp;", " ")
									.replaceAll("&lt;", "<")
									.replaceAll("&gt;", ">")
									.replaceAll("&amp;", "&");

				cleanText = cleanText.replaceAll("[ \t]+\n", "\n").replaceAll("\n[ \t]+", "\n");
				cleanText = cleanText.replaceAll("[ \t\r]+", " ").trim();

				if(cleanText.length() > 100) {
					cleanText = cleanText.substring(0, 100) + "...";
				}
				dto.setProcessCn(cleanText);
			}
		}

		return list;
	}
	/**
	 * 스트리밍 엑셀다운로드
	 *
	 * @param	maintenanceDto
	 * @throws	Exception
	 */
	@Override
	public SXSSFWorkbook streamAllMaintenanceData(MaintenanceDto maintenanceDto) {
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("SR리스트");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		int rowNum 		= 0;
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);;
		cellStyle.title(titleRow, 0, "운영관리 SR게시판");
		Integer lastColIndex;

		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 8000);
		sheet.setColumnWidth(6, 11000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 8000);
		sheet.setColumnWidth(10, 4000);
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 5000);
		sheet.setColumnWidth(14, 5000);
		Row header = sheet.createRow(rowNum++);
		header.setHeight((short) 512);
		cellStyle.header(header, 0, "No");
		cellStyle.header(header, 1, "작성자");
		cellStyle.header(header, 2, "업무구분");
		cellStyle.header(header, 3, "CSR번호");
		cellStyle.header(header, 4, "처리구분");
		cellStyle.header(header, 5, "제목");
		cellStyle.header(header, 6, "내용");
		cellStyle.header(header, 7, "요청경로");
		cellStyle.header(header, 8, "담당자");
		cellStyle.header(header, 9, "답변");
		cellStyle.header(header, 10, "완료구분");
		cellStyle.header(header, 11, "등록일지");
		cellStyle.header(header, 12, "수정일시");
		cellStyle.header(header, 13, "처리시작일");
		cellStyle.header(header, 14, "처리종료일");

		lastColIndex = header.getLastCellNum() - 1;

		List<MaintenanceDto> list = listMaintenance(maintenanceDto);

		for (MaintenanceDto obj : list) {
			Row row = sheet.createRow(rowNum++);
			row.setHeight((short) 400);
			cellStyle.data(row, 0, String.valueOf(rowNum-2));
			cellStyle.data(row, 1, obj.getReqManagerNm());
			cellStyle.data(row, 2, obj.getReqTypeNm());
			cellStyle.data(row, 3, obj.getSrNo());
			cellStyle.data(row, 4, obj.getProcessTypeNm());
			cellStyle.data(row, 5, obj.getBbsTtl());
			cellStyle.data(row, 6, obj.getBbsCn());
			cellStyle.data(row, 7, obj.getReqChannelNm());
			cellStyle.data(row, 8, obj.getProcessManagerNm());
			cellStyle.data(row, 9, obj.getProcessCn());
			cellStyle.data(row, 10, obj.getProcessStateNm());

			if(obj.getFrstCrtDt() != null) {//등록일시
				LocalDateTime ldt = obj.getFrstCrtDt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				cellStyle.data(row, 11, String.valueOf(ldt.format(formatter)));
			} else {
				cellStyle.data(row, 11, String.valueOf(""));
			}
			if(obj.getLastMdfcnDt() != null) {//수정일시
				LocalDateTime ldt = obj.getLastMdfcnDt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				cellStyle.data(row, 12, String.valueOf(ldt.format(formatter)));
			} else {
				cellStyle.data(row, 12, String.valueOf(""));
			}
			cellStyle.data(row, 13, obj.getProcessStartDate());
			cellStyle.data(row, 14, obj.getProcessEndDate());

		}


		return workbook;
	}

	//운영유지보수 sr게시글 등록
	@Override
	public void insertMaintenance(MaintenanceDto maintenanceDto, MultipartFile[] detailImageFiles) {

		//등록자 아이디
		maintenanceDto.setFrstCrtId(UserUtils.getUser().getUserId());

		// 게시글 등록
		maintenanceMapper.insertMaintenance(maintenanceDto);

		// 파일 업로드
		fileUploadHandlerMaintenance(maintenanceDto, detailImageFiles);
	}


	//운영유지보수 sr 파일업로드
	public void fileUploadHandlerMaintenance(MaintenanceDto maintenanceDto, MultipartFile[] detailImageFiles) {

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
				                    .append(maintenanceDto.getUploadPath())
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
				    MaintenanceFileDto file = new MaintenanceFileDto();

	                file.setBbsId(maintenanceDto.getBbsId());
					file.setOrgnlAtchFileNm(fileName);
	                file.setAtchFileNm(saveFileName.toString());
	                file.setAtchFileExtnNm(extension);
	                file.setAtchFilePathNm(uploadPath.toString());
	                file.setAtchFileSz(multipartFile.getSize());
	                file.setFrstCrtId(UserUtils.getUser().getUserId());

	                maintenanceMapper.insertMaintenanceFile(file);
				}
			}
		}
	}



	//운영유지보수 sr게시글 조회수 증가
	@Override
	public void updateMaintenanceInqCnt(long bbsId) {
		maintenanceMapper.updateMaintenanceInqCnt(bbsId);
	}


	//운영유지보수 sr게시글 상세 조회
	@Override
	public MaintenanceDto getMaintenanceDetail(long bbsId, Model model) {

		MaintenanceDto detail = maintenanceMapper.getMaintenanceDetail(bbsId);

		//권한 가져오기
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

			//	ROLE_ADMIN_1	시스템주담당자 / ROLE_ADMIN_2	시스템부담당자
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
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

		model.addAttribute("fileList", getMaintenancefileList(bbsId));
		model.addAttribute("role", role);
		model.addAttribute("userId", UserUtils.getUser().getUserId());
		return detail;
	}

	@Override
	public List<MaintenanceFileDto> getMaintenancefileList(long bbsId) {
		return maintenanceMapper.getMaintenancefileList(bbsId);
	}


	@Override
	public MaintenanceFileDto getMaintenanceFileDetail(long fileId) {
		return maintenanceMapper.getMaintenanceFileDetail(fileId);
	}

	@Override
	public void deleteMaintenanceFileByFileId(long fileId) {

		long userId = UserUtils.getUser().getUserId();

		MaintenanceFileDto cmntyMaintenanceFileDto = new MaintenanceFileDto();
		cmntyMaintenanceFileDto.setFileId(fileId);
		cmntyMaintenanceFileDto.setLastMdfcnId(userId);

		maintenanceMapper.deleteMaintenanceFileByFileId(cmntyMaintenanceFileDto);
	}

	@Override
	public void updateMaintenance(MaintenanceDto maintenanceDto, @RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles) {

		//수정자 아이디
		long userId = UserUtils.getUser().getUserId();
		maintenanceDto.setLastMdfcnId(userId);

		//게시글 수정
		maintenanceMapper.updateMaintenance(maintenanceDto);

		// 파일 업로드
		fileUploadHandlerMaintenance(maintenanceDto ,detailImageFiles);

	}

	@Override
	@Transactional
	public void deleteMaintenance(long bbsId) {

		long userId = UserUtils.getUser().getUserId();

		MaintenanceDto detail= maintenanceMapper.getMaintenanceDetail(bbsId);
		if(userId != detail.getFrstCrtId()) throw new ApiException("본인 게시글만 삭제 가능합니다.");

		MaintenanceDto maintenanceDto = new MaintenanceDto();
		maintenanceDto.setBbsId(bbsId);
		maintenanceDto.setLastMdfcnId(userId);

		//게시글 첨부파일 삭제
		maintenanceMapper.deleteMaintenanceFileByBbsId(maintenanceDto);

		//게시글 삭제
		maintenanceMapper.deleteMaintenance(maintenanceDto);

	}
}
