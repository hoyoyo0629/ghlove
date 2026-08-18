package saleson.shop.log.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.util.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import saleson.common.enumeration.PrivacyAccess;
import saleson.common.enumeration.UserType;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.domain.Seller;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="OP_PRIVACY_ACCESS_LOG")
@SequenceGenerator(
		name = "PRIVACY_ACCESS_LOG_SEQ_GENERATOR"
		, sequenceName = "OP_PRIVACY_ACCESS_LOG_SEQ"
		, initialValue = 2152877
		, allocationSize = 1)
public class PrivacyAccessLog {
    public PrivacyAccessLog(PrivacyAccess privacyAccess, HttpServletRequest request) {
        setDefaultInfo(privacyAccess, request);
        this.url = request.getRequestURI();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRIVACY_ACCESS_LOG_SEQ_GENERATOR")
    private Long id;

    @Column
    private Long managerId;         // 처리자 ID

    @Column
    private Long userId;            // 회원 ID

    @Column(length = 20)
    private String ip;              // 접속 IP

    @Column(length = 100)
    private String name;            // 요청명

    @Column(length = 500)
    private String url;             // URL

    @Column(name = "`method`",length = 5)
    private String method;          // 요청 메소드

    @Column(length = 50)
    private String task;            // 수행업무
    
    @Column(length = 2)
    private String reasonType;		// 엑셀다운로드구분
    
    @Column(length = 1000)
    private String reason;          // 사유

    @Enumerated(EnumType.STRING)
    private UserType loginType;     // 로그인 타입 (관리자, 판매자)

    @JsonIgnore
    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt = Instant.now();  // 접속일시

    public void setDefaultInfo(PrivacyAccess privacyAccess, HttpServletRequest request) {
    	
        Seller seller = SellerUtils.getSeller();
//        if (ValidationUtils.isNotNull(seller) && ShopUtils.isSellerPage()) {
    	if (seller != null && ShopUtils.isSellerPage()) {
            this.managerId = seller.getSellerId();
            this.loginType = UserType.SELLER;
        } else if (UserUtils.getManagerId() > 0) {
            this.managerId = UserUtils.getManagerId();
            this.loginType = UserType.MANAGER;
        }
    	if (privacyAccess != null) {
            this.name = privacyAccess.getName();
        	if ("GET".equals(this.method)) {
                if (privacyAccess.getTask() != null)  {
                	this.task = privacyAccess.getTask().getTitle();
                }
        	} else {
        		if (privacyAccess.getActionTask() != null)  {
            		this.task = privacyAccess.getActionTask().getTitle();
        		}
        	}
    	}
    	if (request != null) {
            this.method = request.getMethod();
    	}
        this.ip = CommonUtils.getClientIp(request);

    }
}