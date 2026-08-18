package saleson.shop.user;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.user.domain.LocgovItemImage;

@Mapper("locgovImageMapper")
public interface LocgovImageMapper {

	/**
	 * 지자체별 답례품몰 배경 이미지 조회
	 * @param locgovCode
	 * @return
	 */
	LocgovItemImage getLocgovImageInfo(String locgovCode);

	/**
	 * 지자체별 답례품몰 배경 이미지 추가
	 * @param locgovItemImage
	 * @return
	 */
	int insertLocgovImageInfo(LocgovItemImage locgovItemImage);

	/**
	 * 지자체별 답례품몰 배경 이미지 수정
	 * @param searchParam
	 * @return
	 */
	int updateLocgovImageInfo(LocgovItemImage locgovItemImage);

	/**
	 * 지자체별 답례품몰 배경 이미지 삭제
	 * @param searchParam
	 * @return
	 */
	int deleteLocgovImageInfo(String locgovCode);
	
	
}