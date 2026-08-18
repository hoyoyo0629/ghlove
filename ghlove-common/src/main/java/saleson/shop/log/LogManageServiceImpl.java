package saleson.shop.log;

import com.onlinepowers.framework.isms.ConfigIsmsService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import saleson.common.Const;

@Service("logManageService")
public class LogManageServiceImpl extends EgovAbstractServiceImpl implements LogManageService {

    private static final Logger log = LoggerFactory.getLogger(LogManageServiceImpl.class);

    @Autowired
    private LogManageMapper logManageMapper;

    @Autowired
    private ConfigIsmsService ismsService;

    @Override
    public boolean getTodayLoginCheck() {

        boolean flag;

        int check = logManageMapper.getTodayLogCount();

        if (check > 0) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

    @Override
    public void logManage() {
        insertTodayLog();
        deleteLog();
    }

    private void insertTodayLog() {
        logManageMapper.insertTodayLog();
    }

    private void deleteLog() {

        String date = getDeleteYearDate("LIFE_TIME_MANAGER_ACTION_LOG");
        if (!ObjectUtils.isEmpty(date)) {
            logManageMapper.deleteManagerActionLog(date);
        }

        date = getDeleteYearDate("LIFE_TIME_QUERY_LOG");
        if (!ObjectUtils.isEmpty(date)) {
            logManageMapper.deleteQueryLog(date);
        }

        date = getDeleteYearDate("LIFE_TIME_MANAGER_CHANGE_LOG");
        if (!ObjectUtils.isEmpty(date)) {
            logManageMapper.deleteManagerChangeLog(date);
        }

        date = getDeleteYearDate("LIFE_TIME_USER_CHANGE_LOG");
        if (!ObjectUtils.isEmpty(date)) {
            logManageMapper.deleteUserChangeLog(date);
        }

        date = getDeleteMonthDate("LIFE_TIME_USER_LOGIN_LOG");
        if (!ObjectUtils.isEmpty(date)) {
            logManageMapper.deleteUserLoginLog(date);
        }

        date = getDeleteMonthDate("LIFE_TIME_LOGIN_LOG");
        if (!ObjectUtils.isEmpty(date)) {
            logManageMapper.deleteLoginLog(date);
        }

    }

    private String getDeleteYearDate(String key) {

        if (ObjectUtils.isEmpty(key)) {
            return "";
        }

        String value = ismsService.getIsmsConfigValueByKey(key);

        if (ObjectUtils.isEmpty(value)) {
            return "";
        }

        String date = "";
        try {
            int year = Integer.parseInt(value);
            date = DateUtils.addYear(getToday(), -year)+"235959";
        } catch (NumberFormatException ignore) {
            log.error("getDeleteYearDate Error [{}]", key, ignore);
        }

        return date;
    }

    private String getDeleteMonthDate(String key) {

        if (ObjectUtils.isEmpty(key)) {
            return "";
        }

        String value = ismsService.getIsmsConfigValueByKey(key);

        if (ObjectUtils.isEmpty(value)) {
            return "";
        }

        String date = "";
        try {
            int month = Integer.parseInt(value);
            date = DateUtils.addMonth(getToday(), -month)+"235959";
        } catch (NumberFormatException ignore) {
            log.error("getDeleteYearDate Error [{}]", key);
        }

        return date;
    }

    private String getToday() {
        return DateUtils.getToday(Const.DATE_FORMAT);
    }
}