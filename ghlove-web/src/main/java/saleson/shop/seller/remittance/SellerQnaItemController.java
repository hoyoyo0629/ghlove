package saleson.shop.seller.remittance;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import saleson.common.utils.SellerUtils;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.qna.QnaItemManagerController;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.domain.QnaAnswer;
import saleson.shop.qna.support.QnaParam;

@Controller
@RequestMapping("/seller/qna-item")
@RequestProperty(title = "고객센터", template="seller", layout = "default")
public class SellerQnaItemController extends QnaItemManagerController {
	
	/**
	 * QNA ITEM
	 * @param qnaParam
	 * @param model
	 * @return
	 */
	@GetMapping("list")
	public String answerList(QnaParam qnaParam, Model model) {
		qnaParam.setSellerId(SellerUtils.getSellerId());
		return super.answerList(qnaParam, model);
	}
	
	@PostMapping("delete")
	public JsonView deleteListData(RequestContext requestContext, ListParam listParam) {
		return super.deleteListData(requestContext, listParam);
	}
	
	@GetMapping(value="/delete/{qnaId}")
	public String qnaDelete(@PathVariable("qnaId") int qnaId, RequestContext requestContext) {
		return super.qnaDelete(qnaId, requestContext);
	}
	
	// 상품 문의 상세 조회
	@GetMapping("/answer/{qnaId}")
	public String answerInsert(Qna qnaParam, @PathVariable("qnaId") int qnaId, Model model, RequestContext requestContext) {
		return super.answerInsert(qnaParam, qnaId, model, requestContext);
	}
	
	// 상품 문의 답변 등록/수정
	@PostMapping("/answer/{qnaId}")
	public String qnaAnswerAction(@PathVariable("qnaId") int qnaId, QnaAnswer qnaAnswer, 
			MailConfig mailConfig, Model model, QnaParam qnaParam, RequestContext requestContext) {
		return super.qnaAnswerAction(qnaId, qnaAnswer, mailConfig, model, qnaParam, requestContext);
	}
	
	// 상품문의 답변 삭제
	@GetMapping(value="/answer/delete/{qnaId}")
	public String answerDelete(@PathVariable("qnaId") int qnaId, RequestContext requestContext) {
		return super.answerDelete(qnaId, requestContext);
	}
}
