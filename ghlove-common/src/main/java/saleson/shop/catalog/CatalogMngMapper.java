package saleson.shop.catalog;

import java.util.HashMap;
import java.util.List;

import saleson.shop.catalog.domain.CatalogCardNews;
import saleson.shop.catalog.domain.CatalogCardNewsImageExplain;
import saleson.shop.catalog.domain.CatalogContentImageExplain;
import saleson.shop.catalog.domain.CatalogContentMng;
import saleson.shop.catalog.domain.CatalogMng;
import saleson.shop.catalog.domain.LocgovFavItemMng;
import saleson.shop.catalog.support.CatalogCardNewsParam;
import saleson.shop.catalog.support.CatalogContentMngParam;
import saleson.shop.catalog.support.CatalogMngParam;
import saleson.shop.catalog.support.LocgovFavItemMngParam;
import saleson.shop.designateddonation.domain.DesignatedDonationNotice;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequest;
import saleson.shop.designateddonation.domain.PrjNoticeFile;
import saleson.shop.designateddonation.support.DesignatedDonationNoticeParam;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.designateddonation.support.DsgnCntrManagerRequestSearchParam;
import saleson.shop.item.domain.Item;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("catalogMngMapper")
public interface CatalogMngMapper {

	/**
	 * 소식지 버전 등록
	 * @param catalogMng
	 */
	int insertCatalogMng(CatalogMng catalogMng);

	/**
	 * 소식지 버전 수정
	 * @param catalogMng
	 */
	int updateCatalogMng(CatalogMngParam catalogMngParam);

	/**
	 * 소식지 버전 목록 조회
	 * @param param
	 * @return List
	 */
	List<CatalogMng> selectCatalogMngList(CatalogMngParam param);

	/**
	 * 소식지 상세 페이지 조회
	 * @param param
	 * @return List
	 */
	List<CatalogMng> selectCatalogMngDetail(CatalogMngParam param);

	/**
	 * 소식지 목록 카운트 조회
	 * @param catalogMng
	 */
	int selectCatalogMngListCnt(CatalogMngParam param);

	/**
	 * 소식지 중복체크
	 * @param catalogMng
	 */
	int isDuplicateCatalog(CatalogMng catalogMng);

	/**
	 * 소식지 버전 조회
	 * @param param
	 * @return CatalogMng
	 */
	CatalogMng selectCatalogMng(CatalogMngParam param);

	/**
	 * 등록된 소식지 년도 조회
	 * @return List
	 */
	List<CatalogMng> selectCatalogMngYearList();

	/**
	 * 등록된 소식지 발간호 조회
	 * @return List
	 */
	List<HashMap<String, Object>> selectCatalogMngNoList(String code);

	/**
	 * 지자체별 인기답례품 등록
	 * @param locgovFavItemMng
	 */
	int insertLocgovFavItemMng(LocgovFavItemMng locgovFavItemMng);

	/**
	 * 지자체별 인기답례품 수정
	 * @param locgovFavItemMng
	 */
	int updateLocgovFavItemMng(LocgovFavItemMng locgovFavItemMng);

	/**
	 * 지자체별 인기답례품 조회
	 * @param param
	 * @return List
	 */
	List<LocgovFavItemMng> selectLocgovFavItemMngList(LocgovFavItemMngParam param);

	/**
	 * 지자체별 인기답례품 조회
	 * @param param
	 * @return LocgovFavItemMng
	 */
	LocgovFavItemMng selectLocgovFavItemMng(LocgovFavItemMngParam param);


	/**
	 * 지자체별 인기답례품 리스트 합계
	 * @param LocgovFavItemMngParam
	 */
	int selectLocgovFavItemMngCount(LocgovFavItemMngParam param);

	/**
	 * 지자체별 주요답례품 목록
	 * @param CatalogMngParam
	 * @return List
	 */
	List<Item> selectNewRgstItemMng(CatalogMngParam param);

	/**
	 * 지자체별 주요답례품 목록 카운트
	 * @param CatalogMngParam
	 * @return int
	 */
	int selectNewRgstItemMngCount(CatalogMngParam param);

	/**
	 * 지자체별 제철식품 목록
	 * @param CatalogMngParam
	 * @return List
	 */
	List<Item> selectSeasonCatalogItemMng(CatalogMngParam param);

	/**
	 * 지자체별 제철식품 목록 카운트
	 * @param CatalogMngParam
	 * @return int
	 */
	int selectSeasonCatalogItemMngCount(CatalogMngParam param);



	// 카드 뉴스 등록
	int insertCardNews(CatalogCardNews catalogCardNews);

	// 카드 뉴스 수정
	int updateCardNews(CatalogCardNews catalogCardNews);

	// 카드 뉴스 목록 조회
	List<CatalogCardNews> selectCardNewsList(CatalogCardNewsParam catalogCardNewsParam);

	// 카드 뉴스 목록 카운트
	int selectCardNewsListCnt(CatalogCardNewsParam catalogCardNewsParam);

	// 카드 뉴스 상세 조회
	CatalogCardNews selectCardNewsDetail(CatalogCardNewsParam catalogCardNewsParam);

	/**
	 * 카드 뉴스 상태변경
	 * @param catalogCardNews
	 * @return
	 */
	int updateCardNewsListDataByLabel(CatalogCardNews catalogCardNews);

	// 카드뉴스 이미지 설명 삭제
	int deleteCardNewsImgDesc(CatalogCardNews catalogCardNews);

	// 카드뉴스 이미지 설명 등록
	int insertCardNewsImgDesc(CatalogCardNews catalogCardNews);

	// 카드뉴스 이미지 설명 조회
	List<CatalogCardNewsImageExplain> selectCardNewsImgDesc(CatalogCardNewsParam catalogCardNewsParam);


	// 소식지 내용 연도 조회
	List<CatalogContentMng> selectCatalogContentMngYear();

	// 소식지 내용 관리 목록 조회
	List<CatalogContentMng> selectCatalogContentMngList(CatalogContentMngParam catalogContentMngParam);

	// 소식지 내용 관리 목록 카운트 조회
	int selectCatalogContentMngListCnt(CatalogContentMngParam catalogContentMngParam);

	// 소식지 내용 관리 상세 조회
	CatalogContentMng selectCatalogContentMngDetail(CatalogContentMngParam catalogContentMngParam);

	// 소식지 내용 관리 등록
	int insertCatalogContentMng(CatalogContentMng catalogContentMng);

	// 소식지 내용 관리 수정
	int updateCatalogContentMng(CatalogContentMng catalogContentMng);

	// 소식지 이미지 설명 삭제
	int deleteCatalogContentImgDesc(CatalogContentMng catalogContentMng);

	// 소식지 이미지 설명 등록
	int insertCatalogContentImgDesc(CatalogContentMng catalogContentMng);

	// 소식지 이미지 설명 조회
	List<CatalogContentImageExplain> selectCatalogContentImgDesc(CatalogContentMngParam catalogContentMngParam);

}
