package saleson.shop.gnb.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.model.Gnb;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GnbDto {

    private String title;
    private String target;

    public GnbDto(Gnb gnb) {
        if (gnb != null) {
            setTitle(gnb.getTitle());
            setTarget(gnb.getTarget());
        }
    }
}
