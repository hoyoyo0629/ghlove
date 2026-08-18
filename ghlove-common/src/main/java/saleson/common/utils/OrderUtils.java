package saleson.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.JsonViewUtils;

import org.springframework.stereotype.Component;
import saleson.shop.order.pg.domain.ReturnUrlParam;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Component
public class OrderUtils {

    public static String encodeUrlSafeBase64(ReturnUrlParam returnUrlParam){

        try {
            String json = JsonViewUtils.objectToJson(returnUrlParam);
            byte[] encode = Base64.getUrlEncoder().encode(json.getBytes("UTF-8"));

            return new String(encode, "UTF-8");
        } catch (OpRuntimeException | UnsupportedEncodingException e) {
            return "";
        }

    }

    public static ReturnUrlParam decodeUrlSafeBase64(String value){

        try {
            byte[] decode = Base64.getUrlDecoder().decode(value.getBytes("UTF-8"));
            String decodeString = new String(decode, "UTF-8");

            return (ReturnUrlParam)JsonViewUtils.jsonToObject(decodeString, new TypeReference<ReturnUrlParam>(){});
        } catch (OpRuntimeException | UnsupportedEncodingException e) {
            return null;
        }

    }
}
