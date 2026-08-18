package saleson.shop.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import saleson.shop.payment.entity.GPayLogEntity;

public interface PaymentRepository extends JpaRepository<GPayLogEntity, Long>, PaymentRepositoryCustom {


}
