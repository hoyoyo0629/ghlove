package saleson.shop.item;

import java.util.List;

import saleson.shop.item.domain.ItemAddition;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("itemAdditionMapper")
public interface ItemAdditionMapper {
	/**
	 * 상품 ID로 추가구성 상품목록 조회.
	 * @param itemId
	 * @return
	 */
	List<ItemAddition> getItemAdditionListByItemId(int itemId);
	
	/**
	 * 추가구성 상품ID로 추가구성 상품 조회.
	 * @param itemAdditionId
	 * @return
	 */
	ItemAddition getItemAdditionById(int itemAdditionId);
	
	/**
	 * 추가구성상품 등록.
	 * @param itemAddition
	 */
	void insertItemAddition(ItemAddition itemAddition);
	
	/**
	 * 추가구성상품 수정.
	 * @param itemAddition
	 */
	void updateItemAddition(ItemAddition itemAddition);
	
	/**
	 * 추가구성상품 삭제 (itemAdditionId)
	 * @param itemAddition
	 */
	void deleteItemAdditionById(int itemAdditionId);
	
	/**
	 * 추가구성상품 삭제 (itemId)
	 * @param itemAddition
	 */
	void deleteItemAdditionByItemId(int itemId);
}
