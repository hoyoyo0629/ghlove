package saleson.common.configuration;


import com.onlinepowers.framework.log.service.LogAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogSvcAspect extends LogAspect {
	private final String type = "SVC";

	@Pointcut("execution(public * com.onlinepowers.web..*Impl.*(..))")
	private void pointcut1() {}

	@Pointcut("execution(public * com.onlinepowers.framework.web..*Impl.*(..))")
	private void pointcut2() {}

	@Pointcut("execution(public * saleson..*Impl.*(..))")
	private void pointcut3() {}

	//around가 적용될 포인트 컷을 명시
	@Around("pointcut1() || pointcut2() || pointcut3()")
	public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
		setPosition(type);
		return log(joinPoint);
	}
}
