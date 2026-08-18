package saleson.shop.offgive;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.privacy.pCrypto;

import saleson.common.file.ExcelCellStyleUtils;
import saleson.common.utils.RandomStringUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.GCntr;
import saleson.model.UserEntity;
import saleson.shop.code.CodeMapper;
import saleson.shop.donation.DonationVerification;
import saleson.shop.donation.NgDonationMapper;
import saleson.shop.offgive.domain.Manager;
import saleson.shop.offgive.domain.Offgive;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.Buyer;
import saleson.shop.order.domain.GiftOrderVo;
import saleson.shop.order.domain.Receiver;
import saleson.shop.present.GCntrRepository;
import saleson.shop.qna.domain.Qna;
import saleson.shop.slave.SlaveOffgiveMapper;
import saleson.shop.user.UserMapper;
import saleson.shop.user.UserRepository;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.userrole.UserRoleService;

@Service("offgiveService")
public class OffgiveServiceImpl implements OffgiveService {

	private static final Logger log = LoggerFactory.getLogger(OffgiveServiceImpl.class);

    @Autowired
    OffgiveMapper offgiveMapper;

    @Autowired
    SlaveOffgiveMapper slaveOffgiveMapper;

    @Autowired
    CodeMapper codeMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    GCntrRepository gCntrRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NgDonationMapper ngDonationMapper;

    @Autowired
	DonationVerification donationVerification;

    @Autowired
    private OrderService orderService;

    /**
     * 오프라인접수 목록 count
     */
    @Override
    public int getOffgiveListCount(Offgive offgive) {
        return slaveOffgiveMapper.getOffgiveListCount(offgive);
    }

    /**
     * 오프라인접수 목록
     */
    @Override
    public List<Offgive> getOffgiveList(Offgive offgive) {
    	if(offgive.getPage() == 0) {
    		offgive.setPage(1);
    	}
        return slaveOffgiveMapper.getOffgiveList(offgive);
    }

    /**
     * 오프라인접수 목록 엑셀 다운로드
     */
    @Override
    public SXSSFWorkbook streamOffgiveList(Offgive searchParam, int totalCount) {
    	// 확인 후 삭제
    	if(searchParam.getPage() == 0) {
    		searchParam.setPage(1);
    	}

    	// Cursor<DTO>가 스트리밍 방식으로 사용하기에는 적절하나, CUBRID DB에서는 사용이 불가
		// JDBC API 기반의 데이터 접근 방식 : Cursor
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// Cursor 를 이용한 스트리밍 방식의 엑셀 다운로드 불가
		// pageSize 와 offset 을 설정하여, 반복문 실행(1회 반복 : 1000 row)
		pageSize 		= 1000;
		offset 			= 1;

		searchParam.getPagination().setItemsPerPage(pageSize);
		searchParam.getPagination().setCurrentPage(offset);

		// SXSSF	: window size = 100
		// workbook	: 엑셀생성을 위한 내부 문서 모델
		// 메모리 적재 최대 100 row 로 설정, 나머지는 disk 로 flush
		// disk 저장 파일은 압축
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		// 엑셀 시트 이름 설정
		Sheet sheet = workbook.createSheet("OFFGIVE_DATA");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "오프라인 기부금 접수 관리 목록");
		Integer lastColIndex;

		// 셀 폭 설정(고정값)
		sheet.setColumnWidth(0,		2000);
		sheet.setColumnWidth(1,		7500);
		sheet.setColumnWidth(2,		3000);
		sheet.setColumnWidth(3,		7500);
		sheet.setColumnWidth(4,		5000);
		sheet.setColumnWidth(5,		7500);
		sheet.setColumnWidth(6,		5000);
		sheet.setColumnWidth(7,		5000);
		sheet.setColumnWidth(8,		7500);
		sheet.setColumnWidth(9,		7500);
		sheet.setColumnWidth(10,	7500);

