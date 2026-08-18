package saleson.shop.integrationsearch.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import saleson.shop.integrationsearch.domain.ItemFlat;
import saleson.shop.item.domain.Item;
import saleson.shop.item.support.ItemParam;

public class SearchEngineItem implements SearchEngine<ItemParam>{
	private static StringBuilder sb;

	// View 컬럼 추가 시 아래 String 배열에 추가 및 OOOOFlat에 추가 필요
	private String[] columns = {
		"item_id", "seller_id", "item_code", "item_user_code", "item_name", "item_summary", "item_data_type", "item_type",
		"item_new_flag", "item_label", "item_type1", "item_type2", "item_type3", "item_type4", "item_type5", "item_type6", "item_type7",
		"item_type8", "item_type9", "item_type10", "sold_out", "stock_flag", "stock_quantity", "tax_type", "item_price", "cost_price",
		"supply_price", "sale_price", "adult_item_yn", "mobile_item_yn", "item_image", "item_option_flag", "item_option_type",
		"item_option_title1", "item_option_title2", "item_option_title3", "data_status_code", "display_flag", "stock_code", "stock_schedule_auto_flag",
		"stock_schedule_type", "stock_schedule_date", "order_min_quantity", "order_max_quantity", "sale_quantity", "created_date",
		"seller_name", "seller_login_id", "seller_company_name", "seller_commission_rate", "locgov_code", "locgov_nm", "code", "category_url"
	};

	// 답례품몰 한 페이지에 노출 개수에 따라 수정
	private final int limit = 12;

	@Override
	public String createParamter(ItemParam param) {
		/*
		 * API 요청 Sample
		 * http://192.168.141.109:7577/search?select=item_id,item_name
		 * &from=item.item
		 * &offset=0
		 * &limit=12
		 * &default-hilite=off
		 * &where=sale_price%3E0%20and%20sold_out%3D%27N%27%20and%20code%3D%27tour%27%20order%20by%20created_date%20desc
		 * &custom=null@$|null||created_date|
		 *
		 * from=볼륨명.테이블명 → volumn명, table명은 검색엔진 담당자에게 확인하여 적용
		 * offset=12 → 0부터 시작하며, 0~11까지 총 2개로 답례품몰 1page, offset=12라면 12번째 결과부터 보여주기 때문에 2page
		 * limit=12 → 몇 개의 결과를 보여줄 것인지 설정
		 * default-hilite=off → 기본값 : default-hilite=on일 경우 검색된 것에 <b></b> 태그를 붙여줌 A라는 컬럼에 N을 조회하면 API 결과에서 N에 볼드 처리가 모두 됨
		 * 		Ex) main-image.p<b>n</b>g
		 * where=조건문 → DB SQL문과 같이 사용
		 * 		sale_price%3E0%20and%20sold_out%3D%27N%27%20and%20code%3D%27tour%27%20order%20by%20created_date%20desc
		 * 		= 인코딩 : sale_price>10000 and sold_out='N' and code='tour' order by created_date desc
		 * */
		sb = new StringBuilder();
    	// 답례품몰 기본 쿼리 셋팅
    	sb.append("select=")
    		.append(String.join(",", columns))
    		.append("&from=").append("item.item")
    		.append("&offset=").append((param.getPage() > 0 ? param.getPage() - 1 : 1 - 1) * limit)
	    	.append("&limit=").append(limit)
	    	.append("&default-hilite=").append("off")
    	;

    	// 답례품몰 Price 조건 쿼리 생성
    	createItemPriceQuery(param.getPrice());

    	// 품절 상품 제외
    	sb.append(and).append("sold_out").append(equal).append(apostrophe).append("N").append(apostrophe);

    	// 그룹코드(OP_CATEGORY_GROUP의 CODE 컬럼)
    	String group =  param.getGroup();
    	if(group != null && !"".equals(group)) {
    		sb.append(and).append("code").append(equal).append(apostrophe).append(group).append(apostrophe);
    	}

    	// 카테고리 URL(세부 분류로 예상 → fruit, beans 등)
    	String category = param.getCategory();
    	if(category != null && !"".equals(category)) {
    		// category가 있다면 조건문 생성
    		createItemCategoryQuery(param);
    	}

    	// 지자체 코드
    	String locgov = param.getLocgov();
    	// 어디서 쓰이는 지 모름
    	String isSidoYn = param.getIsSidoYn();
    	if(param.getLocgov() != null && "Y".equals(isSidoYn)) {
    		sb.append(and).append("upper_locgov_code").append(equal).append(apostrophe).append(locgov.substring(0, 2)).append("000").append(apostrophe);
    	} else if (param.getLocgov() != null && "N".equals(isSidoYn)){
    		sb.append(and).append("locgov_code").append(equal).append(apostrophe).append(locgov).append(apostrophe);
    	}

    	// 답례품 검색 시
    	String keyword = param.getKeyword();
    	if(keyword != null && !"".equals(keyword) && !keyword.isEmpty()) {
    		// keyword_idx에 포함된 컬럼 : item_name, item_keyword
    		sb.append(and).append("keyword_idx").append(space).append("like").append(space).append(apostrophe).append("*").append(keyword.replaceAll(" ", "%20")).append("*").append(apostrophe);
    	}

    	// sortType이 없거나, CATEGORY면 최근 등록일순
    	String sortLog = createItemSort(param.getSortType(), keyword);

    	// 검색엔진 확장을 위한 로그 생성
    	// &custom=null@카테고리$|검색키워드||정렬기준|
    	String searchEngineLog = "".equals(param.getCategory()) && "".equals(param.getGroup()) ?
    			"null" : "".equals(param.getCategory()) ? param.getGroup() : param.getCategory();
    	sb.append("&custom=").append("null").append("@").append(searchEngineLog)
    		.append("$|" + (keyword != null && !"".equals(keyword) && !keyword.isEmpty() ? keyword.replaceAll(" ", "%20") : "null") + "|")
    		.append("|" + sortLog +"|");

    	return sb.toString();

	}

