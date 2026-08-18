package saleson.shop.designateddonation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.context.ThreadContext;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.ArrayUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import saleson.api.common.enumerated.ApiError;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.context.ShopContext;
import saleson.common.enumeration.FaqType;
import saleson.common.enumeration.IdType;
import saleson.common.enumeration.ProgramName;
import saleson.common.file.infra.FileStorage;
import saleson.common.file.service.CustomFileService;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.banword.BanWordService;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.config.domain.Config;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.domain.DesignatedDonationNotice;
import saleson.shop.designateddonation.domain.DesignatedPart;
import saleson.shop.designateddonation.domain.DesignatedStat;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequest;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequestResult;
import saleson.shop.designateddonation.domain.DsgncntrConfirmLog;
import saleson.shop.designateddonation.domain.DsgncntrConfirmLogParam;
import saleson.shop.designateddonation.domain.PrjImage;
import saleson.shop.designateddonation.domain.PrjImageExplain;
import saleson.shop.designateddonation.domain.PrjNoticeFile;
import saleson.shop.designateddonation.support.DesignatedCntr;
import saleson.shop.designateddonation.support.DsgnCntrManagerRequestSearchParam;
import saleson.shop.designateddonation.support.DesignatedDonationNoticeParam;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.designateddonation.support.DesignatedPartParam;
import saleson.shop.designateddonation.support.LocgovSearchParam;
import saleson.shop.integrationsearch.support.SearchEngine;
import saleson.shop.integrationsearch.support.SearchEngineDsgnDntnBiz;
import saleson.shop.user.LocgovService;
import saleson.shop.user.ManagerRequestService;
import saleson.shop.user.domain.ManagerRequest;
import saleson.shop.user.domain.ManagerRequestResult;

@Slf4j
@Service("designatedDonationService")
public class DesignatedDonationServiceImpl extends EgovAbstractServiceImpl implements DesignatedDonationService {

	@Value("${saleson.noimage.path}")
	private String NOIMAGE_PATH;

//	@Value("${saleson.noimage.path}")
//	public void setNoimagePath(String noimagePath) {
//		NOIMAGE_PATH = noimagePath;
//	}

	@Autowired
	private CodeService codeService;

	@Autowired
	private DesignatedDonationMapper designatedDonationMapper;

	@Autowired
	private LocgovService locgovService;

	@Autowired
    private FileService fileService;

	@Autowired
    private FileStorage fileStorage;

	@Autowired
    private SequenceService sequenceService;

	@Autowired
	private CustomFileService customFileService;

	@Autowired
	private ManagerRequestService managerRequestService;

	@Autowired
	private BanWordService banWordService;

	// 검색엔진 현재 환경
    @Value("${integration.env}")
	private String env;

	// 검색엔진 URL
    @Value("${integration.search-engine-url}")
    private String searchEngineUrl;


	/**
	 * 지정기부 등록/수정
	 */
	@Override
	public void saveDesignatedDonationInfo(DesignatedDonation designatedDonation , MultipartFile[] prjImageFiles) {
		designatedDonation.setFrstRegisterId(UserUtils.getUser().getUserId());
		//List<PrjImage> prjimages;
		//String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
		String locgovCode = designatedDonation.getLocgovCode();
		if (!StringUtils.hasLength(locgovCode)) {
			throw new OpRuntimeException("지자체 정보가 없습니다.");
		}
		if (UserUtils.hasLocgovManagerRole()) {
			if (!locgovCode.equals(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER))) {
				throw new OpRuntimeException("권한이 없습니다.");
			}
		} else if (!UserUtils.hasMasterManagerRole()) {
			throw new OpRuntimeException("권한이 없습니다.");
		}
		designatedDonation.setLocgovCode(locgovCode);

		try {
			String startDate = designatedDonation.getPrjStDt();
			String endDate = designatedDonation.getPrjEdDt();

			String startDay = startDate.substring(6);
			String endDay = endDate.substring(6);

			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");

			LocalDate startDateCheck = LocalDate.parse(startDate, format);
			String startCheckDay = String.format("%02d", startDateCheck.getDayOfMonth());

			LocalDate endDateCheck = LocalDate.parse(endDate, format);
			String endCheckDay = String.format("%02d", endDateCheck.getDayOfMonth());

			if (!(startDay.equals(startCheckDay) && endDay.equals(endCheckDay))) {
				throw new NullPointerException();
			}

			LocalDate maxEndDate = startDateCheck.plusYears(3).minusDays(1);
			if (endDateCheck.isAfter(maxEndDate)) {
				throw new UserException("모금 기간은 최대 3년까지 가능합니다.");
			}
		} catch (NullPointerException | DateTimeParseException e) {
			throw new OpRuntimeException(ApiError.DESIGNATED_DONATION_WRONG_DATE.toString(), ApiError.DESIGNATED_DONATION_WRONG_DATE.getMessage());
		}

		if (designatedDonation.getTargetAmt() > 999999999999l || designatedDonation.getTargetAmt() < 1) {
			throw new OpRuntimeException(ApiError.DESIGNATED_DONATION_WRONG_AMT.toString(), ApiError.DESIGNATED_DONATION_WRONG_AMT.getMessage());
		}

		designatedDonation.setLastUpdusrId(UserUtils.getUser().getUserId());

		if (UserUtils.hasDsgncntrManagerRole()) {			// 지정기부 권한일 경우 대기 상태로 저장
			if ("2".equals(designatedDonation.getPrjStatus())) {
				designatedDonation.setPrjStatus("1");
			}
		} else {
			if ("2".equals(designatedDonation.getPrjStatus())) {			// 지정기부 승인 로그
				if (designatedDonation.getPrjId() == 0) {		// 등록일 경우 로직 마지막에 로그 처리
					designatedDonation.setSelectKey(1);
				} else {
					insertDsgncntrConfirmLog(designatedDonation);
				}
			}
		}

		if (designatedDonation.getPrjId() > 0) {			// 수정시 접근성 내용 삭제
			//designatedDonationMapper.deleteImgDescListByPrjId(designatedDonation.getPrjId());
			designatedDonation.setLastUpdtPnttm(Timestamp.valueOf(LocalDateTime.now()));
			if(!designatedDonation.getPrjImageExplain().isEmpty()) {
	        	designatedDonationMapper.deleteImgDescListByPrjId(designatedDonation.getPrjId());
	        	designatedDonationMapper.insertImgDescList(designatedDonation);
			}else {//이미지 설명이 없으면 제거
				designatedDonationMapper.deleteImgDescListByPrjId(designatedDonation.getPrjId());
	    	}
		} else {
			if (prjImageFiles == null) {
				throw new UserException("이미지를 선택해주세요.");
			}

			designatedDonationMapper.insertDesignatedDonationInfo(designatedDonation);
			if(!designatedDonation.getPrjImageExplain().isEmpty()) {
				designatedDonationMapper.insertImgDescList(designatedDonation);
			}

			designatedDonation = designatedDonationMapper.selectDesignatedDonationInfo(designatedDonation.getPrjId());

			designatedDonation.setLastUpdtPnttm(designatedDonation.getFrstRegistPnttm());
		}
		//insertImgDescList(imgDescList, designatedDonation.getPrjId(), designatedDonation.getLastUpdusrId());			// 접근성 내용 추가

