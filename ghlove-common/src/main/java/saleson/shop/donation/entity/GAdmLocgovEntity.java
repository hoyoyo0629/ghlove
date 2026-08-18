package saleson.shop.donation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "g_adm_locgov")
public class GAdmLocgovEntity {

	@Id
    @Column(name = "adm_cd")
    private String admCd;

    @Column(name = "adm_sect_nm")
    private String admSectNm;

    @Column(name = "locgov_code")
    private String locgovCode;

}

