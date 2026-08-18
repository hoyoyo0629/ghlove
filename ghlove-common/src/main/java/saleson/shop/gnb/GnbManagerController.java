package saleson.shop.gnb;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.util.FlashMapUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import saleson.common.utils.CommonUtils;
import saleson.model.Gnb;
import saleson.shop.gnb.support.GnbCriteria;

@Slf4j
@Controller
@RequestMapping("/opmanager/gnb")
@RequestProperty(title="GNB 관리", layout="default", template="opmanager")
@RequiredArgsConstructor
public class GnbManagerController {

    private final GnbService gnbService;
    
    @GetMapping(value = "/list")
    public String list(Model model, GnbCriteria gnbCriteria,
                       @PageableDefault(sort="ordering", direction= Sort.Direction.ASC) Pageable pageable) {

        Page<Gnb> pageContent = gnbService.getGnbList(gnbCriteria.getPredicate(), pageable);

        gnbCriteria.setSize(pageContent.getPageable().getPageSize());

        model.addAttribute("pageContent", pageContent);
        model.addAttribute("criteria", gnbCriteria);
        model.addAttribute("currentPage", pageContent.getPageable().getPageNumber()+1);
        
        return "view";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {

        model.addAttribute("gnb", new Gnb());
        model.addAttribute("mode", "create");

        return "view";
    }

    @PostMapping(value = "/create")
    public String createAction(Gnb gnb) {

        try {

            gnbService.insertGnb(gnb);
            CommonUtils.setMessage("등록되었습니다.");
        } catch (RuntimeException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: createAction RuntimeException ============");
            CommonUtils.setMessage("GNB 등록에 실패 했습니다.");
        }
        
        return "redirect:/opmanager/gnb/list";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(Model model, @PathVariable("id") long id) {

        Gnb gnb = gnbService.getGnbById(id);
         
        if (gnb == null) {
            CommonUtils.setMessage("GNB 정보가 없습니다.");
            return "redirect:/opmanager/gnb/list";
        }
        
        model.addAttribute("gnb", gnb);
        model.addAttribute("mode", "edit");

        return "view";
    }

    @PostMapping(value = "/edit/{id}")
    public String editAction(Gnb gnb, @PathVariable("id") long id) {

        try {

            gnbService.updateGnb(id, gnb);
            CommonUtils.setMessage("수정되었습니다.");

        } catch (RuntimeException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: editAction RuntimeException ============");
            CommonUtils.setMessage("GNB 수정에 실패 했습니다.");
        }

        return "redirect:/opmanager/gnb/list";
    }

    @PostMapping("list/change-ordering")
    public JsonView changeOrdering(RequestContext requestContext, ListParam listParam,
                                   @RequestParam(name="startOrdering", defaultValue = "0") String startOrdering) {

        if (!requestContext.isAjaxRequest()) {
            throw new NotAjaxRequestException();
        }

        try {
            gnbService.updateGnbOrdering(listParam, Integer.parseInt(startOrdering));
        } catch (RuntimeException e) {
//            log.error("change-ordering error",e);
            log.error("ERROR: {}", getClass().getName() + " :: changeOrdering RuntimeException ============");
            return JsonViewUtils.failure("순서 저장에 실패 했습니다.");
        }

        return JsonViewUtils.success();
    }

    @PostMapping("delete")
    public String delete(ListParam listParam) {

        try {
            gnbService.deleteGnb(listParam);
            return "redirect:/opmanager/gnb/list";

        } catch (RuntimeException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: delete RuntimeException ============");
            FlashMapUtils.setMessage("GNB 삭제에 실패 했습니다.");
            return "redirect:/opmanager/gnb/list";
        }
    }
}
