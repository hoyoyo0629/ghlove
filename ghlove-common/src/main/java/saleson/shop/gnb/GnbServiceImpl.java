package saleson.shop.gnb;

import com.onlinepowers.framework.web.domain.ListParam;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ModelUtils;
import saleson.model.Gnb;
import saleson.shop.gnb.support.GnbCriteria;
import saleson.shop.gnb.support.GnbDto;

import java.util.ArrayList;
import java.util.List;

@Service("gnbService")
@RequiredArgsConstructor
public class GnbServiceImpl extends EgovAbstractServiceImpl implements GnbService{

    private final GnbRepository gnbRepository;

    @Override
    public void insertGnb(Gnb gnb) {
        Long count = CommonUtils.longNvl(gnbRepository.count());
        gnb.setOrdering(count.intValue() + 1);
        gnbRepository.save(gnb);
    }

    @Override
    public void updateGnb(long id, Gnb gnb) {

        Gnb store = getGnbById(id);
        if (store != null) {
            store.update(gnb);
        }
        gnbRepository.save(store);
    }

    @Override
    public void deleteGnb(ListParam listParam){

        if (listParam.getId() == null) {
            throw new NullPointerException("처리할 데이터가 없습니다.");
        }

        List<Long> ids = ModelUtils.getIds(listParam.getId());

        for(Long id : ids){
            Gnb gnb = getGnbById(id);
            if (gnb != null) {
                gnbRepository.deleteById(id);
            }
        }
    }

    @Override
    public Page<Gnb> getGnbList(Predicate predicate, Pageable pageable) {
        return gnbRepository.findAll(predicate, pageable);
    }

    @Override
    public Gnb getGnbById(long id) {
        return gnbRepository.findById(id).orElse(null);
    }

    public List<GnbDto> getFrontGnbList() {

        List<GnbDto> list = new ArrayList<>();

        GnbCriteria criteria = new GnbCriteria();
        criteria.setSearchDisplayFlag("Y");
        Iterable<Gnb> iterable = gnbRepository.findAll(criteria.getPredicate(), Sort.by("ordering").ascending());

        if (iterable != null) {
            iterable.forEach(gnb -> {
                list.add(new GnbDto(gnb));
            });
        }

        return list;
    }

    @Override
    public void updateGnbOrdering(ListParam listParam, int startOrdering) {
        if (listParam.getId() == null) {
            throw new NullPointerException("처리할 데이터가 없습니다.");
        }

        List<Long> ids = ModelUtils.getIds(listParam.getId());
        int ordering = startOrdering;

        for(Long id : ids){
            Gnb gnb = getGnbById(id);
            if (gnb != null) {
                gnb.setOrdering(ordering);
                gnbRepository.save(gnb);
                ordering++;
            }
        }

    }
}
