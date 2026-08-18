package saleson.shop.magicline.controller;

import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

//import saleson.shop.isms.IsmsService;
@Slf4j
@Controller
@RequestMapping("")
@RequestProperty(template="seller", layout="default")
public class MagicLineSellerController extends MagicLineController {

	@Override
	@PostMapping("/seller/magicline/signedFormRGhlove")
	public String signedFormRGhlove(Model model) {
		super.signedFormRGhlove(model);
		return ViewUtils.getView("/magicline/signedFormRGhlove");
	}

}
