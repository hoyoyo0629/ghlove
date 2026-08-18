package saleson.shop.campaign.statistics;

import saleson.common.notification.domain.UmsStatistics;
import saleson.common.notification.domain.UmsStatisticsTable;
import saleson.common.notification.support.StatisticsParam;
import saleson.model.campaign.*;

import java.util.List;

import com.onlinepowers.framework.exception.OpRuntimeException;

public interface CampaignStatisticsService {

    void updateCampaignSentBatch(String batchDate, Campaign campaign) throws OpRuntimeException;
    void updateCampaignSentBatchStep2(String batchDate) throws OpRuntimeException;
    void updateCampaignSentBatchStep3(String batchDate) throws OpRuntimeException;
    Campaign getAutoMonthCampaign(String batchDate);

    void insertSentForUser(String sentType, List<String> tables);

    void insertAutoSentForUser(String sentType, List<String> tables, Campaign campaign);


    UmsStatistics getUserList(StatisticsParam param);

    List<UmsStatisticsTable> getLogTables(String... months);
}
