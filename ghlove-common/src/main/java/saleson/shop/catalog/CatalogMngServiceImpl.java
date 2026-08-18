package saleson.shop.catalog;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import saleson.shop.catalog.domain.CatalogCardNews;
import saleson.shop.catalog.domain.CatalogCardNewsImageExplain;
import saleson.shop.catalog.domain.CatalogContentMng;
import saleson.shop.catalog.domain.CatalogMng;
import saleson.shop.catalog.support.CatalogCardNewsParam;
import saleson.shop.catalog.support.CatalogContentMngParam;
import saleson.shop.catalog.support.CatalogMngParam;
import saleson.common.enumeration.IdType;
import saleson.common.file.service.CustomFileService;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.catalog.domain.LocgovFavItemMng;
import saleson.shop.catalog.support.LocgovFavItemMngParam;
import saleson.shop.item.domain.Item;
import saleson.shop.user.LocgovService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("catalogMngService")
public class CatalogMngServiceImpl  implements CatalogMngService {

	@Autowired
	private CatalogMngMapper catalogMngMapper;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private  CustomFileService customFileService;

	private final String programName = "catalogContent";

	// 소식지 목록 조회
	@Override
	public List<CatalogMng> selectCatalogMngList(CatalogMngParam catalogMngParam) {

		int catalogMngCount = catalogMngMapper.selectCatalogMngListCnt(catalogMngParam);
		Pagination pagination = Pagination.getInstance(catalogMngCount);

		catalogMngParam.setPagination(pagination);

		return catalogMngMapper.selectCatalogMngList(catalogMngParam);
	}

	// 소식지 상세 페이지 조회
	@Override
	public List<CatalogMng> selectCatalogMngDetail(CatalogMngParam catalogMngParam) {
		return catalogMngMapper.selectCatalogMngList(catalogMngParam);
	}

	// 소식지 목록 카운트 조회
	@Override
	public int selectCatalogMngListCnt(CatalogMngParam catalogMngParam) {
		return catalogMngMapper.selectCatalogMngListCnt(catalogMngParam);
	}

	// 소식지 목록 연도 리스트 조회
	@Override
	public List<CatalogMng> selectCatalogMngYearList() {
		return catalogMngMapper.selectCatalogMngYearList();
	}

	// 소식지 목록 발간호 리스트 조회
	@Override
	public List<HashMap<String, Object>> selectCatalogMngNoList(String code) {
		return catalogMngMapper.selectCatalogMngNoList(code);
	}

	// 소식지 등록
	@Override
	public int insertCatalogMng(CatalogMng catalogMng) {
		return catalogMngMapper.insertCatalogMng(catalogMng);
	}

	// 소식지 중복체크
	@Override
    public boolean isDuplicateCatalog(CatalogMng catalogMng) {
        int dupCnt = catalogMngMapper.isDuplicateCatalog(catalogMng);
		return dupCnt > 0 ? true : false;
    }

	// 소식지 수정
	@Override
	public int updateCatalogMng(CatalogMngParam catalogMngParam) {
		return catalogMngMapper.updateCatalogMng(catalogMngParam);
	}

	@Override
	public List<LocgovFavItemMng> selectLocgovFavItemMngList(LocgovFavItemMngParam locgovFavItemMngParam) {
		if (ShopUtils.isOpmanagerPage()) {
			long userId = UserUtils.getUser().getUserId();
			if (userId == 0) {
				throw new UserException("로그인 상태가 아닙니다.");
			}

			if (UserUtils.hasLocgovManagerRole()) {
				String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
				if (StringUtils.hasLength(locgovCode)) {
					locgovFavItemMngParam.setLocgovCode(locgovCode);
				}
			} else if (!UserUtils.hasMasterManagerRole()) {
				throw new OpRuntimeException("권한이 없습니다.");
			}
		} else {
			locgovFavItemMngParam.setConditionType("FRONT");
		}

		int LocgovFavItemMngCount = catalogMngMapper.selectLocgovFavItemMngCount(locgovFavItemMngParam);

		Pagination pagination = Pagination.getInstance(LocgovFavItemMngCount);

		locgovFavItemMngParam.setPagination(pagination);

		return catalogMngMapper.selectLocgovFavItemMngList(locgovFavItemMngParam);
	}

