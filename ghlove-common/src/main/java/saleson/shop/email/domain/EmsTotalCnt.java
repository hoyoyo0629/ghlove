package saleson.shop.email.domain;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmsTotalCnt {

	private long emailId;
	private int totCnt;
	private int succCnt;
	private int failCnt;

	@Override
	public boolean equals(Object obj) {
		long emailId = (long) obj;
		return this.emailId == emailId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(emailId);
	}
}
