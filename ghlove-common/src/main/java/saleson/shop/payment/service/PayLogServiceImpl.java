package saleson.shop.payment.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.utils.UserUtils;
import saleson.shop.payment.dto.EndPayLogRequestDto;
import saleson.shop.payment.dto.EndPayLogResponseDto;
import saleson.shop.payment.dto.StartPayLogRequestDto;
import saleson.shop.payment.dto.StartPayLogResponseDto;
import saleson.shop.payment.entity.GPayLogEntity;
import saleson.shop.payment.enumeration.PaymentProcess;
import saleson.shop.payment.repository.PaymentRepository;
import saleson.shop.user.domain.UserDetail;

@Slf4j
@Service("payLogService")
@RequiredArgsConstructor
public class PayLogServiceImpl extends EgovAbstractServiceImpl implements PayLogService {

	@Autowired
	private final PaymentRepository paymentRepository;

	@Override
	@Transactional
	public StartPayLogResponseDto startPayLog(StartPayLogRequestDto startPayLogRequestDto) {
		UserDetail userDetail = UserUtils.getUserDetail();
		LocalDateTime payStartDt = LocalDateTime.now();

		log.debug("======= startPayLog : {}", userDetail);

		GPayLogEntity entity = GPayLogEntity.builder()
				.elctrnPayNo(startPayLogRequestDto.getElctrnPayNo())
				.payMethod(startPayLogRequestDto.payMethod)
				.payProcess(startPayLogRequestDto.payProcess)
				.payStartDt(payStartDt)
				.frstRegId(userDetail.getUserId())
				.frstRegDt(payStartDt)
				.build();

		GPayLogEntity result = paymentRepository.save(entity);

		return StartPayLogResponseDto.builder()
				.payLogId(result.getPayLogId())
				.elctrnPayNo(startPayLogRequestDto.getElctrnPayNo())
				.payMethod(startPayLogRequestDto.payMethod)
				.payProcess(startPayLogRequestDto.payProcess)
				.payStartDt(result.getPayStartDt())
				.build();
	}

	@Override
	@Transactional
	public EndPayLogResponseDto endPayLog(EndPayLogRequestDto endPayLogRequestDto) {
		UserDetail userDetail = UserUtils.getUserDetail();
		LocalDateTime payEndDt = LocalDateTime.now();

		endPayLogRequestDto.setPayEndDt(payEndDt);
		endPayLogRequestDto.setUserId(userDetail.getUserId());

		paymentRepository.updateEndPayLog(endPayLogRequestDto);

		return EndPayLogResponseDto.builder()
				.payLogId(endPayLogRequestDto.getPayLogId())
				.payProcess(endPayLogRequestDto.payProcess)
				.payEndDt(payEndDt)
				.build();
	}



}
