package saleson.common.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Aspect
@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class TransactionConfig {
	private static final int TX_METHOD_TIMEOUT = -1;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public TransactionInterceptor txAdvice() {
		TransactionInterceptor txAdvice = new TransactionInterceptor();
		Properties txAttributes = new Properties();
		List<RollbackRuleAttribute> rollbackRules = new ArrayList<RollbackRuleAttribute>();
		rollbackRules.add(new RollbackRuleAttribute(Exception.class));

		// NOT_SUPPORTED
		DefaultTransactionAttribute notSupportedAttribute = new DefaultTransactionAttribute(
				TransactionDefinition.PROPAGATION_NOT_SUPPORTED);


		// READ_ONLY
		DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute(
				TransactionDefinition.PROPAGATION_REQUIRED);
		readOnlyAttribute.setReadOnly(true);
//		readOnlyAttribute.setTimeout(TX_METHOD_TIMEOUT);


		// REQUIRED
		RuleBasedTransactionAttribute requiredAttribute = new RuleBasedTransactionAttribute(
				TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		requiredAttribute.setTimeout(TX_METHOD_TIMEOUT);

		// REQUIRED_NEW
		RuleBasedTransactionAttribute requiredNewAttribute = new RuleBasedTransactionAttribute(
				TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		requiredNewAttribute.setTimeout(TX_METHOD_TIMEOUT);

		// REQUIRED_NEW__READ_COMMITTED)
		RuleBasedTransactionAttribute requiredNewReadCommittedAttribute = new RuleBasedTransactionAttribute(
				TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		requiredNewReadCommittedAttribute.setTimeout(TX_METHOD_TIMEOUT);
		requiredNewReadCommittedAttribute.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);


		String notSupportedDefinition = notSupportedAttribute.toString();
		String readOnlyDefinition = readOnlyAttribute.toString();
		String requiredDefinition = requiredAttribute.toString();
		String requiredNewAttributeDefinition = requiredNewAttribute.toString();
		String requiredNewReadCommittedAttributeDefinition = requiredNewReadCommittedAttribute.toString();

		// Transaction Attributes
		txAttributes.setProperty("*NoTx", notSupportedDefinition);
		txAttributes.setProperty("get*", notSupportedDefinition);
		txAttributes.setProperty("*NewTx", readOnlyDefinition);
		txAttributes.setProperty("getLong", requiredNewReadCommittedAttributeDefinition);
		txAttributes.setProperty("getId", requiredNewReadCommittedAttributeDefinition);
		txAttributes.setProperty("updateSequence", requiredNewReadCommittedAttributeDefinition);
		txAttributes.setProperty("*", requiredDefinition);

		txAdvice.setTransactionAttributes(txAttributes);
		txAdvice.setTransactionManager(transactionManager);
		return txAdvice;
	}

	@Bean
	public Advisor txAdviceAdvisor1() {

		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(* com.onlinepowers..*Impl.*(..))");

		return new DefaultPointcutAdvisor(pointcut, txAdvice());
	}

	@Bean
	public Advisor txAdviceAdvisor2() {

		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(* saleson..*Impl.*(..))");

		return new DefaultPointcutAdvisor(pointcut, txAdvice());
	}
}
