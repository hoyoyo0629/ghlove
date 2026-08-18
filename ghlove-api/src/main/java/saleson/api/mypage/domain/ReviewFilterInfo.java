package saleson.api.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saleson.model.FilterCode;
import saleson.model.FilterGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFilterInfo {

    private static Logger log = LoggerFactory.getLogger(ReviewFilterInfo.class);

    private String label;
    private String description;
    private long id;

    private List<Map<String, Object>> codes;

    public ReviewFilterInfo(FilterGroup group) {

        if (group != null) {

            try {

                setLabel(group.getLabel());
                setDescription(group.getDescription());
                setId(group.getId());

                List<Map<String, Object>> codes = new ArrayList<>();
                List<FilterCode> codeList = group.getCodeList();

                if (codeList != null && !codeList.isEmpty()) {
                    codeList.forEach(c -> {
                        Map<String, Object> map = new LinkedHashMap<>();

                        map.put("id", c.getId());
                        map.put("label", c.getLabel());
                        map.put("code", c.getLabelCode());

                        codes.add(map);
                    });
                }

                setCodes(codes);

            } catch (NullPointerException ignore) {
                log.error("make ReviewFilterInfo error {}", ignore.getMessage(), ignore);
            }

        }
    }
}
