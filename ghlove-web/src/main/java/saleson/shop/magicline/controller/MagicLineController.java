package saleson.shop.magicline.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import saleson.shop.access.support.AccessParam;

//import saleson.shop.isms.IsmsService;
@Slf4j
@Controller
@RequestMapping("/opmanager/magicline/")
@RequestProperty(layout="default")
public class MagicLineController {

	@Value("${magicline.prop-path}")
	String propPath;

	@PostMapping("signedFormRGhlove")
	public String signedFormRGhlove(Model model) {
		model.addAttribute("prop-path", propPath);
		Properties prop = new Properties();
		try {
			FileReader fr = new java.io.FileReader(propPath);
			prop.load(fr);
		} catch (FileNotFoundException e) {
			e.getStackTrace();
		} catch (IOException e) {
			e.getStackTrace();
		}
		return ViewUtils.view();
	}







	/**
	 * 접속 ip 리스트
	 * @return
	 */
	@GetMapping("index")
	@RequestProperty(title="index")
	public String index(AccessParam accessParam, Model model) {

//		accessParam.setDisplayFlag("Y");
//		int count = accessService.getAllowIpCount(accessParam);
//
//		Pagination pagination = Pagination.getInstance(count, 10);
//		accessParam.setPagination(pagination);
//
//		List<Access> list = accessService.getAllowIpList(accessParam);
//
//		model.addAttribute("list", list);
//		model.addAttribute("pagination", pagination);
//		model.addAttribute("count", count);

		return ViewUtils.view();
	}

	@GetMapping("signedForm")
	public String signedForm(Model model) {
		return ViewUtils.view();
	}

	@PostMapping("signedFormR")
	public String signedFormR(Model model) {
		model.addAttribute("prop-path", propPath);
		Properties prop = new Properties();
		try (FileReader fr = new java.io.FileReader(propPath);) {
			prop.load(fr);
			log.debug("[prop]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}", prop.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error("[prop] >>>>>>>{}", getClass().getName() + " :: signedFormR FileNotFoundException =========");
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error("[prop] >>>>>>>{}", getClass().getName() + " :: signedFormR IOException =========");
		}
		return ViewUtils.view();
	}

	@GetMapping("vidClientIDN")
	public String vidClientIDN(Model model) {
		return ViewUtils.view();
	}

	@PostMapping("vidClientIDNR")
	public String vidClientIDNR(Model model) {
		model.addAttribute("prop-path", propPath);
		Properties prop = new Properties();
		try (FileReader fr = new java.io.FileReader(propPath);) {
			prop.load(fr);
			log.debug("[prop]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}", prop.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error("[prop] >>>>>>>{}", getClass().getName() + " :: vidClientIDNR FileNotFoundException =========");
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error("[prop] >>>>>>>{}", getClass().getName() + " :: vidClientIDNR IOException =========");
		}
		return ViewUtils.view();
	}
}
