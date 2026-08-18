package saleson.api.seo.domain;

import lombok.*;
import saleson.common.opengraph.OpenGraph;
import saleson.shop.seo.domain.Seo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeoInfo {

    private String title;
    private String keywords;
    private String description;
    private String headerContents1;
    private String themawordTitle;
    private String themawordDescription;
    private String indexFlag = "N";

    private OpenGraph openGraph;

    public SeoInfo(Seo seo, OpenGraph openGraph) {
        setTitle(seo.getTitle());
        setKeywords(seo.getKeywords());
        setDescription(seo.getDescription());
        setHeaderContents1(seo.getHeaderContents1());
        setThemawordTitle(seo.getThemawordTitle());
        setThemawordDescription(seo.getThemawordDescription());
        setIndexFlag(seo.getIndexFlag());

        setOpenGraph(openGraph);
    }
}