	private static void createItemPriceQuery(Integer price) {
    	// sale_price는 무조건 값이 존재
    	sb.append("&where=sale_price");

    	switch(price) {
	    	case 0 :
	    		sb.append(gt).append(price);
	    		break;
	    	case 10000 :
	    		sb.append(lt).append(equal).append(price);
	    		break;
	    	case 30000 :
	    		sb.append(space).append("between").append(space).append(10000).append(space).append("and").append(space).append(price);
	    		break;
	    	case 50000 :
	    		sb.append(space).append("between").append(space).append(30000).append(space).append("and").append(space).append(price);
	    		break;
	    	case 50001 :
	    		sb.append(gt).append(equal).append(50000);
	    		break;
	    	default : break;
		}
    }

    private static void createItemCategoryQuery(ItemParam itemParam) {
    	// 카테고리 Depth
    	String categoryLevel = itemParam.getCategoryLevel();
    	// 위의 category에 맞는 코드들(101, 000 등)
    	List<String> categoryClassList = new ArrayList<>();
    	categoryClassList.add("padding");
    	categoryClassList.add(itemParam.getCategoryClass1());
    	categoryClassList.add(itemParam.getCategoryClass2());
    	categoryClassList.add(itemParam.getCategoryClass3());
    	categoryClassList.add(itemParam.getCategoryClass4());

		sb.append(and).append("category_url").append(equal).append(apostrophe).append(itemParam.getCategory()).append(apostrophe);
		switch(categoryLevel) {
    		case "4" :
    			sb.append(and).append("category_class4").append(equal).append(apostrophe).append(categoryClassList.get(4)).append(apostrophe);
    		case "3" :
    			sb.append(and).append("category_class3").append(equal).append(apostrophe).append(categoryClassList.get(3)).append(apostrophe);
    		case "2" :
    			sb.append(and).append("category_class2").append(equal).append(apostrophe).append(categoryClassList.get(2)).append(apostrophe);
    		case "1" :
    			sb.append(and).append("category_class1").append(equal).append(apostrophe).append(categoryClassList.get(1)).append(apostrophe);
    		default : break;
		}
    }

    private static String createItemSort(String sortType, String keyword) {
    	String sortStd = "";
    	// 검색어(keyword) 있을 경우 지자체 대표 답례품 노출 안 되도록 조건 추가
    	if((keyword != null && !"".equals(keyword)) || sortType == null || "".equals(sortType) || "CATEGORY".equalsIgnoreCase(sortType)) {
    		sortStd = "created_date";
//    		sb.append(space).append(orderby).append("created_date").append(space).append("desc");
    	} else {
    		// 그 외 조건은 대표 답례품 오름차순, SortType에 따른 정렬
    		sortStd = "sort_seq1";
    		sb.append(space).append(orderby).append("sort_seq1").append(space).append("asc");
    		switch(sortType) {
	    		case "LATEST" :
	    			sortStd += ",created_date";
	    			sb.append(",").append("created_date").append(space).append("desc");
	    			break;
	    		case "OLDEST" :
	    			sortStd += ",created_date";
	    			sb.append(",").append("created_date").append(space).append("asc");
	    			break;
	    		case "HIGHPRICE" :
	    			sortStd += ",sale_price";
	    			sb.append(",").append("sale_price").append(space).append("desc");
	    			break;
	    		case "LOWPRICE" :
	    			sortStd += ",sale_price";
	    			sb.append(",").append("sale_price").append(space).append("asc");
	    			break;
    			default: break;
    		}
    	}
    	return sortStd;
    }

	@Override
	public Map<String, Object> requestApi(String parameter, String searchEngineUrl) throws IOException {
		Map<String, Object> result = new HashMap<>();
		URL url = new URL(searchEngineUrl + parameter);
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    	conn.setRequestMethod("GET");
    	conn.setRequestProperty("Content-Type", "application/json");

    	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	String input = "";
    	StringBuilder sb = new StringBuilder();

    	while((input = br.readLine()) != null) {
    		sb.append(input);
    	}

    	br.close();

    	String data = sb.toString();

    	ObjectMapper mapper = new ObjectMapper();
    	SearchApiResponse<ItemFlat> response = mapper.readValue(data, new TypeReference<SearchApiResponse<ItemFlat>>() {});
    	List<ItemFlat> resultList = response.getResult().getRows().stream()
    			.map(SearchApiResponse.Row::getFields)
    			.collect(Collectors.toList());

    	result.put("itemList", resultList.stream().map(ItemFlat::toItem).collect(Collectors.toList()));
    	result.put("totalCount", response.getResult().getTotalCount());

    	return result;
	}
}
