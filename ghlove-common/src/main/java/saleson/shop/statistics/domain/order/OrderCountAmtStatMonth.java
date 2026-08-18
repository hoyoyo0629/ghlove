package saleson.shop.statistics.domain.order;

import java.util.ArrayList;
import java.util.List;

import com.onlinepowers.framework.util.StringUtils;

import lombok.Data;

@Data
public class OrderCountAmtStatMonth {
	
	private List<OrderCountAmtStatMonthLclgv> orderCountAmtStatMonthLclgvList;
	
	public void setOrderCountAmtStatMonthLclgvList(List<OrderCountAmtStatMonthLclgv> list) {
		if (list == null) {
			orderCountAmtStatMonthLclgvList = null;
		} else {
			orderCountAmtStatMonthLclgvList = new ArrayList<>();
			
			for (OrderCountAmtStatMonthLclgv orderCountAmtStatMonthLclgv : list) {
				if (orderCountAmtStatMonthLclgv != null) {
					orderCountAmtStatMonthLclgvList.add(orderCountAmtStatMonthLclgv.getCopyObj());
				}
			}
			
			int length = orderCountAmtStatMonthLclgvList.size();
			
			if (length > 0) {
				for (int i = length ; i > 0 ; i--) {			// 지자체별 소계 세팅
					OrderCountAmtStatMonthLclgv obj = orderCountAmtStatMonthLclgvList.get(i - 1);
					if (obj.getUpperLocgovCode().equals(obj.getLocgovCode())) {
						orderCountAmtStatMonthLclgvList.add(i - 1, getOrderCountAmtStatMonthUpLclgv(obj.getUpperLocgovCode()));
					}
				}
				orderCountAmtStatMonthLclgvList.add(0, getOrderCountAmtStatMonthUpLclgv(""));		// 총계 세팅
			}
		}
	}
	