		// 데이터 Header 값 설정
		Row header = sheet.createRow(rowNum++);
		header.setHeight((short) 512);
		cellStyle.header(header, 0,		"No");
		cellStyle.header(header, 1,		"접수번호(지자체)");
		cellStyle.header(header, 2,		"기부상태");
		cellStyle.header(header, 3,		"전자납부번호");
		cellStyle.header(header, 4,		"수납일");
		cellStyle.header(header, 5,		"기부 지자체");
		cellStyle.header(header, 6,		"이름");
		cellStyle.header(header, 7,		"기부 금액");
		cellStyle.header(header, 8,		"지점/센터명");
		cellStyle.header(header, 9,		"신고일");
		cellStyle.header(header, 10,	"특정사업에 기부하기 사업명");

		// title row cell merging
		lastColIndex = header.getLastCellNum() - 1;
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {
			if (totalCount == 0) break;

			// 엑셀 시트 구성 데이터 조회(1000건) 및 전처리
			List<Offgive> offGiveList = slaveOffgiveMapper.getOffgiveList(searchParam);

			// 1000건 데이터를 엑셀 시트에 입력 가능하도록 row 단위의 전처리 실행
			for (Offgive offGive : offGiveList) {
				// workbook의 row 생성 및 각 건수별 데이터 입력/저장
				// 생성된 row가 앞서 설정한 최댓값 100 row 가 넘어가게 되면, 가장 먼저 생성된 row 는 디스크에 flush
				// flush는 row가 새로 생성되는 시점에서 메모리 적재  row 수 를 확인하고 설정 값 이상이 되는 경우 실행
				Row row = sheet.createRow(rowNum++);
				row.setHeight((short) 400);
				cellStyle.data(row, 0,	StringUtils.numberFormat(totalCount--));
				cellStyle.data(row, 1,	offGive.getCntrSn());
				cellStyle.data(row, 2,	Integer.parseInt(offGive.getCntrSttusCode()) == 100 ? "신고" : (Integer.parseInt(offGive.getCntrSttusCode()) == 200 ? "수납" : "과오납"));
				cellStyle.data(row, 3,	offGive.getElctrnPayNo());
				cellStyle.data(row, 4,	offGive.getSttemntPayDe());
				cellStyle.data(row, 5,	offGive.getUpperLocgovNm() + " " + offGive.getLocgovNm());
				cellStyle.data(row, 6,	offGive.getUserName());
				cellStyle.data(row, 7,	offGive.getCntrAmt());
				cellStyle.data(row, 8,	offGive.getRceptBankCodeNm() + " " + offGive.getRceptBankNm());
				cellStyle.data(row, 9,	offGive.getFrstRegistPnttm());
				cellStyle.data(row, 10,	offGive.getPrjSubject());

			}

			// 다음 1000 row 조회를 위한 offset 설정
			// 쿼리문
			// LIMIT (#{pagination.currentPage} - 1) * #{pagination.itemsPerPage}, #{pagination.itemsPerPage}
			searchParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		return workbook;
    }

