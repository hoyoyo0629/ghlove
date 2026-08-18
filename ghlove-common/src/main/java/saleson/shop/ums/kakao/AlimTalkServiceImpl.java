package saleson.shop.ums.kakao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import saleson.common.enumeration.kakao.alimtalk.TemplateButtonType;

import saleson.common.enumeration.kakao.alimtalk.TemplateEmphasizeType;
import saleson.common.enumeration.kakao.alimtalk.TemplateMessageType;
import saleson.common.exception.KakaoAlimTalkException;
import saleson.common.utils.CommonUtils;
import saleson.model.kakao.AlimTalkTemplate;
import saleson.model.kakao.AlimTalkTemplateButton;
import saleson.model.kakao.AlimTalkTemplateComment;

import java.net.URI;
import java.util.*;

@Service("alimTalkService")
public class AlimTalkServiceImpl extends EgovAbstractServiceImpl implements AlimTalkService {

    private static final Logger log = LoggerFactory.getLogger(AlimTalkServiceImpl.class);

    @Autowired
    RestTemplate trustSSLRestTemplate;

    @Autowired
    private AlimTalkTemplateRepository alimTalkTemplateRepository;

    @Autowired
    private AlimTalkTemplateButtonRepository alimTalkTemplateButtonRepository;

    @Autowired
    Environment environment;

    private HashMap<String, Object> getTemplateBody(ResponseEntity<String> entity) {

        if (entity != null) {

            String body = entity.getBody();

            if (!ObjectUtils.isEmpty(body)) {
                try {
                    return (HashMap<String, Object>) JsonViewUtils.jsonToObject(body, new TypeReference<HashMap<String, Object>>() {});
                } catch (RuntimeException e) {
                	log.error("========== getTemplateBody RuntimeException =============");
                }
            }
        }

        return null;
    }

    private Map<String, Object> getTemplateData(ResponseEntity<String> entity) {

        if (entity != null) {
            HashMap<String, Object> body = getTemplateBody(entity);

            if(body != null && body.get("data") != null) {
            	return (HashMap<String, Object>) body.get("data");
            }
        }

        return null;
    }

    private String getTemplateResult(ResponseEntity<String> entity) {

        if (entity != null) {

            if (HttpStatus.OK == entity.getStatusCode()) {

                HashMap<String, Object> body = getTemplateBody(entity);

                if (body != null) {
                    String successCode = "200";
                    String code = CommonUtils.dataNvl(body.get("code"));
                    String message = CommonUtils.dataNvl(body.get("message"));

                    if (!successCode.equals(code)) {
                        log.error(JsonViewUtils.objectToJson(entity));
                        return "(" + code + ") : " + message;
                    }

                    return "OK";
                }
            } else {
                log.error(JsonViewUtils.objectToJson(entity));
            }
        }

        return "";
    }

    @Override
    public AlimTalkTemplate getTemplate(String applyCode) throws RuntimeException {

        try {
            if (ObjectUtils.isEmpty(applyCode)) {
                return new AlimTalkTemplate();
            }

            String path = "/api/alimtalk/v3/" + getMplaceid() + "/profile/" + getSenderkey() + "/templt/" + applyCode;

            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(getHost())
                    .port(getHostPort())
                    .path(path)
                    .build()
                    .expand(getMplaceid(), getSenderkey())
                    .encode()
                    .toUri();

            RequestEntity requestEntity = getDefaultRequestEntity(HttpMethod.GET, uri);
            ResponseEntity<String> entity = trustSSLRestTemplate.exchange(requestEntity, String.class);

            String result = this.getTemplateResult(entity);
            if (!"OK".equals(result)) {
                throw new KakaoAlimTalkException("알림톡 API 조회 에러" + result);
            }

            Map<String, Object> data = getTemplateData(entity);
            if (data != null) {
                return convertAlimTalkTemplate(data);
            }

            return new AlimTalkTemplate();

        } catch (RuntimeException e) {
//            String message = e.getMessage();
//            if (ObjectUtils.isEmpty(message)) {
//                message = "알림톡 정보 조회시 문제가 발생했습니다.";
//            }
//            throw new KakaoAlimTalkException(message, e);
        	log.error("========= getTemplate RuntimeException ===========");

        	String message = "알림톡 정보 조회시 문제가 발생했습니다.";
        	throw new KakaoAlimTalkException(message);
        }

    }

