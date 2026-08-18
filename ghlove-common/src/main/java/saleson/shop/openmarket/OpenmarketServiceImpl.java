package saleson.shop.openmarket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Breadcrumb;
import saleson.shop.openmarket.domain.SyncNaverItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("openmarketService")
@RequiredArgsConstructor
public class OpenmarketServiceImpl extends EgovAbstractServiceImpl implements OpenmarketService{

    private final OpenmarketMapper openmarketMapper;
    private final CategoriesService categoriesService;

    @Override
    public String getSyncNaverItemXml(List<String> itemUserCodes) {

        StringBuilder sb = new StringBuilder();

        Map<Integer, List<Breadcrumb>> breadcrumbMap = new HashMap<>();

        if (itemUserCodes != null && !itemUserCodes.isEmpty()) {

            List<SyncNaverItem> items = openmarketMapper.getSyncNaverItems(itemUserCodes);

            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            sb.append("<response>");
            if (items != null && !items.isEmpty()) {
                items.forEach(item -> {
                    int categoryId = item.getFirstCategoryId();
                    List<Breadcrumb> breadcrumbs = breadcrumbMap.get(categoryId);
                    if (ObjectUtils.isEmpty(breadcrumbs)) {
                        breadcrumbs = categoriesService.getBreadcrumbListByCategoryId(categoryId);
                        breadcrumbMap.put(categoryId, breadcrumbs);
                    }

                    item.setBreadcrumbs(breadcrumbs);
                    sb.append(item.getXml());
                });
            }

            sb.append("</response>");
        }
        return sb.toString();
    }
}