    @Override
    public String insertOffgive(Offgive offgive) {
    	/*
    	 * 1. user 등록
    	 * 		1.1 회원 신규 추가 컬럼 update
    	 * 2. user detail 등록
    	 * 3. role 등록
    	 * 4. 기부등록
    	 *
    	 */
    	String today = DateUtils.getToday("yyyyMMdd");
    	String gCntrUserId = "";
    	String loginId = "";

    	Integer limitAmt = Integer.parseInt(donationVerification.donationLimitAmt().getLabel());

    	// validation
    	if (100 > Integer.parseInt(offgive.getCntrAmt()) || Integer.parseInt(offgive.getCntrAmt()) > limitAmt) {
        	throw new UserException("기 신고한 기부정보(기부한도 사용)가 있어 추가 등록할 수 없습니다. \n기 신고한 기부정보(기부한도 사용)는 신고일 익일 자정에 초기화 \n됩니다.");
        }
    	if ((Integer.parseInt(offgive.getCntrAmt()) % 100) > 0) {
    		throw new UserException("기부 금액 단위는 100원 단위입니다.");
    	}

    	if (StringUtils.isEmpty(offgive.getUserId())) {

    		long userId = userService.selectNewUserId();
    		gCntrUserId = Long.toString(userId);


    		// 중복확인
    		int cnt = 0;
    		loginId = "";
    		try {
	    		do {
	    			char[] charSet = new char[] {
	    	                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	    	                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	    			StringBuffer sb = new StringBuffer();
	    	        SecureRandom sr = new SecureRandom();
	    	        sr.setSeed(new Date().getTime());

	    	        int idx = 0;
	    	        int len = charSet.length;
	    	        for (int i=0; i<9; i++) {
	    	            idx = sr.nextInt(len);    // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
	    	            sb.append(charSet[idx]);
	    	        }
	    	        loginId = sb.toString();
	    			UserEntity userEntityTemp = userRepository.findByLoginId(pCrypto.Encrypt("normal", loginId, ""));
	    			if (userEntityTemp == null) {
	    				cnt++;
	    			}
	    		} while(cnt <= 0);
    		} catch (UnsupportedEncodingException e) {
				log.error("OffgiveServiceImpl > insertOffgive METHOD ERROR");
			}

    		// user
	        User user = new User();
	        user.setUserId(userId);
	        user.setPassword(RandomStringUtils.getRandomString("", 4, 8));
	        user.setLoginId(loginId);
	        user.setUserName(offgive.getUserName().replaceAll(" ", ""));
	        user.setPasswordType("T");	// 패스워드 타입 (N:정상 T:임시)
	        userService.insertUser(user);

	        // 회원 신규 추가 컬럼 update
	         UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserException("정보가 없습니다."));
	         userEntity.setLocgovCode(offgive.getPsitnLocgovCode());
	         userEntity.setMberCi(offgive.getMberCi());
	         userEntity.setMberDi(offgive.getMberDi());
	         userEntity.setSbscrbSeCode("200");
	         // 2023.02.15 일반 사용자 코드 100 추가
	         userEntity.setLoginPathCode("100");
	         // 2023.02.15 일반 사용자 코드 100 추가
	         userEntity.setLoginPathCode("100");
	         userRepository.save(userEntity);

	         // user Detail
	        UserDetail userDetail = new UserDetail();
	        userDetail.setUserId(userId);
	        userDetail.setPost(offgive.getPost());
	        userDetail.setAddress(offgive.getAddress());
	        userDetail.setAddressDetail(offgive.getAddressDetail());
	        userDetail.setPhoneNumber(offgive.getPhoneNumber1()+"-"+offgive.getPhoneNumber2()+"-"+offgive.getPhoneNumber3());
	        userDetail.setGender(offgive.getGender());
	        userDetail.setPoint(ShopUtils.getConfig().getPointJoin());
	        userDetail.setUseFlag("Y");
	        userDetail.setBirthdayType("1");	// 생년월일 분류(1:양력, 2:음력)
	        userDetail.setBirthdayFull(offgive.getBirthday());

	        userService.insertUserDetail(userDetail);

	        // role
	        UserRole userRole = new UserRole();
	        userRole.setUserId(userId);
	        userRole.setAuthority("ROLE_USER");
	        userRoleService.insertUserRole(userRole);
    	} else {
    		gCntrUserId = offgive.getUserId();
    		try {
				if(!donationVerification.isDonationNormalAmount(Long.valueOf(offgive.getUserId()) , Long.valueOf(offgive.getCntrAmt()))) {
					throw new UserException();
				}
			} catch (NumberFormatException | UnsupportedEncodingException e) {
				throw new UserException();
			}
    	}
        // 기부등록
    	Manager manager = this.getManager(UserUtils.getUser().getUserId());

    	GCntr entity = new GCntr();
        if(offgive.getLocgovUpperCode().equals("11000")) {
        	String linkMngKey = ngDonationMapper.getLinkMngKeyNextValue();
        	entity.setCntrSn(linkMngKey);
        	entity.setSeoulTrgetAt("Y");
        	entity.setPayValidDe(DateUtils.addDay(today, 1));
        	offgive.setLinkMngKey(linkMngKey);
        } else {
        	entity.setCntrSn(offgive.getLinkMngKey());
        	entity.setSeoulTrgetAt("N");
        	entity.setPayValidDe(today);
        }

        entity.setCntrDe(today);									// 기부일자
    	entity.setUserId(Long.parseLong(gCntrUserId));
        entity.setPsitnLocgovCode(offgive.getPsitnLocgovCode());	// 소속지자체코드
        entity.setCntrLocgovCode(offgive.getLocgovCode());			// 기부지자체코드
        entity.setCntrAmt(Integer.parseInt(offgive.getCntrAmt()));	// 기부금액
        entity.setCntrPoint(0);										// 기부포인트

    	if (SecurityUtils.hasRole("ROLE_ADMIN_11")) {
			entity.setCntrPathCode("300");								// CNTR_PATH(온라인 100 / 오프라인 200 / 행정복지센터 300)
			entity.setRceptBankCode(manager.getBankCode());			// 접수은행코드
			entity.setRceptBankNm(manager.getPsitnNm()); 				// 접수은행명(지점명)
	        entity.setRcepterNm(manager.getUserName()); 				// 접수자명
    	} else if (SecurityUtils.hasRole("ROLE_ADMIN_7")
    			|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			entity.setCntrPathCode("200");								// CNTR_PATH(온라인 100 / 오프라인 200 / 행정복지센터 300)
			entity.setRceptBankCode(manager.getBankCode());				// 접수은행코드
	        entity.setRceptBankNm(manager.getPsitnNm()); 				// 접수은행명(지점명)
	        entity.setRcepterNm(manager.getUserName()); 				// 접수자명
		} else {
			throw new UserException();
		}

        entity.setElctrnPayNo(offgive.getElctrnPayNo()); 				// 전자납부번호
        entity.setRtnpsntReqstCode(offgive.getRtnpsntReqstCode());  // 답례품신청코드 (이번 고향사랑 기부금에 대해서 신청/이번 고향사랑 기부금은 이월/답례품을 제공받지 않음)
        entity.setInfoAgreAt(offgive.getInfoAgreAt());				// 행정정보이용동의여부
        entity.setCntrSttusCode("100");								// 기부상태코드 (신고/정상/취소) <- (구)신규/취소 <- 승인/취소
        entity.setFrstRegisterId(UserUtils.getUser().getUserId());
        entity.setFrstRegistPnttm(LocalDateTime.now());;
        entity.setFrstRegisterId(UserUtils.getUser().getUserId());
        entity.setFrstRegistPnttm(LocalDateTime.now());;

        entity.setPrjId(offgive.getPrjId());
        entity.setDsgnDntnBizId(offgive.getPrjId());

        gCntrRepository.save(entity);

        //오프라인 답례품 주문 처리

        //test


        //기부일련번호를 주문테이블 filler9 에 넣음으로써 길제확정시 주문데이터의 상태를 변경하고 배송등의 이후 처리를 함
//        Buy buy = new Buy();
//        buy.setCntrSn(offgive.getLinkMngKey());

        GiftOrderVo giveOrderVo = new GiftOrderVo();
        giveOrderVo.setMbrCi(offgive.getMberCi());
      //itemId||수량||optionID
        String[] orderItems = {};
        if(!offgive.getOrderItems().isEmpty()) {
        	String orderSplit[] = offgive.getOrderItems().split("@");
        	orderItems = orderSplit;
        };
        giveOrderVo.setOrderItems(orderItems);
        //giveOrderVo.setLoginId("blue0k37");
        //giveOrderVo.setLoginId("calmnature");

        if(StringUtils.isEmpty(offgive.getUserId())) {
        	// 기존유저가 아니면 위에서 계정생성하여 위에서 gCntrUserId 값에 셋팅
        	giveOrderVo.setUserId(Long.parseLong(gCntrUserId));
        } else {
        	// 기존유저 존재
        	giveOrderVo.setUserId(Long.parseLong(offgive.getUserId()));
        }
        giveOrderVo.setUpdatedAdminUserName(UserUtils.getUser().getUserName());

        Buyer buyer = new Buyer();
        buyer.setMobile(offgive.getPhoneNumber());
        giveOrderVo.setBuyer(buyer);

        Receiver receiver = new Receiver();
//        receiver.setReceiveAddress("경기도 고양시 덕양구 도래울로 86");
//        receiver.setReceiveAddressDetail("315동 1104호");
//        receiver.setReceiveMobile("010-3051-3716");
//        receiver.setReceivePhone("010-3051-3716");
//        receiver.setReceiveName("강성호");
//        receiver.setReceiveZipcode("10551");

		String rcvPhoneNumber = offgive.getRcvPhoneNumber1()+"-"+offgive.getRcvPhoneNumber2()+"-"+offgive.getRcvPhoneNumber3();

        receiver.setReceiveAddress(offgive.getRcvAddress());
        receiver.setReceiveAddressDetail(offgive.getRcvAddressDetail());
        receiver.setReceiveMobile(rcvPhoneNumber);
        receiver.setReceivePhone(rcvPhoneNumber);
        receiver.setReceiveName(offgive.getRcvUserName());
        receiver.setReceiveZipcode(offgive.getRcvPost());

        giveOrderVo.setReceiver(receiver);
        giveOrderVo.setLinkMngKey(offgive.getLinkMngKey());

        //신규아이디 세팅
        //giveOrderVo.
        if (giveOrderVo.getLoginId() == null) {
        	giveOrderVo.setLoginId(loginId);
        }

        try {
        	if(orderItems.length > 0) {
        		orderService.saveOffGiveOrder(giveOrderVo);
        	}
        }catch(Exception e) {
        	log.error("OffgiveService insertOffgive error2 :: ", e);
        }


        return offgive.getLinkMngKey();
    }

