package saleson.shop.payment.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.shop.payment.dto.EndPayLogRequestDto;
import saleson.shop.payment.entity.GPayLogEntity;
import saleson.shop.payment.entity.QGPayLogEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PaymentRepositoryImpl implements PaymentRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public long updateEndPayLog(EndPayLogRequestDto endPayLogRequestDto) {
		QGPayLogEntity payLog = QGPayLogEntity.gPayLogEntity;

		return jpaQueryFactory
				.update(payLog)
				.set(payLog.payProcess, endPayLogRequestDto.payProcess)
				.set(payLog.payEndDt, endPayLogRequestDto.getPayEndDt())
				.set(payLog.lastMdfcnId, endPayLogRequestDto.getUserId())
				.set(payLog.lastMdfcnDt, endPayLogRequestDto.getPayEndDt())
				.where(payLog.payLogId.eq(endPayLogRequestDto.getPayLogId()))
				.execute();
	}

}
