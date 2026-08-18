package saleson.api.common.exception;

import lombok.Getter;
import saleson.api.common.enumerated.DonationError;

@Getter
public class DonationException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	private String code = null;
	private String message = "";

	public DonationException(DonationError donationError) {
		this.code = donationError.name();
		this.message  = donationError.getMessage();
	}

	public DonationException(DonationError donationError, Throwable  cause) {
		this.code = donationError.name();
		this.message  = cause.getMessage();
	}

	public DonationException(Throwable cause) {
        this.code = DonationError.UNKNOWN_ERROR.name();
        this.message = cause.getMessage();
    }

	public DonationException(Exception exception) {
		if(exception.getClass() == DonationException.class) {
			DonationException donationException = (DonationException) exception;
			this.code = donationException.getCode();
			this.message = donationException.getMessage();
		} else {
			this.code = DonationError.UNKNOWN_ERROR.name();;
			this.message = exception.getMessage();
		}
	}

	public DonationException(DonationError donationError, String reponseMessage) {
		this.code = donationError.name();
		this.message  = reponseMessage;
	}

}
