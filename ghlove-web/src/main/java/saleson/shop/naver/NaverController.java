package saleson.shop.naver;

import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.CommonUtils;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Category;
import saleson.shop.categories.domain.Group;
import saleson.shop.categories.domain.Team;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.support.ItemParam;
import saleson.shop.openmarket.OpenmarketService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/naver")
@RequestProperty(layout="base")
public class NaverController {

    @Autowired
    ItemService itemService;

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    OpenmarketService openmarketService;

    @RequestMapping("/ep")
    public String ep() {
        ItemParam itemParam = new ItemParam();

        itemParam.setConditionType("EP_ITEM_LIST");
        itemParam.setNaverShoppingFlag("Y");
        itemParam.setDisplayFlag("Y");					// 공개 상품만
        itemParam.setDataStatusCode("1");

        StringBuffer sb = new StringBuffer();

        String path = "/ep/";
        String fileName = "ep.txt";

        // 상품 정보
        List<Item> list = itemService.getItemList(itemParam);

        // 카테고리 정보
        List<Team> categories = categoriesService.getCategoriesForApi();

        sb.append("id\ttitle\tprice_pc\tlink\timage_link\tcategory_name1\tshipping\treview_count\n");

        for (Item item : list) {
        	if (item != null) {
                sb.append(item.getItemUserCode()+"\t");             // id
                sb.append(item.getNaverShoppingItemName()+"\t");    // title
                sb.append(item.getSalePrice()+"\t");                // price_pc

                sb.append(SalesonProperty.getSalesonUrlFrontend() + "/items/details.html?code=" + item.getItemUserCode()+"\t");  // link
                sb.append(SalesonProperty.getSalesonUrlShoppingmall() + item.getImageSrc()+"\t");  // image_link

                String categoryName1 = "";
                if (item.getItemCategories().size() > 0) {
                    Map<String, String> infos = getCategoryNameInfo(categories, Integer.toString(item.getItemCategories().get(0).getCategoryId()));
                    if (infos.size() > 0) {
                        categoryName1 = infos.get("category_name1");
                    }
                }

                sb.append(categoryName1+"\t");                      // category_name1
                sb.append(item.getShipping()+"\t");                 // shipping
                sb.append(item.getReviewCount()+"\n");              // review_count
        	}
        }

        try {
            String uploadPath = FileUtils.getDefaultUploadPath() + path;

            File savePath = new File(uploadPath);
            if (!savePath.exists()) {
                savePath.mkdirs();
            }
            
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(uploadPath + fileName), "UTF-8");) {
                osw.write(sb.toString());
            } catch (IOException e) {
            	throw new IOException();
            }
            
        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        	return ViewUtils.redirect("/ep", MessageUtils.getMessage("실패했습니다."));
        	return ViewUtils.redirect("/ep", "실패했습니다.");
		} /*
			 * finally { if (osw != null) try { osw.close(); } catch (Exception e) {} }
			 */

        return ViewUtils.redirect("/upload" + path + fileName);
    }

    private Map<String, String> getCategoryNameInfo(List<Team> categories, String categoryId) {
        Map<String, String> infos = new HashMap<>();

        for (Team team : categories) {
            for (Group group : team.getGroups()) {
                for (Category category1 : group.getCategories()) {
                    if (categoryId.equals(category1.getCategoryId())) {
                        infos.put("category_name1", group.getName());
                        infos.put("category_name2", category1.getName());
                        infos.put("category_name3", "");
                        infos.put("category_name4", "");
                        return infos;
                    }
                    for (Category category2 : category1.getChildCategories()) {
                        if (categoryId.equals(category2.getCategoryId())) {
                            infos.put("category_name1", group.getName());
                            infos.put("category_name2", category1.getName());
                            infos.put("category_name3", category2.getName());
                            infos.put("category_name4", "");
                            return infos;
                        }
                        for (Category category3 : category2.getChildCategories()) {
                            if (categoryId.equals(category3.getCategoryId())) {
                                infos.put("category_name1", group.getName());
                                infos.put("category_name2", category1.getName());
                                infos.put("category_name3", category2.getName());
                                infos.put("category_name4", category3.getName());
                                return infos;
                            }
                            for (Category category4 : category3.getChildCategories()) {
                                if (categoryId.equals(category4.getCategoryId())) {
                                    infos.put("category_name1", group.getName());
                                    infos.put("category_name2", category1.getName());
                                    infos.put("category_name3", category2.getName());
                                    infos.put("category_name4", category3.getName());
                                    infos.put("category_name5", category4.getName());
                                    return infos;
                                }
                            }
                        }
                    }
                }
            }
        }

        return infos;
    }

    @GetMapping(value= "/sync-item", produces="application/xml; charset=UTF-8")
    @ResponseBody
    public String syncItem(HttpServletRequest request) {

        String parameter = "ITEM_ID";
        List<String> codes = new ArrayList<>();

        String[] array = CommonUtils.copy(request.getParameterValues(parameter));

        if (array != null && array.length > 0) {
            codes.addAll(List.of(array));
        }

        if (codes.isEmpty()) {
            return "";
        }

        return openmarketService.getSyncNaverItemXml(codes);
    }
}