		if (prjImageFiles != null) {
			// TODO :: 이미지 저장 / 기존 이미지 삭제
			// TODO :: 이미지 저장경로 객체에 추가

			//지정기부 접근성 이미지 설명
//			if(!item.getItemImageExplain().isEmpty()) {
//	                itemMapper.deleteItemImagesExplainByItemId(item.getItemId());
//	                item.setUpdatedUserId(UserUtils.getUser().getUserId());
//	                itemMapper.insertItemImagesExplain(item);
//            }
				// 3. 답례품상세이미지
//	            int uploadFileCount = 0;
				// ordering 조회(이미지 순서)
	            int ordering = designatedDonationMapper.getMaxOrderingOfPrjImageByPrjId(designatedDonation.getPrjId());
	            //int uploadFileCount = 0;

	            StringBuilder uploadPath = new StringBuilder();

	            uploadPath
	                    .append(designatedDonation.getUploadPath())
	                    .append(File.separator)
	                    .append(designatedDonation.getPrjId());

	            fileService.makeUploadPath(uploadPath.toString());
	            //designatedDonationMapper.deleteImg(designatedDonation.getPrjId());
	            for (MultipartFile multipartFile : prjImageFiles) {
                	if (ShopUtils.getThumbnailType() == null) {
                		break;
                	}
	                if (multipartFile != null && multipartFile.getSize() > 0) {

	                    StringBuilder saveFileName = new StringBuilder();
	                    BufferedImage bufferedImage = null;
	                    String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);

	                    // 이미지를 사이즈별로 저장[2017-05-31]minae.yun
	                    try {
	                        for (String sizeName : ShopUtils.getThumbnailType()) {
	                            try (InputStream is = multipartFile.getInputStream();) {
	                                bufferedImage = ImageIO.read(is);
	                                if (bufferedImage == null) {
	                                    throw new IOException();
	                                }
	                            } catch (IOException e1) {
	                                log.debug("이미지 읽어오기 실패 : {}", getClass().getName() + " :: saveItem IOException1 ==========");
	                                throw new UserException("이미지 읽어오기 실패");
	                            }

	                            // 2. 파일명
	                            saveFileName.setLength(0);
	                            saveFileName.append(dateNanoTime);
	                            saveFileName.append("_").append(sizeName).append(".").append(FileUtils.getExtension(multipartFile.getOriginalFilename()));

	                            // 3. 저장될 파일
	                            File saveFile = new File(new StringBuilder(uploadPath).append(File.separator).append(saveFileName).toString());
	                            int imageSize = ShopUtils.getImageSize(sizeName);

	                            // 이상우 [2017-03-31 수정] 이미지의 가로 세로 중 작은 쪽이 imageSize보다 크면 true, imageSize+10하면 사이즈 비율에 맞게 조절
	                            if (ShopUtils.checkImageSize2(bufferedImage.getWidth(), bufferedImage.getHeight(), imageSize)) {
	                                try {
	                                    bufferedImage = ShopUtils.getThumbnailImage(bufferedImage, imageSize);
	                                } catch (IOException e) {
	                                    log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException2 ==========");
	                                    throw new UserException("썸네일 생성중 오류");
	                                }
	                            }
	                            try (ByteArrayOutputStream output = new ByteArrayOutputStream();) {
		                            output.flush();
		                            ImageIO.write(bufferedImage, FileUtils.getExtension(multipartFile.getOriginalFilename()), output);
//		                            output.close();
		                            fileStorage.upload(output.toByteArray(), saveFile);
	                            } catch (IOException e) {
	                                log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException3 ==========");
	                            	throw new IOException(e);
	                            }
	                        }
	                    } catch (IOException e) {
	                        log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException4 ==========");
	                        throw new UserException("썸네일 생성중 오류");
	                    }



		                if (designatedDonation.getPrjImage() == null) {
		                	designatedDonation.setPrjImage(saveFileName.toString());
		                }
	                    PrjImage prjImage = new PrjImage();

//	                    prjImage.setDsgncntrPrjImageId(sequenceService.getId("G_DSGNCNTR_PRJ_IMG"));
	                    prjImage.setPrjId(designatedDonation.getPrjId());
	                    prjImage.setImageName(saveFileName.toString());
	                    prjImage.setOrdering(ordering);
	                    prjImage.setFrstRegisterId(UserUtils.getUser().getUserId());

	                    designatedDonationMapper.insertPrjImage(prjImage);

	                    // 첫 번째 이미지가 목록에 출력
	                    //if (!hasItemImage && uploadFileCount == 0) {
	                    //if (uploadFileCount == 0) {
	                    //	designatedDonation.setPrjImage(saveFileName.toString());
	                    //}

	                    //uploadFileCount++;
	                    ordering++;
	                }
	            }
	        }

