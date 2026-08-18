package saleson.shop.banner;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.banner.domain.MainBannerManager;
import saleson.shop.banner.support.MainBannerManagerSearchParam;

@Mapper("mainBannerMapper")
public interface MainBannerMapper {

	/**
	 * 메인 배너 등록
	 * @param mainBannerManager
	 * @return
	 */
	int insertMainBanner(MainBannerManager mainBannerManager);

	/**
	 * 메인 배너 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<MainBannerManager> getMainBannerList(MainBannerManagerSearchParam searchParam);

	/**
	 * 메인 배너 순서 변경
	 * @param param
	 * @return
	 */
	int updateDisplayOrder(MainBannerManager param);

	/**
	 * 메인 배너 상세 조회
	 * @param bannerId
	 * @return
	 */
	MainBannerManager getMainBannerDetails(Integer bannerId);

	/**
	 * 메인 배너 논리적 파일 삭제
	 * @param mainBannerManager
	 * @return
	 */
	int updateMainBannerFileDel(MainBannerManager mainBannerManager);

	/**
	 * 메인 배너 수정
	 * @param mainBannerManager
	 * @return
	 */
	int updateMainBanner(MainBannerManager mainBannerManager);
}
