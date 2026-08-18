package saleson.shop.lclgvHnrUser;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.domain.PrjImageExplain;
import saleson.shop.lclgvHnrUser.domain.HnrUserInfo;
import saleson.shop.lclgvHnrUser.domain.LclgvHnrUserMng;
import saleson.shop.lclgvHnrUser.support.LclgvHnrUserMngParam;

@Mapper("lclgvHnrMngMapper")
public interface LclgvHnrUserMngMapper {

	/**
	 * 지자체 명예 사용자 버전 수정
	 * @param lclgvHnrMng
	 */
	int updateLclgvHnrUserMng(LclgvHnrUserMng lclgvHnrUserMng);

	/**
	 * 지자체 명예 사용자 목록 조회
	 * @param param
	 * @return List
	 */
	List<LclgvHnrUserMng> selectLclgvHnrUserMngList(LclgvHnrUserMngParam param);

	/**
	 * 지자체 명예 사용자 상세 페이지 조회
	 * @param param
	 * @return List
	 */
	LclgvHnrUserMng selectLclgvHnrUserMngDetail(LclgvHnrUserMngParam param);

	/**
	 * 지자체 명예 사용자 목록 카운트 조회
	 * @param lclgvHnrMng
	 */
	int selectLclgvHnrUserMngListCnt(LclgvHnrUserMngParam param);


	/**
	 * 지자체 명예 사용자 배경이미지 삭제
	 * @param lclgvHnrMng
	 */
	int deleteItemFile(String lclgvCd);

	/**
	 * 명예시도민증 자격 조회
	 * @param userId
	 * @return
	 */
	List<HnrUserInfo> getHnrUserInfoList(long userId);

	/**
	 * 지자체 명예 사용자 접근성 이미지 설명 조회
	 * @param itemId
	 * @return
	 */
	List<PrjImageExplain> selectImgDescListByPrjId(String lclgvCd);

	/**
	 * 지자체 명예 사용자 접근성 이미지 설명 등록
	 * @param lclgvHnrUserMng
	 */
	int insertImgDescList(LclgvHnrUserMng lclgvHnrUserMng);

	/**
	 * 지정기부 접근성 이미지 설명 삭제
	 * @param itemId
	 * @return
	 */
	int deleteImgDescListByPrjId(String lclgvCd);

	/**
	 * 기부혜택증 열람현황 조회
	 * @param param
	 * @return List
	 */
	List<LclgvHnrUserMng> lclgvHnrUserViewHist(LclgvHnrUserMngParam param);


	/**
	 * 기부혜택증 열람현황 카운트 조회
	 * @param lclgvHnrMng
	 */
	int lclgvHnrUserViewHistCnt(LclgvHnrUserMngParam param);

}
