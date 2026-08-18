package saleson;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import lombok.extern.slf4j.Slf4j;
import saleson.batch.scheduler.SchedulerService;
import saleson.batch.scheduler.SchedulerServiceImpl;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages={"com.onlinepowers", "saleson"})
@ImportResource({"classpath*:spring/context-*.xml"})
@Configuration
public class SalesonBatchApplication {
	@Value("${saleson.root}")
	private String salesonRoot;
	
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SalesonBatchApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
	}

	@Bean
	public SchedulerFactoryBean scheduler() {
		return new SchedulerFactoryBean();
	}


	@Bean
	@ConditionalOnProperty(prefix = "saleson", name = "batch", havingValue = "true")
	public SchedulerService schedulerService() {
		return new SchedulerServiceImpl();
	}
	
    @PostConstruct
    public void PostConstruct() {
    	log.info("[kdj] SalesonBatchApplication start!! saleson.root=[{}], active=[{}]", salesonRoot, System.getProperty("spring.profiles.active"));
    }
}
