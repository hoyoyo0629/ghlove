package saleson.seller.main;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.privacy.pCrypto;

import io.netty.util.internal.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import saleson.common.enumeration.AuthorityType;
import saleson.common.enumeration.SmsType;
import saleson.common.file.infra.FileStorage;
import saleson.common.security.crypto.SellerPwSalt;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.ShopUtils;
import saleson.seller.main.domain.Seller;
import saleson.seller.main.domain.SellerCategory;
import saleson.seller.main.domain.SellerCriteriaEncryptor;
import saleson.seller.main.domain.SellerEncryptor;
import saleson.seller.main.support.SellerException;
import saleson.seller.main.support.SellerListParam;
import saleson.seller.main.support.SellerParam;
import saleson.seller.user.SellerUserService;
import saleson.shop.item.ItemMapper;
import saleson.shop.item.support.ItemParam;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.SellerUser;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.userrole.UserRoleService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("sellerService")
public class SellerServiceImpl implements SellerService {

	private static final Logger log = LoggerFactory.getLogger(SellerServiceImpl.class);

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private ItemMapper itemMapper;


	@Autowired
	private SequenceService sequenceService;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Autowired
	private SellerUserService sellerUserService;

    @Autowired
    private UserRoleService userRoleService;

	@Autowired
	private SellerCriteriaEncryptor sellerCriteriaEncryptor;

	@Autowired
	private SellerEncryptor sellerEncryptor;

	@Autowired
	private FileStorage fileStorage;

	@Autowired
	private FileService fileService;

	@Autowired
	private SellerPwSalt sellerPwSalt;

	@Autowired
	private UserService userService;

	@Autowired
	private SmsIpsService smsIpsService;


	@Override
	public Seller getSellerByLoginId(String loginId) {
		Seller seller = sellerMapper.getSellerByLoginId(loginId);
		decryptSellerData(seller);
		return seller;
	}

	@Override
	public Seller getSellerById(long sellerId) {
		Seller seller = sellerMapper.getSellerById(sellerId);
		decryptSellerData(seller);
		return seller;
	}

	@Override
	public List<Seller> getSellerListByParam(SellerParam sellerParam) {
		sellerParam.encrypt(sellerCriteriaEncryptor);

		List<Seller> list = sellerMapper.getSellerListByParam(sellerParam);
		list.forEach(s -> decryptSellerData(s));

		sellerParam.decrypt(sellerCriteriaEncryptor);
		return list;
	}

	@Override
	public List<Seller> getAllSellerList() {
		List<Seller> list = sellerMapper.getAllSellerList();
		list.forEach(s -> decryptSellerData(s));
		return list;
	}


	@Override
	public int getSellerCount(SellerParam sellerParam) {
		sellerParam.encrypt(sellerCriteriaEncryptor);
		if(sellerParam.getPage() == 0) {
			sellerParam.setPage(1);
    	}
		int count = sellerMapper.getSellerCount(sellerParam);
		sellerParam.decrypt(sellerCriteriaEncryptor);
		return count;
	}

	@Override
	public String makeSellerId(Seller seller) {
		int count = 0;		// 증가할 카운트
		String loginId = "";	// 판매자 로그인ID
		String isUserYn = "";	// 사용자ID와 중복되는지 체크여부

		do {
			count++;
			seller.setPlusSeq(count);
			loginId = sellerMapper.makeSellerId(seller);	// 판매자 아이디 생성
			try {
				isUserYn = sellerMapper.getDuplicateUserCheck(pCrypto.Encrypt("normal", loginId, ""));	// OP_USER에 생성된 아이디와 중복체크
			} catch (UnsupportedEncodingException e) {
				log.error("makeSellerId UnsupportedEncodingException Occured", e);
			}

		} while(isUserYn.equals("Y"));	// 중복되는 ID가 없을때까지 계속 실행

		return loginId;
	}

