package saleson.shop.lclgvHnrUser;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.web.pagination.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.file.service.CustomFileService;
import saleson.common.utils.UserUtils;
import saleson.shop.catalog.CatalogMngServiceImpl;
import saleson.shop.designateddonation.domain.PrjImageExplain;
import saleson.shop.lclgvHnrUser.domain.HnrUserInfo;
import saleson.shop.lclgvHnrUser.domain.LclgvHnrUserMng;
import saleson.shop.lclgvHnrUser.support.LclgvHnrUserMngParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("lclgvHnrMngService")
public class LclgvHnrUserMngServiceImpl  implements LclgvHnrUserMngService {

	@Autowired
	private LclgvHnrUserMngMapper lclgvHnrMngMapper;

	@Autowired
	private  CustomFileService customFileService;

	private final String programName = "lclgvHnrUserMng";

	// 지자체 명예 사용자 목록 조회
	@Override
	public List<LclgvHnrUserMng> selectLclgvHnrUserMngList(LclgvHnrUserMngParam lclgvHnrUserMngParam) {

		int lclgvHnrMngCount = lclgvHnrMngMapper.selectLclgvHnrUserMngListCnt(lclgvHnrUserMngParam);
		Pagination pagination = Pagination.getInstance(lclgvHnrMngCount);

		lclgvHnrUserMngParam.setPagination(pagination);

		return lclgvHnrMngMapper.selectLclgvHnrUserMngList(lclgvHnrUserMngParam);
	}

	// 지자체 명예 사용자 상세 페이지 조회
	@Override
	public LclgvHnrUserMng selectLclgvHnrUserMngDetail(LclgvHnrUserMngParam lclgvHnrUserMngParam) {
		return lclgvHnrMngMapper.selectLclgvHnrUserMngDetail(lclgvHnrUserMngParam);
	}

	// 지자체 명예 사용자 목록 카운트 조회
	@Override
	public int selectLclgvHnrUserMngListCnt(LclgvHnrUserMngParam lclgvHnrUserMngParam) {
		return lclgvHnrMngMapper.selectLclgvHnrUserMngListCnt(lclgvHnrUserMngParam);
	}

	// 지자체 명예 사용자 수정
	@Override
	public int updateLclgvHnrUserMng(LclgvHnrUserMng lclgvHnrUserMng, MultipartFile[] prjImageFiles) throws IOException{
		int resultCnt = 0;
		long lclgvCd = Long.parseLong(lclgvHnrUserMng.getLclgvCd());
		if(prjImageFiles[0].getSize() != 0) {
			String fileName = customFileService.saveFileByProgramNameId(programName,lclgvCd,prjImageFiles[0]);
			lclgvHnrUserMng.setRprsImgNm(fileName);
		}

		if (lclgvHnrUserMng.getLclgvCd() != null) {			// 수정시 접근성 내용 삭제
			if(!lclgvHnrUserMng.getPrjImageExplain().isEmpty()) {
				lclgvHnrMngMapper.deleteImgDescListByPrjId(lclgvHnrUserMng.getLclgvCd());
				lclgvHnrMngMapper.insertImgDescList(lclgvHnrUserMng);
			}else { // 이미지 설명이 없으면 제거
				lclgvHnrMngMapper.deleteImgDescListByPrjId(lclgvHnrUserMng.getLclgvCd());
	    	}
		} else {
			throw new UserException("이미지를 선택해주세요.");
		}

		resultCnt = lclgvHnrMngMapper.updateLclgvHnrUserMng(lclgvHnrUserMng);

		return resultCnt;
	}

	// 지자체 명예 사용자 배경 이미지 삭제
	@Override
	public int deleteItemFile(String lclgvCd) {
		return lclgvHnrMngMapper.deleteItemFile(lclgvCd);
	}

	@Override
	public List<HnrUserInfo> getHnrUserInfoList() {
		if (UserUtils.getUser() != null) {
			return lclgvHnrMngMapper.getHnrUserInfoList(UserUtils.getUser().getUserId());
		}
		return null;
	}

	// 지자체 명예 사용자 이미지 설명 조회
	@Override
	public List<PrjImageExplain> selectImgDescListByPrjId(String lclgvCd) {
		List<PrjImageExplain> prjImageExplain = lclgvHnrMngMapper.selectImgDescListByPrjId(lclgvCd);
		return prjImageExplain;
    }

	// 기부혜택증 열람현황 조회
	@Override
	public List<LclgvHnrUserMng> lclgvHnrUserViewHist(LclgvHnrUserMngParam lclgvHnrUserMngParam) {

		int lclgvHnrMngCount = lclgvHnrMngMapper.lclgvHnrUserViewHistCnt(lclgvHnrUserMngParam);
		Pagination pagination = Pagination.getInstance(lclgvHnrMngCount);

		lclgvHnrUserMngParam.setPagination(pagination);

		return lclgvHnrMngMapper.lclgvHnrUserViewHist(lclgvHnrUserMngParam);
	}

	// 기부혜택증 열람현황 카운트 조회
	@Override
	public int lclgvHnrUserViewHistCnt(LclgvHnrUserMngParam lclgvHnrUserMngParam) {
		return lclgvHnrMngMapper.lclgvHnrUserViewHistCnt(lclgvHnrUserMngParam);
	}

}
