package saleson.common.alimtalk;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("alimtalkMapper")
public interface AlimtalkMapper {

	void insertTifAlimtalkInfo(Alimtalk alimtalk);

}
