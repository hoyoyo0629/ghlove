package saleson.common.sweettracker.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeliveryRequest {
    @NotBlank
    private String carrierCode;  // 택배사 코드
    @NotBlank
    private String invoiceNo;    // 운송장 번호
}
