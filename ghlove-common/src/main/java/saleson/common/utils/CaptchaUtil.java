package saleson.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.captcha.Captcha;
import nl.captcha.audio.AudioCaptcha;
import nl.captcha.audio.producer.VoiceProducer;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.NumbersAnswerProducer;
import saleson.shop.auth.AuthServiceImpl;

public class CaptchaUtil {

	private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

	public Map<String, Object> captchaImg(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> result = new HashMap<String, Object>();

		NumbersAnswerProducer nsp = new NumbersAnswerProducer(6);
		String answer = nsp.getText();

		Captcha captcha = new Captcha.Builder(200, 60)
							         .addText(new CaptchaTextProducer(answer))
							         .addNoise().addNoise().addNoise()
							         .addBackground(new GradiatedBackgroundProducer())
							         .addBorder()
							         .build();



		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buffer[] = null;
		try  {
			ImageIO.write(captcha.getImage(), "jpeg", baos);

			buffer = baos.toByteArray();
			baos.close();

			result.put("answer", answer);
			result.put("buff", buffer);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			 log.error("■■■ERROR■■■ captchaImg Exception {}",e.getStackTrace()[0]);
			result = null;
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error("■■■ERROR■■■ captchaImg Exception {}",e1.getStackTrace()[0]);
			}
		} finally {
			return result;
		}



	}


	public void captchaAudio(HttpServletRequest request, HttpServletResponse response) {
		String answer = request.getParameter("answer");

		VoiceProducer prod = new SetKorVoiceProducer();

		AudioCaptcha ac = new AudioCaptcha.Builder()
										  .addAnswer(new CaptchaTextProducer(answer))
										  .addVoice(prod)
										  .addNoise()
										  .build();

		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Max-Age", 0);

		CaptchaServletUtil.writeAudio(response, ac.getChallenge());

	}

}
