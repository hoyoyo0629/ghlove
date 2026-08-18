package saleson.shop.catalog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.web.domain.ListParam;

import saleson.shop.catalog.domain.CatalogCardNews;
import saleson.shop.catalog.domain.CatalogContentMng;
import saleson.shop.catalog.domain.CatalogMng;
import saleson.shop.catalog.support.CatalogCardNewsParam;
import saleson.shop.catalog.support.CatalogContentMngParam;
import saleson.shop.catalog.support.CatalogMngParam;
import saleson.shop.catalog.domain.LocgovFavItemMng;
import saleson.shop.catalog.support.LocgovFavItemMngParam;
import saleson.shop.item.domain.Item;

public interface CatalogMngService {

	// 소식지 목록 조회
	List<CatalogMng> selectCatalogMngList(CatalogMngParam catalogMngParam);

	// 소식지 상세 페이지 조회
	List<CatalogMng> selectCatalogMngDetail(CatalogMngParam catalogMngParam);

	// 소식지 목록 카운트 조회
	int selectCatalogMngListCnt(CatalogMngParam catalogMngParam);

	// 소식지 목록 연도 리스트 조회
	List<CatalogMng> selectCatalogMngYearList();

	/**
	 * 등록된 소식지 발간호 조회
	 * @return List
	 */
	List<HashMap<String, Object>> selectCatalogMngNoList(String code);

	// 소식지 등록
	int insertCatalogMng(CatalogMng catalogMng);

	// 소식지 중복체크
	boolean isDuplicateCatalog(CatalogMng catalogMng);

	// 소식지 수정
	int updateCatalogMng(CatalogMngParam catalogMngParam);

	// 소식지-인기답례품 목록 조회
	List<LocgovFavItemMng> selectLocgovFavItemMngList(LocgovFavItemMngParam locgovFavItemMngParam);

	// 소식지-인기답례품 정보 조회
	LocgovFavItemMng selectLocgovFavItemMng(LocgovFavItemMngParam locgovFavItemMngParam);

	/**
	 * 소식지-인기답례품 정보 수정
	 * @param locgovFavItemMng
	 * @return int
	 * @throws RuntimeException
	 */
	int updateLocgovFavItemMng(LocgovFavItemMng locgovFavItemMng) throws RuntimeException;

	/**
	 * 소식지-인기답례품 정보 등록
	 * @param locgovFavItemMng
	 * @return int
	 * @throws RuntimeException
	 */
	int insertLocgovFavItemMng(LocgovFavItemMng locgovFavItemMng) throws RuntimeException;


	// 주요 답례품 목록 조회
	List<Item> selectNewRgstItemMng(CatalogMngParam param);

	// 주요 답례품 목록 조회
	int selectNewRgstItemMngCount(CatalogMngParam param);

	// 제철 답례품 목록 조회
	List<Item> selectSeasonCatalogItemMng(CatalogMngParam param);

	// 제철 답례품 목록 조회
	int selectSeasonCatalogItemMngCount(CatalogMngParam param);

	int deleteListLocgovFavItemMng(ListParam listParam);

	int saveCatalogCardNews(CatalogCardNews catalogCardNews);

	// 카드 뉴스 등록
//	int insertCatalogCardNews(CatalogCardNews catalogCardNews);

	// 카드 뉴스 수정
//	int updateCatalogCardNews(CatalogCardNews catalogCardNews);

	// 카드 뉴스 목록 조회
	List<CatalogCardNews> selectCatalogCardNewsList(CatalogCardNewsParam catalogCardNewsParam);

	// 카드 뉴스 상세 조회
	CatalogCardNews selectCatalogCardNews(CatalogCardNewsParam catalogCardNewsParam);

	// 카드 뉴스 목록에서 삭제 처리
	int updateCardNewsListDataByLabel(CatalogCardNews catalogCardNews);


	// 소식지 내용 관리 목록 조회
	List<CatalogContentMng> selectCatalogContentMngList(CatalogContentMngParam catalogContentMngParam);

	// 소식지 내용 관리 상세 조회
	CatalogContentMng selectCatalogContentMngDetail(CatalogContentMngParam catalogContentMngParam);

	// 소식지 내용 관리 등록
	int insertCatalogContentMng(CatalogContentMng catalogContentMng);

	// 소식지 내용 관리 수정
	int updateCatalogContentMng(CatalogContentMng catalogContentMng);

	// 소식지 내용 관리 저장
	int saveCatalogContent(CatalogContentMng catalogContentMng, MultipartFile[] detailImageFiles) throws IOException;



}
