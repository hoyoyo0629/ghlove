package saleson.common.utils;

import nl.captcha.text.producer.TextProducer;

public class CaptchaTextProducer implements TextProducer {
	
	private String answer;
	
	public CaptchaTextProducer (String txt) {
		this.answer = txt;
	}
	
	@Override
	public String getText() {
		return this.answer;
	}

}
