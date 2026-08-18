package saleson.common.configuration;

import com.popbill.api.cashbill.CashbillServiceImp;
import com.popbill.api.taxinvoice.TaxinvoiceServiceImp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PopbillConfig {

	@Value("${popbill.link.id}")
	private String linkId;

	@Value("${popbill.secret.key}")
	private String secretKey;

	@Value("${popbill.is.test}")
	private boolean test;


	@Bean
	public TaxinvoiceServiceImp taxinvoiceService() {
		TaxinvoiceServiceImp taxinvoiceService = new TaxinvoiceServiceImp();
		taxinvoiceService.setLinkID(linkId);
		taxinvoiceService.setSecretKey(secretKey);
		taxinvoiceService.setTest(test);

		return taxinvoiceService;
	}

	@Bean
	public CashbillServiceImp cashbillService() {
		CashbillServiceImp cashbillService = new CashbillServiceImp();
		cashbillService.setLinkID(linkId);
		cashbillService.setSecretKey(secretKey);
		cashbillService.setTest(test);

		return cashbillService;
	}
}
