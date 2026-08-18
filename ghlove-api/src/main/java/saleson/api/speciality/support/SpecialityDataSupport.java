package saleson.api.speciality.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import saleson.api.speciality.domain.SpecialityDomain;
import saleson.shop.specialityitem.domain.SpecialityFrontDomain;

@Component
public class SpecialityDataSupport {

    public List<SpecialityDomain> bindList(List<SpecialityFrontDomain> list) {
        List<SpecialityDomain> resultList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (SpecialityFrontDomain item : list) {
                resultList.add(new SpecialityDomain(item.getSpclItemMngId(), item.getLocgovCode(), item.getLocgovNm(), item.getUpperLocgovNm(), item.getSpclItemInfo()));
            }
        }
        return resultList;
    }
}
