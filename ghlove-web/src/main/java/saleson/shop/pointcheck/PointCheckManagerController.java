package saleson.shop.pointcheck;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;

import saleson.shop.batchlog.BatchlogManagerController;
import saleson.shop.pointcheck.domain.PointCheck;
import saleson.shop.pointcheck.support.PointCheckParam;


@Controller
@RequestMapping("/opmanager/point-check")
@RequestProperty(title="pointCheck", layout="default", template="opmanager")
public class PointCheckManagerController {

	private static final Logger log = LoggerFactory.getLogger(PointCheckManagerController.class);

	@Autowired
    PointCheckService pointCheckService;

    @Autowired
    SequenceService sequenceService;

    /**	배치 작업 조회
     * @param param
     * @param model
     * @return
     */
    @GetMapping(value="list")
    public String pointCheckList(@ModelAttribute PointCheckParam param, Model model) {

    	if(!StringUtils.hasLength(param.getSearchStartDate()) && !StringUtils.hasLength(param.getSearchEndDate())) {
    		Year currentYear = Year.now();
            LocalDate tempStartDate = currentYear.atMonth(1).atDay(1);
            LocalDate tempEndDate = LocalDate.now();
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    		String tempStartDateString = formatter.format(tempStartDate);
    		String tempEndDateString = formatter.format(tempEndDate);
    		param.setSearchStartDate(tempStartDateString);
    		param.setSearchEndDate(tempEndDateString);
    	}

        List<PointCheck> pointCheckList = pointCheckService.getPointCheckList(param);
        for(PointCheck pointCheck : pointCheckList) {
        	String message	= pointCheck.getMatchMessage();
        	pointCheck.setMatchYn(message);
        }
        log.debug("**** pointCheckList.size() **** :", pointCheckList.size());
        model.addAttribute("pointCheckParam", param);
        model.addAttribute("pointCheckList", pointCheckList);
        model.addAttribute("pagination", param.getPagination());
		model.addAttribute("totalCount", param.getPagination().getTotalItems());

        return "view:/point-check/list";
    }

    @PostMapping(value="list")
    public String searchPointCheckList(@ModelAttribute PointCheckParam param, Model model) {
    	return pointCheckList(param, model);
    }
}
