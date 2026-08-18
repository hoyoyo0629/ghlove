package saleson.shop.log;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.JsonViewUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.log.domain.ChangeLog;
import saleson.shop.log.support.ChangeTypeEnum;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;

@Service("changeLogService")
public class ChangeLogServiceImpl extends EgovAbstractServiceImpl implements ChangeLogService {

    @Autowired
    private ChangeLogMapper changeLogMapper;


    @Override
    public void insertUserChangeLog(long userId, HttpServletRequest request) {

        long managerId;

        if (UserUtils.isManagerLogin()) {
            managerId = UserUtils.getManagerId();
        } else {
            managerId = 0;
        }

        changeLogMapper
                .insertUserChangeLog(
                        new ChangeLog.Builder(managerId, getParameterByJson(request),
                                CommonUtils.getClientIp(request)).setUserId(userId).build());
    }

    @Override
    public void insertManagerChangeLog(HttpServletRequest request, User user, ChangeTypeEnum type) {

        ChangeLog changeLog = null;

        if (user.getUserId() > 0) {
            changeLog = new ChangeLog.Builder(UserUtils.getManagerId(),
                                                        getParameterByJson(request),
                                                        CommonUtils.getClientIp(request)
                                                ).setUserId(user.getUserId()).setChangeType(type).build();
        } else {
            changeLog = new ChangeLog.Builder(UserUtils.getManagerId(),
                                                        getParameterByJson(request),
                                                        CommonUtils.getClientIp(request)
                                                ).setChangeType(type).build();
        }

        changeLogMapper.insertManagerChangeLog(changeLog);

    }

    private String getParameterByJson(HttpServletRequest request) {

        Enumeration<?> paramNames = request.getParameterNames();

        LinkedHashMap<String, Object> paramValueMap = new LinkedHashMap<>();

        while (paramNames.hasMoreElements()) {

            String paramName = (String) paramNames.nextElement();
            String[] parameterValues = request.getParameterValues(paramName);

            if ("id".equals(paramName)) {
                paramName = "userId";
            }

            if (paramName.startsWith("password")
                    || paramName.startsWith("changePassword")
                    || paramName.startsWith("reChangePassword")) {

            } else {

                if (parameterValues.length == 1) {
                    paramValueMap.put(paramName, parameterValues[0]);
                } else {
                    paramValueMap.put(paramName, parameterValues);
                }
            }

        }

        return JsonViewUtils.objectToJson(paramValueMap);
    }

}
