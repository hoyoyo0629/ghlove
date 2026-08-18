package saleson.shop.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.comment.dto.CommentDto;
import saleson.shop.community.locgovdataboard.LocgovDataBoardMapper;


@Service("commentService")
public class CommentServiceImple implements CommentService {
	
	@Autowired
	private CommentMapper commentMapper;
	/**
	 * <pre>
	 * author    : csh
	 * since     : 2023. 9. 14.
	 * comment   : 댓글 카운트
	 * </pre>
	 */
	@Override
	public int getCommentCount(int id) {
		// TODO Auto-generated method stub
		return commentMapper.getCommentCount(id);
	}
	/**
	 * <pre>
	 * author    : csh
	 * since     : 2023. 9. 14.
	 * comment   : 댓글 생성
	 * </pre>
	 */
	@Override
	public void insertComment(CommentDto commentDto) {
		// TODO Auto-generated method stub
	 commentMapper.insertComment(commentDto);
	}
	/**
	 * <pre>
	 * author    : csh
	 * since     : 2023. 9. 14.
	 * comment   : 댓글목록 가져오기
	 * </pre>
	 */
	@Override
	public List<CommentDto> getCommentList(int id) {
		// TODO Auto-generated method stub
		return commentMapper.getCommentList(id);
	}
	/**
	 * <pre>
	 * author    : csh
	 * since     : 2023. 9. 14.
	 * comment   : 댓글 업데이트
	 * </pre>
	 */
	@Override
	public void updateComment(CommentDto commentDto) {
		commentMapper.updateComment(commentDto);

	}
	/**
	 * <pre>
	 * author    : csh
	 * since     : 2023. 9. 14.
	 * comment   : 댓글 삭제
	 * </pre>
	 */

	@Override
	public void deleteComment(long Id) {
		commentMapper.deleteComment(Id);

	}
	@Override
	public CommentDto getComment(int id) {
		// TODO Auto-generated method stub
		return commentMapper.getComment(id);
	}

}
