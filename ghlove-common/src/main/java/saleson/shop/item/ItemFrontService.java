package saleson.shop.item;

import java.util.List;

import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemImageExplain;
import saleson.shop.item.support.ItemParam;

public interface ItemFrontService {

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	public int getItemCount(ItemParam itemParam);


	/**
	 * 답례품 노출 기준 변경으로 상품 카운트 조회 조건 변경
	 * 1. 품절 상품이 더 이상 노출되지 않도록 수정
	 * 2. 포인트 카테고리 기준 변경
	 * 	2-1. 1만원 이하 : 0 ~ 10,000원			/ 기존 : ~ 10,000
	 *  2-2. 3만원 이하 : 10,000원 ~ 30,000원	/ 기존 : ~ 30,000
	 *  2-3. 5만원 이하 : 30,000원 ~ 50,000원	/ 기존 : ~ 50,000
	 *  2-4. 5만원 이상 : 50,000원 ~			/ 기존 : 50,000 ~
	 * @param itemParam
	 * @return
	 * */
	public int getItemCountForNewPolicy(ItemParam itemParam);


	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	public List<Item> getItemList(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	public int getItemCountBySeason(ItemParam itemParam);


	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	public List<Item> getItemListBySeason(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	public int getItemCountBySpeciality(ItemParam itemParam);


	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	public List<Item> getItemListBySpeciality(ItemParam itemParam);

	/**
	 * 답례품 화면 제한 여부
	 * @return
	 */
	public boolean isItemRestrict();


	/**
	 * 국정자원진단 제시 코드 적용 답례품 목록 조회
	 * @param itemParam
	 * @return
	 */
	public List<Item> getItemListNew(ItemParam itemParam);

	/**
	 * 상품 이미지의 링크뷰 설명을 조회한다.
	 * @param itemId
	 * @return
	 */
	public List<ItemImageExplain> getItemImagesExplainByLinkView(String itemUserCode);

	/**
	 * 마을 기업 리스트 조회
	 * @param itemParam
	 * @return
	 */
	public List<Item> getFrontItemListByCommunity(ItemParam itemParam);

	/**
	 * 마을 기업 리스트 상품 카운트
	 * @param itemParam
	 * @return
	 */
	public int getFrontItemCountByCommunity(ItemParam itemParam);



}
