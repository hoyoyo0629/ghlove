package saleson.shop.keyword;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.util.MessageUtils;

import saleson.common.utils.HangulUtils;
import saleson.shop.item.support.ItemParam;
import saleson.shop.keyword.domain.Keyword;

@Controller
@RequestMapping("keyword")
public class KeywordController {
	private static final Logger log = LoggerFactory.getLogger(KeywordController.class);

	@Autowired
	private KeywordService keywordService;

	@Autowired
	Environment environment;
	
	@PostMapping("auto-complete")
	@ResponseBody
	public List<Keyword> autoComplete(ItemParam itemParam, Model model, HttpServletResponse response) {
		// 배치에 등록 할 서비스 2017-05-15_seungil.lee
//		 keywordService.setKeywordDaily();

		List<Keyword> list = new ArrayList<>();
		List<Keyword> result = new ArrayList<>();
		String separated = HangulUtils.seperate(itemParam.getQuery());

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			JSONParser parser = new JSONParser();
			// 저장 된 json파일 불러오기
			fis = new FileInputStream(environment.getProperty("upload.root") + "/auto-complete.json");
			isr = new InputStreamReader(fis,"UTF-8");
			br = new BufferedReader(isr);

			JSONArray ja = (JSONArray)parser.parse(br);
			ObjectMapper om = new ObjectMapper();
			list = om.readValue(ja.toString(), new TypeReference<List<Keyword>>(){});
		} catch (ParseException | IOException e) {
//			log.warn("[Exception] JSONParser : {}", e.getMessage(), e);
			log.warn("[Exception] JSONParser : {}", getClass().getName() + " :: autoComplete ParseException | IOException ========", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
//					log.warn("fis close", e);
					log.warn("fis close : {}", getClass().getName() + " :: autoComplete IOException ========", e);
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
//					log.warn("isr close", e);
					log.warn("isr close : {}", getClass().getName() + " :: autoComplete IOException ========", e);
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
//					log.warn("br close", e);
					log.warn("br close : {}", getClass().getName() + " :: autoComplete IOException ========", e);
				}
			}
		}

		if (list != null && !list.isEmpty() && !"".equals(itemParam.getQuery())) {
			int i = 0;
			for (Keyword keyword : list) {
				if (i > 7) {
					break;
				}

				if (keyword.getKeywordSeperation() != null
					&& keyword.getKeywordSeperation().indexOf(separated)>-1) {
					result.add(keyword);
				}

				i++;
			}
		}
		
		return result;
	}
	
	@GetMapping("keyword-init")
	@ResponseBody
	public String init() {
		try {
			keywordService.setKeywordDaily();
		}
		catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
//			return "error - " + e.getMessage();
			log.error("ERROR: {}", getClass().getName() + " :: init RuntimeException ==============");
			//return "error - " + MessageUtils.getMessage("실패했습니다.");			// 실패했습니다.
			return "error - 실패했습니다.";			// 실패했습니다.
		}
		return "ok";
	}
}
