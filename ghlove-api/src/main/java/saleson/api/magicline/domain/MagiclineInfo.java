package saleson.api.magicline.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MagiclineInfo {

    private String signOrigin;
    private String sign;
    private String csCheckType;
    private String signData;

}
