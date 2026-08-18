package saleson.shop.item;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.common.utils.UserUtils;
import saleson.shop.categories.CategoriesMapper;
import saleson.shop.categories.domain.Categories;
import saleson.shop.config.ConfigService;
import saleson.shop.integrationsearch.domain.ItemFlat;
import saleson.shop.integrationsearch.support.SearchApiResponse;
import saleson.shop.integrationsearch.support.SearchEngine;
import saleson.shop.integrationsearch.support.SearchEngineItem;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemImageExplain;
import saleson.shop.item.support.ItemParam;

@Service("itemFrontService")
public class ItemFrontServiceImpl extends EgovAbstractServiceImpl implements ItemFrontService {
    private static final Logger log = LoggerFactory.getLogger(ItemFrontServiceImpl.class);

    @Autowired
    private CategoriesMapper categoriesMapper;

    @Autowired
    private ItemFrontMapper itemFrontMapper;

    @Autowired
    private ConfigService configService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Value("${integration.env}")
	private String env;

	// 검색엔진 URL
    @Value("${integration.search-engine-url}")
    private String searchEngineUrl;

	@Override
    public int getItemCount(ItemParam itemParam) {
        // categoryGroupId가 있는 경우 조회 조건 추가.
        //if (itemParam.getCategoryGroupId() > 0 && "".equals(itemParam.getCategoryClass())) {
        //    itemParam.setGroupCategoryClassCodes(categoriesMapper.getCategoryClassCodesByCategoryGroupId(itemParam.getCategoryGroupId()));
        //}

        int resultCnt = 0;
        // 지자체 시도선택 여부
        if (StringUtils.hasLength(itemParam.getLocgov())) {
            if(itemParam.getLocgov().substring(0, 1).equals("U")) {
            	itemParam.setIsSidoYn("Y");	// 시도
            } else {
            	itemParam.setIsSidoYn("N");	// 시군구
            }
        }

        if ("C".equals(itemParam.getSearchGbn())) {
        	resultCnt = itemFrontMapper.getFrontItemTypecListCount(itemParam);
        } else if("L".equals(itemParam.getSearchGbn())) {
        	resultCnt = itemFrontMapper.getFrontItemTypelListCount(itemParam);
        } else { // T
        	resultCnt = itemFrontMapper.getFrontItemCount(itemParam);
        }
		transactionManager.commit(transactionManager.getTransaction(new DefaultTransactionDefinition()));
        return resultCnt;
    }

	@Override
    public int getItemCountForNewPolicy(ItemParam itemParam) {
        // categoryGroupId가 있는 경우 조회 조건 추가.
        //if (itemParam.getCategoryGroupId() > 0 && "".equals(itemParam.getCategoryClass())) {
        //    itemParam.setGroupCategoryClassCodes(categoriesMapper.getCategoryClassCodesByCategoryGroupId(itemParam.getCategoryGroupId()));
        //}

        int resultCnt = 0;
        // 지자체 시도선택 여부
        if (StringUtils.hasLength(itemParam.getLocgov())) {
            if(itemParam.getLocgov().substring(0, 1).equals("U")) {
            	itemParam.setIsSidoYn("Y");	// 시도
            } else {
            	itemParam.setIsSidoYn("N");	// 시군구
            }
        }

        if ("C".equals(itemParam.getSearchGbn())) {
        	resultCnt = itemFrontMapper.getFrontItemTypecListCountForNewPolicy(itemParam);
        } else if("L".equals(itemParam.getSearchGbn())) {
        	resultCnt = itemFrontMapper.getFrontItemTypelListCountForNewPolicy(itemParam);
        } else { // T
        	resultCnt = itemFrontMapper.getFrontItemCountForNewPolicy(itemParam);
        }
		transactionManager.commit(transactionManager.getTransaction(new DefaultTransactionDefinition()));
        return resultCnt;
    }

    @Override
    public List<Item> getItemList(ItemParam itemParam) {
        // categoryGroupId가 있는 경우 조회 조건 추가.
        //if (itemParam.getCategoryGroupId() > 0 && "".equals(itemParam.getCategoryClass())) {
        //    itemParam.setGroupCategoryClassCodes(categoriesMapper.getCategoryClassCodesByCategoryGroupId(itemParam.getCategoryGroupId()));
        //}

        List<Item> itemList = null;
        // 지자체 시도선택 여부
        if (StringUtils.hasLength(itemParam.getLocgov())) {
            if(itemParam.getLocgov().substring(0, 1).equals("U")) {
            	itemParam.setIsSidoYn("Y");	// 시도
            } else {
            	itemParam.setIsSidoYn("N");	// 시군구
            }
        }

        if ("C".equals(itemParam.getSearchGbn())) {
        	int paging = itemParam.getPage();
        	itemList = itemFrontMapper.getFrontItemTypecList(itemParam);
        	itemParam.setPage(paging);
        } else if("L".equals(itemParam.getSearchGbn())) {
        	itemList = itemFrontMapper.getFrontItemTypelList(itemParam);
        } else { // T
        	int paging = itemParam.getPage();
        	itemList = itemFrontMapper.getFrontItemList(itemParam);
        	itemParam.setPage(paging);
        }
		transactionManager.commit(transactionManager.getTransaction(new DefaultTransactionDefinition()));
        return itemList;
    }

