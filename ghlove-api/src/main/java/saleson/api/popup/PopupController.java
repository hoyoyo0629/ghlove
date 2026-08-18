package saleson.api.popup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onlinepowers.framework.exception.OpRuntimeException;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.shop.popup.PopupService;
import saleson.shop.popup.domain.Popup;

import java.util.List;


@RestController("ApiPopupController")
@RequestMapping("/api/popup")
public class PopupController {
    private static final Logger log = LoggerFactory.getLogger(PopupController.class);

    @Autowired
    PopupService popupService;

    /**
     * 팝업 리스트
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity popup() {
        ResponseEntity result = null;

        try {
            List<Popup> list = popupService.displayPopupList();
            result = ApiResponseEntity.data().list(list).ok();
        } catch (OpRuntimeException e) {
            log.error("[/api/popup/list] ERROR : {}", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 팝업 조회
     * @param id
     * @return
     */
    @GetMapping("/index/{id}")
    public ResponseEntity index (@PathVariable int id) {
        ResponseEntity result = null;

        try {
            Popup content = popupService.getPopup(id);
            result = ApiResponseEntity.data().put("content", content).ok();
        } catch (OpRuntimeException e) {
            log.error("[/api/popup/index/{}] ERROR : {}", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }
}
