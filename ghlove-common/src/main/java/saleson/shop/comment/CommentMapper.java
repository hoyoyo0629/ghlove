
package saleson.shop.comment;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.comment.dto.CommentDto;

@Mapper("commentMapper")
interface CommentMapper {
	
	int getCommentCount(int id);
	
	void insertComment(CommentDto commentDto);
	
	List<CommentDto> getCommentList(int id);
	
	CommentDto getComment(int id);
	
	void updateComment(CommentDto commentDto);
	
	void deleteComment(long id);

}
