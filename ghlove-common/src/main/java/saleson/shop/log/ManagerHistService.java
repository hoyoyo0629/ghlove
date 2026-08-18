package saleson.shop.log;

public interface ManagerHistService {

	/**
	 * 관리자 추가 및 수정 이력 등록
	 * @param managerHist (Object)
	 * @return
	 */
	<T> int insertManagerHist(T histParam, String type);

}
