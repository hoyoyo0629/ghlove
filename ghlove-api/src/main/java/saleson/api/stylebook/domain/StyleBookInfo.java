package saleson.api.stylebook.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.api.display.domain.ItemList;
import saleson.model.stylebook.StyleBook;
import saleson.model.stylebook.StyleBookItem;
import saleson.shop.item.domain.Item;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StyleBookInfo {

    private Long id;
    private String title;
    private String content;
    private List<ItemList> items;
    private String image;

    public StyleBookInfo(StyleBook styleBook) {

        if (styleBook != null) {

            setId(styleBook.getId());
            setTitle(styleBook.getTitle());
            setContent(styleBook.getContent());
            setItems(getItemLists(styleBook.getItems()));
            setImage(styleBook.getImageSrc());

        }
    }

    private List<ItemList> getItemLists(List<StyleBookItem> styleBookItems) {
        List<ItemList> itemLists = new ArrayList<>();

        for (StyleBookItem si : styleBookItems) {
            Item item = si.getItem();
            if (item != null) {
                itemLists.add(new ItemList(item));
            }
        }

        return itemLists;
    }
}