    @Override
    public int getItemCountBySeason(ItemParam itemParam) {
        return itemFrontMapper.getFrontItemCountBySeason(itemParam);
    }

    @Override
    public List<Item> getItemListBySeason(ItemParam itemParam) {
        return itemFrontMapper.getFrontItemListBySeason(itemParam);
    }

    @Override
    public int getItemCountBySpeciality(ItemParam itemParam) {
        return itemFrontMapper.getFrontItemCountBySpeciality(itemParam);
    }

    @Override
    public List<Item> getItemListBySpeciality(ItemParam itemParam) {
        return itemFrontMapper.getFrontItemListBySpeciality(itemParam);
    }

	@Override
	public boolean isItemRestrict() {
		int level = configService.selectAlternateSystem();
		if (UserUtils.isUserLogin()) {
			return level > 2;
		}
		return level > 1;
	}

	@Override
	public List<Item> getItemListNew(ItemParam itemParam) {
		Pagination pagination;

        List<Item> itemList = null;
        // 지자체 시도선택 여부
        if (StringUtils.hasLength(itemParam.getLocgov())
        		|| StringUtils.hasLength(itemParam.getGroup())
        		|| StringUtils.hasLength(itemParam.getCategory())) {		// 지자체몰(카테고리, 가격 선택) 조회, 답례품 전체 카테고리 조회
        	if (StringUtils.hasLength(itemParam.getLocgov())) {				// 지자체몰(카테고리, 가격 선택) 조회
                if(itemParam.getLocgov().substring(0, 1).equals("U")) {
                	itemParam.setIsSidoYn("Y");	// 시도
                } else {
                	itemParam.setIsSidoYn("N");	// 시군구
                }
        	} else {
        		itemParam.setSortType("CATEGORY");
        	}
        }
        /* AS-IS : 답례품몰 및 지자체몰 조회 */
        if("local".equals(env)) { // 로컬은 답례품몰 리스트 조회 XML 사용 - 로컬 라이센스 종료 이슈
        	pagination = Pagination.getInstance(getItemCountForNewPolicy(itemParam), itemParam.getItemsPerPage());

        	if (itemParam.getPage() > 0) {
        		pagination.setCurrentPage(itemParam.getPage());
        	} else {
        		pagination.setCurrentPage(1);
        	}

        	pagination.setItemsPerPage(itemParam.getItemsPerPage());

        	itemParam.setPagination(pagination);

            if (StringUtils.isEmpty(itemParam.getGroup()) && StringUtils.isEmpty(itemParam.getCategory()) && !StringUtils.hasLength(itemParam.getLocgov())) {		// 답례품 전체, 답례품 전체 가격 조회, 개수 모자르게 나올 수 있음(국정자원진단 제시 쿼리 자체 원인)
            	itemList = itemFrontMapper.getFrontItemTypelListNew(itemParam);
            } else {			// 지자체몰(카테고리, 가격선택 포함), 답례품 전체 카테고리
//            	getFrontItemTypelList를 getItemList(ItemParam) 메서드에서 사용하기 때문에 복사하여 수정
//            	itemList = itemFrontMapper.getFrontItemTypelList(itemParam);
            	itemList = itemFrontMapper.getFrontItemTypelListForNewPolicy(itemParam);
            }

    		transactionManager.commit(transactionManager.getTransaction(new DefaultTransactionDefinition()));
        }
        /* TO-BE : 검색엔진 */
        else { // 개발, 운영은 검색엔진 사용
        	try {
            	SearchEngine<ItemParam> se = new SearchEngineItem();
            	Map<String, Object> result = se.requestApi(se.createParamter(itemParam), searchEngineUrl);
            	itemList = (List<Item>) result.get("itemList");

            	// AS-IS에서는 xml을 통해 totalCount를 가져왔었으나,
            	// 검색 엔진에서 가져오는 totalCount로 Pagination 값을 셋팅해줌
            	pagination = Pagination.getInstance((int)result.get("totalCount"), itemParam.getItemsPerPage());

            	if (itemParam.getPage() > 0) {
            		pagination.setCurrentPage(itemParam.getPage());
            	} else {
            		pagination.setCurrentPage(1);
            	}

            	pagination.setItemsPerPage(itemParam.getItemsPerPage());

            	itemParam.setPagination(pagination);
            	log.info("답례품몰 검색엔진 쿼리 정상 작동");
            }catch(Exception e) {
            	log.error("■■■■■■■■■Now Application■■■■■■■■■ : " + env);
            	log.error("■■■■■■■■■SearchEngineItem Error■■■■■■■■■ : " + e.toString());
            }
        }

        return itemList;
	}

	@Override
	public List<ItemImageExplain> getItemImagesExplainByLinkView(String itemUserCode) {
		List<ItemImageExplain> itemExplainList = itemFrontMapper.getItemImagesExplainByLinkView(itemUserCode);
		return itemExplainList;
	}

	@Override
    public List<Item> getFrontItemListByCommunity(ItemParam itemParam) {
        return itemFrontMapper.getFrontItemListByCommunity(itemParam);
    }

	@Override
    public int getFrontItemCountByCommunity(ItemParam itemParam) {
        return itemFrontMapper.getFrontItemCountByCommunity(itemParam);
    }

}