	@Override
    public void updateOffgive(Offgive offgive) {

    }

    /**
     * 오프라인접수 상세
     */
    @Override
    public Offgive getOffgive(String cntrSn) {
        return slaveOffgiveMapper.getOffgive(cntrSn);
    }

    /**
     * 오프라인접수 상세 출력
     */
    @Override
    public Offgive getOffgivePrint(Offgive offgive) {
        return slaveOffgiveMapper.getOffgivePrint(offgive);
    }

    /**
     * 관리자 정보 조회
     */
    @Override
    public Manager getManager(long userId) {
        return slaveOffgiveMapper.getManager(userId);
    }

    /**
     * 사용자 정보 조회
     */
    @Override
    public HashMap<String, Object> getUserByMberCi(String mberCi) {
    	HashMap<String, Object> map = new HashMap<String, Object>();

    	User user = new User();

    	UserEntity userEntity = userRepository.findByMberCi(mberCi);
    	if (userEntity != null) {
    		user = userMapper.getUserByUserId(userEntity.getUserId());
    	}

    	map.put("user", user);

    	return map;
    }

    /**
     * 사용자 기부 한도 체크
     */
    @Override
    public Integer getMaxCheck(String userId, String mberCi) {
    	Integer getGCntrSumCntrAmt = 0;

    	Integer limitAmt = Integer.parseInt(donationVerification.donationLimitAmt().getLabel());

    	if (StringUtils.isNotEmpty(userId)) {
    		getGCntrSumCntrAmt = ngDonationMapper.getGCntrSumCntrAmt(Long.parseLong(userId));
    	}

    	Integer getGMberSecsnSumCntrAmt = ngDonationMapper.getGMberSecsnSumCntrAmt(mberCi);
    	Integer maxCntrAmt =  limitAmt - getGCntrSumCntrAmt - getGMberSecsnSumCntrAmt;

    	return maxCntrAmt;
//    	return offgiveMapper.getMaxCheck(userId);
    }

