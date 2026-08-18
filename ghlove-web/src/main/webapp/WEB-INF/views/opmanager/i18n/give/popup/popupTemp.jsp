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
			<h1 class="popup_title">[안내] 지자체 담당자분들 필독</h1>
			<a href="javascript:self.close();" class="btn_close"><img
				src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
			<div style="padding:15px 30px 0px; line-height:1.5em; text-align:left; border-top:1px solid #dfdfdf; font-size: 11pt">
              	<p>현재 옵션 등록되어있는 상품이 모두 미승인 상태로 전환되어</p>
              	<p>대국민 몰에서는 미노출 상태입니다.</p>
              	<p>따라서 답례품제공자분들께서 등록하신 옵션을 모두 확인하실수 있도록</p>
              	<p>지자체 담당자분들께서는 해당 답례품 제공자분들께 안내를 해주시기 바랍니다.</p>
              	<p>해당 사항은 운영사업단 담당자들이 전화를 드리고 있습니다.</p>
				<br>
				<p>옵션 및 해당 사항목록은 커뮤니티 사이트>자료실>공지사항>공지 2차 오픈 관련 옵션 조정이 필요한 답례품 리스트 및 옵션설정 가이드</p>
              	<p>항목에서 옵션상품 리스트 및 옵션설정가이드를 다운받으시기 바랍니다.</p> <br>
              	<p><strong>1. 답례품 옵션 상품 리스트</strong></p>
              	<p>- 수정해야될 답례품제공자 및 상품 리스트</p>
              	<p>- 필히 해당 목록을 확인하시기 바랍니다.</p><br>
              	<p><strong>2. 답례품 옵션 설정 가이드</strong></p>
              	<p>- 해당 가이드를 확인하시고 진행하시기 바랍니다.</p>
			</div>
		</div>

		<p class="popup_btns">
			<button type="button" class="btn btn-active" onclick="self.close()">닫기</button>
		</p>
	</div>
</div>
<script type="text/javascript"></script>