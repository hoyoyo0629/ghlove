package saleson.common.web;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.Getter;
import lombok.Setter;
import saleson.model.UmsSendLog;

import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

@Getter @Setter
public class Param {

	private static final Logger log = LoggerFactory.getLogger(Param.class);
	
	
	private int size = 10;
	private int page = 1;
	private String where;
	private String query;
	private String sort;

	public boolean hasQuery() {
		return StringUtils.hasText(getWhere())
				&& StringUtils.hasText(getQuery());
	}

	public String getSortColumn() {

		try {
			String[] array = sort.split(",");
			return array[0];
		} catch (PatternSyntaxException | NullPointerException e){
			log.error(getClass().getName() + " getSortColumn error", e);
		}

		return "";
	}

	public String getSortDirection() {
		try {
			String[] array = sort.split(",");
			return array[1];
		} catch (PatternSyntaxException | NullPointerException e){
			log.error(getClass().getName() + " getSortDirection error", e);
		}

		return "";
	}

	public void setSortByPageable(Pageable pageable) {
		if (pageable != null) {

			try{

				String sortString = pageable.getSort().toString();

				setSort(sortString.replace(": ",","));

			} catch (NullPointerException e) {
				log.error(getClass().getName() + " setSortByPageable error", e);
			}

		}
	}

	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor encryptor) {
		encryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}
}
