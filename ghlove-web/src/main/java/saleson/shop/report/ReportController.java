package saleson.shop.report;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JacksonException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/opmanager/shop-statistics/dashboard/day/report")
public class ReportController {


	@Autowired
	private ReportService reportService;

	@ResponseBody
	@GetMapping("/daily/{searchDay}")
	public Map<String, Object> dailyReport(@PathVariable("searchDay") String searchDay) {
//		Map<String, Object> result = new HashMap<>();
		Map<String, Object> result = reportService.dailyReportTotalStatistics(searchDay);
		log.info("====== daily searchDay {} result {}", searchDay, result);
		return result;

	}

	@ResponseBody
	@GetMapping("/daily/{startDay}/{endDay}")
	public Map<String, Object> dayReport(@PathVariable("startDay")String startDay, @PathVariable("endDay")String endDay) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = reportService.dayReportTotalStatistics(startDay, endDay);
			log.info("====== daily searchDay {} result {}", startDay, endDay, result);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return result;
	}

	@ResponseBody
	@PostMapping("/daily-save")
	public String save() {
		try {
			reportService.saveDailyReport();
		} catch (JacksonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "OKTEST";

	}

}
