package saleson.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {

	@RequestMapping("/healthcheck")
	public String healthcheck() {
		return "Saleson API Server is alive!!";
	}
}
