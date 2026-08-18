package saleson.common.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class LogbackFilter extends Filter<ILoggingEvent> {
	@Override
	public FilterReply decide(ILoggingEvent event) {
		String message = event.getMessage().toUpperCase();
		if (containsWords(message, "OP_SESSION, OP_SESSION_ATTRIBUTE, PRIMARY_ID")) {
			return FilterReply.DENY;
		} else {
			return FilterReply.NEUTRAL;
		}
	}


	private boolean containsWords(String message, String words) {
		message = message.toUpperCase();
		words = words.toUpperCase();

		String[] wordsArray = StringUtils.delimitedListToStringArray(words, ",");
		for (String word : wordsArray) {
			if (message.contains(word.trim())) {
				return true;
			}
		}

		return false;
	}
}
