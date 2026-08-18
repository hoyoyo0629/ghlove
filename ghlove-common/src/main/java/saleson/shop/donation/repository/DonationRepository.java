package saleson.shop.donation.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import saleson.shop.donation.entity.GCntrEntity;

public interface DonationRepository extends JpaRepository<GCntrEntity, String>, DonationRepositoryCustom {

}
