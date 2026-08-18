package saleson.api.seo;

import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.api.seo.domain.SeoInfo;
import saleson.common.configuration.SalesonProperty;
import saleson.common.opengraph.OpenGraph;
import saleson.common.utils.ShopUtils;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Categories;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.featured.FeaturedService;
import saleson.shop.featured.domain.Featured;
import saleson.shop.featured.support.FeaturedParam;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.seo.SeoService;
import saleson.shop.seo.domain.Seo;

import java.util.List;

@RestController("ApiSeoController")
@RequestMapping("/api/seo")
public class SeoController {

    private Logger log = LoggerFactory.getLogger(SeoController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private SeoService seoService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private FeaturedService featuredService;

    @Autowired
    Environment environment;

    @GetMapping("")
    public ResponseEntity seo(@RequestParam(name = "u", defaultValue = "") String u) {

        SeoInfo seo;

        try {

            if (u.contains("/item/") && getValueByUri(u).indexOf("result") == -1) {
                seo = getItemSeo(getValueByUri(u));
            } else if (u.contains("/category/")){
                seo = getCategorySeo(getValueByUri(u));
            } else if (u.contains("/featured/")) {
                seo = getFeaturedSeo(getValueByUri(u));
            } else {
                seo = getDefaultSeo(u);
            }

        } catch (RuntimeException e) {
            log.error("get seo error [{}]", u, e);
            seo = getDefaultSeo(u);
        }

        return ApiResponseEntity.data()
                .put("seo", seo)
                .ok();
    }

    private String getValueByUri(String uri) {

        String value = "";

        try {

            String[] list = StringUtils.delimitedListToStringArray(uri, "/");

            if (list != null && list.length > 0) {
                int index = list.length -1;
                return list[index];
            }

        } catch (RuntimeException e) {
            log.error("getValueByUri error [{}]", uri, e);
        }

        return value;
    }

    private SeoInfo getItemSeo (String itemUserCode) {

        Seo seo = getShopConfigSeo();
        OpenGraph openGraph = new OpenGraph();

        try {

            if (!ObjectUtils.isEmpty(itemUserCode)) {

                Item item = itemService.getItemByItemUserCode(itemUserCode);

                if (item != null) {

                    seo = getSeo(item.getSeo());

                    openGraph = new OpenGraph(item);

                    String link = getLink("item", item.getItemUserCode());
                    String image = getImageSrc(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "S"));

                    openGraph.setImage(image);
                    openGraph.setLink(link);
                    openGraph.setUrl(link);
                    openGraph.setDescription(item.getItemSummary());
                }

            }

        } catch (RuntimeException e) {
            log.error("seo item [{}] [{}]", JsonViewUtils.objectToJson(openGraph), JsonViewUtils.objectToJson(seo), e);
        }

        return new SeoInfo(seo, openGraph);
    }

    private SeoInfo getCategorySeo (String categoryCode) {

        Seo seo = getShopConfigSeo();
        OpenGraph openGraph = new OpenGraph();

        try {

            if (!ObjectUtils.isEmpty(categoryCode)) {

                Categories category = categoriesService.getCategoryByCategoryUrl(categoryCode);

                if (category != null) {

                    seo = getSeo(category.getCategoriesSeo());

                    openGraph = new OpenGraph(category);

                    String link = getLink("category", category.getCategoryUrl());

                    openGraph.setLink(link);
                    openGraph.setUrl(link);
                }
            }

        } catch (RuntimeException e) {
            log.error("seo categories [{}] [{}]", JsonViewUtils.objectToJson(openGraph), JsonViewUtils.objectToJson(seo), e);
        }
        return new SeoInfo(seo, openGraph);
    }

    private SeoInfo getFeaturedSeo (String featuredUrl) {

        Seo seo = getShopConfigSeo();
        OpenGraph openGraph = new OpenGraph();

        try {

            if (!ObjectUtils.isEmpty(featuredUrl)) {

                FeaturedParam param = new FeaturedParam();
                param.setFeaturedUrl(featuredUrl);
                Featured featured = featuredService.getFeaturedById(param);

                if (featured != null) {

                    seo = getSeo(featured.getSeo());

                    openGraph = new OpenGraph(featured);

                    String link = getLink("featured", featured.getFeaturedUrl());
                    String image = getImageSrc(featured.getThumbnailImageSrc());

                    openGraph.setImage(image);
                    openGraph.setLink(link);
                    openGraph.setUrl(link);
                }

            }

        } catch (RuntimeException e) {
            log.error("seo item [{}] [{}]", JsonViewUtils.objectToJson(openGraph), JsonViewUtils.objectToJson(seo), e);
        }

        return new SeoInfo(seo, openGraph);
    }

    private Seo getShopConfigSeo() {
        Config config = configService.getShopConfig(Config.SHOP_CONFIG_ID);

        Seo seo = new Seo();

        seo.setTitle(config.getSeoTitle());
        seo.setKeywords(config.getSeoKeywords());
        seo.setDescription(config.getSeoDescription());
        seo.setHeaderContents1(config.getSeoHeaderContents1());
        seo.setThemawordTitle(config.getSeoThemawordTitle());
        seo.setThemawordDescription(config.getSeoThemawordDescription());

        return seo;
    }

    private Seo getSeo(Seo seo) {
        if (seo != null && !seo.isSeoNull()) {
            return seo;
        }

        return getShopConfigSeo();
    }

    private SeoInfo getDefaultSeo(String uri) {

        Seo seo = getShopConfigSeo();

        if (!ObjectUtils.isEmpty(uri)) {

            List<Seo> list = seoService.getSeoListAll();

            if (list != null && !list.isEmpty()) {
                for (Seo seo2 : list) {
                    if (uri.equalsIgnoreCase(seo2.getSeoUrl())) {
                        seo.setTitle(seo2.getTitle());
                        seo.setKeywords(seo2.getKeywords());
                        seo.setDescription(seo2.getDescription());
                        seo.setHeaderContents1(seo2.getHeaderContents1());
                        seo.setThemawordTitle(seo2.getThemawordTitle());
                        seo.setThemawordDescription(seo2.getThemawordDescription());
                        seo.setIndexFlag(seo2.getIndexFlag());
                        break;
                    }
                }
            }
        }

        return new SeoInfo(seo, new OpenGraph());
    }

    private String getImageSrc(String imageSrc) {

        if (ObjectUtils.isEmpty(imageSrc)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(SalesonProperty.getSalesonUrlCdn());
        sb.append(SalesonProperty.getUploadBaseFolder());
        sb.append(imageSrc);
        return sb.toString();
    }

    private String getLink(String... list) {

        StringBuilder sb = new StringBuilder();
        sb.append(SalesonProperty.getSalesonUrlFrontend());

        if (list != null && list.length > 0) {
            for (String element : list) {
                sb.append("/");
                sb.append(element);
            }
        }

        return sb.toString();
    }
}
