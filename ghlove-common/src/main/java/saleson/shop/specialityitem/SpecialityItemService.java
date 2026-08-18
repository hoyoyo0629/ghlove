package saleson.shop.specialityitem;

import java.util.List;

import saleson.shop.specialityitem.domain.SpecialityFrontDomain;
import saleson.shop.specialityitem.domain.SpecialityItem;
import saleson.shop.specialityitem.domain.SpecialityItemManage;
import saleson.shop.specialityitem.support.SpecialityItemParam;

public interface SpecialityItemService {

	/**
	 * 특산물관 관리 정보 조회
	 * @param params
	 * @return List<SpecialityItemManage>
	 */
	List<SpecialityItemManage> getSpecialityItemManageList(SpecialityItemParam params);

	/**
	 * 특산물관 관리 정보 저장
	 * @param specialityItemParam
	 * @return SpecialityItemManage
	 */
	SpecialityItemManage insertSpecialityItemManage(SpecialityItemParam specialityItemParam);

	/**
	 * 특산물관 관리 정보 수정
	 * @param specialityItemManage
	 * @return int
	 * @throws RuntimeException
	 */
	int updateSpecialityItemManage(SpecialityItemManage specialityItemManage) throws RuntimeException;

	/**
	 * 특산물관 관리 상품 정보 조회
	 * @param SpecialityItemParam
	 * @return List<SpecialityItem>
	 */
	List<SpecialityItem> getSpecialityItemListByParam(SpecialityItemParam specialityItemParam);

	/**
	 * 특산물관 관리 정보 갯수 조회
	 * @param
	 * @return long
	 */
	long getSpecialityItemManageCount();

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
