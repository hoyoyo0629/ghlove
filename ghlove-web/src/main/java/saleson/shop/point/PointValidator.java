package saleson.shop.point;

import org.springframework.stereotype.Component;
import saleson.shop.point.domain.Point;

@Component
public class PointValidator {

    public void validate(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("포인트 정보가 없습니다.");
        }

        if (point.getPoint() < 0) {
            throw new IllegalArgumentException("포인트가 음수 입니다.");
        }
    }

}
