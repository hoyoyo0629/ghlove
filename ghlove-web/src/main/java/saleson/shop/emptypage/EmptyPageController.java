package saleson.shop.emptypage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.opmanager.menu.domain.OpmanagerMenu;

import saleson.model.EmptyPageOpmanagerMenu;

@Controller
@RequestMapping("/opmanager/page")
@RequestProperty(layout="default", template="opmanager")
public class EmptyPageController {

	private static final Logger log = LoggerFactory.getLogger(EmptyPageController.class);
	
	/**
	 * 
	 * @param model
	 * @param displayItemParam
	 * @param code
	 * @return
	 */
	@GetMapping("")
	public String form(Model model, HttpServletRequest request, RequestContext requestContext) {
		HttpSession session = request.getSession();
		EmptyPageOpmanagerMenu emptyPageOpmanagerMenu = (EmptyPageOpmanagerMenu) session.getAttribute("emptyPageOpmanagerMenu");
		
		session.removeAttribute("emptyPageOpmanagerMenu");
		
		OpmanagerMenu opmanagerMenu = new OpmanagerMenu();
		opmanagerMenu.setFirstMenuId(emptyPageOpmanagerMenu.getFirstMenuId());
		opmanagerMenu.setFirstMenuList(emptyPageOpmanagerMenu.getFirstMenu());
		opmanagerMenu.setSecondAndThirdMenuList(emptyPageOpmanagerMenu.getSecondMenu());
		opmanagerMenu.setMenuCode(emptyPageOpmanagerMenu.getMenuCode());
		requestContext.setOpmanagerMenu(opmanagerMenu);
		
		
		return ViewUtils.getView("/emptypage/form");
	}

}
