package saleson.api.eventview;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.common.enumeration.eventcode.EventCodeLogType;
import saleson.common.enumeration.eventcode.EventCodeType;
import saleson.common.utils.UserUtils;
import saleson.model.eventcode.EventCodeLog;
import saleson.shop.eventcode.EventCodeService;
import saleson.shop.eventcode.support.EventLogParam;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/event-log")
public class EventLogController {

    private static final Logger log = LoggerFactory.getLogger(EventLogController.class);

    @Autowired
    private EventCodeService eventCodeService;

    @PostMapping("/item")
    public ResponseEntity item(@RequestBody EventLogParam param) {

        try {

            EventCodeLog eventCodeLog = getBaseEventCodeLog(param, EventCodeType.NONE, EventCodeLogType.ITEM);
            eventCodeLog.setItemUserCode(param.getId());

            eventCodeService.insertLog(eventCodeLog);

        } catch (OpRuntimeException ignore) {
            //log.error("event item view log error [{}] - {}", param.getId(), ignore.getMessage(),ignore);
            log.error("event item view log error [{}] - {}", param.getId(), "ERROR-69: event item view log error", ignore);
        }

        return ApiResponseEntity.data().ok();
    }

    @PostMapping("/join-user")
    public ResponseEntity joinUser(@RequestBody EventLogParam param) {

        try {

            long userId = Long.parseLong(param.getId());

            EventCodeLog eventCodeLog = getBaseEventCodeLog(param, EventCodeType.NONE, EventCodeLogType.USER);
            eventCodeLog.setUserId(userId);

            eventCodeService.insertLog(eventCodeLog);

        } catch (OpRuntimeException ignore) {
            //log.error("event join user log error [{}] - {}", param.getId(), ignore.getMessage(),ignore);
            log.error("event join user log error [{}] - {}", param.getId(), "ERROR-70: event join user log error", ignore);
        }

        return ApiResponseEntity.data().ok();
    }

    @PostMapping("/featured")
    public ResponseEntity featured(@RequestBody EventLogParam param) {

        try {

            List<EventCodeLog> list = getBaseEventCodeLogList(param, EventCodeType.NONE, EventCodeLogType.FEATURED, param.getItems());
            eventCodeService.insertLog(list);
        } catch (OpRuntimeException ignore) {
            //log.error("event featured log error [{}] - {}", param.getId(), ignore.getMessage(),ignore);
        	log.error("event featured log error [{}] - {}", param.getId(), "ERROR-71: event featured log error", ignore);
        }

        return ApiResponseEntity.data().ok();
    }

    @PostMapping("/order")
    public ResponseEntity order(@RequestBody EventLogParam param) {

        try {
            List<EventCodeLog> list = getBaseEventCodeLogList(param, EventCodeType.NONE, EventCodeLogType.ORDER, param.getItems());
            eventCodeService.insertLog(list);
        } catch (OpRuntimeException ignore) {
            //log.error("event order error [{}] - {}", param.getId(), ignore.getMessage(),ignore);
        	log.error("event order error [{}] - {}", param.getId(), "ERROR-72: event order error", ignore);
        }

        return ApiResponseEntity.data().ok();
    }

    private List<EventCodeLog> getBaseEventCodeLogList(EventLogParam param, EventCodeType codeType, EventCodeLogType logType, List<String> items) {

        List<EventCodeLog> list = new ArrayList<>();

        if (items != null && !items.isEmpty()) {
            items.forEach(i->{

                if (!ObjectUtils.isEmpty(i)) {

                    EventCodeLog l = getBaseEventCodeLog(param, codeType, logType);
                    l.setItemUserCode(i);

                    list.add(l);
                }

            });
        }

        return list;
    }

    private EventCodeLog getBaseEventCodeLog(EventLogParam param, EventCodeType codeType, EventCodeLogType logType) {

        EventCodeLog eventCodeLog = new EventCodeLog();

        if (param != null) {

            eventCodeLog.setCodeType(codeType);
            eventCodeLog.setLogType(logType);

            eventCodeLog.setEventCode(param.getEventCode());
            eventCodeLog.setEventUid(param.getUid());
            eventCodeLog.setSourceUserId(param.getSourceUserId());
            eventCodeLog.setChannel(param.getChannel());
            eventCodeLog.setUtmSource(param.getUtmSource());
            eventCodeLog.setUtmMedium(param.getUtmMedium());
            eventCodeLog.setUtmCampaign(param.getUtmCampaign());
            eventCodeLog.setUtmItem(param.getUtmItem());
            eventCodeLog.setUtmContent(param.getUtmContent());

            eventCodeLog.setUserId(UserUtils.getUserId());

        }

        return eventCodeLog;
    }
}
