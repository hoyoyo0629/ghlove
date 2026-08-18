package saleson.shop.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import saleson.shop.donation.entity.GLocgovEntity;

public interface LocgovRepository extends JpaRepository<GLocgovEntity, String>, LocgovRepositoryCustom {

}
