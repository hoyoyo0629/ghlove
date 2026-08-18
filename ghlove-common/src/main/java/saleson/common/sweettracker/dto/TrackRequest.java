package saleson.common.sweettracker.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TrackRequest {
    @NotBlank
    private String carrierCode;  // 택배사 코드
    @NotBlank private String invoiceNo;    // 운송장 번호
    @NotBlank private String orderNo;      // 주문 번호
}
