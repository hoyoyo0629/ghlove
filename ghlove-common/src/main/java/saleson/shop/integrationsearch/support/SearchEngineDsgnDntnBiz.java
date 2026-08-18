package saleson.shop.integrationsearch.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.integrationsearch.domain.DesignatedDonationFlat;
import saleson.shop.integrationsearch.domain.ItemFlat;

public class SearchEngineDsgnDntnBiz implements SearchEngine<DesignatedDonationSearchParam>{
	private static StringBuilder sb;

	// View 컬럼 추가 시 아래 String 배열에 추가 및 OOOOFlat에 추가 필요
	private String[] columns = {
		"prj_id", "locgov_nm", "prj_status", "prj_image", "bsns_type", "bsns_sub_type", "prj_subject", "cntr_cnt", "rate_amt",
		"prj_st_dt", "prj_ed_dt", "target_amt", "cntr_amt", "display_flag", "frst_regist_pnttm", "in_date", "dsgncntr_part_name"
	};

	private static int limit = 12;

	@Override
	public String createParamter(DesignatedDonationSearchParam param) {
		if(param.getSort() != null && "RANDOM".equals(param.getSort())) {
			limit = 999999;
		} else {
			limit = 12;
		}

		sb = new StringBuilder();

		sb.append("select=")
			.append(String.join(",", columns))
			.append("&from=").append("dsgnDntn.dsgnDntn")
			.append("&offset=").append((param.getPage() > 0 ? param.getPage() - 1 : 1 - 1) * limit)
			.append("&limit=").append(limit)
			.append("&default-hilite=").append("off")
			.append("&where=").append("prj_id").append(gt).append(0)
		;

		String locgovCd = param.getLocgovCode();
		String upperLocgovCd = param.getUpperLocgovCode();
		if(locgovCd != null && !"".equals(locgovCd)) {
			createEqualSingleQuery("locgov_cd", locgovCd);
		} else if(upperLocgovCd != null && !"".equals(upperLocgovCd)) {
			createEqualSingleQuery("upper_locgov_cd", upperLocgovCd);
		}

		createEqualSingleQuery("bsns_type", param.getBsnsType());
		createEqualSingleQuery("display_flag", param.getDisplayFlag());
		createLikeSingleQuery("prj_subject", param.getSearchKeyword());
		createEqualSingleQuery("prj_status", param.getPrjStatus());

		String conditionType = param.getConditionType();
		if(conditionType != null && !"".equals(conditionType)) {
			createEqualSingleQuery("prj_status", "2");
			String sysdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			sb.append(and).append("(").append(sysdate).append(gt).append(equal).append("prj_st_dt").append(and).append(sysdate).append(lt).append(equal).append("prj_ed_dt").append(")");
		}

		createDisplayAndOrderbyQuery(param.getDisplay(), param.getSort());

		// 검색엔진 쿼리 확인용
//		 System.out.println(sb.toString().replaceAll("%20", " ").replaceAll("%3D", "=").replaceAll("%3E", ">").replaceAll("%3C", "<").replaceAll("%27", "'"));

		return sb.toString();
	}

	private void createEqualSingleQuery(String column, String value) {
		if(value != null && !"".equals(value)) {
			sb.append(and).append(column).append(equal).append(apostrophe).append(value).append(apostrophe);
		}
	}

	private void createLikeSingleQuery(String column, String value) {
		if(value != null && !"".equals(value)) {
			value = value.replaceAll(" ", "%20");
			sb.append(and).append(column).append(space).append("like").append(space).append(apostrophe).append("*").append(value).append("*").append(apostrophe);
		}
	}

	private void createDisplayAndOrderbyQuery(String display, String sort) {
		if(display != null && "FRONT".equals(display)) {
//			sb.append(and).append("prj_status").append(in).append("{'2', '9'}");
			createEqualSingleQuery("display_flag", "Y");

			if(!"RANDOM".equals(sort)) {
				sb.append(space).append(orderby).append("prj_status").append(space).append("asc");
				if(sort == null || "".equals(sort)) {
					sort = "LATEST";
				}
				switch(sort) {
					case "LATEST" :
						sb.append(",").append("frst_regist_pnttm").append(space).append("desc");
						break;
					case "AMOUNT" :
						sb.append(",").append("cntr_amt").append(space).append("desc");
						break;
					case "RATE" :
						sb.append(",").append("rate_amt").append(space).append("desc");
						break;
					case "ENDING" :
						sb.append(",").append("in_date").append(space).append("asc");
						sb.append(",").append("prj_ed_dt").append(space).append("asc");
						break;
					case "BSNS" :
						sb.append(",").append("bsns_sub_type").append(space).append("asc");
						break;
				}
			}
		} else {
			sb.append(space).append(orderby).append("prj_id").append(space).append("asc");
		}
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
		sb = new StringBuilder();

		while((input = br.readLine()) != null) {
			sb.append(input);
		}

		br.close();

		String data = sb.toString();

		ObjectMapper mapper = new ObjectMapper();
		SearchApiResponse<DesignatedDonationFlat> response = mapper.readValue(data, new TypeReference<SearchApiResponse<DesignatedDonationFlat>>() {});
    	List<DesignatedDonationFlat> resultList = response.getResult().getRows().stream()
    			.map(SearchApiResponse.Row::getFields)
    			.collect(Collectors.toList());

		result.put("dsgndntnList", resultList.stream().map(DesignatedDonationFlat::toDesignatedDonation).collect(Collectors.toList()));
		result.put("totalCount", response.getResult().getTotalCount());

		return result;
	}
}
