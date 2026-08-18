package saleson.api.stylebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.OpRuntimeException;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.stylebook.domain.StyleBookInfo;
import saleson.model.stylebook.StyleBook;
import saleson.shop.stylebook.StyleBookService;
import saleson.shop.stylebook.support.StyleBookDto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController("ApiStyleBookController")
@RequestMapping("/api/style-book")
public class StyleBookController {

    private static final Logger log = LoggerFactory.getLogger(StyleBookController.class);

    @Autowired
    private StyleBookService styleBookService;

    @GetMapping("")
    public ResponseEntity list(StyleBookDto styleBookDto,
                               @PageableDefault(sort="id", direction= Sort.Direction.DESC) Pageable pageable) {

        ResponseEntity result = null;

        try {

            int size = 10;

            styleBookDto.setSize(size);

            Page<StyleBook> pageContent = styleBookService.getStyleBookList(styleBookDto.getPredicate(), pageable);

            Map<String, Object> map = new LinkedHashMap<>();

            List<StyleBook> styleBooks = pageContent.getContent();
            List<StyleBookInfo> list = new ArrayList<>();

            if (styleBooks != null && !styleBooks.isEmpty()) {

                for (StyleBook s : styleBooks) {
                    styleBookService.setItemList(s);

                    list.add(new StyleBookInfo(s));
                }
            }

            map.put("list", list);
            map.put("last", pageContent.isLast());

            result = ApiResponseEntity.data().map(map).ok();

        } catch (OpRuntimeException e) {
            //log.error("[/api/style-book] ERROR : {}", e.getMessage(), e);
            log.error("ERROR-46: style-book 목록 return 실패", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable("id") long id) {

        ResponseEntity result = null;

        try {

            StyleBook styleBook = styleBookService.getStyleBookById(id);

            Map<String, Object> map = new LinkedHashMap<>();
            StyleBookInfo styleBookInfo = null;
            if (styleBook != null) {
                styleBookInfo = new StyleBookInfo(styleBook);
            }

            map.put("detail", styleBookInfo);

            result = ApiResponseEntity.data().map(map).ok();

        } catch (OpRuntimeException e) {
            //log.error("[/api/style-book/"+id+"] ERROR : {}", e.getMessage(), e);
            log.error("ERROR-47: style-book 상세정보 return 실패", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }


}