//		designatedDonation.setPrjImageExplain(designatedDonationMapper.selectImgDescListByPrjId(designatedDonation.getPrjId()));
//		//접근성 관련 이미지 설명 insert
//        if(!designatedDonation.getPrjImageExplain().isEmpty()) {
//        	designatedDonationMapper.deleteImgDescListByPrjId(designatedDonation.getPrjId());
//            //designatedDonation.setUpdatedUserId(UserUtils.getUser().getUserId());
//        	designatedDonationMapper.insertImgDescList(designatedDonation);
//        }else {//이미지 설명이 없으면 제거
//        	designatedDonationMapper.deleteImgDescListByPrjId(designatedDonation.getPrjId());
//        }

		//대표 이미지 변경
		List<PrjImage> prjImages = designatedDonationMapper.selectImgListByPrjId(designatedDonation.getPrjId());

		if (prjImages.size() > 0) {//기존에 등록된 이미지가 있고
			if (prjImageFiles != null) {//새로 등록하는 이미지가 있으면
				designatedDonation.setPrjImage(prjImages.get(0).getImageName());
	        }
		}


		// 업데이트(오류 발생시 불필요 파일 쌓이는 상황 줄이기위해 이미지 저장 마지막으로 처리...)
		designatedDonationMapper.updateDesignatedDonationInfo(designatedDonation);

		if (designatedDonation.getSelectKey() == 1) {
			insertDsgncntrConfirmLog(designatedDonation);
		}

	}

	/**
	 * 지정기부 실제 이미지 삭제
	 */
    @Override
    public void deletePrjImageById(int prjImageId) {

        PrjImage prjImage = designatedDonationMapper.selectPrjImageByImageId(prjImageId);

        if (prjImage != null) {

            // 1. 이미지 파일 삭제.
            if (prjImage.getImageName().indexOf("/") > -1) {
                //fileStorage.delete((FileUtils.getWebRootPath() + ShopUtils.unescapeHtml(itemImage.getImageName())).replaceAll("/",  File.separator));
            } else {
                String detailsUploadBase = "/prj/" + prjImage.getPrjId();

                //썸네일 사이즈별 이미지 삭제 [2017-06-01] minae.yun
                for (String size : ShopUtils.getThumbnailType()) {

                    String imageName = prjImage.getImageName().substring(0, 18);
                    String detailsImage = "";

                    //파일이름에 괄호()가 있을 경우
                    if (prjImage.getImageName().contains("(") && prjImage.getImageName().contains(")")) {
                        int startIndex = prjImage.getImageName().lastIndexOf("(");
                        int endIndex = prjImage.getImageName().lastIndexOf(")");
                        String subName = prjImage.getImageName().substring(startIndex, endIndex+1);
                        detailsImage = detailsUploadBase + "/" + ShopUtils.unescapeHtml(imageName) + size + subName + "." + FileUtils.getExtension(prjImage.getImageName());
                    } else {
                        detailsImage = detailsUploadBase + "/" + ShopUtils.unescapeHtml(imageName) + "_" +size + "." + FileUtils.getExtension(prjImage.getImageName());
                    }

                    fileStorage.delete(detailsImage);
                }

				/*
				String detailsUploadBase = "/item/" + itemImage.getItemUserCode() + "/details";
				String detailsImage = detailsUploadBase + "/" + itemImage.getImageName();
				String detailsBigImage = detailsUploadBase + "/big/" + itemImage.getImageName();
				String detailsThumbImage = detailsUploadBase + "/thumb/" + itemImage.getImageName();

				fileStorage.delete(detailsImage);
				fileStorage.delete(detailsBigImage);
				fileStorage.delete(detailsThumbImage);
				*/

            }

            // 2. 이미지 파일 정보 삭제.
            designatedDonationMapper.deleteImgByImageId(prjImageId);

            // 3. 대표 이미지 설정.
            List<PrjImage> prjImages = designatedDonationMapper.selectImgListByPrjId(prjImage.getPrjId());
            DesignatedDonation designatedDonation = new DesignatedDonation();
            //수정
//            PrjImage prjImage2 = new PrjImage();
//            item.setItemId(itemImage.getItemId());
//
            if (prjImages.isEmpty()) {
            	designatedDonation.setPrjImage("");
            } else {
            	designatedDonation.setPrjImage(prjImages.get(0).getImageName());
            	designatedDonation.setPrjId(prjImage.getPrjId());
            }
            designatedDonationMapper.updatePrjImageName(designatedDonation);

        }

    }


    /**
	 * 지정기부 목록 조회
	 */
	@Override
	public List<DesignatedDonation> selectDesignatedDonationList(DesignatedDonationSearchParam designatedDonationSearchParam) {
		if (ShopUtils.isOpmanagerPage()) {
			long userId = UserUtils.getUser().getUserId();
			if (userId == 0) {
				throw new UserException("로그인 상태가 아닙니다.");
			}

			if (UserUtils.hasLocgovManagerRole()) {
				if (!"FRONT".equals(designatedDonationSearchParam.getDisplay())) {
					String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
					if (StringUtils.hasLength(locgovCode)) {
						designatedDonationSearchParam.setLocgovCode(locgovCode);
					}
				}
			} else if (!UserUtils.hasMasterManagerRole()) {
				throw new OpRuntimeException("권한이 없습니다.");
			}
		}

		int designatedDonationCount = designatedDonationMapper.selectDesignatedDonationListCount(designatedDonationSearchParam);

		int itemsPerPage = designatedDonationSearchParam.getItemsPerPage();
		if (itemsPerPage < 10) {
			itemsPerPage = 10;
		}

		Pagination pagination = Pagination.getInstance(designatedDonationCount, itemsPerPage);

		pagination.setItemsPerPage(itemsPerPage);
		designatedDonationSearchParam.setPagination(pagination);

		return designatedDonationMapper.selectDesignatedDonationList(designatedDonationSearchParam);
	}

	/**
	 * 지정기부 목록 조회 - 검색엔진
	 */
	@Override
	public List<DesignatedDonation> selectDesignatedDonationListBySearchEngine(DesignatedDonationSearchParam designatedDonationSearchParam) {
		if (ShopUtils.isOpmanagerPage()) {
			long userId = UserUtils.getUser().getUserId();
			if (userId == 0) {
				throw new UserException("로그인 상태가 아닙니다.");
			}

			if (UserUtils.hasLocgovManagerRole()) {
				if (!"FRONT".equals(designatedDonationSearchParam.getDisplay())) {
					String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
					if (StringUtils.hasLength(locgovCode)) {
						designatedDonationSearchParam.setLocgovCode(locgovCode);
					}
				}
			} else if (!UserUtils.hasMasterManagerRole()) {
				throw new OpRuntimeException("권한이 없습니다.");
			}
		}

		// 1. designatedDonationCount = 검색엔진 tocalCount로 대체
		// 2. 검색엔진 Call을 한 뒤 Pagination.getInstance(totalCount, ~~)
		// 3. List<DesinatedDonation> 형태로 리턴
		List<DesignatedDonation> dsgndntnList = null;

		try {
			SearchEngine<DesignatedDonationSearchParam> se = new SearchEngineDsgnDntnBiz();
			Map<String, Object> result = se.requestApi(se.createParamter(designatedDonationSearchParam), searchEngineUrl);
			dsgndntnList = (List<DesignatedDonation>) result.get("dsgndntnList");

			/*
			 * main.html 접근 시 'sort = RANDOM'으로 들어옴
			 * 검색엔진에는 order by RANDOM 같은 것이 없어, 전체 레코드 조회 후 셔플
			 * */
			String sortType = designatedDonationSearchParam.getSort();
			if(sortType != null && "RANDOM".equals(sortType)) {
				Collections.shuffle(dsgndntnList);
			}

			int itemsPerPage = designatedDonationSearchParam.getItemsPerPage();
			if (itemsPerPage < 10) {
				itemsPerPage = 10;
			}

			Pagination pagination = Pagination.getInstance((int) result.get("totalCount"), itemsPerPage);

			pagination.setItemsPerPage(itemsPerPage);
			designatedDonationSearchParam.setPagination(pagination);

		}catch(Exception e) {
			log.error("■■■■■■■■■Now Application■■■■■■■■■ : " + env);
        	log.error("■■■■■■■■■SearchEngineDsgnDntnBiz Error■■■■■■■■■ : " + e.toString());
		}

		return dsgndntnList;
	}

	/**
	 * 지정기부 사업구분 코드 조회
	 */
	@Override
	public List<Code> getDesignatedDonationBsnsTypes() {
		CodeParam param = new CodeParam();
		param.setCodeType("BUSINESS_TYPE");
		return codeService.getCodeList(param);
	}


	/**
	 * 지정기부 썸네일 호출
	 * @author park
	 * @param itemUserCode
	 * @param fileName
	 * @param sizeName
	 * @return
	 */
	public String loadPrjImage(String itemUserCode, String fileName, String sizeName) {
		//상품에 등록된 이미지가 없을경우
		if (!StringUtils.hasText(fileName) || !fileName.contains(".")) {
			return ShopUtils.getNoImagePath();
		}

		// 상품 이미지명에 경로가 포함된 경우
		if (fileName.indexOf("/") > -1) {
			return fileName;
		}

		//호출한 사이즈가 존재하는지 확인
//		if (!Arrays.stream(getThumbnailType()).anyMatch(type -> sizeName.equals(type))) {
//			return ShopUtils.getNoImagePath();
//		}

		//Config shopConfig = getConfig();
		//StringBuilder targetImageSrc = new StringBuilder();

		StringBuilder imageSrc = new StringBuilder(SalesonProperty.getSalesonUrlCdn());
		imageSrc.append(getImageFilePath("prj", itemUserCode, fileName, sizeName));

		//if (!"1".equals(shopConfig.getThumbnailType())) {	// 호출시 썸네일 리사이즈
			imageSrc.insert(0, "/thumbnail?src=");
			imageSrc.append("&size=").append(getImageSize(sizeName));
		//}

		return imageSrc.toString();

	}

	public static StringBuilder getImageFilePath(String prefix, String code, String fileName, String sizeName) {
		StringBuilder path = new StringBuilder();
		path.append(SalesonProperty.getUploadBaseFolder());
		path.append("/").append(prefix).append("/").append(code).append("/");

		if (StringUtils.hasText(sizeName) && fileName.contains("_") && fileName.contains(".")) {
			path.append(fileName, 0, fileName.lastIndexOf("_") + 1);	// ex: picture123_
			path.append(sizeName);
			path.append(fileName, fileName.lastIndexOf("."), fileName.length());	// ex: .png
		} else {
			path.append(fileName);
		}

		return path;
	}

	/**
	 * 이미지 없을때 보이는 이미지경로
	 * @return
	 */
	public String getNoImagePath() {
		return NOIMAGE_PATH;
	}

	/**
	 * 지정기부 이미지 파일 경로 리턴
	 * @author park
	 * @param itemUserCode
	 * @param fileName
	 * @param sizeName
	 * @return
	 */
	public static StringBuilder getPrjImageFilePath(String prefix, String code, String fileName, String sizeName) {
		StringBuilder path = new StringBuilder();
		path.append(SalesonProperty.getUploadBaseFolder());
		path.append("/").append(prefix).append("/").append(code).append("/");

		if (StringUtils.hasText(sizeName) && fileName.contains("_") && fileName.contains(".")) {
			path.append(fileName, 0, fileName.lastIndexOf("_") + 1);	// ex: picture123_
			path.append(sizeName);
			path.append(fileName, fileName.lastIndexOf("."), fileName.length());	// ex: .png
		} else {
			path.append(fileName);
		}

		return path;
	}

	/**
	 * 지정기부 상태 코드 조회
	 */
	@Override
	public List<Code> getDesignatedDonationPrjStatus() {
		CodeParam param = new CodeParam();
		param.setCodeType("PRJ_STATUS");
		return codeService.getCodeList(param);
	}

	/**
	 * 지정기부 상세내역 조회
	 */
	@Override
	public DesignatedDonation selectDesignatedDonationDetail(DesignatedDonationSearchParam designatedDonationSearchParam) {
		if (ShopUtils.isOpmanagerPage()) {
			if (UserUtils.hasLocgovManagerRole() && !UserUtils.hasOfflineRole()) {
				long userId = UserUtils.getUser().getUserId();
				String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
				if (StringUtils.hasLength(locgovCode)) {
					designatedDonationSearchParam.setLocgovCode(locgovCode);
				} else {
					throw new OpRuntimeException("권한이 없습니다.");
				}
			}
		}

		DesignatedDonation designatedDonation = designatedDonationMapper.selectDesignatedDonationDetail(designatedDonationSearchParam);

		if (designatedDonation == null || designatedDonation.getPrjId() == 0) {
			return null;
		}

		selectDesignatedCntrList(designatedDonation, designatedDonationSearchParam);

		List<PrjImage> images = designatedDonationMapper.selectImgListByPrjId(designatedDonation.getPrjId());

		designatedDonation.setPrjImages(images);
//		designatedDonation.setPrjImageExplain(designatedDonationMapper.selectImgDescListByPrjId(designatedDonation.getPrjId()));

		return designatedDonation;
	}

	/**
	 * 상점 설정 정보를 리턴한다.
	 */
	public Config getConfig() {
		ShopContext shopContext = (ShopContext) ThreadContext.get(ShopContext.REQUEST_NAME);
		return shopContext.getConfig();
	}

	/**
	 * 관리자 썸네일 설정에서 입력한 값으로 이미지 사이즈를 리턴
	 * @author [2017-06-09]minae.yun
	 * @param sizeName
	 * @return
	 */
	public final int L = 1200;

	public int getImageSize(String sizeName) {

		//호출한 사이즈가 존재하는지 확인
		boolean type = false;
//		for (String inputSize : getThumbnailType()) {
//			if (sizeName.equals(inputSize)) type = true;
//		}

		//제일 큰 사이즈 default
		if (!type) return L;

//		int imageSize = 0;
		int imageSize = L;
		Config shopConfig = getConfig();
		String jsonSize = shopConfig.getThumbnailSize(); //json 데이터

//		if (jsonSize.isEmpty()) {
//			return L;
//		}

		JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonSize);  //json 파싱
		JSONArray infoArray = (JSONArray) jsonObject.get("list"); //list의 배열을 추출

		try {
			for(int i=0; i<infoArray.size(); i++){
				//배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
				JSONObject listObject = (JSONObject) infoArray.get(i);

				//list배열 안에 데이터으로 추출
				String listSizeName = (String) listObject.get("sizeName");
				int listSize = Integer.parseInt((String)listObject.get("size"));

				if (sizeName.equals(listSizeName)) imageSize = listSize;
			}

		} catch (JSONException e) {
			imageSize = L; //제일 큰 사이즈 default
		}

		return imageSize;
	}

	/**
	 * 지정기부 정보 조회
	 * param : prjId
	 */
