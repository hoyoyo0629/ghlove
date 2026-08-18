package saleson.common.web;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Criteria {
	private int size = 10;
	private int page = 1;
	private String where;
	private String query;
	private String sort;


	/**
	 * 검색을 했는가?
	 * @return
	 */
	public boolean hasQuery() {
		return where != null
				&& !where.isEmpty()
				&& query != null
				&& !where.isEmpty();
	}


	public void encrypt(DataEncryptor dataEncryptor) {
		dataEncryptor.encrypt(this);
	}

	public void decrypt(DataEncryptor dataEncryptor) {
		dataEncryptor.decrypt(this, false);
	}
}
