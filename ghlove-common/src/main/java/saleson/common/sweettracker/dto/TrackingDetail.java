package saleson.common.sweettracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingDetail {
    private String kind;
    private Integer level;
    private String manName;
    private String manPic;
    private String telno;
    private String telno2;
    private Long time;          // epoch millis
    private String timeString;  // "2025-09-12 12:16:00"
    private String where;
    private String code;
    private String remark;

}
