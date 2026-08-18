package saleson.shop.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import saleson.model.campaign.CampaignQueryConst;
import saleson.shop.donation.entity.OpUserEntity;

public interface DonatorRepository extends JpaRepository<OpUserEntity, Long>, DonatorRepositoryCustom {
	
	/*@Modifying
    @Query(value = CampaignQueryConst.TEST_G_CNTR_SUNAP_SUCCESS, nativeQuery = true)
    int sunapSuccess(
            @Param("sttemntPayDe") String sttemntPayDe,
            @Param("userId") Long userId,
            @Param("elctrnPayNo") String elctrnPayNo
    );*/
}
