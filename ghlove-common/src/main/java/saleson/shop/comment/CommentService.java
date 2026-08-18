package saleson.shop.comment;

import java.util.List;

import saleson.shop.comment.dto.CommentDto;

public interface CommentService {
	public int getCommentCount(int id);
	
	public void insertComment(CommentDto commentDto);
	
	public List<CommentDto> getCommentList(int id);
	
	public void updateComment(CommentDto commentDto);
	
	public void deleteComment(long Id);
	
	public CommentDto getComment(int id);
	
	
	
	

}
