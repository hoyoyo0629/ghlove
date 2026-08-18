package saleson.shop.comment.dto;

import lombok.Data;

@Data
public class CommentDto {
	private long boardCommentId; // 댓글 id
	private long parentCommentId; // 부모댓글 id
	private long boardId; // 게시판 id
	private String boardCode; // 게시판 코드
	private long userId; // 글쓴이
	private String userName; // 글쓴이 이름
	private String comments; // 댓글 내용
	private String idDelete; // 삭제여부 디폴트 N
	private String createdDate; // 생성날짜
	private String updatedDate; // 업데이트 날짜
	private String deletedDate; // 삭제 날짜
	private String myComment; // 내가쓴글
	private String locgovNm; //지차체
	private String commentDepth;
	
	
	
}