//	public DesignatedDonation selectDesignatedDonation(DesignatedDonation designatedDonation){
//		designatedDonation = designatedDonationMapper.selectDesignatedDonationInfo(designatedDonation.getPrjId());
//		designatedDonation.setPrjImages(designatedDonationMapper.selectImgListByPrjId(designatedDonation.getPrjId()));
//		return designatedDonation;
//	}

	/**
	 * 지정기부 기부내역 조회
	 */
	@Override
	public DesignatedDonation selectDesignatedCntrList(DesignatedDonation designatedDonation, DesignatedDonationSearchParam designatedDonationSearchParam) {
		if (designatedDonation == null) {
			designatedDonation = new DesignatedDonation();
		}
		if (ShopUtils.isOpmanagerPage()) {
			long userId = UserUtils.getUser().getUserId();
			if (userId == 0) {
				throw new UserException("로그인 상태가 아닙니다.");
			}
			String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
			if (StringUtils.hasLength(locgovCode)) {
				designatedDonationSearchParam.setLocgovCode(locgovCode);
			}
		}

		int totalCnt = designatedDonationMapper.selectDesignatedCntrCnt(designatedDonationSearchParam);

		Pagination pagination = Pagination.getInstance(totalCnt);
		pagination.setCurrentPage(designatedDonationSearchParam.getPage());
		if (pagination.getCurrentPage() < 1) {
			pagination.setCurrentPage(1);
		}
		pagination.setItemsPerPage(designatedDonationSearchParam.getItemsPerPage());
		designatedDonationSearchParam.setPagination(pagination);

		List<DesignatedCntr> designatedCntrList = designatedDonationMapper.selectDesignatedCntrList(designatedDonationSearchParam);
		for (DesignatedCntr designatedCntr : designatedCntrList) {
			if (StringUtils.hasLength(banWordService.getCheckedBanWord(designatedCntr.getCheerMsg()))) {
				designatedCntr.setCheerMsg("부적절한 단어가 포함되었습니다.");
			}
		}
		designatedDonation.setCntrList(designatedCntrList);

		return designatedDonation;
	}

	/**
	 * 지지정기부 지자체별 통계 집계
	 */
	@Override
	public DesignatedStat selectDesignatedLocgovStat(
			LocgovSearchParam locgovSearchParam) {
		long userId = UserUtils.getUser().getUserId();
		if (userId == 0) {
			throw new UserException("로그인 상태가 아닙니다.");
		}
		String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
		if (StringUtils.hasLength(locgovCode)) {
			locgovSearchParam.setShLocgovCode(locgovCode);
		}

		return designatedDonationMapper.selectDesignatedLocgovStat(locgovSearchParam);
	}

	/**
	 * 지정기부 지자체별 기부 집계
	 */
	@Override
	public List<DesignatedStat> selectDesignatedLocgovCntrStat(
			LocgovSearchParam locgovSearchParam) {
		long userId = UserUtils.getUser().getUserId();
		if (userId == 0) {
			throw new UserException("로그인 상태가 아닙니다.");
		}
		String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
		if (StringUtils.hasLength(locgovCode)) {
			locgovSearchParam.setShLocgovCode(locgovCode);
		}
		if (UserUtils.hasDsgncntrManagerRole()) {
			locgovSearchParam.setDsgncntrPartId(checkDsgncntrAuthPartInfo());
		}
		return designatedDonationMapper.selectDesignatedLocgovCntrStat(locgovSearchParam);
	}

	/**
	 * 지정기부 이미지 설명 조회
	 */
	@Override
	public List<PrjImageExplain> selectImgDescListByPrjId(long prjId) {
		List<PrjImageExplain> prjImageExplain = designatedDonationMapper.selectImgDescListByPrjId(prjId);
		return prjImageExplain;
    }

	/**
	 * 사업별 갬페인 진행건 비율 조회
	 */
	@Override
	public List<DesignatedStat> selectDesignatedLocgovMonthCampaign(LocgovSearchParam locgovSearchParam) {
		long userId = UserUtils.getUser().getUserId();
		if (userId == 0) {
			throw new UserException("로그인 상태가 아닙니다.");
		}
		String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
		if (StringUtils.hasLength(locgovCode)) {
			locgovSearchParam.setShLocgovCode(locgovCode);
		}
		if (UserUtils.hasDsgncntrManagerRole()) {
			locgovSearchParam.setDsgncntrPartId(checkDsgncntrAuthPartInfo());
		}

		return designatedDonationMapper.selectDesignatedLocgovMonthCampaign(locgovSearchParam);
	}

	/**
	 * 모금액 비율 조회
	 */
	@Override
	public List<DesignatedStat> selectDesignatedLocgovMonthAmountRaised(LocgovSearchParam locgovSearchParam) {
		long userId = UserUtils.getUser().getUserId();
		if (userId == 0) {
			throw new UserException("로그인 상태가 아닙니다.");
		}
		String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
		if (StringUtils.hasLength(locgovCode)) {
			locgovSearchParam.setShLocgovCode(locgovCode);
		}
		if (UserUtils.hasDsgncntrManagerRole()) {
			locgovSearchParam.setDsgncntrPartId(checkDsgncntrAuthPartInfo());
		}
		return designatedDonationMapper.selectDesignatedLocgovMonthAmountRaised(locgovSearchParam);
	}

	/**
	 * 모금액 추이
	 */
	@Override
	public List<DesignatedStat> selectDesignatedLocgovMonthAmount(LocgovSearchParam locgovSearchParam) {
		long userId = UserUtils.getUser().getUserId();
		if (userId == 0) {
			throw new UserException("로그인 상태가 아닙니다.");
		}
		String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
		if (StringUtils.hasLength(locgovCode)) {
			locgovSearchParam.setShLocgovCode(locgovCode);
		}
		if (UserUtils.hasDsgncntrManagerRole()) {
			locgovSearchParam.setDsgncntrPartId(checkDsgncntrAuthPartInfo());
		}
		return designatedDonationMapper.selectDesignatedLocgovMonthAmount(locgovSearchParam);
	}

	/**
	 * 지정기부 지자체별 기부 집계 리스트 카운트
	 */
	@Override
	public int selectDesignatedLocgovCntrStatCount(LocgovSearchParam locgovSearchParam) {
		long userId = UserUtils.getUser().getUserId();
		if (userId == 0) {
			throw new UserException("로그인 상태가 아닙니다.");
		}
		String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
		if (StringUtils.hasLength(locgovCode)) {
			locgovSearchParam.setShLocgovCode(locgovCode);
		}
		if (UserUtils.hasDsgncntrManagerRole()) {
			locgovSearchParam.setDsgncntrPartId(checkDsgncntrAuthPartInfo());
		}
		return designatedDonationMapper.selectDesignatedLocgovCntrStatCount(locgovSearchParam);
	}

	/**
	 * 지정기부 지자체별 상세내역
	 */
	@Override
	public DesignatedStat selectDesignatedLocgovCntrDetail(LocgovSearchParam locgovSearchParam) {
		return designatedDonationMapper.selectDesignatedLocgovCntrDetail(locgovSearchParam);
	}

	/**
	 * 지정기부 상태값 변경
	 */
	@Override
	public void updateListDataByLabel(DesignatedDonation designateddonation) {
		if (designateddonation.getId() != null) {
			for (int i = 0; i < designateddonation.getId().length; i++) {
				DesignatedDonation searchParam = new DesignatedDonation();
				searchParam.setPrjId(Integer.parseInt(ArrayUtils.get(designateddonation.getId(), i)));
//				searchParam.setLastUpdusrId(UserUtils.getManagerId());
				searchParam.setLastUpdusrId(UserUtils.getUser().getUserId());
				searchParam.setPrjStatus(designateddonation.getPrjStatus());
				searchParam.setDisplayFlag(designateddonation.getDisplayFlag());

				// TODO : 승인 처리 로그
				if ("2".equals(searchParam.getPrjStatus())) {
					insertDsgncntrConfirmLog(searchParam);
				}

				designatedDonationMapper.updatePrjLabelByListParam(searchParam);

            }
		}
	}

	/**
	 * 지정기부 하위코드(부문) 조회
	 */
	@Override
	public List<Code> getDesignatedDonationBsnsSubTypes(String upId) {
		CodeParam codeParam = new CodeParam();
		codeParam.setUpId(upId);
		codeParam.setCodeType("BUSINESS_SUB_TYPE");
		return codeService.getCodeList(codeParam);
	}

	/**
	 * 지정기부 응원 메시지 저장
	 */
	@Override
	public int saveCheerMsg(DesignatedCntr designatedCntr) {
		DesignatedCntr checkData = designatedDonationMapper.selectCntrInfo(designatedCntr);

		if (checkData == null || !StringUtils.hasLength(checkData.getCntrSn())) {
			throw new UserException("기부정보가 올바르지 않습니다.");
		}

		if (StringUtils.hasLength(designatedCntr.getCheerMsg())) {
			int length = designatedCntr.getCheerMsg().length();
			if (length > 30) {
				throw new UserException("30자 까지 입력가능합니다.");
			}
			if (StringUtils.hasLength(banWordService.getCheckedBanWord(designatedCntr.getCheerMsg()))) {
				throw new UserException("사용불가능한 단어가 포함되었습니다.");
			}
		}

		return designatedDonationMapper.saveCheerMsg(designatedCntr);
	}

	/**
	 * 지정기부 공지사항 목록 조회
	 */
	@Override
	public List<DesignatedDonationNotice> selectPrjNoticeList(DesignatedDonationSearchParam designatedDonationSearchParam) {
		if (ShopUtils.isOpmanagerPage()) {
			if (UserUtils.hasLocgovManagerRole()) {
				designatedDonationSearchParam.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			}
			DesignatedDonation designatedDonation = designatedDonationMapper.selectDesignatedDonationDetail(designatedDonationSearchParam);

			if (designatedDonation == null || designatedDonation.getPrjId() == 0) {
				return new ArrayList<>();
			}
		}

		int noticeCnt = designatedDonationMapper.selectDesignatedDonationNoticeListCnt(designatedDonationSearchParam);

		Pagination pagination = Pagination.getInstance(noticeCnt, designatedDonationSearchParam.getItemsPerPage());
		pagination.setCurrentPage(designatedDonationSearchParam.getPage());
		designatedDonationSearchParam.setPagination(pagination);

		return designatedDonationMapper.selectDesignatedDonationNoticeList(designatedDonationSearchParam);
	}

	@Override
	public DesignatedDonationNotice selectPrjNoticeDetail(DesignatedDonationNoticeParam designatedDonationNoticeParam) {
		if (UserUtils.hasLocgovManagerRole()) {
			designatedDonationNoticeParam.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}
		DesignatedDonationNotice designatedDonationNotice = designatedDonationMapper.selectDesignatedDonationNoticeDetail(designatedDonationNoticeParam);

		designatedDonationNotice.setPrjNoticeImageExplains(designatedDonationMapper.selectDesignatedDonationNoticeImgDescList(designatedDonationNotice.getPrjNoticeId()));
		designatedDonationNotice.setPrjFiles(designatedDonationMapper.selectDesignatedDonationNoticeFileList(designatedDonationNotice.getPrjNoticeId()));

		return designatedDonationNotice;
	}

	@Override
	public int savePrjNoticeInfo(DesignatedDonationNotice designatedDonationNotice) {
		long userId = UserUtils.getUser().getUserId();
		if (userId == 0) {
			throw new UserException("로그인 상태가 아닙니다.");
		}
		DesignatedDonationSearchParam param = new DesignatedDonationSearchParam();
		param.setPrjId(designatedDonationNotice.getPrjId());
		if (UserUtils.hasLocgovManagerRole()) {
			param.setLocgovCode(locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER));
		}
		DesignatedDonation designatedDonation = designatedDonationMapper.selectDesignatedDonationDetail(param);

		if (designatedDonation == null || designatedDonation.getPrjId() == 0) {			// 해당 지정기부 권한 체크
			throw new UserException("권한이 없습니다.");
		}

		int resultCnt = 0;
		if (designatedDonationNotice.getPrjNoticeId() == 0) {		// 등록
			resultCnt = designatedDonationMapper.insertDesignatedDonationNotice(designatedDonationNotice);
		} else {		// 수정
			resultCnt = designatedDonationMapper.updateDesignatedDonationNotice(designatedDonationNotice);
		}

		// 상세내용 이미지 설명 전체 삭제 후 전체 저장
		designatedDonationMapper.deleteDesignatedDonationNoticeImgDescList(designatedDonationNotice.getPrjNoticeId());
		if (designatedDonationNotice.getPrjNoticeImageExplains() != null && !designatedDonationNotice.getPrjNoticeImageExplains().isEmpty()) {
			designatedDonationMapper.insertDesignatedDonationNoticeImgDesc(designatedDonationNotice);
		}

		// 업로드 파일 있을 경우
		if (designatedDonationNotice.getPrjNoticeFiles() != null) {
			List<PrjNoticeFile> storeFileList = new ArrayList<>();
			try {
				PrjNoticeFile fileCnt = designatedDonationMapper.getDesignatedDonationNoticeFileMaxSequence(designatedDonationNotice.getPrjNoticeId());
				for (MultipartFile multipartFile : designatedDonationNotice.getPrjNoticeFiles()) {
					if (StringUtils.hasLength(multipartFile.getOriginalFilename())) {
						PrjNoticeFile prjNoticeFile = new PrjNoticeFile();
						fileCnt.setFileSeq(fileCnt.getFileSeq() + 1);
						String pathName = customFileService.getFilePathForSave(ProgramName.PRJ_NOTICE.getProgramPath(), designatedDonationNotice.getPrjNoticeId());
						String savedFileName = customFileService.saveFileByProgramNameId(ProgramName.PRJ_NOTICE.getProgramPath(), designatedDonationNotice.getPrjNoticeId(), multipartFile);
						prjNoticeFile.setFileName(savedFileName);
						prjNoticeFile.setFileSeq(fileCnt.getFileSeq());
						prjNoticeFile.setOrgFileName(multipartFile.getOriginalFilename());
						prjNoticeFile.setPrjNoticeId(designatedDonationNotice.getPrjNoticeId());
						prjNoticeFile.setPathName(pathName);
						storeFileList.add(prjNoticeFile);
					}
				}
				if (!storeFileList.isEmpty()) {
					designatedDonationMapper.insertDesignatedDonationNoticeFile(storeFileList);
				}
			} catch (OpRuntimeException | IOException e) {		// 오류 발생할 경우 파일 삭제 및 롤백 처리
				for (PrjNoticeFile savePrjNoticeFile : storeFileList) {
					customFileService.deleteFileByProgramNameId("prjNotice", designatedDonationNotice.getPrjNoticeId(), savePrjNoticeFile.getFileName());
				}
				log.error(getClass().getName() + " savePrjNoticeInfo error :: ", e);
				throw new OpRuntimeException("저장에 실패했습니다.");
			}
		}

		return resultCnt;		// 1 이면 정상 등록 판단
	}

	// 공지사항 파일 삭제
	@Override
	public int deletePrjNoticeFile(DesignatedDonationNoticeParam param) {
		long userId = UserUtils.getUser().getUserId();
		if (userId == 0) {
			throw new UserException("로그인 상태가 아닙니다.");
		}
		DesignatedDonationSearchParam searchParam = new DesignatedDonationSearchParam();
		searchParam.setPrjId(param.getPrjId());
		if (UserUtils.hasLocgovManagerRole()) {
			searchParam.setLocgovCode(locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER));
		}
		DesignatedDonation designatedDonation = designatedDonationMapper.selectDesignatedDonationDetail(searchParam);

		if (designatedDonation == null || designatedDonation.getPrjId() == 0) {			// 해당 지정기부 권한 체크
			throw new UserException("권한이 없습니다.");
		}

		PrjNoticeFile prjNoticeFile = designatedDonationMapper.selectDesignatedDonationNoticeFile(param);

		String fileName = prjNoticeFile.getFileName();

		int result = designatedDonationMapper.deleteDesignatedDonationNoticeFile(param);

		customFileService.deleteFileByProgramNameId(ProgramName.PRJ_NOTICE.getProgramPath(), param.getPrjNoticeId(), fileName);

		return result;
	}

	@Override
	public boolean hasDesignatedDonationAuth(long prjId) {
		DesignatedDonationSearchParam param = new DesignatedDonationSearchParam();
		param.setPrjId(prjId);
		long userId = UserUtils.getUser().getUserId();
		if (userId > 0) {
			if (UserUtils.hasLocgovManagerRole()) {
				param.setLocgovCode(locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER));
			}
			int cnt = designatedDonationMapper.selectDesignatedDonationListCount(param);
			if (cnt > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ResponseEntity<byte[]> downloadPrjNoticeFile(DesignatedDonationNoticeParam param) {

		PrjNoticeFile prjNoticeFile = designatedDonationMapper.selectDesignatedDonationNoticeFile(param);
		if (prjNoticeFile != null && prjNoticeFile.getPrjNoticeId() > 0) {
			try {
				return customFileService.getFileDownloadDataByProgramNameIdFileOrgName(ProgramName.PRJ_NOTICE.getProgramPath(), prjNoticeFile.getPrjNoticeId(), prjNoticeFile.getFileName(), prjNoticeFile.getOrgFileName());
			} catch (IOException e) {
				log.error(getClass().getName() + " downloadPrjNoticeFile error :: " , e);
				throw new UserException("파일이 존재하지 않습니다.");
			}
		}
		throw new UserException("파일이 존재하지 않습니다.");
	}

	@Override
	public List<DesignatedDonationNotice> selectPrjNoticeListFront (
			DesignatedDonationSearchParam designatedDonationSearchParam) {

		List<DesignatedDonationNotice> searchList = selectPrjNoticeList(designatedDonationSearchParam);

		if (searchList != null && !searchList.isEmpty()) {
			for (DesignatedDonationNotice designatedDonationNotice : searchList) {
				designatedDonationNotice.setPrjFiles(designatedDonationMapper.selectDesignatedDonationNoticeFileList(designatedDonationNotice.getPrjNoticeId()));
			}
		}

		return searchList;
	}




	@Override
	public List<DsgnCntrManagerRequest> getManagerRequestHistory(DsgnCntrManagerRequestSearchParam searchParam) {
		List<DsgnCntrManagerRequest> result = new ArrayList<>();
		List<ManagerRequest> searchList = managerRequestService.getManagerRequestHistory(searchParam);
		for (ManagerRequest managerRequest : searchList) {
			DsgnCntrManagerRequest dsgnCntrManagerRequest = new DsgnCntrManagerRequest();
			dsgnCntrManagerRequest.setExtendsCopy(managerRequest);
			result.add(dsgnCntrManagerRequest);
		}
		return result;
	}

	@Override
	public int getManagerRequestDetailsAuthCount(DsgnCntrManagerRequestSearchParam searchParam) {
		searchParam.setConditionType(FaqType.F_CNTR_DESIGNATED.name());
		return managerRequestService.getManagerRequestDetailsAuthCount(searchParam);
	}

	@Override
	public DsgnCntrManagerRequestResult updateManagerRequestReject(DsgnCntrManagerRequest dsgnCntrManagerRequest) {
		DsgnCntrManagerRequestResult dsgnCntResult = new DsgnCntrManagerRequestResult();

		ManagerRequest param = new ManagerRequest();
		param.setUserId(dsgnCntrManagerRequest.getUserId());
		param.setReqstSn(dsgnCntrManagerRequest.getReqstSn());
		param.setLastUpdusrId(UserUtils.getUser().getUserId());
		param.setConfmSttusCode(dsgnCntrManagerRequest.getConfmSttusCode());
		param.setRejectResn(dsgnCntrManagerRequest.getRejectResn());

		ManagerRequestResult result = managerRequestService.updateManagerRequestReject(param);

		dsgnCntResult.setCode(result.getCode());
		dsgnCntResult.setStatusCode(result.getStatusCode());

		return dsgnCntResult;
	}

	@Override
	public DsgnCntrManagerRequestResult updateManagerRequestApproval(DsgnCntrManagerRequest dsgnCntrManagerRequest) {
		DsgnCntrManagerRequestResult dsgnManagerApprovalResult = new DsgnCntrManagerRequestResult();

		ManagerRequest param = new ManagerRequest();
		param.setUserId(dsgnCntrManagerRequest.getUserId());
		param.setReqstSn(dsgnCntrManagerRequest.getReqstSn());
		param.setLastUpdusrId(UserUtils.getUser().getUserId());
		param.setConfmSttusCode(dsgnCntrManagerRequest.getConfmSttusCode());
		param.setRejectResn(dsgnCntrManagerRequest.getRejectResn());

		ManagerRequestResult result = managerRequestService.updateManagerRequestApproval(param);

		dsgnManagerApprovalResult.setCode(result.getCode());
		dsgnManagerApprovalResult.setStatusCode(result.getStatusCode());

		if (dsgnCntrManagerRequest.getDsgncntrPartId() > 0) {
			designatedDonationMapper.updateManagerRequestPartName(dsgnCntrManagerRequest);
		}

		if (dsgnCntrManagerRequest.getDsgncntrPartId() > 0 && "SUCC".equals(result.getCode())) {
			DesignatedPart designatedPart = new DesignatedPart();
			designatedPart.setPartUserId(dsgnCntrManagerRequest.getUserId());
			designatedPart.setDsgncntrPartId(dsgnCntrManagerRequest.getDsgncntrPartId());
			designatedPart.setFrstRegisterId(UserUtils.getUser().getUserId());
			designatedPart.setLastUpdusrId(UserUtils.getUser().getUserId());
			designatedPart.setReqstSn(dsgnCntrManagerRequest.getReqstSn());
			DsgnCntrManagerRequestResult partResult = new DsgnCntrManagerRequestResult();
			updatePartInfo(designatedPart, partResult);
			switch (partResult.getCode()) {
			case "NO_AUTH":
				dsgnManagerApprovalResult.setStatusCode("901");
				break;
			case "FAIL":
				dsgnManagerApprovalResult.setStatusCode("902");
				break;
			}
		}

		return dsgnManagerApprovalResult;
	}

	@Override
	public List<DsgnCntrManagerRequest> getManagerRequestListByParam(DsgnCntrManagerRequestSearchParam searchParam) {
		searchParam.setReqstSeCode("ROLE_ADMIN_10");
		if (!UserUtils.hasMasterManagerRole()) {
			searchParam.setSuperUserId(UserUtils.getUser().getUserId());
		}
		int cnt = designatedDonationMapper.getManagerRequestCountByParam(searchParam);
		if (searchParam.getItemsPerPage() < 10) {
			searchParam.setItemsPerPage(10);
		}

		Pagination pagination = Pagination.getInstance(cnt, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		return designatedDonationMapper.getManagerRequestListByParam(searchParam);
	}

	@Override
	public DsgnCntrManagerRequest getManagerRequestDetails(DsgnCntrManagerRequestSearchParam searchParam) {
		return designatedDonationMapper.getManagerRequestDetails(searchParam);
	}

	@Override
	public void updateNoticeListDataByLabel(DesignatedDonationNotice designatedDonationNotice) {
		designatedDonationMapper.updateNoticeListDataByLabel(designatedDonationNotice);
	}

	@Override
	public List<DesignatedPart> selectDsgncntrPartMngList(DesignatedPartParam searchParam) {
		if (!"REQUEST".equals(searchParam.getConditionType()) && !UserUtils.hasMasterManagerRole()) {
			searchParam.setLocgovCode(locgovService.getLocgovCodeByOpId(searchParam.getUserId(), IdType.MANAGER));
		}

		if (searchParam.getItemsPerPage() < 10) {
			searchParam.setItemsPerPage(10);
		}

		Pagination pagination = Pagination.getInstance(designatedDonationMapper.selectDsgncntrPartMngCnt(searchParam), searchParam.getItemsPerPage());

		searchParam.setPagination(pagination);

		return designatedDonationMapper.selectDsgncntrPartMngList(searchParam);
	}

	@Override
	public int insertDsgncntrPartMng(DesignatedPart designatedPart) {
		if (!UserUtils.hasMasterManagerRole()) {
			designatedPart.setLocgovCode(locgovService.getLocgovCodeByOpId(designatedPart.getUserId(), IdType.MANAGER));
		}

		return designatedDonationMapper.insertDsgncntrPartMng(designatedPart);
	}

	@Override
	public int updateDsgncntrPartMng(DesignatedPart designatedPart) {
		if (!UserUtils.hasMasterManagerRole()) {
			designatedPart.setLocgovCode(locgovService.getLocgovCodeByOpId(designatedPart.getUserId(), IdType.MANAGER));
		}

		return designatedDonationMapper.updateDsgncntrPartMng(designatedPart);
	}

	@Override
	public DesignatedPart selectDsgncntrPartMngDetail(DesignatedPartParam searchParam) {
		if (!UserUtils.hasMasterManagerRole()) {
			searchParam.setLocgovCode(locgovService.getLocgovCodeByOpId(searchParam.getUserId(), IdType.MANAGER));
		}

		return designatedDonationMapper.selectDsgncntrPartMngDetail(searchParam);
	}

	@Override
	public void updatePartInfo(DesignatedPart designatedPart, DsgnCntrManagerRequestResult result) {
		if (result == null) {
			result = new DsgnCntrManagerRequestResult();
		}
		if (!UserUtils.hasMasterManagerRole()) {
			if (UserUtils.hasLocgovManagerRole() && !UserUtils.hasDsgncntrManagerRole()) {
				String confirmLocgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

				DsgnCntrManagerRequestSearchParam searchParam = new DsgnCntrManagerRequestSearchParam();
				searchParam.setUserId(designatedPart.getPartUserId());
				searchParam.setReqstSn(designatedPart.getReqstSn());
				DsgnCntrManagerRequest request = getManagerRequestDetails(searchParam);
				if (!StringUtils.hasLength(confirmLocgovCode) || !confirmLocgovCode.equals(request.getLocgovCode())) {
					result.setCode("NO_AUTH");
					return;
				}
			} else {
				result.setCode("NO_AUTH");
				return;
			}
		}

		designatedDonationMapper.deleteDsgncntrPartMppng(designatedPart);
		int resultCnt = designatedDonationMapper.insertDsgncntrPartMppng(designatedPart);

		if (designatedPart.getDsgncntrPartId() > 0) {
			DsgnCntrManagerRequest dsgnCntrManagerRequest = new DsgnCntrManagerRequest();
			dsgnCntrManagerRequest.setUserId(designatedPart.getPartUserId());
			dsgnCntrManagerRequest.setLastUpdusrId(UserUtils.getUser().getUserId());
			dsgnCntrManagerRequest.setDsgncntrPartId(designatedPart.getDsgncntrPartId());
			dsgnCntrManagerRequest.setReqstSn(designatedPart.getReqstSn());
			designatedDonationMapper.updateManagerRequestPartName(dsgnCntrManagerRequest);
		}

		if (resultCnt == 0) {
			result.setCode("FAIL");
			return;
		} else {
			result.setCode("SUCC");
			return;
		}
	}

	@Override
	public int insertDsgncntrConfirmLog(DesignatedDonation designatedDonation) {
		return designatedDonationMapper.insertDsgncntrConfirmLog(designatedDonation);
	}

	@Override
	public List<DsgncntrConfirmLog> selectDsgncntrConfirmLog(DsgncntrConfirmLogParam param) {
		if (param.getItemsPerPage() < 10) {
			param.setItemsPerPage(10);
		}

		Pagination pagination = Pagination.getInstance(designatedDonationMapper.selectDsgncntrConfirmLogCnt(param), param.getItemsPerPage());

		param.setPagination(pagination);

		return designatedDonationMapper.selectDsgncntrConfirmLog(param);
	}

	@Override
	public long checkDsgncntrAuthPartInfo() {
		if (UserUtils.hasDsgncntrManagerRole()) {
			return designatedDonationMapper.selectDsgncntrPartId(UserUtils.getUser().getUserId());
		} else if (UserUtils.hasLocgovManagerRole() || UserUtils.hasMasterManagerRole()) {
			return -1;
		} else {
			return 0;
		}
	}


	/**
	 * 지정기부 상세내역 조회_미리보기
	 */
	@Override
	public DesignatedDonation selectDesignatedDonationDetailPreview(DesignatedDonationSearchParam designatedDonationSearchParam) {

		DesignatedDonation designatedDonation = designatedDonationMapper.selectDesignatedDonationDetail(designatedDonationSearchParam);

		if (designatedDonation == null || designatedDonation.getPrjId() == 0) {
			return null;
		}
		selectDesignatedCntrList(designatedDonation, designatedDonationSearchParam);

		//이미지 처리
		List<PrjImage> images = designatedDonationMapper.selectImgListByPrjId(designatedDonation.getPrjId());
		designatedDonation.setPrjImages(images);
		designatedDonation.setPrjImageExplain(designatedDonationMapper.selectImgDescListByPrjId(designatedDonation.getPrjId()));

		return designatedDonation;
	}

	/**
	 * 모금완료된 기정기부 상태 변경(배치)
	 */
	@Override
	public void updateDesinatedDonationStatus() {

		List<Map<String, Long>> selectResult = designatedDonationMapper.selectDesinatedDonationGoalAmt();

		if(!selectResult.isEmpty()) {
			for(Map<String, Long> dsgn: selectResult) {
				designatedDonationMapper.updateDesinatedDonationStatus(dsgn.get("dsgnDntnBizId"));
			}
		}

	}

}