	/**
	 * 상위 지자체 코드로 상위지자체 합산 정보 계산, 지자체 코드 값 없으면 전체 합산
	 */
	public OrderCountAmtStatMonthLclgv getOrderCountAmtStatMonthUpLclgv(String upLclgvCd) {
		OrderCountAmtStatMonthLclgv data = new OrderCountAmtStatMonthLclgv();
		if (orderCountAmtStatMonthLclgvList != null) {
			if (StringUtils.hasLength(upLclgvCd)) {
				for (OrderCountAmtStatMonthLclgv orderCountAmtStatMonthLclgv : orderCountAmtStatMonthLclgvList) {
					if (orderCountAmtStatMonthLclgv != null && upLclgvCd.equals(orderCountAmtStatMonthLclgv.getLocgovCode())) {
						data.setUpperLocgovCode(orderCountAmtStatMonthLclgv.getUpperLocgovCode());
						data.setUpperLocgovNm(orderCountAmtStatMonthLclgv.getUpperLocgovNm());
						data.setLocgovNm("소계");
						
						data.setNowYr1mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr1mmWholNtslAmt()
													+ data.getNowYr1mmWholNtslAmt());
						data.setNowYr2mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr2mmWholNtslAmt()
													+ data.getNowYr2mmWholNtslAmt());
						data.setNowYr3mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr3mmWholNtslAmt()
													+ data.getNowYr3mmWholNtslAmt());
						data.setNowYr4mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr4mmWholNtslAmt()
													+ data.getNowYr4mmWholNtslAmt());
						data.setNowYr5mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr5mmWholNtslAmt()
													+ data.getNowYr5mmWholNtslAmt());
						data.setNowYr6mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr6mmWholNtslAmt()
													+ data.getNowYr6mmWholNtslAmt());
						data.setNowYr7mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr7mmWholNtslAmt()
													+ data.getNowYr7mmWholNtslAmt());
						data.setNowYr8mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr8mmWholNtslAmt()
													+ data.getNowYr8mmWholNtslAmt());
						data.setNowYr9mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr9mmWholNtslAmt()
													+ data.getNowYr9mmWholNtslAmt());
						data.setNowYr10mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr10mmWholNtslAmt()
													+ data.getNowYr10mmWholNtslAmt());
						data.setNowYr11mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr11mmWholNtslAmt()
													+ data.getNowYr11mmWholNtslAmt());
						data.setNowYr12mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr12mmWholNtslAmt()
													+ data.getNowYr12mmWholNtslAmt());
						
						data.setNowYr1mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr1mmWholNtslNocs()
													+ data.getNowYr1mmWholNtslNocs());
						data.setNowYr2mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr2mmWholNtslNocs()
													+ data.getNowYr2mmWholNtslNocs());
						data.setNowYr3mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr3mmWholNtslNocs()
													+ data.getNowYr3mmWholNtslNocs());
						data.setNowYr4mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr4mmWholNtslNocs()
													+ data.getNowYr4mmWholNtslNocs());
						data.setNowYr5mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr5mmWholNtslNocs()
													+ data.getNowYr5mmWholNtslNocs());
						data.setNowYr6mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr6mmWholNtslNocs()
													+ data.getNowYr6mmWholNtslNocs());
						data.setNowYr7mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr7mmWholNtslNocs()
													+ data.getNowYr7mmWholNtslNocs());
						data.setNowYr8mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr8mmWholNtslNocs()
													+ data.getNowYr8mmWholNtslNocs());
						data.setNowYr9mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr9mmWholNtslNocs()
													+ data.getNowYr9mmWholNtslNocs());
						data.setNowYr10mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr10mmWholNtslNocs()
													+ data.getNowYr10mmWholNtslNocs());
						data.setNowYr11mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr11mmWholNtslNocs()
													+ data.getNowYr11mmWholNtslNocs());
						data.setNowYr12mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr12mmWholNtslNocs()
													+ data.getNowYr12mmWholNtslNocs());
						
						data.setBfrYr1mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr1mmWholNtslAmt()
													+ data.getBfrYr1mmWholNtslAmt());
						data.setBfrYr2mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr2mmWholNtslAmt()
													+ data.getBfrYr2mmWholNtslAmt());
						data.setBfrYr3mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr3mmWholNtslAmt()
													+ data.getBfrYr3mmWholNtslAmt());
						data.setBfrYr4mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr4mmWholNtslAmt()
													+ data.getBfrYr4mmWholNtslAmt());
						data.setBfrYr5mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr5mmWholNtslAmt()
													+ data.getBfrYr5mmWholNtslAmt());
						data.setBfrYr6mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr6mmWholNtslAmt()
													+ data.getBfrYr6mmWholNtslAmt());
						data.setBfrYr7mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr7mmWholNtslAmt()
													+ data.getBfrYr7mmWholNtslAmt());
						data.setBfrYr8mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr8mmWholNtslAmt()
													+ data.getBfrYr8mmWholNtslAmt());
						data.setBfrYr9mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr9mmWholNtslAmt()
													+ data.getBfrYr9mmWholNtslAmt());
						data.setBfrYr10mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr10mmWholNtslAmt()
													+ data.getBfrYr10mmWholNtslAmt());
						data.setBfrYr11mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr11mmWholNtslAmt()
													+ data.getBfrYr11mmWholNtslAmt());
						data.setBfrYr12mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr12mmWholNtslAmt()
													+ data.getBfrYr12mmWholNtslAmt());
						
						data.setBfrYr1mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr1mmWholNtslNocs()
													+ data.getBfrYr1mmWholNtslNocs());
						data.setBfrYr2mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr2mmWholNtslNocs()
													+ data.getBfrYr2mmWholNtslNocs());
						data.setBfrYr3mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr3mmWholNtslNocs()
													+ data.getBfrYr3mmWholNtslNocs());
						data.setBfrYr4mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr4mmWholNtslNocs()
													+ data.getBfrYr4mmWholNtslNocs());
						data.setBfrYr5mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr5mmWholNtslNocs()
													+ data.getBfrYr5mmWholNtslNocs());
						data.setBfrYr6mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr6mmWholNtslNocs()
													+ data.getBfrYr6mmWholNtslNocs());
						data.setBfrYr7mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr7mmWholNtslNocs()
													+ data.getBfrYr7mmWholNtslNocs());
						data.setBfrYr8mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr8mmWholNtslNocs()
													+ data.getBfrYr8mmWholNtslNocs());
						data.setBfrYr9mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr9mmWholNtslNocs()
													+ data.getBfrYr9mmWholNtslNocs());
						data.setBfrYr10mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr10mmWholNtslNocs()
													+ data.getBfrYr10mmWholNtslNocs());
						data.setBfrYr11mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr11mmWholNtslNocs()
													+ data.getBfrYr11mmWholNtslNocs());
						data.setBfrYr12mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr12mmWholNtslNocs()
													+ data.getBfrYr12mmWholNtslNocs());

						if (data.getBfrYr1mmWholNtslAmt() == 0) {
							data.setWholNtslAmt1mmRt(-1);
						} else {
							data.setWholNtslAmt1mmRt(Math.round(data.getNowYr1mmWholNtslAmt() * 100 / data.getBfrYr1mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr2mmWholNtslAmt() == 0) {
							data.setWholNtslAmt2mmRt(-1);
						} else {
							data.setWholNtslAmt2mmRt(Math.round(data.getNowYr2mmWholNtslAmt() * 100 / data.getBfrYr2mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr3mmWholNtslAmt() == 0) {
							data.setWholNtslAmt3mmRt(-1);
						} else {
							data.setWholNtslAmt3mmRt(Math.round(data.getNowYr3mmWholNtslAmt() * 100 / data.getBfrYr3mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr4mmWholNtslAmt() == 0) {
							data.setWholNtslAmt4mmRt(-1);
						} else {
							data.setWholNtslAmt4mmRt(Math.round(data.getNowYr4mmWholNtslAmt() * 100 / data.getBfrYr4mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr5mmWholNtslAmt() == 0) {
							data.setWholNtslAmt5mmRt(-1);
						} else {
							data.setWholNtslAmt5mmRt(Math.round(data.getNowYr5mmWholNtslAmt() * 100 / data.getBfrYr5mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr6mmWholNtslAmt() == 0) {
							data.setWholNtslAmt6mmRt(-1);
						} else {
							data.setWholNtslAmt6mmRt(Math.round(data.getNowYr6mmWholNtslAmt() * 100 / data.getBfrYr6mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr7mmWholNtslAmt() == 0) {
							data.setWholNtslAmt7mmRt(-1);
						} else {
							data.setWholNtslAmt7mmRt(Math.round(data.getNowYr7mmWholNtslAmt() * 100 / data.getBfrYr7mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr8mmWholNtslAmt() == 0) {
							data.setWholNtslAmt8mmRt(-1);
						} else {
							data.setWholNtslAmt8mmRt(Math.round(data.getNowYr8mmWholNtslAmt() * 100 / data.getBfrYr8mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr9mmWholNtslAmt() == 0) {
							data.setWholNtslAmt9mmRt(-1);
						} else {
							data.setWholNtslAmt9mmRt(Math.round(data.getNowYr9mmWholNtslAmt() * 100 / data.getBfrYr9mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr10mmWholNtslAmt() == 0) {
							data.setWholNtslAmt10mmRt(-1);
						} else {
							data.setWholNtslAmt10mmRt(Math.round(data.getNowYr10mmWholNtslAmt() * 100 / data.getBfrYr10mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr11mmWholNtslAmt() == 0) {
							data.setWholNtslAmt11mmRt(-1);
						} else {
							data.setWholNtslAmt11mmRt(Math.round(data.getNowYr11mmWholNtslAmt() * 100 / data.getBfrYr11mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr12mmWholNtslAmt() == 0) {
							data.setWholNtslAmt12mmRt(-1);
						} else {
							data.setWholNtslAmt12mmRt(Math.round(data.getNowYr12mmWholNtslAmt() * 100 / data.getBfrYr12mmWholNtslAmt()) / 100.0);
						}

						if (data.getBfrYr1mmWholNtslNocs() == 0) {
							data.setWholNtslNocs1mmRt(-1);
						} else {
							data.setWholNtslNocs1mmRt(Math.round(data.getNowYr1mmWholNtslNocs() * 100 / data.getBfrYr1mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr2mmWholNtslNocs() == 0) {
							data.setWholNtslNocs2mmRt(-1);
						} else {
							data.setWholNtslNocs2mmRt(Math.round(data.getNowYr2mmWholNtslNocs() * 100 / data.getBfrYr2mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr3mmWholNtslNocs() == 0) {
							data.setWholNtslNocs3mmRt(-1);
						} else {
							data.setWholNtslNocs3mmRt(Math.round(data.getNowYr3mmWholNtslNocs() * 100 / data.getBfrYr3mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr4mmWholNtslNocs() == 0) {
							data.setWholNtslNocs4mmRt(-1);
						} else {
							data.setWholNtslNocs4mmRt(Math.round(data.getNowYr4mmWholNtslNocs() * 100 / data.getBfrYr4mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr5mmWholNtslNocs() == 0) {
							data.setWholNtslNocs5mmRt(-1);
						} else {
							data.setWholNtslNocs5mmRt(Math.round(data.getNowYr5mmWholNtslNocs() * 100 / data.getBfrYr5mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr6mmWholNtslNocs() == 0) {
							data.setWholNtslNocs6mmRt(-1);
						} else {
							data.setWholNtslNocs6mmRt(Math.round(data.getNowYr6mmWholNtslNocs() * 100 / data.getBfrYr6mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr7mmWholNtslNocs() == 0) {
							data.setWholNtslNocs7mmRt(-1);
						} else {
							data.setWholNtslNocs7mmRt(Math.round(data.getNowYr7mmWholNtslNocs() * 100 / data.getBfrYr7mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr8mmWholNtslNocs() == 0) {
							data.setWholNtslNocs8mmRt(-1);
						} else {
							data.setWholNtslNocs8mmRt(Math.round(data.getNowYr8mmWholNtslNocs() * 100 / data.getBfrYr8mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr9mmWholNtslNocs() == 0) {
							data.setWholNtslNocs9mmRt(-1);
						} else {
							data.setWholNtslNocs9mmRt(Math.round(data.getNowYr9mmWholNtslNocs() * 100 / data.getBfrYr9mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr10mmWholNtslNocs() == 0) {
							data.setWholNtslNocs10mmRt(-1);
						} else {
							data.setWholNtslNocs10mmRt(Math.round(data.getNowYr10mmWholNtslNocs() * 100 / data.getBfrYr10mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr11mmWholNtslNocs() == 0) {
							data.setWholNtslNocs11mmRt(-1);
						} else {
							data.setWholNtslNocs11mmRt(Math.round(data.getNowYr11mmWholNtslNocs() * 100 / data.getBfrYr11mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr12mmWholNtslNocs() == 0) {
							data.setWholNtslNocs12mmRt(-1);
						} else {
							data.setWholNtslNocs12mmRt(Math.round(data.getNowYr12mmWholNtslNocs() * 100 / data.getBfrYr12mmWholNtslNocs()) / 100.0);
						}
					}
				}
			} else {
				for (OrderCountAmtStatMonthLclgv orderCountAmtStatMonthLclgv : orderCountAmtStatMonthLclgvList) {
					if (orderCountAmtStatMonthLclgv != null) {
						data.setUpperLocgovNm("총계");
						
						data.setNowYr1mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr1mmWholNtslAmt()
													+ data.getNowYr1mmWholNtslAmt());
						data.setNowYr2mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr2mmWholNtslAmt()
													+ data.getNowYr2mmWholNtslAmt());
						data.setNowYr3mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr3mmWholNtslAmt()
													+ data.getNowYr3mmWholNtslAmt());
						data.setNowYr4mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr4mmWholNtslAmt()
													+ data.getNowYr4mmWholNtslAmt());
						data.setNowYr5mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr5mmWholNtslAmt()
													+ data.getNowYr5mmWholNtslAmt());
						data.setNowYr6mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr6mmWholNtslAmt()
													+ data.getNowYr6mmWholNtslAmt());
						data.setNowYr7mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr7mmWholNtslAmt()
													+ data.getNowYr7mmWholNtslAmt());
						data.setNowYr8mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr8mmWholNtslAmt()
													+ data.getNowYr8mmWholNtslAmt());
						data.setNowYr9mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr9mmWholNtslAmt()
													+ data.getNowYr9mmWholNtslAmt());
						data.setNowYr10mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr10mmWholNtslAmt()
													+ data.getNowYr10mmWholNtslAmt());
						data.setNowYr11mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr11mmWholNtslAmt()
													+ data.getNowYr11mmWholNtslAmt());
						data.setNowYr12mmWholNtslAmt(orderCountAmtStatMonthLclgv.getNowYr12mmWholNtslAmt()
													+ data.getNowYr12mmWholNtslAmt());
						
						data.setNowYr1mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr1mmWholNtslNocs()
													+ data.getNowYr1mmWholNtslNocs());
						data.setNowYr2mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr2mmWholNtslNocs()
													+ data.getNowYr2mmWholNtslNocs());
						data.setNowYr3mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr3mmWholNtslNocs()
													+ data.getNowYr3mmWholNtslNocs());
						data.setNowYr4mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr4mmWholNtslNocs()
													+ data.getNowYr4mmWholNtslNocs());
						data.setNowYr5mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr5mmWholNtslNocs()
													+ data.getNowYr5mmWholNtslNocs());
						data.setNowYr6mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr6mmWholNtslNocs()
													+ data.getNowYr6mmWholNtslNocs());
						data.setNowYr7mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr7mmWholNtslNocs()
													+ data.getNowYr7mmWholNtslNocs());
						data.setNowYr8mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr8mmWholNtslNocs()
													+ data.getNowYr8mmWholNtslNocs());
						data.setNowYr9mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr9mmWholNtslNocs()
													+ data.getNowYr9mmWholNtslNocs());
						data.setNowYr10mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr10mmWholNtslNocs()
													+ data.getNowYr10mmWholNtslNocs());
						data.setNowYr11mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr11mmWholNtslNocs()
													+ data.getNowYr11mmWholNtslNocs());
						data.setNowYr12mmWholNtslNocs(orderCountAmtStatMonthLclgv.getNowYr12mmWholNtslNocs()
													+ data.getNowYr12mmWholNtslNocs());
						
						data.setBfrYr1mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr1mmWholNtslAmt()
													+ data.getBfrYr1mmWholNtslAmt());
						data.setBfrYr2mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr2mmWholNtslAmt()
													+ data.getBfrYr2mmWholNtslAmt());
						data.setBfrYr3mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr3mmWholNtslAmt()
													+ data.getBfrYr3mmWholNtslAmt());
						data.setBfrYr4mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr4mmWholNtslAmt()
													+ data.getBfrYr4mmWholNtslAmt());
						data.setBfrYr5mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr5mmWholNtslAmt()
													+ data.getBfrYr5mmWholNtslAmt());
						data.setBfrYr6mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr6mmWholNtslAmt()
													+ data.getBfrYr6mmWholNtslAmt());
						data.setBfrYr7mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr7mmWholNtslAmt()
													+ data.getBfrYr7mmWholNtslAmt());
						data.setBfrYr8mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr8mmWholNtslAmt()
													+ data.getBfrYr8mmWholNtslAmt());
						data.setBfrYr9mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr9mmWholNtslAmt()
													+ data.getBfrYr9mmWholNtslAmt());
						data.setBfrYr10mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr10mmWholNtslAmt()
													+ data.getBfrYr10mmWholNtslAmt());
						data.setBfrYr11mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr11mmWholNtslAmt()
													+ data.getBfrYr11mmWholNtslAmt());
						data.setBfrYr12mmWholNtslAmt(orderCountAmtStatMonthLclgv.getBfrYr12mmWholNtslAmt()
													+ data.getBfrYr12mmWholNtslAmt());
						
						data.setBfrYr1mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr1mmWholNtslNocs()
													+ data.getBfrYr1mmWholNtslNocs());
						data.setBfrYr2mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr2mmWholNtslNocs()
													+ data.getBfrYr2mmWholNtslNocs());
						data.setBfrYr3mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr3mmWholNtslNocs()
													+ data.getBfrYr3mmWholNtslNocs());
						data.setBfrYr4mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr4mmWholNtslNocs()
													+ data.getBfrYr4mmWholNtslNocs());
						data.setBfrYr5mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr5mmWholNtslNocs()
													+ data.getBfrYr5mmWholNtslNocs());
						data.setBfrYr6mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr6mmWholNtslNocs()
													+ data.getBfrYr6mmWholNtslNocs());
						data.setBfrYr7mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr7mmWholNtslNocs()
													+ data.getBfrYr7mmWholNtslNocs());
						data.setBfrYr8mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr8mmWholNtslNocs()
													+ data.getBfrYr8mmWholNtslNocs());
						data.setBfrYr9mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr9mmWholNtslNocs()
													+ data.getBfrYr9mmWholNtslNocs());
						data.setBfrYr10mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr10mmWholNtslNocs()
													+ data.getBfrYr10mmWholNtslNocs());
						data.setBfrYr11mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr11mmWholNtslNocs()
													+ data.getBfrYr11mmWholNtslNocs());
						data.setBfrYr12mmWholNtslNocs(orderCountAmtStatMonthLclgv.getBfrYr12mmWholNtslNocs()
													+ data.getBfrYr12mmWholNtslNocs());

						if (data.getBfrYr1mmWholNtslAmt() == 0) {
							data.setWholNtslAmt1mmRt(-1);
						} else {
							data.setWholNtslAmt1mmRt(Math.round(data.getNowYr1mmWholNtslAmt() * 100 / data.getBfrYr1mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr2mmWholNtslAmt() == 0) {
							data.setWholNtslAmt2mmRt(-1);
						} else {
							data.setWholNtslAmt2mmRt(Math.round(data.getNowYr2mmWholNtslAmt() * 100 / data.getBfrYr2mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr3mmWholNtslAmt() == 0) {
							data.setWholNtslAmt3mmRt(-1);
						} else {
							data.setWholNtslAmt3mmRt(Math.round(data.getNowYr3mmWholNtslAmt() * 100 / data.getBfrYr3mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr4mmWholNtslAmt() == 0) {
							data.setWholNtslAmt4mmRt(-1);
						} else {
							data.setWholNtslAmt4mmRt(Math.round(data.getNowYr4mmWholNtslAmt() * 100 / data.getBfrYr4mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr5mmWholNtslAmt() == 0) {
							data.setWholNtslAmt5mmRt(-1);
						} else {
							data.setWholNtslAmt5mmRt(Math.round(data.getNowYr5mmWholNtslAmt() * 100 / data.getBfrYr5mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr6mmWholNtslAmt() == 0) {
							data.setWholNtslAmt6mmRt(-1);
						} else {
							data.setWholNtslAmt6mmRt(Math.round(data.getNowYr6mmWholNtslAmt() * 100 / data.getBfrYr6mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr7mmWholNtslAmt() == 0) {
							data.setWholNtslAmt7mmRt(-1);
						} else {
							data.setWholNtslAmt7mmRt(Math.round(data.getNowYr7mmWholNtslAmt() * 100 / data.getBfrYr7mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr8mmWholNtslAmt() == 0) {
							data.setWholNtslAmt8mmRt(-1);
						} else {
							data.setWholNtslAmt8mmRt(Math.round(data.getNowYr8mmWholNtslAmt() * 100 / data.getBfrYr8mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr9mmWholNtslAmt() == 0) {
							data.setWholNtslAmt9mmRt(-1);
						} else {
							data.setWholNtslAmt9mmRt(Math.round(data.getNowYr9mmWholNtslAmt() * 100 / data.getBfrYr9mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr10mmWholNtslAmt() == 0) {
							data.setWholNtslAmt10mmRt(-1);
						} else {
							data.setWholNtslAmt10mmRt(Math.round(data.getNowYr10mmWholNtslAmt() * 100 / data.getBfrYr10mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr11mmWholNtslAmt() == 0) {
							data.setWholNtslAmt11mmRt(-1);
						} else {
							data.setWholNtslAmt11mmRt(Math.round(data.getNowYr11mmWholNtslAmt() * 100 / data.getBfrYr11mmWholNtslAmt()) / 100.0);
						}
						if (data.getBfrYr12mmWholNtslAmt() == 0) {
							data.setWholNtslAmt12mmRt(-1);
						} else {
							data.setWholNtslAmt12mmRt(Math.round(data.getNowYr12mmWholNtslAmt() * 100 / data.getBfrYr12mmWholNtslAmt()) / 100.0);
						}

						if (data.getBfrYr1mmWholNtslNocs() == 0) {
							data.setWholNtslNocs1mmRt(-1);
						} else {
							data.setWholNtslNocs1mmRt(Math.round(data.getNowYr1mmWholNtslNocs() * 100 / data.getBfrYr1mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr2mmWholNtslNocs() == 0) {
							data.setWholNtslNocs2mmRt(-1);
						} else {
							data.setWholNtslNocs2mmRt(Math.round(data.getNowYr2mmWholNtslNocs() * 100 / data.getBfrYr2mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr3mmWholNtslNocs() == 0) {
							data.setWholNtslNocs3mmRt(-1);
						} else {
							data.setWholNtslNocs3mmRt(Math.round(data.getNowYr3mmWholNtslNocs() * 100 / data.getBfrYr3mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr4mmWholNtslNocs() == 0) {
							data.setWholNtslNocs4mmRt(-1);
						} else {
							data.setWholNtslNocs4mmRt(Math.round(data.getNowYr4mmWholNtslNocs() * 100 / data.getBfrYr4mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr5mmWholNtslNocs() == 0) {
							data.setWholNtslNocs5mmRt(-1);
						} else {
							data.setWholNtslNocs5mmRt(Math.round(data.getNowYr5mmWholNtslNocs() * 100 / data.getBfrYr5mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr6mmWholNtslNocs() == 0) {
							data.setWholNtslNocs6mmRt(-1);
						} else {
							data.setWholNtslNocs6mmRt(Math.round(data.getNowYr6mmWholNtslNocs() * 100 / data.getBfrYr6mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr7mmWholNtslNocs() == 0) {
							data.setWholNtslNocs7mmRt(-1);
						} else {
							data.setWholNtslNocs7mmRt(Math.round(data.getNowYr7mmWholNtslNocs() * 100 / data.getBfrYr7mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr8mmWholNtslNocs() == 0) {
							data.setWholNtslNocs8mmRt(-1);
						} else {
							data.setWholNtslNocs8mmRt(Math.round(data.getNowYr8mmWholNtslNocs() * 100 / data.getBfrYr8mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr9mmWholNtslNocs() == 0) {
							data.setWholNtslNocs9mmRt(-1);
						} else {
							data.setWholNtslNocs9mmRt(Math.round(data.getNowYr9mmWholNtslNocs() * 100 / data.getBfrYr9mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr10mmWholNtslNocs() == 0) {
							data.setWholNtslNocs10mmRt(-1);
						} else {
							data.setWholNtslNocs10mmRt(Math.round(data.getNowYr10mmWholNtslNocs() * 100 / data.getBfrYr10mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr11mmWholNtslNocs() == 0) {
							data.setWholNtslNocs11mmRt(-1);
						} else {
							data.setWholNtslNocs11mmRt(Math.round(data.getNowYr11mmWholNtslNocs() * 100 / data.getBfrYr11mmWholNtslNocs()) / 100.0);
						}
						if (data.getBfrYr12mmWholNtslNocs() == 0) {
							data.setWholNtslNocs12mmRt(-1);
						} else {
							data.setWholNtslNocs12mmRt(Math.round(data.getNowYr12mmWholNtslNocs() * 100 / data.getBfrYr12mmWholNtslNocs()) / 100.0);
						}
					}
				}
			}
		}
		return data;
	}
	
}
