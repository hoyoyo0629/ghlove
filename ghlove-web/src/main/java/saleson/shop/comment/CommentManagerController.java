package saleson.shop.comment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.common.utils.UserUtils;
import saleson.shop.comment.dto.CommentDto;
import saleson.shop.community.freeboard.CommunityManagerController;
import saleson.shop.databoard.support.DataboardParam;

@Controller
@RequestMapping("/opmanager/community/comment")
public class CommentManagerController {

	private static final Logger log = LoggerFactory.getLogger(CommentManagerController.class);

	@Autowired
	private CommentService commentService;

    @Autowired
    private SequenceService sequenceService;


	@GetMapping("count/{id}")
	public ResponseEntity<Integer> getCommentCount(RequestContext requestContext, Model model, @PathVariable int id, @ModelAttribute("searchParam")  CommentDto commentDto) {
		commentDto.setBoardId(id);
		int count = commentService.getCommentCount(id);

		return ResponseEntity.ok(count);

	}

	@GetMapping("list/{id}")
	public ResponseEntity<List<CommentDto>> getCommentList(RequestContext requestContext, Model model, @PathVariable int id) {
		List<CommentDto> comment = commentService.getCommentList(id);
		return new ResponseEntity<>(comment,HttpStatus.OK);
	}



	@PostMapping("create/{id}")
	public JsonView  createCommnet(RequestContext requestContext, Model model, @PathVariable("id") int Id, @ModelAttribute("searchParam")  CommentDto commentDto) {
		long commentId = sequenceService.getLong("OP_COMMUNITY_COMMENT");
		long user = UserUtils.getUser().getUserId();

		commentDto.setUserId(user);
		commentDto.setBoardCommentId(commentId);
		commentDto.setBoardId(Id);
		try {
			commentService.insertComment(commentDto);
		} catch (RuntimeException e) {
			return JsonViewUtils.failure("댓글 등록 실패");
		} catch (Exception e) {
			return JsonViewUtils.failure("댓글 등록 실패");
		}
		return JsonViewUtils.success("댓글생성 완료");

	}

	@PostMapping("update/{id}")
	public JsonView  updateCommnet(RequestContext requestContext, Model model, @PathVariable("id") int Id, @ModelAttribute("searchParam")  CommentDto commentDto) {

		long user = UserUtils.getUser().getUserId();


		commentDto.setUserId(user);
		commentDto.setBoardCommentId(Id);

		try {

			commentService.updateComment(commentDto);
		} catch (RuntimeException e) {
			return JsonViewUtils.failure("댓글 수정 실패");
		} catch (Exception e) {

			return JsonViewUtils.failure("댓글 수정 실패");
		}
		return JsonViewUtils.success("댓글수정 완료");

	}

	@PostMapping("delete/{id}")
	public JsonView deleteComment(RequestContext requestContext, Model model, @PathVariable("id") int Id, @ModelAttribute("searchParam")  CommentDto commentDto) {


		long commentid = commentDto.getBoardCommentId();

		try {

			commentService.deleteComment(commentid);


		} catch (RuntimeException e) {
			return JsonViewUtils.failure("댓글 삭제 실패");
		} catch (Exception e) {

			return JsonViewUtils.failure("댓글 삭제 실패");
		}
		return JsonViewUtils.success("삭제 완료 완료");
	}


}
