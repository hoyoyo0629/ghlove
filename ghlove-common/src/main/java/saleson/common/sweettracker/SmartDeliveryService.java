package saleson.common.sweettracker;

import java.util.Map;

import saleson.common.sweettracker.dto.DeliveryRequest;

public interface SmartDeliveryService {

	public Map<String, Object> delivery(DeliveryRequest req);


}