    /**
     * 지자체코드 행정기관코드 조회
     */
    @Override
    public HashMap<String, Object> getLocgovMapngCode(String locgovCode) {
    	return slaveOffgiveMapper.getLocgovMapngCode(locgovCode);
    }

    /**
	 * 로그인 사용자 지자체코드정보 조회
	 * @param userId
	 * @return
	 */
	@Override
	public String getLocgovCodeByUserId(Long userId) {
		return slaveOffgiveMapper.getLocgovCodeByUserId(userId);
	}

	/**
	 * 오프라인 기탁서 등록
	 * 대표 답례품 조회
	 */

	@Override
	public List<Map<String,Object>> selectOffRprs(String lclgvCd) {
		List<Map<String, Object>> result = offgiveMapper.selectOffRprs(lclgvCd);

		Map<String, Object> s = new HashMap<String, Object>(); // 옵션조회할 조건 데이터
		Map<String, Object> sResult = new HashMap<String, Object>(); // 조회한 옵션 데이터
		if(result != null && result.size() > 0) {

			ArrayList<Integer> removeIndex = new ArrayList<Integer>();
			for(int i = 0; i < result.size(); i++) {
				String option = (String) result.get(i).get("options");

				result.get(i).put("load_image", ShopUtils.loadImage(result.get(i).get("item_code").toString(), result.get(i).get("item_image").toString(), "L"));

				if(option != null && !option.isEmpty()) {
					String[] sp = option.split("\\|\\|");

					//	option_type : S
					if(sp.length == 4) {
						s.put("gdsId", result.get(i).get("gds_id"));
						s.put("optionType", sp[0]);
						s.put("optionName2", sp[2]);
						s.put("optionPrice", sp[3]);
					};

					// option_type : S3
					if(sp.length == 8) {
						s.put("gdsId", result.get(i).get("gds_id"));
						s.put("optionType", sp[0]);
						s.put("optionName1", sp[2]);
						s.put("optionName2", sp[4]);
						s.put("optionName3", sp[6]);
						s.put("optionPrice", sp[7]);
					}

					// 분리한 option 값으로 option 상세 조회
					sResult = offgiveMapper.selectOffRprsOption(s);

					if (sResult != null) {
						//수량체크 : 10개 미만이면 노출안하게
						if (sResult.get("option_stock_quantity") == null) sResult.put("option_stock_quantity", 0);

						int checkStockQuantity = NumberUtils.toInt(sResult.get("option_stock_quantity").toString(), 0);

						//-1개는 수량 무제한
						if (checkStockQuantity > 0 && checkStockQuantity < 10) {
							removeIndex.add(i);
							//result.remove(i);
						}

						// List 순회하면서 가져온 Object 추가 하기
						result.get(i).put("item_option_id", sResult.get("item_option_id"));
						result.get(i).put("option_price", sResult.get("option_price"));
						result.get(i).put("option_stock_flag", sResult.get("option_stock_flag"));
						result.get(i).put("option_stock_quantity", sResult.get("option_stock_quantity"));
						result.get(i).put("total_price", (Integer)result.get(i).get("sale_price") + (Integer)sResult.get("option_price"));
						result.get(i).put("changed_option", "N");
					} else {
						result.get(i).put("changed_option", "Y");
					}
				} else {

					//수량체크 : 10개 미만이면 노출안하게
					if (result.get(i).get("stock_quantity") == null) result.get(i).put("stock_quantity", 0);

					int checkStockQuantity = NumberUtils.toInt(result.get(i).get("stock_quantity").toString(), 0);

					//-1개는 수량 무제한
					if (checkStockQuantity > 0 && checkStockQuantity < 10) {
						removeIndex.add(i);
						//result.remove(i);
					}

					// option이 없음
					result.get(i).put("changed_option", "N");
					result.get(i).put("total_price", (Integer)result.get(i).get("sale_price"));

				}
	  		}

			//배열참조 문제 때문에 역방향 루프로 재고 10개 이하 제거
			if ( removeIndex != null && removeIndex.size() > 0 ) {
				for(int j = removeIndex.size() - 1; j >= 0 ; j--) {
					result.remove((int)removeIndex.get(j));
				}

				//제거후 값이 없으면 널처리
				if (result != null && result.size() == 0 ) {
					result = null;
				}
			}

		} else {
			result = null;
		}

		return result;
	}

	/**
	 * 오프라인 기탁서
	 * 기탁자 휴대전화번호 수정
	 */
	@Override
	public int updatePhoneNumber(Integer userId, String phoneNumber) {
		return offgiveMapper.updatePhoneNumber(userId, phoneNumber);
	};
}
