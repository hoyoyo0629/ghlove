package saleson.shop.disposable;

import saleson.shop.disposable.domain.TempData;

public interface TempDataService {

	public TempData insertTempData(Object object);
	
	public Object getTempData(TempData tempData, Class<?> classObj);
	
	
}
