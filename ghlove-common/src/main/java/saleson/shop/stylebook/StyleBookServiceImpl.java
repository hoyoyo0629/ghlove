package saleson.shop.stylebook;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import com.querydsl.core.types.Predicate;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.ShopUtils;
import saleson.model.stylebook.StyleBook;
import saleson.model.stylebook.StyleBookItem;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.support.ItemParam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("styleBookService")
public class StyleBookServiceImpl extends EgovAbstractServiceImpl implements StyleBookService{

    private static final Logger log = LoggerFactory.getLogger(StyleBookServiceImpl.class);

    @Autowired
    private StyleBookRepository styleBookRepository;

    @Autowired
    private StyleBookItemRepository styleBookItemRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileStorage fileStorage;

    @Autowired
    private ItemService itemService;

    @Override
    public void insertStyleBook(StyleBook styleBook) throws IOException {

        styleBook.setImage(saveImageFile(styleBook));

        setStyleBookItem(styleBook);

        styleBookRepository.save(styleBook);
    }

    @Override
    public void updateStyleBook(StyleBook styleBook) throws IOException {

        StyleBook storeStyleBook = getStyleBookById(styleBook.getId());

        if (storeStyleBook != null) {

            storeStyleBook.setTitle(styleBook.getTitle());
            storeStyleBook.setContent(styleBook.getContent());
            storeStyleBook.setDisplayItemIds(styleBook.getDisplayItemIds());
            storeStyleBook.setImageFile(styleBook.getImageFile());

            String filePath = saveImageFile(storeStyleBook);

            if (!ObjectUtils.isEmpty(filePath)) {
                storeStyleBook.setImage(filePath);
            }

            setStyleBookItem(storeStyleBook);

            styleBookRepository.save(storeStyleBook);

        }
    }

    private void setStyleBookItem(StyleBook styleBook) {

        List<Integer> itemIds = styleBook.getDisplayItemIds();
        List<StyleBookItem> items = styleBook.getItems();

        List<StyleBookItem> saveItems = new ArrayList<>();

        // 기존 상품 정보 삭제
        if (items != null && !items.isEmpty()) {
            styleBookItemRepository.deleteAll(items);
        }
        // 상품 정보 재 등록
        if (itemIds != null && !itemIds.isEmpty()) {
            int ordering = 0;
            for (Integer id : itemIds) {

                if (id == null) {
                    continue;
                }

                saveItems.add(new StyleBookItem(id, ordering));
                ordering++;
            }

            styleBook.setItems(saveItems);
        }
    }

    private String saveImageFile(StyleBook styleBook) throws IOException {

        String filePath = "";
        MultipartFile imageFile = styleBook.getImageFile();
        String orgImage = styleBook.getImage();

        if (imageFile != null && !imageFile.isEmpty() && imageFile.getSize() > 0) {

            String extension = FileUtils.getExtension(imageFile.getOriginalFilename());
            int maxSize = 5 * 1024 * 1024; // 업로드 가능한 최대 용량 : 5MB

            String[] AVAILABLE_EXTENSION = { "jpg", "jpeg", "png" };

            boolean extenstionFlag = false;

            for (int i = 0 ; i < AVAILABLE_EXTENSION.length ; i++) {
                if (extension.equals(AVAILABLE_EXTENSION[i])) {
                    extenstionFlag = true;
                }
            }

            if (!extenstionFlag) {
                throw new IOException("유효하지 않은 파일입니다.");
            }

            if (maxSize < imageFile.getSize()) {
                throw new IOException("업로드 가능한 최대 용량 : 5MB 입니다");
            }

            String uploadPath = styleBook.getUploadPath();

            fileService.makeUploadPath(uploadPath);

            String defaultFileName = fileStorage.getNewFileName(imageFile.getOriginalFilename());
            String newFileName = FileUtils.getNewFileName(uploadPath, defaultFileName);

            File saveFile = new File(uploadPath + File.separator + newFileName);

            fileStorage.upload(imageFile.getInputStream(), saveFile);

            filePath = saveFile.getName();

            // 기존 파일 삭제
            fileStorage.delete(uploadPath, ShopUtils.unescapeHtml(orgImage));
        }

        return filePath;
    }

    @Override
    public void deleteStyleBookById(long id) throws IOException {

        StyleBook styleBook = getStyleBookById(id);

        if (styleBook != null) {

            List<StyleBookItem> items = styleBook.getItems();

            if (items != null && !items.isEmpty()) {
                styleBookItemRepository.deleteAll(items);
            }

            styleBookRepository.deleteById(id);
        }
    }

    @Override
    public Page<StyleBook> getStyleBookList(Predicate predicate, Pageable pageable) {
        return styleBookRepository.findAll(predicate, pageable);
    }

    @Override
    public StyleBook getStyleBookById(long id) {

        StyleBook styleBook = styleBookRepository.findById(id)
                .orElse(null);

        setItemList(styleBook);

        return styleBook;
    }

    @Override
    public void setItemList(StyleBook styleBook) {

        if (styleBook != null) {
            List<StyleBookItem> items = styleBook.getItems();

            if (items != null && !items.isEmpty()) {
                ItemParam itemParam = new ItemParam();
                itemParam.setStyleBookId(styleBook.getId());

                List<Item> searchItemList = itemService.getItemList(itemParam);

                if (searchItemList != null && !searchItemList.isEmpty()) {

                    for (StyleBookItem styleBookItem : items) {

                        for (Item item : searchItemList) {

                            if (styleBookItem.getItemId() != null) {
                                int itemId = styleBookItem.getItemId().intValue();

                                if (itemId == item.getItemId()) {
                                    styleBookItem.setItem(item);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void deleteStyleBookByIds(ListParam listParam) {
        if (listParam.getId() == null) {
            throw new UserException("처리할 데이터가 없습니다.");
        }

        // 1. Paramter 설정.
        List<Long> ids = Arrays.stream(listParam.getId())
                .map(i -> Long.parseLong(i))
                .collect(Collectors.toList());

        // 2. 삭제 대상 조회
        List<StyleBook> faqList = styleBookRepository.findAllById(ids);

        // 3. 삭제
        styleBookRepository.deleteAll(faqList);
    }
}
