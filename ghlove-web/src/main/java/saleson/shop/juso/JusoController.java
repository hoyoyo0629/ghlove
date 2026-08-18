package saleson.shop.juso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/opmanager")
@RequestProperty(title = "도로명주소", layout = "base")
@RequiredArgsConstructor
public class JusoController {
	private static final Logger log = LoggerFactory.getLogger(JusoController.class);
	
	/**
	 * 주소 팝업
	 * @return
	 */
	@GetMapping("/juso-popup")
	public String jusoList() {
		return ViewUtils.getView("/juso/juso-popup");
	}
}
