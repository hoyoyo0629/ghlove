package saleson.shop.log;


import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import saleson.shop.log.domain.PasswordLog;

import java.util.List;

@Mapper("passwordLogMapper")
public interface PasswordLogMapper {

    void insertManagerPasswordLog(PasswordLog passwordLog);

    List<String> getPasswordListById(PasswordLog passwordLog);

}