	@Override
	public void insertSeller(Seller seller) {

		long sellerId = sequenceService.getLong("OP_SELLER");
		String rawPassword = seller.getPassword();

		// 국번이 없는 경우 분기처리
		String telephoneNumber = (seller.getTelephoneNumber1().equals("미선택") ?
				"" : seller.getTelephoneNumber1() + "-")
				+ seller.getTelephoneNumber2() + "-"
				+ seller.getTelephoneNumber3();
		String phoneNumber = seller.getPhoneNumber1() + "-" + seller.getPhoneNumber2() + "-" + seller.getPhoneNumber3();
		String faxNumber = seller.getFaxNumber1() + "-" + seller.getFaxNumber2() + "-" + seller.getFaxNumber3();
		String post = seller.getPost();
		String businessNumber = seller.getBusinessNumber1() + "-" + seller.getBusinessNumber2() + "-" + seller.getBusinessNumber3();


		String secondTelephoneNumber = seller.getSecondTelephoneNumber1() + "-" + seller.getSecondTelephoneNumber2() + "-" + seller.getSecondTelephoneNumber3();
		String secondPhoneNumber = seller.getSecondPhoneNumber1() + "-" + seller.getSecondPhoneNumber2() + "-" + seller.getSecondPhoneNumber3();


		seller.setSecondTelephoneNumber(secondTelephoneNumber);
		seller.setSecondPhoneNumber(secondPhoneNumber);

		seller.setTelephoneNumber(telephoneNumber);
		seller.setPhoneNumber(phoneNumber);

		seller.setFaxNumber(faxNumber);
		seller.setPost(post);
		seller.setBusinessNumber(businessNumber);

//		seller.setPassword(passwordEncoder.encode(rawPassword));
		try {
			seller.setPassword(pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + seller.getLoginId() + rawPassword, ""));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}

		seller.setSellerId(sellerId);

		seller.encrypt(sellerEncryptor);
		// 사업자등록증
		if (seller.getUploadFile1() != null) {
			if (seller.getUploadFile1().getSize() > 0) {
				this.saveFile(seller, "create", "file1");
			} else {
				seller.setFileNameCertificate1(null);
			}
		}
		// 통신판매신고증
		if (seller.getUploadFile2() != null) {
			if (seller.getUploadFile2().getSize() > 0) {
				this.saveFile(seller, "create", "file2");
			} else {
				seller.setFileNameCertificate2(null);
			}
		}
		// 구매안전이용확인증
		if (seller.getUploadFile3() != null) {
			if (seller.getUploadFile3().getSize() > 0) {
				this.saveFile(seller, "create", "file3");
			} else {
				seller.setFileNameCertificate3(null);
			}
		}

		// 정산 주기 입력화면 없어서 세팅(필수값, 정산주기 (1: 일정산, 2: 주정산, 3:15일정산, 4:월정산) - 월정산일 경우 remittance_day 값 필수)
		seller.setRemittanceType("4");
		seller.setRemittanceDay("8");

		sellerMapper.insertSeller(seller);

		try {
			User user = new User();

			user.setLoginId(seller.getLoginId());
			user.setPassword(rawPassword);
			user.setUserName(seller.getSellerName());
			user.setPhoneNumber(phoneNumber);
			user.setEmail(seller.getEmail());
			user.setDormancyMailSent(seller.getReceiveSms());

			sellerUserService.insertSellerMasterUser(sellerId, user);
		} catch (RuntimeException e) {
			log.error("insert seller user error > {}", sellerId, e);
		}

