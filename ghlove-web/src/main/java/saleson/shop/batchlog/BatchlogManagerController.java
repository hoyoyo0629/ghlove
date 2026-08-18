package saleson.shop.batchlog;

import java.time.LocalDate;
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

import saleson.batch.BatchLogService;
import saleson.batch.domain.BatchLog;
import saleson.batch.support.BatchLogParam;

@Controller
@RequestMapping("/opmanager/batch-log")
@RequestProperty(title="batchLog", layout="default", template="opmanager")
public class BatchlogManagerController {

	private static final Logger log = LoggerFactory.getLogger(BatchlogManagerController.class);
	
	@Autowired
    BatchLogService batchLogService;

    @Autowired
    SequenceService sequenceService;
	
    /**	배치 작업 조회
     * @param param
     * @param model
     * @return
     */
    @GetMapping(value="list")
    public String batchLogList(@ModelAttribute BatchLogParam param, Model model) {
    	
    	if(!StringUtils.hasLength(param.getSearchStartDate()) && !StringUtils.hasLength(param.getSearchEndDate())) {
    		LocalDate tempDate = LocalDate.now();
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    		String tempDateString = formatter.format(tempDate);
    		param.setSearchStartDate(tempDateString);
    		param.setSearchEndDate(tempDateString);
    	}
    	
        List<BatchLog> batchLogList = batchLogService.getBatchLogList(param);

        log.debug("**** batchLogList.size() **** :", batchLogList.size());

        // 사용하는 곳은 어디인가?  skc
        //HttpSession session = RequestContextUtils.getSession();
        //session.setAttribute("reCustomerParam", param);

        model.addAttribute("batchLogParam", param);
        model.addAttribute("batchLogList", batchLogList);
        model.addAttribute("pagination", param.getPagination());
		model.addAttribute("totalCount", param.getPagination().getTotalItems());

        return "view:/batch-log/list";
    }
    
    /**	배치 작업 조회
     * @param param
     * @param model
     * @return
     */
    @PostMapping(value="list")
    public String searchBatchLogList(@ModelAttribute BatchLogParam param, Model model) {
    	
    	if(!StringUtils.hasLength(param.getSearchStartDate()) && !StringUtils.hasLength(param.getSearchEndDate())) {
    		LocalDate tempDate = LocalDate.now();
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    		String tempDateString = formatter.format(tempDate);
    		param.setSearchStartDate(tempDateString);
    		param.setSearchEndDate(tempDateString);
    	}
    	
        List<BatchLog> batchLogList = batchLogService.getBatchLogList(param);

        log.debug("**** batchLogList.size() **** :", batchLogList.size());

        // 사용하는 곳은 어디인가?  skc
        //HttpSession session = RequestContextUtils.getSession();
        //session.setAttribute("reCustomerParam", param);

        model.addAttribute("batchLogParam", param);
        model.addAttribute("batchLogList", batchLogList);
        model.addAttribute("pagination", param.getPagination());
		model.addAttribute("totalCount", param.getPagination().getTotalItems());

        return "view:/batch-log/list";
    }
}
