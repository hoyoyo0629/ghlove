package saleson.common.notification.micesoft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.StringUtils;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import saleson.common.notification.micesoft.domain.api.Calculate;
import saleson.common.notification.micesoft.domain.api.ResponseImage;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service("miceApiService")
public class MiceApiService {

    private static final Logger log = LoggerFactory.getLogger(MiceApiService.class);

    @Autowired
    Environment environment;

    @Autowired
    RestTemplate customRestTemplate;

    public ResponseImage getMmsImageUrl(String encodingFile) {

        if (ObjectUtils.isEmpty(encodingFile)) {
            return null;
        }

        String path = environment.getProperty("ums.mms.image.upload.path");
        String host = environment.getProperty("ums.mms.image.upload.host");
        String port = environment.getProperty("ums.mms.image.upload.port");

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(port)
                .path(path)
                .build()
                .expand()
                .encode()
                .toUri();


        Map<String, Object> map = new LinkedHashMap<>();
        JSONArray files = new JSONArray();
        files.add(encodingFile);
        String jsonString = files.toString();

        map.put("CUST_ID", environment.getProperty("ums.client-id"));
        map.put("FILES", jsonString);

        RequestEntity requestEntity = getDefaultRequestEntity(HttpMethod.POST, uri, map);

        customRestTemplate.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("UTF-8")));

        ResponseEntity<String> entity = customRestTemplate.exchange(requestEntity, String.class);

        return new ResponseImage(getTemplateBody(entity));
    }

    private RequestEntity getDefaultRequestEntity(HttpMethod method, URI uri, Map<String, Object> map) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity requestEntity = new RequestEntity(map, headers, method, uri);

        return requestEntity;
    }

    private HashMap<String, Object> getTemplateBody(ResponseEntity<String> entity) {

        if (entity != null) {

            String body = entity.getBody();

            if (!ObjectUtils.isEmpty(body)) {
                try {
                    return (HashMap<String, Object>) JsonViewUtils.jsonToObject(body, new TypeReference<HashMap<String, Object>>() {});
                } catch (OpRuntimeException e) {
                	log.error(getClass().getName() + " getTemplateBody error", e);
                }
            }
        }

        return null;
    }

    // 정산내역
    public Calculate getCalculateList(String calDt) {

        String path = environment.getProperty("ums.calculate.path");
        String host = environment.getProperty("ums.calculate.host");
        String port = environment.getProperty("ums.calculate.port");

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(port)
                .path(path)
                .build()
                .encode()
                .toUri();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("CAL_DT", calDt);
        map.put("GRP_CD", environment.getProperty("ums.grp.cd"));    // 개발 01003
        map.put("CUST_ID", environment.getProperty("ums.client-id"));   // onlinepowers

        RequestEntity requestEntity = getDefaultRequestEntity(HttpMethod.POST, uri, map);

        customRestTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        ResponseEntity<String> entity = customRestTemplate.exchange(requestEntity, String.class);

        return new Calculate(getTemplateBody(entity));
    }

}
