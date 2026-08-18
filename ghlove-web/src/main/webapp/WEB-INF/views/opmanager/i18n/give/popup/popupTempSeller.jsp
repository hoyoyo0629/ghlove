<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div class="popup_wrap">
		<div id="pop_header">
			<h1 class="popup_title">[안내] 답례품제공자 분들 필독</h1>
			<a href="javascript:self.close();" class="btn_close"><img
				src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
			<div style="padding:15px 30px 0px; line-height:1.5em; text-align:left; border-top:1px solid #dfdfdf; font-size: 11pt">
              	<p>현재 설정하신 옵션값이 잘못 등록되어있습니다.</p>
              	<p>따라서 답례품 등록 시 옵션설정 하신 분들은</p>
              	<p>옵션설정을 재설정하셔야 됩니다.</p>
              	<p>현재 등록하신 상품들은 모두 승인 대기상태로 변경되어</p>
              	<p>상품이 미노출 되고 있습니다.</p>
				<br>
				<p>따라서 지자체 담당자분들께서 전화를 드리도록 하겠습니다.</p> <br>
              	<p>옵션을 설정하지 않으신 분들은 기존대로 노출됩니다.</p> <br>
              	<p>감사합니다.</p>
			</div>
		</div>

		<p class="popup_btns">
			<button type="button" class="btn btn-active" onclick="self.close()">닫기</button>
		</p>
	</div>
</div>
<script type="text/javascript"></script>