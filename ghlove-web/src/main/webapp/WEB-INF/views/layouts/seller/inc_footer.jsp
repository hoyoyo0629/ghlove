<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<c:if test="${main != 'main'}">
			</div>
			</div>
		</c:if>
		</div>

		<%@ include file="/WEB-INF/tags/seller/login-footer.tag" %>
		<script>
			//조회조건 날짜설정 버튼 클릭시 음영처리
			$('.day_btns a.btn_date').click(function () {
				$(this).addClass('on').siblings().removeClass('on');
			});
		</script>
