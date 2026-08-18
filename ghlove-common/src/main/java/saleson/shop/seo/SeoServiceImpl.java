package saleson.shop.seo;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.cache.ReloadType;
import saleson.common.utils.ItemUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.cache.ShopCacheService;
import saleson.shop.categories.CategoriesMapper;
import saleson.shop.categories.domain.Categories;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.support.ItemParam;
import saleson.shop.seo.domain.Seo;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.domain.SearchParam;

@Service("seoService")
public class SeoServiceImpl extends EgovAbstractServiceImpl implements SeoService {
	@Autowired
	private SeoMapper seoMapper;
	
	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private CategoriesMapper categoriesMapper;

	@Autowired
	private ShopCacheService shopCacheService;

	@Autowired
	private ConfigService configService;

	@Override
	public Seo getSeoById(int seoId) {
		return seoMapper.getSeoById(seoId);
	}

	@Override
	public int getSeoCount(SearchParam param) {
		return seoMapper.getSeoCount(param);
	}

	@Override
	public List<Seo> getSeoList(SearchParam param) {
		return seoMapper.getSeoList(param);
	}

	@Override
	public void insertSeo(Seo seo) {
		
		// URL 중복체크 
		if (seoMapper.getSeoCountBySeoUrl(seo.getSeoUrl()) > 0) {
			throw new UserException("이미 등록된 URL입니다.");
		}
		
		seo.setSeoId(sequenceService.getId("OP_SEO"));
		seo.setCreatedUserId(UserUtils.getManagerId());
		seoMapper.insertSeo(seo);
		sendFrontReloadFixedMmetaData();
		
	}

	@Override
	public void updateSeo(Seo seo) {
		seoMapper.updateSeo(seo);
		sendFrontReloadFixedMmetaData();
	}

	@Override
	public void deleteSeoById(int seoId) {
		seoMapper.deleteSeoById(seoId);
		sendFrontReloadFixedMmetaData();
	}

	@Override
	@Cacheable("fixedMetaData")
	public List<Seo> getSeoListAll() {
		return seoMapper.getSeoList(new SearchParam());
	}

	@Override
	public void updateListData(ListParam listParam) {

		
	}

	@Override
	public void deleteListData(ListParam listParam) {
		
		if (listParam.getId() != null) {

			for (String seoId : listParam.getId()) {
				deleteSeoById(Integer.parseInt(seoId));
			}
			
		}
	}

	@Override
	public String getSitemapString() {

		ItemParam itemParam = new ItemParam();
		itemParam = ItemUtils.bindItemParam(itemParam);
		List<Item> itemList = itemService.getItemList(itemParam);

		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
		for (Item item : itemList) {

			String link = SalesonProperty.getSalesonUrlShoppingmall()
					+ "/seo/item/"
					+ item.getItemUserCode();

			sb.append(getSitemapUrlString(link));

		}

		List<Categories> categories =  categoriesMapper.getCategoryListBySitemap();

		for (Categories c : categories) {

			String link = SalesonProperty.getSalesonUrlShoppingmall()
					+ "/seo/categories/"
					+ c.getCategoryUrl();

			sb.append(getSitemapUrlString(link));

		}

		sb.append("</urlset>");

		return sb.toString();
	}

	private String getSitemapUrlString(String loc) {
		StringBuffer sb = new StringBuffer();
		sb.append("<url>");
		sb.append("<loc>");
		sb.append(loc);
		sb.append("</loc>");
		sb.append("<changefreq>always</changefreq>");
		sb.append("</url>");
		return sb.toString();
	}

	private void sendFrontReloadFixedMmetaData() {
		shopCacheService.sendFrontReload(ReloadType.FIXED_META_DATA);
	}

	@Override
	public Seo getShopConfigSeo() {
		Config config = ShopUtils.getConfig();

		if (config == null) {
			config = configService.getShopConfig(Config.SHOP_CONFIG_ID);
		}

		Seo seo = new Seo();

		seo.setIndexFlag("Y");
		seo.setTitle(config.getSeoTitle());
		seo.setKeywords(config.getSeoKeywords());
		seo.setDescription(config.getSeoDescription());
		seo.setHeaderContents1(config.getSeoHeaderContents1());
		seo.setThemawordTitle(config.getSeoThemawordTitle());
		seo.setThemawordDescription(config.getSeoThemawordDescription());

		return seo;
	}

	@Override
	public Seo getSeoByItem(SearchParam param) {

		Seo seo = seoMapper.getSeoMetaByItem(param);
		if (seo == null|| seo.isSeoNull()) {
			return getShopConfigSeo();
		}
		return seo;
	}

	@Override
	public Seo getSeoByCategory(SearchParam param) {
		Seo seo = seoMapper.getSeoMetaByCategory(param);
		if (seo == null || seo.isSeoNull()) {
			return getShopConfigSeo();
		}

		return seo;
	}
}