	@Override
	public List<Item> selectNewRgstItemMng(CatalogMngParam param) {
		return catalogMngMapper.selectNewRgstItemMng(param);
	}

	@Override
	public List<Item> selectSeasonCatalogItemMng(CatalogMngParam param) {
		return catalogMngMapper.selectSeasonCatalogItemMng(param);
	}

	@Override
	public int selectNewRgstItemMngCount(CatalogMngParam param) {
		return catalogMngMapper.selectNewRgstItemMngCount(param);
	}

	@Override
	public int selectSeasonCatalogItemMngCount(CatalogMngParam param) {
		return catalogMngMapper.selectSeasonCatalogItemMngCount(param);
	}

	@Override
	public int insertLocgovFavItemMng(LocgovFavItemMng locgovFavItemMng) {
//		long userId = UserUtils.getUserId();
//		Timestamp now = Timestamp.valueOf(LocalDateTime.now());

		if (locgovFavItemMng.getItemId() == 0) {
            throw new UserException("답례품을 선택해주세요.");
        }
		return catalogMngMapper.insertLocgovFavItemMng(locgovFavItemMng);
	}

	@Override
	public int updateLocgovFavItemMng(LocgovFavItemMng locgovFavItemMng) throws RuntimeException {
		if (locgovFavItemMng.getItemId() == 0) {
            throw new UserException("답례품을 선택해주세요.");
        }

		long userId = UserUtils.getUserId();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());

		locgovFavItemMng.setLastUpdusrId(userId);
		locgovFavItemMng.setLastUpdtPnttm(now);

		return catalogMngMapper.updateLocgovFavItemMng(locgovFavItemMng);
	}

	@Override
	public LocgovFavItemMng selectLocgovFavItemMng(LocgovFavItemMngParam locgovFavItemMngParam) {
		return catalogMngMapper.selectLocgovFavItemMng(locgovFavItemMngParam);
	}

	@Override
    public int deleteListLocgovFavItemMng(ListParam listparam) {
    	int resultCnt = 0;
        if (listparam.getId() != null) {
            for (String id : listparam.getId()) {
	            LocgovFavItemMng locgovFavItemMng = new LocgovFavItemMng();
	            String[] idArray = id.split("-");
	            locgovFavItemMng.setCatalogYear(Integer.parseInt(idArray[0]));
	            locgovFavItemMng.setCatalogNo(Integer.parseInt(idArray[1]));
	            locgovFavItemMng.setLocgovCode(idArray[2]);
	            locgovFavItemMng.setDeleteYn("Y");
	            resultCnt += catalogMngMapper.updateLocgovFavItemMng(locgovFavItemMng);
            }
        }
        return resultCnt;
    }

