package saleson.shop.eventcode.domain;

import lombok.Data;
import org.springframework.util.ObjectUtils;
import saleson.common.enumeration.eventcode.EventCodeType;
import saleson.common.utils.CommonUtils;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class EventStatistics {

    private String eventCode;
    private String contents;
    private int visitCount;
    private int orderCount;
    private double orderRate;
    private int joinCount;
    private double joinRate;

    public EventStatistics(Map<String, Object> map) {

        if (map != null) {
            setEventCode(CommonUtils.dataNvl(map.get("EVENT_CODE")));
            setVisitCount(CommonUtils.bigDecimalNvl(convert(map,"VISIT_COUNT")).intValue());
            setOrderCount(CommonUtils.bigDecimalNvl(convert(map,"ORDER_COUNT")).intValue());
            setOrderRate(CommonUtils.bigDecimalNvl(convert(map,"ORDER_RATE")).doubleValue());
            setJoinCount(CommonUtils.bigDecimalNvl(convert(map,"JOIN_COUNT")).intValue());
            setJoinRate(CommonUtils.bigDecimalNvl(convert(map,"JOIN_RATE")).doubleValue());
        }
    }

    private BigDecimal convert(Map<String, Object> map, String key) {

        if (ObjectUtils.isEmpty(map)) {
            return BigDecimal.ZERO;
        }

        Object o = map.get(key);

        if (ObjectUtils.isEmpty(o)) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(String.valueOf(o));
    }
}

