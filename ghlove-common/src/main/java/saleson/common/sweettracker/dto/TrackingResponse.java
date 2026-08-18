package saleson.common.sweettracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingResponse {

    @JsonProperty("secret_value")
    private String secretValue;

    @JsonProperty("fid")
    private String fid;

    @JsonProperty("courier_code")
    private String courierCode;

    @JsonProperty("invoice_no")
    private String invoiceNo;

    // "2" 처럼 문자열로 내려오므로 String으로 받는 것이 안전
    @JsonProperty("level")
    private String level;

    // "2021-04-22 16:49:36"
    @JsonProperty("time_trans")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeTrans;

    // "2021-04-26 12:08:00"
    @JsonProperty("time_sweet")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeSweet;

    // where는 자바 예약어가 아니라 필드명으로 사용 가능
    @JsonProperty("where")
    private String whereAt;

    @JsonProperty("telno_office")
    private String telnoOffice;

    @JsonProperty("telno_man")
    private String telnoMan;

    @JsonProperty("details")
    private String details;

    @JsonProperty("recv_addr")
    private String recvAddr;

    @JsonProperty("recv_name")
    private String recvName;

    @JsonProperty("send_name")
    private String sendName;

    @JsonProperty("man")
    private String man;

    @JsonProperty("estmate")
    private String estmate;

    @JsonProperty("comcode")
    private String comcode;
}
