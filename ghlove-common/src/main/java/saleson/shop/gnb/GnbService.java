package saleson.shop.gnb;

import com.onlinepowers.framework.web.domain.ListParam;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import saleson.model.Gnb;
import saleson.shop.gnb.support.GnbDto;

import java.util.List;

public interface GnbService {

    /**
     * GNB 등록
     * @param gnb
     */
    void insertGnb(Gnb gnb);

    /**
     * GNB 수정
     * @param id
     * @param gnb
     */
    void updateGnb(long id, Gnb gnb);

    /**
     * GNB 삭제
     * @param listParam
     */
    void deleteGnb(ListParam listParam);

    /**
     * GNB 목록 조회
     * @param predicate
     * @param pageable
     * @return
     */
    Page<Gnb> getGnbList(Predicate predicate, Pageable pageable);

    /**
     * 해당 GNB 조회
     * @param id
     * @return
     */
    Gnb getGnbById(long id);

    /**
     * 모든 GNB 조회
     * @return
     */
    List<GnbDto> getFrontGnbList();

    /**
     * GNB 순서 변경
     * @param listParam
     * @param startOrdering
     */
    void updateGnbOrdering(ListParam listParam, int startOrdering);
}
