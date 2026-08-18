package saleson.shop.config;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saleson.common.enumeration.LogType;
import saleson.shop.config.domain.ConfigLog;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigLogService extends EgovAbstractServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(ConfigLogService.class);

    @Autowired
    private ConfigLogRepository configLogRepository;

    public List<ConfigLog> findConfigLogs() {
        return configLogRepository.findAll();
    }

    public List<ConfigLog> findConfigLogsByLogType (LogType logType) {
        return configLogRepository.findConfigLogsByLogType(logType);
    }

    public void updateAllConfigLogs (List<ConfigLog> list) {
        configLogRepository.saveAll(list);
    }

    public void insertConfigLog (ConfigLog configLog) {
        configLogRepository.save(configLog);
    }

    public boolean isUsedConfigLogs (LogType logType, String key) {
        try {
            Optional<ConfigLog> optional = configLogRepository.findConfigLogByLogTypeAndKey(logType, key);
            return optional.isPresent() && optional.get().isUsed();
        } catch (RuntimeException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: isUsedConfigLogs RuntimeException ============");
            return false;
        }
    }
}
