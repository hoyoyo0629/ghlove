package saleson.common.sweettracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingInfoResponse {
    private String adUrl;
    private boolean complete;


    @JsonProperty("invoiceNo")
    private String invoiceNo;

    private String itemImage;
    private String itemName;
    private Integer level;

    @JsonProperty("receiverAddr")
    private String receiverAddr;

    @JsonProperty("receiverName")
    private String receiverName;

    private String recipient;
    private String result;

    @JsonProperty("senderName")
    private String senderName;

    @JsonProperty("trackingDetails")
    private List<TrackingDetail> trackingDetails;

    private String orderNumber;
    private String estimate;
    private String productInfo;
    private String zipCode;

    @JsonProperty("lastDetail")
    private TrackingDetail lastDetail;

    @JsonProperty("lastStateDetail")
    private TrackingDetail lastStateDetail;

    @JsonProperty("firstDetail")
    private TrackingDetail firstDetail;

    @JsonProperty("completeYN")
    private String completeYN;

}