		// 국민비서 알림 전송 (업체등록 완료시)
		try {
			seller.decrypt(sellerEncryptor, false);
			if (StringUtils.hasLength(seller.getMberCi()) && StringUtils.hasLength(seller.getPhoneNumber()) && "0".equals(seller.getReceiveSms())) {
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.COMPANY_REGISTER);
				receiverInfo.setPrvcIdntfcInfo(seller.getMberCi());
				StringBuilder sb = new StringBuilder();
				sb.append(seller.getCompanyName());	// 상호명
				sb.append("|");
				sb.append(receiverInfo.getLocalDateTimeToStr());	// 승인요청일시
				sb.append("|");
				sb.append(seller.getRepresentativeName());	// 대표자명
				sb.append("|");
				sb.append(seller.getLoginId()+"/1111");		// 아이디 및 임시비밀번호
				sb.append("|");
				sb.append(seller.getPhoneNumber().replaceAll("-", ""));
				receiverInfo.setSndngCntnts(sb.toString());
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);
			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: insertSeller send sms error", e);
		}

	}

	@Override
	public void updateSeller(Seller seller) {

		// 국번이 없는 경우 분기처리
		String telephoneNumber = ("미선택".equals(seller.getTelephoneNumber1()) ?
				"" : seller.getTelephoneNumber1() + "-")
				+ seller.getTelephoneNumber2() + "-"
				+ seller.getTelephoneNumber3();

		String phoneNumber = seller.getPhoneNumber1() + "-" + seller.getPhoneNumber2() + "-" + seller.getPhoneNumber3();
		String faxNumber = seller.getFaxNumber1() + "-" + seller.getFaxNumber2() + "-" + seller.getFaxNumber3();
		String post = seller.getPost1() + "-" + seller.getPost2();
		String businessNumber = seller.getBusinessNumber1() + "-" + seller.getBusinessNumber2() + "-" + seller.getBusinessNumber3();

		String secondTelephoneNumber = seller.getSecondTelephoneNumber1() + "-" + seller.getSecondTelephoneNumber2() + "-" + seller.getSecondTelephoneNumber3();
		String secondPhoneNumber = seller.getSecondPhoneNumber1() + "-" + seller.getSecondPhoneNumber2() + "-" + seller.getSecondPhoneNumber3();


		seller.setSecondTelephoneNumber(secondTelephoneNumber);
		seller.setSecondPhoneNumber(secondPhoneNumber);

		if (seller.getTelephoneNumber1() != null){
			seller.setTelephoneNumber(telephoneNumber);
		}
		if (seller.getPhoneNumber1() != null){
			seller.setPhoneNumber(phoneNumber);
		}
		if (seller.getFaxNumber1() != null){
			seller.setFaxNumber(faxNumber);
		}
		if (seller.getPost1() != null){
			seller.setPost(post);
		}
		if (seller.getBusinessNumber1() != null){
			seller.setBusinessNumber(businessNumber);
		}

		if (seller.getPassword() != null){
			if(!seller.getPassword().equals("")){
//			seller.setPassword(passwordEncoder.encode(seller.getPassword()));
				try {
					seller.setPassword(pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + seller.getLoginId() + seller.getPassword(), ""));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException();
				}
			}
		}

		seller.encrypt(sellerEncryptor);
		// 사업자등록증
		if (seller.getUploadFile1() != null) {
			if (seller.getUploadFile1().getSize() > 0) {
				this.saveFile(seller, "edit", "file1");
			} else {
				seller.setFileNameCertificate1(null);
			}
		}
		// 통신판매신고증
		if (seller.getUploadFile2() != null) {
			if (seller.getUploadFile2().getSize() > 0) {
				this.saveFile(seller, "edit", "file2");
			} else {
				seller.setFileNameCertificate2(null);
			}
		}
		// 구매안전이용확인증
		if (seller.getUploadFile3() != null) {
			if (seller.getUploadFile3().getSize() > 0) {
				this.saveFile(seller, "edit", "file3");
			} else {
				seller.setFileNameCertificate3(null);
			}
		}
		sellerMapper.updateSeller(seller);


		// 판매자 배송비 조건부 상품 배송비 일괄 업데이트.
		itemMapper.updateShipmentPriceForSeller(seller);

		// 판매자 정보의 담당MD가 변경된 경우 해당 업체 상품 정보에 변경 전 담당MD가 지정된 경우에만 상품MD 일괄 UPDATE
		if (seller.getCurrentMdId() != null
				&& seller.getMdId() != null
				&& !seller.getCurrentMdId().equals(seller.getMdId())) {

			itemMapper.updateItemMdUserForSeller(seller);
		}

        String hasRole = sellerMapper.getSellerHasRole(sellerUserService.getSellerUserIdByLoginId(seller.getLoginId())); // 판매자권한 등록여부

        // 승인대기중인 판매자의 권한이 등록되어 있지 않으면 권한추가 (리얼커머스 이관대상) - 2023.04.12 추가 (시스템 통합 후에는 필요없음)
        if(seller.getStatusCode().equals("2") && hasRole.equals("N")) {

            UserRole userRole = new UserRole();
            userRole.setUserId(sellerUserService.getSellerUserIdByLoginId(seller.getLoginId()));

            String baseAuthority = AuthorityType.SELLER.getCode();
            // ROLE_SELLER 추가
            userRole.setAuthority(baseAuthority);
            userRoleService.insertUserRole(userRole);

            // 해당 판매자 ROLE 추가
            userRole.setAuthority(baseAuthority+"_"+seller.getSellerId());
            userRoleService.insertUserRole(userRole);

            // 엑셀 다운로드 권한 추가
            userRole.setAuthority("ROLE_EXCEL");
            userRoleService.insertUserRole(userRole);

            userRole.setAuthority(AuthorityType.SELLER_MASTER.getCode());
            userRoleService.insertUserRole(userRole);

        }
	}

	private void saveFile(Seller seller, String mode, String fileType) {
		if (fileType == null || seller == null) {
			throw new SellerException("파일 업로드에 실패 하였습니다.");
		}
		MultipartFile multipartFile = null;	// 업로드 대상파일
		String deleteFile = "";	// 삭제파일명

		if(fileType.equals("file1")) {	// 사업자등록증
			multipartFile = seller.getUploadFile1();
			deleteFile = seller.getFileNameCertificate1();
		} else if(fileType.equals("file2")) {	// 통신판매신고증
			multipartFile = seller.getUploadFile2();
			deleteFile = seller.getFileNameCertificate2();
		} else if(fileType.equals("file3")) {	// 구매안전이용확인증
			multipartFile = seller.getUploadFile3();
			deleteFile = seller.getFileNameCertificate3();
		} else {
			throw new SellerException("파일 업로드에 실패 하였습니다.");
		}

		if (multipartFile == null) {
			throw new SellerException("파일 업로드에 실패 하였습니다.");
		}

		String fileName = multipartFile.getOriginalFilename();	// 원본 파일명
		String fileExtension = FileUtils.getExtension(fileName);	// 원본 파일확장자

		//String defaultFileName = fileStorage.getNewFileName(fileName);

		String uploadPath = seller.getUploadPath();	// 파일 업로드 경로
		fileService.makeUploadPath(uploadPath);	// 업로드 디렉토리 생성

		try {
			// 수정일 경우 기존파일 삭제
			if(mode.equals("edit")) {
				fileStorage.delete(uploadPath + File.separator + deleteFile);
			}
			// 파일 업로드
			fileStorage.upload(multipartFile, uploadPath + File.separator + fileName);
		} catch (IOException e) {
			throw new SellerException("파일 업로드에 실패 하였습니다.");
		}

		// 첨부파일명
		if(fileType.equals("file1")) {
			seller.setFileNameCertificate1(fileName);
		} else if(fileType.equals("file2")) {
			seller.setFileNameCertificate2(fileName);
		} else if(fileType.equals("file3")) {
			seller.setFileNameCertificate3(fileName);
		}
	}

	@Override
	public void deleteFile(Seller seller, String fileName) {
		fileStorage.delete(seller.getUploadPath() + File.separator + fileName);	// 파일삭제
		sellerMapper.deleteFile(seller);
	}


	@Override
	public void deleteSeller(Seller seller) {
		sellerMapper.deleteSeller(seller);
	}

    @Override
    public void deleteSellerList(SellerListParam sellerListParam) {

    	if (sellerListParam.getId() != null) {
    		// 선택한 업체를 일괄삭제한다.
    		for (String sellerId : sellerListParam.getId()) {
    			Seller seller = new Seller();
    			seller.setSellerId(Integer.parseInt(sellerId));
    			sellerMapper.deleteSeller(seller);
    		}

    	}

    }


	@Override
	public List<SellerCategory> getSellerCategoriesById(long sellerId) {
		return sellerMapper.getSellerCategoriesById(sellerId);
	}

	@Override
	public List<SellerCategory> getSellerItemsByParam(ItemParam itemParam) {
		//return sellerMapper.getSellerItemsByParam(itemParam);
		return null;
	}

	@Override
	public void updateSellerMinimall(Seller seller) {
		sellerMapper.updateSellerMinimall(seller);

	}

	@Override
	public List<Seller> getSellerIdBySmsSendTime(String Hour) {
		List<Seller> list = sellerMapper.getSellerIdBySmsSendTime(Hour);
		list.forEach(s -> decryptSellerData(s));
		return list;
	}

	@Override
	public void updateSellerPassword(Seller seller) {
		if (seller.getPassword() != null){
			if(!seller.getPassword().equals("")){
//				seller.setPassword(passwordEncoder.encode(seller.getPassword()));

				SellerUser sellerUser = new SellerUser();

				sellerUser.setLoginId(seller.getLoginId());
				sellerUser.setPassword(seller.getPassword());

				try {
					seller.setPassword(pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + seller.getLoginId() + seller.getPassword(), ""));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException();
				}
				sellerMapper.updateSellerPassword(seller);

				long userId = sellerUserService.getSellerUserIdByLoginId(seller.getLoginId());
//				User user = new User();
//				user.setPassword(seller.getPassword());
//				user.setUserId(userId);
//
//				sellerUserService.updateSellerUserPassword(user);
				sellerUser.setUserId(userId);

				sellerUserService.updatePasswordForSellerLogin(sellerUser);
			}
		}
	}

	@Override
	public void decryptSellerData(Seller seller) {
		// 복호화
		if (seller != null) {
			seller.decrypt(sellerEncryptor, ShopUtils.needMasking());
		}
	}

	@Override
	public void initSellerPassword(Seller seller) {
		if (seller.getPassword() != null){
			if(!seller.getPassword().equals("")){
				try {
					seller.setPassword(pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + seller.getLoginId() + seller.getPassword(), ""));
				} catch (UnsupportedEncodingException e) {
					throw new OpRuntimeException("문제가 발생했습니다.");
				}
				sellerMapper.updateSellerPassword(seller);

				long userId = sellerUserService.getSellerUserIdByLoginId(seller.getLoginId());
				User user = new User();
				user.setPassword(seller.getPassword());
				user.setUserId(userId);
				user.setActionType("INIT_PWD");
				sellerUserService.updateSellerUserPassword(user);
			}
		} else {
			throw new OpRuntimeException("문제가 발생했습니다.");
		}
	}

	/**
	 * 판매자 ID로 판매자 정보 조회 - 답례품승인관리 미리보기 기능 전용.
	 * @param sellerId
	 * @return
	 */
	@Override
	public Seller getSellerByIdPreview(long sellerId) {
		Seller seller = sellerMapper.getSellerById(sellerId);
		return seller;
	}

	/**
	 * 판매자 LOGIN_ID로 이메일 UPDATE 등록(op_seller)
	 * @param Seller seller
	 * @return
	 */
	@Override
	public int updateSellerInfo(Seller seller) {
		int isUpdated = sellerMapper.updateSellerInfo(seller);
		return isUpdated;
	}
}