    @Override
    public List<AlimTalkTemplate> getTemplateList() throws RuntimeException {
        try {

            List<AlimTalkTemplate> templates = new ArrayList<>();

            String path = "/api/alimtalk/v3/" + getMplaceid() + "/profile/" + getSenderkey() + "/templt";

            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(getHost())
                    .port(getHostPort())
                    .path(path)
                    .build()
                    .expand(getMplaceid(), getSenderkey())
                    .encode()
                    .toUri();

            RequestEntity requestEntity = getDefaultRequestEntity(HttpMethod.GET, uri);
            ResponseEntity<String> entity = trustSSLRestTemplate.exchange(requestEntity, String.class);

            String result = this.getTemplateResult(entity);
            if (!"OK".equals(result)) {
                throw new KakaoAlimTalkException("알림톡 API 목록 조회 에러" + result);
            }

            Map<String, Object> data = getTemplateData(entity);

            if (data != null) {

                List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) data.get("list");

                if (list != null) {
                    list.stream().forEach(map -> {
                        try {
                            templates.add(convertAlimTalkTemplate(map));
                        } catch (RuntimeException ignore) {
                        	log.error("========= getTemplateList convertAlimTalkTemplate RuntimeException ===========");
                        }
                    });
                }

            }

            return templates;
        } catch (RuntimeException e) {
//            String message = e.getMessage();
//            if (ObjectUtils.isEmpty(message)) {
//                message = "알림톡 정보 조회시 문제가 발생 했습니다.";
//            }
//
//            throw new KakaoAlimTalkException(message, e);
        	log.error("========= getTemplateList RuntimeException ===========");
            String message = "알림톡 정보 조회시 문제가 발생 했습니다.";
            throw new KakaoAlimTalkException(message);
        }
    }

    private MultiValueMap<String, String> getTemplateMap(AlimTalkTemplate alimTalkTemplate) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("templtCode", alimTalkTemplate.getApplyCode());
        map.add("templtName", alimTalkTemplate.getTitle());
        map.add("templtContent", alimTalkTemplate.getContent());
        map.add("templtMessageType", alimTalkTemplate.getMessageType().getCode());
        map.add("templtEmphasizeType", alimTalkTemplate.getEmphasizeType().getCode());

        List<AlimTalkTemplateButton> buttons = alimTalkTemplate.getButtons();
        List<Map<String, String>> buttonMaps = new ArrayList<>();

        if (buttons != null && buttons.size() > 0) {

            for (int i = 0; i < buttons.size(); i++) {
                AlimTalkTemplateButton button = buttons.get(i);
                Map<String,String> buttonMap = new HashMap<>();
            	buttonMap.put("linkType", org.apache.commons.lang.StringUtils.defaultIfEmpty(button.getType(), ""));
            	buttonMap.put("name", org.apache.commons.lang.StringUtils.defaultIfEmpty(button.getName(), ""));

            	this.changeButtonLinkByType(button);

            	if(button != null) {
            		buttonMap.put("linkM", org.apache.commons.lang.StringUtils.defaultIfEmpty(button.getLinkMobile(), ""));
            		buttonMap.put("linkP", org.apache.commons.lang.StringUtils.defaultIfEmpty(button.getLinkPc(), ""));
            		buttonMap.put("linkI", org.apache.commons.lang.StringUtils.defaultIfEmpty(button.getSchemeIos(), ""));
            		buttonMap.put("linkA", org.apache.commons.lang.StringUtils.defaultIfEmpty(button.getSchemeAndroid(), ""));
            	}

                buttonMaps.add(buttonMap);
            }
        }

        map.add("buttons", JsonViewUtils.objectToJson(buttonMaps));

        return map;
    }

    @Override
    public void registerTemplate(AlimTalkTemplate alimTalkTemplate) throws RuntimeException {
    	try {
            if (alimTalkTemplate == null) {
            	log.error("========= registerTemplate RuntimeException :: 알림톡 정보가 없습니다. =========== ");
                throw new KakaoAlimTalkException("알림톡 정보가 없습니다.");
            }

            String path = "/api/alimtalk/v2/" + getMplaceid() + "/profile/" + getSenderkey() + "/templt";

            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(getHost())
                    .port(getHostPort())
                    .path(path)
                    .build()
                    .expand(getMplaceid(), getSenderkey())
                    .encode()
                    .toUri();

            RequestEntity requestEntity = getDefaultRequestEntity(HttpMethod.POST, uri, getTemplateMap(alimTalkTemplate));
            ResponseEntity<String> entity = trustSSLRestTemplate.exchange(requestEntity, String.class);

            String result = this.getTemplateResult(entity);
            if (!"OK".equals(result)) {
            	log.error("========= registerTemplate RuntimeException :: 알림톡 API 등록 에러 :: " + result + "=========== ");
                throw new KakaoAlimTalkException("알림톡 API 등록 에러" + result);
            }

        } catch (RuntimeException e) {
//            String message = e.getMessage();
//            if (ObjectUtils.isEmpty(message)) {
//                message = "알림톡 정보 등록중 문제가 발생했습니다.";
//            }
//
//            throw new KakaoAlimTalkException(message, e);
        	log.error("========= registerTemplate RuntimeException ===========");
            String message = "알림톡 정보 등록중 문제가 발생했습니다.";
            throw new KakaoAlimTalkException(message);
        }

    }

    @Override
    public void editTemplate(AlimTalkTemplate alimTalkTemplate) throws RuntimeException {

        try {
            if (alimTalkTemplate == null) {
            	log.error("========= editTemplate RuntimeException :: 알림톡 정보가 없습니다. =========== ");
                throw new KakaoAlimTalkException("알림톡 정보가 없습니다.");
            }

            String path = "/api/alimtalk/v2/" + getMplaceid() + "/profile/" + getSenderkey() + "/templt/" + alimTalkTemplate.getApplyCode();

            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(getHost())
                    .port(getHostPort())
                    .path(path)
                    .build()
                    .expand(getMplaceid(), getSenderkey())
                    .encode()
                    .toUri();

            RequestEntity requestEntity = getDefaultRequestEntity(HttpMethod.PUT, uri, getTemplateMap(alimTalkTemplate));
            ResponseEntity<String> entity = trustSSLRestTemplate.exchange(requestEntity, String.class);

            String result = this.getTemplateResult(entity);
            if (!"OK".equals(result)) {
            	log.error("========= editTemplate RuntimeException :: 알림톡 API 수정 에러 :: " + result + "=========== ");
                throw new KakaoAlimTalkException("알림톡 API 수정 에러" + result);
            }

        } catch (RuntimeException e) {
//            String message = e.getMessage();
//            if (ObjectUtils.isEmpty(message)) {
//                message = "알림톡 정보 수정중 문제가 발생했습니다.";
//            }
//
//            throw new KakaoAlimTalkException(message, e);
        	log.error("========= editTemplate RuntimeException ===========");
            String message = "알림톡 정보 수정중 문제가 발생했습니다.";
            throw new KakaoAlimTalkException(message);
        }

    }

    @Override
    public void deleteTemplate(String applyCode) throws RuntimeException {
        try {
            if (ObjectUtils.isEmpty(applyCode)) {
            	log.error("========= deleteTemplate RuntimeException :: 승인 코드 정보가 없습니다. =========== ");
                throw new KakaoAlimTalkException("승인 코드 정보가 없습니다.");
            }
            // 템플릿상태가 대기(R)이고 템플릿 검수상태가 등록(REG) 또는 반려(REJ)인 경우에만 삭제 가능
            String path = "/api/alimtalk/v2/" + getMplaceid() + "/profile/" + getSenderkey() + "/templt/" + applyCode;

            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(getHost())
                    .port(getHostPort())
                    .path(path)
                    .build()
                    .expand(getMplaceid(), getSenderkey())
                    .encode()
                    .toUri();

            RequestEntity requestEntity = getDefaultRequestEntity(HttpMethod.DELETE, uri);
            ResponseEntity<String> entity = trustSSLRestTemplate.exchange(requestEntity, String.class);

            String result = this.getTemplateResult(entity);
            if (!"OK".equals(result)) {
            	log.error("========= deleteTemplate RuntimeException :: 알림톡 API 삭제 에러 :: " + result + "=========== ");
                throw new KakaoAlimTalkException("알림톡 API 삭제 에러" + result);
            }

        } catch (RuntimeException e) {
//            String message = e.getMessage();
//            if (ObjectUtils.isEmpty(message)) {
//                message = "알림톡 정보 삭제중 문제가 발생했습니다.";
//            }
//
//            throw new KakaoAlimTalkException(message, e);

        	log.error("========= deleteTemplate RuntimeException ===========");
            String message = "알림톡 정보 삭제중 문제가 발생했습니다.";
            throw new KakaoAlimTalkException(message);
        }
    }

    @Override
    public void verifyTemplate(String applyCode) throws RuntimeException {

        try {
            if (ObjectUtils.isEmpty(applyCode)) {
            	log.error("========= verifyTemplate RuntimeException :: 승인 코드 정보가 없습니다. =========== ");
                throw new KakaoAlimTalkException("승인 코드 정보가 없습니다.");
            }

            String path = "/api/alimtalk/v2/" + getMplaceid() + "/profile/" + getSenderkey() + "/templt/" + applyCode + "/req-inspect";
            // 등록된 템플릿을 검수 요청 합니다. 템플릿상태가 대기(R)이고 템플릿 검수상태가 등록(REG)인 경우에만 요청 가능합니다.

            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(getHost())
                    .port(getHostPort())
                    .path(path)
                    .build()
                    .expand(getMplaceid(), getSenderkey())
                    .encode()
                    .toUri();

            RequestEntity requestEntity = getDefaultRequestEntity(HttpMethod.POST, uri);
            ResponseEntity<String> entity = trustSSLRestTemplate.exchange(requestEntity, String.class);

            String result = this.getTemplateResult(entity);
            if (!"OK".equals(result)) {
            	log.error("========= verifyTemplate RuntimeException :: 알림톡 API 검수 요청 에러 :: " + result + "=========== ");
                throw new KakaoAlimTalkException("알림톡 API 검수 요청 에러" + result);
            }

        } catch (RuntimeException e) {
//            String message = e.getMessage();
//            if (ObjectUtils.isEmpty(message)) {
//                message = "알림톡 검수 요청중 문제가 발생했습니다.";
//            }
//
//            throw new KakaoAlimTalkException(message, e);

        	log.error("========= verifyTemplate RuntimeException ===========");
            String message = "알림톡 검수 요청중 문제가 발생했습니다.";
            throw new KakaoAlimTalkException(message);
        }
    }

    @Override
    public void changeButtonLinkByType(AlimTalkTemplateButton button) {
        if (button != null) {

            TemplateButtonType buttonType = null;

            for (TemplateButtonType type : TemplateButtonType.values()) {
                if (type.getCode().equals(button.getType())) {
                    buttonType = type;
                    break;
                }
            }

            String linkPc = "";
            String linkMobile = "";
            String schemeIos = "";
            String schemeAndroid = "";

            if (TemplateButtonType.AL == buttonType) {

                schemeIos = button.getLink1();
                schemeAndroid = button.getLink2();

            } else if (TemplateButtonType.WL == buttonType) {

                linkMobile = button.getLink1();
                linkPc = button.getLink2();

            }

            button.setLinkPc(linkPc);
            button.setLinkMobile(linkMobile);
            button.setSchemeIos(schemeIos);
            button.setSchemeAndroid(schemeAndroid);
        }
    }

    private RequestEntity getDefaultRequestEntity(HttpMethod method, URI uri) {

        HttpHeaders headers = new HttpHeaders();
        RequestEntity requestEntity = new RequestEntity(headers, method, uri);

        return requestEntity;
    }

    private RequestEntity getDefaultRequestEntity(HttpMethod method, URI uri, MultiValueMap<String, String> map) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RequestEntity requestEntity = new RequestEntity(map, headers, method, uri);

        return requestEntity;
    }

    private String getHost() {
        return environment.getProperty("ums.kakao.alimtalk.host");
    }

    private int getHostPort() {
        return StringUtils.string2integer(environment.getProperty("ums.kakao.alimtalk.host.port"), 0);
    }

    private String getMplaceid() {
        return environment.getProperty("ums.kakao.alimtalk.mplaceid");
    }

    private String getSenderkey() {
        return environment.getProperty("ums.kakao.alimtalk.senderkey");
    }

    private AlimTalkTemplate convertAlimTalkTemplate(Map<String, Object> map) throws RuntimeException {
        try {

            if (map == null) return null;

            AlimTalkTemplate template = new AlimTalkTemplate();
            List<AlimTalkTemplateButton> buttons = new ArrayList<>();
            List<AlimTalkTemplateComment> comments = new ArrayList<>();

            String code = CommonUtils.dataNvl(map.get("templtCode"));
            String title = CommonUtils.dataNvl(map.get("templtName"));
            String content = CommonUtils.dataNvl(map.get("templtContent"));
            String inspStatus = CommonUtils.dataNvl(map.get("inspStatus"));
            String emphasizeType = CommonUtils.dataNvl(map.get("templtEmphasizeType"));
            String messageType = CommonUtils.dataNvl(map.get("templtMessageType"));
            String status = CommonUtils.dataNvl(map.get("status"));
            String strComments = CommonUtils.dataNvl(map.get("comments"));
            String strButtons = CommonUtils.dataNvl(map.get("buttons"));

            List<HashMap<String, Object>> objComments = null;
            List<HashMap<String, Object>> objButtons = null;

            if (!ObjectUtils.isEmpty(strComments)) {
                objComments = (List<HashMap<String, Object>>) map.get("comments");
            }

            if (!ObjectUtils.isEmpty(strButtons)) {
                objButtons = (List<HashMap<String, Object>>) map.get("buttons");
            }

            if (objComments != null && objComments.size() > 0) {
                objComments.forEach(o -> {
                    AlimTalkTemplateComment comment = new AlimTalkTemplateComment();

                    // 카카오톡 문의하기 > 댓글의 \\r \제거
                    String commentContent = CommonUtils.dataNvl(o.get("commentContent"))
                            .replaceAll("\\\\r", "").replaceAll("\\\\", "");

                    comment.setId(CommonUtils.intNvl(o.get("id")));
                    comment.setContent(commentContent);
                    comment.setUserName(CommonUtils.dataNvl(o.get("name")));
                    comment.setCreatedAt(CommonUtils.dataNvl(o.get("cdate")));
                    comment.setStatus(CommonUtils.dataNvl(o.get("status")));

                    comments.add(comment);
                });
            }

            if (objButtons != null && objButtons.size() > 0) {

                objButtons.forEach(o -> {
                    AlimTalkTemplateButton button = new AlimTalkTemplateButton();
                    button.setOrdering(CommonUtils.intNvl(o.get("ordering")));
                    button.setName(CommonUtils.dataNvl(o.get("name")));
                    button.setType(CommonUtils.dataNvl(o.get("linkType")));
                    button.setTypeName(CommonUtils.dataNvl(o.get("linkTypeName")));
                    button.setLinkMobile(CommonUtils.dataNvl(o.get("linkM")));
                    button.setLinkPc(CommonUtils.dataNvl(o.get("linkP")));
                    button.setSchemeIos(CommonUtils.dataNvl(o.get("linkI")));
                    button.setSchemeAndroid(CommonUtils.dataNvl(o.get("linkA")));

                    if (TemplateButtonType.AL.getCode().equals(button.getType())) {
                        button.setLink1(button.getSchemeIos());
                        button.setLink2(button.getSchemeAndroid());
                    } else if (TemplateButtonType.WL.getCode().equals(button.getType())) {
                        button.setLink1(button.getLinkMobile());
                        button.setLink2(button.getLinkPc());
                    }

                    buttons.add(button);
                });

            }

            template.setApplyCode(code);
            template.setTitle(title);
            template.setContent(content);

            template.setInspStatus(inspStatus);
            template.setStatus(status);

            TemplateMessageType messageType2 = TemplateMessageType.BA;          // templtMessageType 기본값세팅
            TemplateEmphasizeType emphasizeType2 = TemplateEmphasizeType.NONE;  // templtEmphasizeType 기본값세팅
            /*if (!ObjectUtils.isEmpty(messageType)) {
                messageType2 = TemplateMessageType.valueOf(messageType);
            }
            if (!ObjectUtils.isEmpty(emphasizeType)) {
                emphasizeType2 = TemplateEmphasizeType.valueOf(emphasizeType);
            }*/

            template.setMessageType(messageType2);
            template.setEmphasizeType(emphasizeType2);

            template.setComments(comments);
            template.setButtons(buttons);

            return template;
        } catch (RuntimeException e) {
//            log.error("Convert AlimTalkTemplate Error {} : ", e.getMessage(), e);
//            throw new KakaoAlimTalkException("Convert AlimTalkTemplate Error", e);
            log.error("Convert AlimTalkTemplate Error {} : ", "========== convertAlimTalkTemplate RuntimeException ==========");
            throw new KakaoAlimTalkException("Convert AlimTalkTemplate Error");
        }
    }

    @Override
    public void setAlimTalkTemplate(AlimTalkTemplate orgTemplate, AlimTalkTemplate template) {

        template.setId(orgTemplate.getId());

        template.setCreated(orgTemplate.getCreated());
        template.setCreatedBy(orgTemplate.getCreatedBy());

        List<AlimTalkTemplateButton> buttons = template.getButtons();
        List<AlimTalkTemplateButton> orgButtons = orgTemplate.getButtons();

        HashSet<Long> updateIdSet = new HashSet<>();
        HashSet<Long> removeIdSet = new HashSet<>();

        if (orgButtons != null && orgButtons.size() > 0) {
            orgButtons.forEach(orgButton -> {
                removeIdSet.add(orgButton.getId());
            });
        }

        if (buttons != null && buttons.size() > 0) {

            int maxButtonLength = 5;

            if (buttons.size() > maxButtonLength) {
                throw new KakaoAlimTalkException("알림톡 버튼은 최대 " + maxButtonLength + "개 까지 입니다.");
            }

            buttons.forEach(button -> {

                if (orgButtons != null && orgButtons.size() > 0) {
                    orgButtons.forEach(orgButton -> {

                        if (orgButton.getId().equals(button.getId())) {

                            button.setCreated(orgButton.getCreated());
                            button.setCreatedBy(orgButton.getCreatedBy());

                            button.setId(orgButton.getId());

                            updateIdSet.add(button.getId());
                        }
                    });
                }

                this.changeButtonLinkByType(button);
            });

            template.setButtons(buttons);
        }

        // 신규 & 업데이트가 진행이 안된 버튼 삭제
        removeIdSet.removeAll(updateIdSet);

        if (removeIdSet.size() > 0) {
            removeIdSet.forEach(id -> {
                alimTalkTemplateButtonRepository.deleteById(id);
            });
        }
    }

    @Override
    public List<Map<String, String>> getAlimTalkButtonsMaps(List<AlimTalkTemplateButton> buttons) {
        List<Map<String, String>> buttonMaps = new ArrayList<>();

        if (buttons != null && buttons.size() > 0) {

            for (int i = 0; i < buttons.size(); i++) {
                AlimTalkTemplateButton button = buttons.get(i);
                Map<String, String> buttonMap = new HashMap<>();

                buttonMap.put("id", Long.toString(button.getId()));
                buttonMap.put("type", button.getType());
                buttonMap.put("name", button.getName());
                buttonMap.put("typeName", button.getTypeName());
                buttonMap.put("linkPc", button.getLinkPc());
                buttonMap.put("linkMobile", button.getLinkMobile());
                buttonMap.put("schemeIos", button.getSchemeIos());
                buttonMap.put("schemeAndroid", button.getSchemeAndroid());
                buttonMap.put("ordering", Integer.toString(button.getOrdering()));

                buttonMaps.add(buttonMap);
            }
        }

        return buttonMaps;
    }
}

