package saleson.batch.aop;

import com.onlinepowers.framework.context.ThreadContext;
import com.onlinepowers.framework.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import saleson.batch.BatchExecutionMapper;
import saleson.common.Const;
import saleson.common.scheduling.domain.BatchExecution;


/**
 * 2022.04.27 xml config -> java config (context-batch-log.xml)
 * 나중에 적용하자 .
 *
 * SKC
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BatchExecutionAspect {
	private final BatchExecutionMapper batchExecutionMapper;

	final String BATCH_EXECUTION = "BATCH_EXECUTION";


	@Pointcut("execution(public * saleson.batch.job.JobServiceImpl.*Batch(..))")
	private void pointcut() {}



	@Before("pointcut()")
	public void before(JoinPoint joinPoint) {
		String executionDate = DateUtils.getToday(Const.DATE_FORMAT);
		BatchExecution batchExecution = new BatchExecution(joinPoint.getSignature().toShortString(), executionDate);


		ThreadContext.put(BATCH_EXECUTION, batchExecution);
		
		batchExecutionMapper.mergeBatchExecution(batchExecution);
		
	}
	
	@AfterReturning("pointcut()")
	public void afterReturning() {
			BatchExecution batchExecution = (BatchExecution) ThreadContext.get(BATCH_EXECUTION);
			
			// 로그 기록
			batchExecution.setResult("1");
			batchExecution.setEndTime(DateUtils.getToday("HH:mm:ss"));
			//batchExecution.setMessage(getBatchTtitle() + " 배치 작업 성공");
			
			batchExecutionMapper.mergeBatchExecution(batchExecution);
	}


	@AfterThrowing(pointcut = "pointcut()", throwing = "cause")
	public void afterThrowing(Throwable cause) {
		
			BatchExecution batchExecution = (BatchExecution) ThreadContext.get(BATCH_EXECUTION);
			log.error("{} 배치 작업 중 오류 발생", batchExecution.getBatchType());
			
	
			batchExecution.setMessage(batchExecution.getBatchType() + " 배치 작업 중 오류 발생 - " + cause.getMessage());
			batchExecution.setResult("2");
			batchExecution.setEndTime(DateUtils.getToday("HH:mm:ss"));		// 오류 발생 시간
	
			batchExecutionMapper.mergeBatchExecution(batchExecution);
	}
	
}
