package saleson.shop.reviewfilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.exception.OpRuntimeException;

import saleson.model.FilterCode;
import saleson.model.FilterGroup;
import saleson.model.review.ItemReviewFilter;
import saleson.model.review.ReviewFilter;
import saleson.shop.categories.CategoriesMapper;
import saleson.shop.categories.domain.Breadcrumb;
import saleson.shop.categories.domain.BreadcrumbCategory;
import saleson.shop.categories.domain.Categories;
import saleson.shop.categoriesfilter.FilterGroupRepository;
import saleson.shop.reviewfilter.support.ReviewFilterDto;

@Slf4j
@RequiredArgsConstructor
@Service("reviewFilterService")
public class ReviewFilterServiceImpl extends EgovAbstractServiceImpl implements ReviewFilterService {
    private final ReviewFilterRepository reviewFilterRepository;
    private final FilterGroupRepository filterGroupRepository;
    private final ItemReviewFilterRepository itemReviewFilterRepository;
    private final CategoriesMapper categoriesMapper;

    @Override
    public List<FilterGroup> getFilterGroupList(int categoryId) {

        ReviewFilterDto dto = new ReviewFilterDto();
        dto.setCategoryId(categoryId);

        Iterable<ReviewFilter> iterable = reviewFilterRepository.findAll(dto.getPredicate());

        List<Long> ids = new ArrayList<>();

        iterable.forEach(filter -> {
            ids.add(filter.getFilterGroupId());
        });

        List<FilterGroup> tempGroups = filterGroupRepository.findAllById(ids);
        List<FilterGroup> groups = new ArrayList<>();

        for (Long id : ids) {
            for (FilterGroup group: tempGroups) {
                if (id.equals(group.getId())) {
                    groups.add(group);
                    break;
                }
            }
        }

        return groups;
    }

    @Override
    public List<FilterGroup> getBreadcrumbFilterGroupList(int categoryId) {

        List<FilterGroup> filterGroups = new ArrayList<>();

        Categories categories = categoriesMapper.getCategoriesById(categoryId);

        if (categories == null ) {
            return new ArrayList<>();
        }

        List<Breadcrumb> breadcrumbs = categoriesMapper.getBreadcrumbListByCategoryUrl(categories.getCategoryUrl());

        if (breadcrumbs != null && !breadcrumbs.isEmpty()) {

            List<BreadcrumbCategory> breadcrumbCategories = breadcrumbs.get(0).getBreadcrumbCategories();

            if (breadcrumbCategories != null && !breadcrumbCategories.isEmpty()) {

                List<Long> addIdList = new ArrayList<>();

                for (BreadcrumbCategory breadcrumbCategory : breadcrumbCategories) {

                    try {
                        int id = Integer.parseInt(breadcrumbCategory.getCategoryId());
                        List<FilterGroup> tempList = getFilterGroupList(id);

                        tempList.stream().forEach(t ->{
                            if(!addIdList.contains(t.getId())) {

                                t.setCodeList(t.getCodeList().stream()
                                        .sorted(Comparator.comparing(FilterCode::getOrdering))
                                        .collect(Collectors.toList()));

                                filterGroups.add(t);
                                addIdList.add(t.getId());
                            }
                        });

                    } catch (OpRuntimeException ignore) {
                    	log.error(getClass().getName() + " getBreadcrumbFilterGroupList error", ignore);
                    }
                }
            }
        }

        return filterGroups;
    }

