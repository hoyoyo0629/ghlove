package saleson.shop.lclgvHnrUser;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import saleson.shop.designateddonation.domain.PrjImageExplain;
import saleson.shop.lclgvHnrUser.domain.HnrUserInfo;
import saleson.shop.lclgvHnrUser.domain.LclgvHnrUserMng;
import saleson.shop.lclgvHnrUser.support.LclgvHnrUserMngParam;

public interface LclgvHnrUserMngService {

	// 지자체 명예 사용자 목록 조회
	List<LclgvHnrUserMng> selectLclgvHnrUserMngList(LclgvHnrUserMngParam lclgvHnrMngParam);

	// 지자체 명예 사용자 상세 페이지 조회
	LclgvHnrUserMng selectLclgvHnrUserMngDetail(LclgvHnrUserMngParam lclgvHnrMngParam);

	// 지자체 명예 사용자 목록 카운트 조회
	int selectLclgvHnrUserMngListCnt(LclgvHnrUserMngParam lclgvHnrMngParam);

	// 지자체 명예 사용자 수정
	int updateLclgvHnrUserMng(LclgvHnrUserMng lclgvHnrUserMng, MultipartFile[] prjImageFiles) throws IOException;

	// 명예사용자 배경이미지 삭제
	int deleteItemFile(String lclgvCd);

	// 지자체 명예 사용자 이미지 설명 조회
	public List<PrjImageExplain> selectImgDescListByPrjId(String lclgvCd);

	/**
	 * 명예시도민증 자격 조회
	 * @param userId
	 * @return
	 */
	List<HnrUserInfo> getHnrUserInfoList();

	// 기부혜택증 열람현황 조회
	List<LclgvHnrUserMng> lclgvHnrUserViewHist(LclgvHnrUserMngParam lclgvHnrMngParam);

	// 기부혜택증 열람현황 카운트 조회
	int lclgvHnrUserViewHistCnt(LclgvHnrUserMngParam lclgvHnrMngParam);

}
