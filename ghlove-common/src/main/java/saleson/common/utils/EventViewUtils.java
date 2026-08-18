package saleson.common.utils;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.eventcode.EventCodeCookieType;
import saleson.common.enumeration.eventcode.EventCodeLogType;
import saleson.common.enumeration.eventcode.EventCodeType;
import saleson.model.eventcode.EventCodeLog;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class EventViewUtils {

	private static final Logger log = LoggerFactory.getLogger(EventViewUtils.class);

    public static String getPrefixUrl(EventCodeType type) {

        if(type == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        if ("api".equals(SalesonProperty.getSalesonViewType())) {
            sb.append(SalesonProperty.getSalesonUrlBo());
        } else {
            sb.append(SalesonProperty.getSalesonUrlShoppingmall());
        }
        sb.append("/ev");

        switch (type) {
            case EP_ITEM: sb.append("/ep"); break;
            case FEATURED: sb.append("/e"); break;
            case SHARE: sb.append("/s"); break;
            case CAMPAIGN: sb.append("/c"); break;
            default:
        }

        return sb.toString();
    }

    public static String getUrl(EventCodeType type, String code) {

        // EP_상품은 URI 규격상 getEpItemUrl 사용
        if (ObjectUtils.isEmpty(code) || type == null || type == EventCodeType.EP_ITEM) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        sb.append(EventViewUtils.getPrefixUrl(type));
        sb.append("/"+code);

        if (type == EventCodeType.CAMPAIGN || type == EventCodeType.SHARE || type ==EventCodeType.NONE) {

            if (UserUtils.isUserLogin()) {
                sb.append("/"+UserUtils.getUserId());
            }
        }

        return sb.toString();
    }

    public static String getEpItemUrl(String channel, String code) {

        if (ObjectUtils.isEmpty(code)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        sb.append(EventViewUtils.getPrefixUrl(EventCodeType.EP_ITEM));
        sb.append("/"+channel);
        sb.append("/"+code);

        return sb.toString();

    }

    public static Map<String, String> getParamMap(String queryString) {

        Map<String, String>  map = new LinkedHashMap<>();

        if (!ObjectUtils.isEmpty(queryString)) {
            String[] params = StringUtils.delimitedListToStringArray(queryString, "&");

            if (params != null && params.length > 0) {
                Arrays.stream(params).forEach(p->{
                    try {

                        String[] array = StringUtils.delimitedListToStringArray(p,"=");

                        if (array != null && array.length == 2) {

                            String value = URLDecoder.decode(array[1],"UTF-8");
                            value = ShopUtils.unescapeHtml(value);

                            map.put(array[0], value);
                        }else if (array != null && array.length == 1) {
                            map.put(array[0],"");
                        }

                    } catch (OpRuntimeException | UnsupportedEncodingException ignore) {
                    	log.error("EventViewUtils getParamMap error", ignore);
                    }
                });
            }
        }

        return map;
    }

    public static String getUid() {
        return LocalDateUtils.localDateTimeToString(LocalDateTime.now(), Const.DATEHOUR_FORMAT)
                +"."+ UUID.randomUUID().toString().replace("-","").substring(0,10);
    }

    public static EventCodeLog getEventCodeLog(String url, EventCodeType codeType, EventCodeLogType logType) {
        String[] array = StringUtils.delimitedListToStringArray(url, "?");
        String queryString = "";

        if (array != null && array.length == 2) {
            queryString = array[1];
        }
        return EventViewUtils.getBaseEventCodeLog(codeType, logType, queryString);
    }

    public static EventCodeLog getEventCodeLog(HttpServletRequest request, EventCodeType codeType, EventCodeLogType logType) {
        String queryString = EventViewUtils.getQueryStringCookieValue(request);

        return EventViewUtils.getBaseEventCodeLog(codeType, logType, queryString);
    }

    public static EventCodeLog getBaseEventCodeLog(EventCodeType codeType, EventCodeLogType logType, String queryString) {
        EventCodeLog eventCodeLog;

        logType = logType != null ? logType : EventCodeLogType.NONE;
        codeType = codeType != null ? codeType : EventCodeType.NONE;


        if (!ObjectUtils.isEmpty(queryString)) {
            eventCodeLog = new EventCodeLog(codeType, logType, queryString);
        } else {
            eventCodeLog = new EventCodeLog(codeType, logType);
        }

        if (EventCodeType.EP_ITEM == codeType) {
            eventCodeLog.setChannel(eventCodeLog.getUtmSource());
        }

        return eventCodeLog;
    }

    public static void setUidCookie(HttpServletRequest request, HttpServletResponse response, String uid) {
        EventViewUtils.setCookie(request, response, EventCodeCookieType._EVENT_VIEW_UID, uid);
    }

    public static String getUidCookieValue(HttpServletRequest request) {
        return EventViewUtils.getCookieValue(request, EventCodeCookieType._EVENT_VIEW_UID);
    }

    public static void setQueryStringCookie(HttpServletRequest request, HttpServletResponse response, String uid) {
        EventViewUtils.setCookie(request, response, EventCodeCookieType._EVENT_VIEW_QUERY_STRING, uid);
    }

    public static String getQueryStringCookieValue(HttpServletRequest request) {
        return EventViewUtils.getCookieValue(request, EventCodeCookieType._EVENT_VIEW_QUERY_STRING);
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, EventCodeCookieType cookieType, String value) {

        try {
            value = CommonUtils.dataNvl(value);

            value = value
                    .replaceAll(" ", "_")
                    .replaceAll(",","_");

            if (response != null && cookieType != null) {
                //int age = 60 * 30; // 30분
                Cookie cookie = new Cookie(cookieType.getCode(), value);
                //cookie.setMaxAge(age);
                String r = request.getParameter("maxAge");
            	/* FIX */
            	String num = "0";

            	if(r != null && r.matches("[0-9]+"))
            	{
            		String sessionID = ShopUtils.removeNewlineCharaters(request.getParameter("sessionID"));
            		if(sessionID != null && sessionID.matches("[A-Z=0-9a-z]+"))
            		{
            			Cookie c = new Cookie("sessionID", sessionID);
            			c.setMaxAge(Integer.parseInt(num));
            		}
            	}
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        } catch (OpRuntimeException ignore) {
        	log.error("EventViewUtils getParamMap error", ignore);
        }

    }

    public static String getCookieValue(HttpServletRequest request, EventCodeCookieType cookieType) {
        String value = "";
        // 미사용 주석처리(시큐어코딩 지적)
//        if (request != null && cookieType != null) {
//            Cookie[] cookies = request.getCookies();
//
//            if (cookies != null && cookies.length > 0) {
//                for (Cookie c:  cookies) {
//                    if(cookieType.getCode().equals(c.getName())) {
//                        value = c.getValue();
//                        break;
//                    }
//                }
//            }
//        }
//
//        value = CommonUtils.dataNvl(value);

        return value;
    }
}
