package saleson.shop.donation.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder
@Table(name = "g_intrst_locgov")
public class GIntrstLocgovEntity {
	@Id
	@Column(name = "user_id")
    private long userId;
	
	@Column(name = "locgov_code")
    private String locgovCode;
	
    @Column(name = "regist_de")
    private String registDe;
    
    @Column(name = "frst_register_id")
    private String frstRegisterId;
    
    //@Column(name = "frst_regist_pnttm")
    //private String frstRegistPnttm;
    
    @Column(name = "last_updusr_id")
    private String lastUpdusrId;
    
    @Column(name = "last_updt_pnttm")
    private String lastUpdtPnttm;
}