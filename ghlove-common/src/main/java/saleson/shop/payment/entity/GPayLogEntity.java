package saleson.shop.payment.entity;

import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.payment.enumeration.PaymentMethod;
import saleson.shop.payment.enumeration.PaymentProcess;

@Converter
class PaymentProcessConverter implements AttributeConverter<PaymentProcess, String> {
    @Override
    public String convertToDatabaseColumn(PaymentProcess attribute) {
        return attribute.name().toUpperCase();
    }

    @Override
    public PaymentProcess convertToEntityAttribute(String data) {
        return PaymentProcess.valueOf(data);
    }
}

@Converter
class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {
    @Override
    public String convertToDatabaseColumn(PaymentMethod attribute) {
        return attribute.name().toUpperCase();
    }

    @Override
    public PaymentMethod convertToEntityAttribute(String data) {
        return PaymentMethod.valueOf(data);
    }
}

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Convert(converter = PaymentProcessConverter.class, attributeName ="payProcess")
@Convert(converter = PaymentMethodConverter.class, attributeName ="payMethod")
@Table(name = "g_pay_log")
public class GPayLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_log_id")
    private Long payLogId;

    @Column(name = "elctrn_pay_no")
    private String elctrnPayNo;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "pay_method")
    private PaymentMethod payMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "pay_process")
    private PaymentProcess payProcess;

    @Column(name = "pay_start_dt")
    private LocalDateTime payStartDt;

    @Column(name = "pay_end_dt")
    private LocalDateTime payEndDt;

    @Column(name = "frst_reg_id")
    private Long frstRegId;

    @Column(name = "frst_reg_dt")
    private LocalDateTime frstRegDt;

    @Column(name = "last_mdfcn_id")
    private Long lastMdfcnId;

    @Column(name = "last_mdfcn_dt")
    private LocalDateTime lastMdfcnDt;

}

