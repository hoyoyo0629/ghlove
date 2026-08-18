package saleson.batch.manual;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import saleson.batch.job.JobService;

@RestController("ManualBatchController")
@RequestMapping("/batch")
public class ManualBatchController {

	@Autowired
	private JobService jobService;

	@GetMapping("/run/{jobName}")
	public String runJob(@PathVariable String jobName) {
		try {
			Method method = jobService.getClass().getMethod(jobName);
			method.invoke(jobService);
			return "Job triggered manually : " + jobName;
		}catch (Exception e) {
			return "Error triggering job : " + e.getMessage();
			// TODO: handle exception
		}
	}
}
