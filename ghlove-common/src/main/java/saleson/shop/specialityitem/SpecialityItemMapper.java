package saleson.shop.specialityitem;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.specialityitem.domain.SpecialityFrontDomain;
import saleson.shop.specialityitem.domain.SpecialityItem;
import saleson.shop.specialityitem.domain.SpecialityItemManage;
import saleson.shop.specialityitem.support.SpecialityItemParam;

@Mapper("SpecialityItemMapper")
public interface SpecialityItemMapper {

	/**
	 * 특산물관 관리 정보 조회
	 * @param params
	 * @return List<SpecialityItemManage>
	 */
	List<SpecialityItemManage> getSpecialityItemManageList(SpecialityItemParam params);

	/**
	 * 특산물관 관리 정보 저장
	 * @param specialityItemManage
	 * @return int
	 */
	int insertSpecialityItemManage(SpecialityItemManage specialityItemManage);

	/**
	 * 특산물관 관리 정보 수정
	 * @param specialityItemManage
	 * @return int
	 */
	int updateSpecialityItemManage(SpecialityItemManage specialityItemManage);

	/**
	 * 특산물관 관리 상품 정보 저장
	 * @param SpecialityItem
	 * @return int
	 */
	int insertSpecialityItem(SpecialityItem specialityItem);

	/**
	 * 특산물관 관리 상품 정보 조회
	 * @param SpecialityItemParam
	 * @return List<SpecialityItem>
	 */
	List<SpecialityItem> getSpecialityItemListByParam(SpecialityItemParam specialityItemParam);

	/**
	 * 특산물관 관리 상품 정보 조회
	 * @param specialityItemManage
	 * @return int
	 */
	int deleteSpecialityItem(SpecialityItemManage specialityItemManage);

	/**
	 * 특산물관 관리 정보 갯수 조회
	 * @param
	 * @return long
	 */
	long getSpecialityItemManageCount();

	/**
	 * 특산물관 관리 상품 키워드 추가
	 * @param specialityItemParam
	 * @return int
	 */
	int insertSpecialityItemKeyword(SpecialityItemParam specialityItemParam);

	/**
	 * 특산물관 관리 상품 키워드 삭제
	 * @param specialityItemManage
	 * @return int
	 */
	int deleteSpecialityItemKeyword(SpecialityItemManage specialityItemManage);

	/**
	 * 특산물관 관리 정보 갯수 조회
	 * @param
	 * @return long
	 */
	int getSpecialityFrontListCount(SpecialityItemParam params);

	/**
	 * 특산물관 관리 정보 조회
	 * @param params
	 * @return List<SpecialityItemManage>
	 */
	List<SpecialityFrontDomain> getSpecialityFrontList(SpecialityItemParam params);


}