    @Override
    public void saveReviewFilter(int categoryId, Set<Long> filterGroupIdSet) {
        if (categoryId > 0) {

            boolean flag = filterGroupIdSet != null && !filterGroupIdSet.isEmpty();
            List<Long> keepFilterGroupIds = new ArrayList<>();

            if (flag) {

                List<FilterGroup> filterGroups = getFilterGroupList(categoryId);

                List<Long> filterGroupIds = new ArrayList<>();

                filterGroups.forEach( fg ->{
                    filterGroupIds.add(fg.getId());
                });


                for (Long id : filterGroupIdSet) {
                    boolean keepFlag = false;
                    for (Long searchId : filterGroupIds) {
                        if (searchId.equals(id)) {
                            keepFlag = true;
                        }
                    }

                    if (keepFlag) {
                        keepFilterGroupIds.add(id);
                    }
                }
            }

            deleteFilterByCategoryId(categoryId, keepFilterGroupIds);

            if (flag) {

                int ordering = 0;
                for (Long id : filterGroupIdSet) {
                    ReviewFilter reviewFilter = new ReviewFilter();
                    reviewFilter.setCategoryId(categoryId);
                    reviewFilter.setFilterGroupId(id);
                    reviewFilter.setOrdering(ordering);
                    ordering++;

                    reviewFilterRepository.save(reviewFilter);
                }
            }
        }
    }

    private void deleteFilterByCategoryId(int categoryId, List<Long> keepFilterGroupIds) {

        List<Breadcrumb> breadcrumbs = categoriesMapper.getBreadcrumbListByCategoryId(categoryId);
        List<BreadcrumbCategory> breadcrumbCategories = new ArrayList<>();
        if (breadcrumbs != null && !breadcrumbs.isEmpty()) {
            breadcrumbCategories = breadcrumbs.get(0).getBreadcrumbCategories();
        }

        for (BreadcrumbCategory breadcrumbCategory : breadcrumbCategories) {
            int breadcrumbCategoryId = Integer.parseInt(breadcrumbCategory.getCategoryId());
            if (breadcrumbCategoryId == categoryId) {
                continue;
            }

            List<FilterGroup> filterGroups = getFilterGroupList(breadcrumbCategoryId);
            List<Long> ids = new ArrayList<>();

            filterGroups.forEach(fg -> {
                Long id = fg.getId();
                if (!keepFilterGroupIds.contains(id)) {
                    ids.add(id);
                }
            });

            keepFilterGroupIds.addAll(ids);
        }

        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);

        ReviewFilterDto dto = new ReviewFilterDto();
        dto.setCategoryId(categoryId);

        Iterable<ReviewFilter> iterable = reviewFilterRepository.findAll(dto.getPredicate());

        reviewFilterRepository.deleteAll(iterable);
    }

    @Override
    public Iterable<ReviewFilter> getReviewFilters(ReviewFilterDto dto) {
        return reviewFilterRepository.findAll(dto.getPredicate());
    }

    @Override
    public void saveItemFilter(int itemReviewId, int itemId, Map<String, List<String>> filterCodes) {

        if (itemReviewId > 0) {

            if (filterCodes != null) {
                Iterator<String> iter = filterCodes.keySet().iterator();
                while(iter.hasNext()){
                    Long filterGroupId = Long.parseLong(iter.next());
                    List<String> filterCodeIds = (ArrayList<String>)filterCodes.get(Long.toString(filterGroupId));
                    int ordering = 0;
                    for(int i = 0; i < filterCodeIds.size(); i++){

                        ItemReviewFilter reviewFilter = new ItemReviewFilter();
                        reviewFilter.setItemReviewId(itemReviewId);
                        reviewFilter.setItemId(itemId);
                        reviewFilter.setFilterGroupId(filterGroupId);
                        reviewFilter.setFilterCodeId(Long.parseLong(filterCodeIds.get(i)));
                        reviewFilter.setOrdering(i);
                        itemReviewFilterRepository.save(reviewFilter);
                    }
                }
            }
        }
    }

    @Override
    public List<FilterGroup> getFilterGroupsByItemReviewFilters(List<ItemReviewFilter> filters) {

        if (filters != null && !filters.isEmpty()) {

            List<Long> ids = new ArrayList<>();

            filters.forEach(filter -> {
                ids.add(filter.getFilterGroupId());
            });

            return filterGroupRepository.findAllById(ids);

        }

        return new ArrayList<>();
    }
}
