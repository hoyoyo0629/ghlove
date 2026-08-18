package saleson.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.util.StringUtils;
import lombok.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;
import saleson.model.base.BaseEntity;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name="OP_FILTER_CODE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class FilterCode extends BaseEntity {

	//사용 안하는 테이블
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 1000, nullable = false)
    private String label;

    @Column(length = 1000)
    private String labelCode;

    @Column(length = 1000)
    private String labelImage;

    @Column
    private Integer ordering;

    @Transient
    private MultipartFile imageFile;

    @JsonIgnore
    public String getUploadPath() {

        StringBuilder sb = new StringBuilder();
        sb.append(SalesonProperty.getUploadSaveFolder());
        sb.append(File.separator);
        sb.append("categories-filter");
        sb.append(File.separator);

        return sb.toString();
    }

    public String getImageSrc() {

        if (ObjectUtils.isEmpty(getLabelImage())) {
            return ShopUtils.getNoImagePath();
        }

        StringBuilder sb = new StringBuilder();

        sb.append(SalesonProperty.getSalesonUrlCdn());
        sb.append(SalesonProperty.getUploadBaseFolder());
        sb.append("/");
        sb.append("categories-filter");
        sb.append("/");
        sb.append(getLabelImage());

        return sb.toString();
    }
}