//	@Override
//	public int insertCatalogCardNews(CatalogCardNews catalogCardNews) {
//		if (!UserUtils.hasMasterManagerRole()) {
//			throw new UserException("권한이 없습니다.");
//		}
//		return catalogMngMapper.insertCardNews(catalogCardNews);
//	}
//
//	@Override
//	public int updateCatalogCardNews(CatalogCardNews catalogCardNews) {
//		if (!UserUtils.hasMasterManagerRole()) {
//			throw new UserException("권한이 없습니다.");
//		}
//		return catalogMngMapper.updateCardNews(catalogCardNews);
//	}

	@Override
	public List<CatalogCardNews> selectCatalogCardNewsList(CatalogCardNewsParam catalogCardNewsParam) {
		if (catalogCardNewsParam.getItemsPerPage() < 10) {
			catalogCardNewsParam.setItemsPerPage(10);
		}
		Pagination pagination = Pagination.getInstance(catalogMngMapper.selectCardNewsListCnt(catalogCardNewsParam), catalogCardNewsParam.getItemsPerPage());

		catalogCardNewsParam.setPagination(pagination);

		return catalogMngMapper.selectCardNewsList(catalogCardNewsParam);
	}

	@Override
	public CatalogCardNews selectCatalogCardNews(CatalogCardNewsParam catalogCardNewsParam) {
		CatalogCardNews detail = catalogMngMapper.selectCardNewsDetail(catalogCardNewsParam);
		detail.setCardNewsImageExplains(catalogMngMapper.selectCardNewsImgDesc(catalogCardNewsParam));
		return detail;
	}

	@Override
	public int updateCardNewsListDataByLabel(CatalogCardNews catalogCardNews) {
		if (!UserUtils.hasMasterManagerRole()) {
			throw new UserException("권한이 없습니다.");
		}
		return catalogMngMapper.updateCardNewsListDataByLabel(catalogCardNews);
	}

	@Override
	public int saveCatalogCardNews(CatalogCardNews catalogCardNews) {
		if (!UserUtils.hasMasterManagerRole()) {
			throw new UserException("권한이 없습니다.");
		}
		int resultCnt = 0;
		if (catalogCardNews.getCardNewsId() > 0) {
			resultCnt = catalogMngMapper.updateCardNews(catalogCardNews);
		} else {
			resultCnt = catalogMngMapper.insertCardNews(catalogCardNews);
		}

		catalogMngMapper.deleteCardNewsImgDesc(catalogCardNews);
		List<CatalogCardNewsImageExplain> imgDescList = catalogCardNews.getCardNewsImageExplains();
		if (imgDescList != null && !imgDescList.isEmpty()) {
			catalogMngMapper.insertCardNewsImgDesc(catalogCardNews);
		}

		return resultCnt;
	}

	@Override
	public List<CatalogContentMng> selectCatalogContentMngList(CatalogContentMngParam catalogContentMngParam) {
		// TODO :: 목록 조회
		int catalogContentMngListCount = catalogMngMapper.selectCatalogContentMngListCnt(catalogContentMngParam);
		Pagination pagination = Pagination.getInstance(catalogContentMngListCount);
		catalogContentMngParam.setPagination(pagination);

		return catalogMngMapper.selectCatalogContentMngList(catalogContentMngParam);
	}

	@Override
	public CatalogContentMng selectCatalogContentMngDetail(CatalogContentMngParam catalogContentMngParam) {
		// TODO :: 상세 조회
		CatalogContentMng detail = catalogMngMapper.selectCatalogContentMngDetail(catalogContentMngParam);
		detail.setCatalogContentImageExplain(catalogMngMapper.selectCatalogContentImgDesc(catalogContentMngParam));
		return detail;
	}

	@Override
	public int saveCatalogContent(CatalogContentMng catalogContentMng, MultipartFile[] prjImageFiles) throws IOException{
		if (!UserUtils.hasMasterManagerRole()) {
			throw new UserException("권한이 없습니다.");
		}
		int resultCnt = 0;
		if (catalogContentMng.getCatalogContentId() > 0) {
			if(prjImageFiles[0].getSize() != 0) {
				catalogContentMng.setThumbnailImgPath(customFileService.saveFileByProgramNameId(programName,catalogContentMng.getCatalogContentId(),prjImageFiles[0]));
			}
			resultCnt = catalogMngMapper.updateCatalogContentMng(catalogContentMng);
		} else {
			catalogMngMapper.insertCatalogContentMng(catalogContentMng);
			catalogContentMng.setThumbnailImgPath(customFileService.saveFileByProgramNameId(programName,catalogContentMng.getCatalogContentId(),prjImageFiles[0]));
			resultCnt = catalogMngMapper.updateCatalogContentMng(catalogContentMng);
		}
		/*
		 * catalogMngMapper.deleteCatalogContentImgDesc(catalogContentMng);
		 *
		 * List<CatalogContentImageExplain> imgDescList =
		 * catalogContentMng.getCatalogContentImageExplain(); if (imgDescList != null &&
		 * !imgDescList.isEmpty()) {
		 * catalogMngMapper.insertCatalogContentImgDesc(catalogContentMng); }
		 */
		return resultCnt;
	}

	@Override
	public int insertCatalogContentMng(CatalogContentMng catalogContentMng) {
		// TODO :: 등록
		return catalogMngMapper.insertCatalogContentMng(catalogContentMng);
	}

	@Override
	public int updateCatalogContentMng(CatalogContentMng catalogContentMng) {
		// TODO :: 수정
		return catalogMngMapper.updateCatalogContentMng(catalogContentMng);
	}
}
