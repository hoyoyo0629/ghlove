<template>
    <div id="NC_ID_1589869366282396" class="npay_storebtn_bx npay_type_A_2">
        <div id="NPAY_BUTTON_BOX_ID" class="npay_button_box ">
            <div class="npay_button">
                <div class="npay_text"><span class="npay_blind">NAVER 네이버 ID로 간편구매 네이버페이</span>
                </div>
                <table class="npay_btn_list" cellspacing="0" cellpadding="0">
                    <tbody>
                    <tr>
                        <td class="npay_btn_item">
                            <a id="naverPayBtn" href="javascript:void(0)" class="npay_btn_link npay_btn_pay btn_green" style="box-sizing:content-box; " title="새창" @click="NaverPay_buyButtonHandler"><span class="npay_blind">네이버페이 구매하기</span></a>
                        </td>
                        <template v-if="orderType == 'item'">
                            <td class="npay_btn_item btn_width">
                                <a id="NPAY_WISH_LINK_IDNC_ID_1589869366282396" href="javascript:void(0)" class="npay_btn_link npay_btn_zzim " style="box-sizing:content-box; " title="새창" @click="NaverPay_wishlistButtonHandler"><span class="npay_blind">찜하기</span></a>
                            </td>
                        </template>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="NPAY_EVENT_ID" class="npay_event">
<!--                <a id="NPAY_PROMOTION_PREV_IDNC_ID_1589869366282396" href="#" class="npay_more npay_more_prev"><span class="npay_blind">이전</span></a>-->
                <p id="NPAY_PROMOTION_IDNC_ID_1589869366282396" class="npay_event_text"><strong class="event_title">이벤트</strong><a href="http://event2.pay.naver.com/event/benefit/list" class="event_link" target="_blank" title="새창 열림">네이버페이</a>
<!--                </p>	<a id="NPAY_PROMOTION_NEXT_IDNC_ID_1589869366282396" href="#" class="npay_more npay_more_next"><span class="npay_blind">다음</span></a>-->
            </div>
        </div>
    </div>
</template>

<script>
    module.exports = {
        props: {
            orderType: {
                type: String,
                default: function() {
                    return 'cart';
                }
            },
            isMobile: {
                type: Boolean,
                default: function() {
                    return false;
                }
            }
        },
        data: function () {
            return {
                naverOrder : [],
            }
        },
        methods: {
            // 네이버 페이 요청 파라미터를 가져옴.
            getNaverPayParamAfterVerification: function (requestType, orderType) {
                this.naverOrder = new Array();
                var naverOrder = this.naverOrder;

                // 상품이 배열이면 장바구니 아니면 상품상세로 구분
                if (orderType === 'cart') {
                    if (vm.selected.length == 0) {
                        alert("상품을 선택해주세요.");
                        return false;
                    }

                    var item = new Array();
                    for (var i=0;i<vm.buyItems.length;i++) {
                        var selectedItems = vm.buyItems[i];
                        for (var j=0;j<selectedItems.length;j++) {
                            var selectedItem = selectedItems[j];
                            for (var k=0;k<vm.selected.length;k++) {
                                var selectedId = vm.selected[k];

                                if (selectedItem.cartId == selectedId) {
                                    naverOrder.push(selectedItem);
                                }
                            }
                        }
                    }
                } else {
                    naverOrder.push(vm.item);
                }

                var data = new Array();
                if (orderType != "cart") {
                    if (vm.item.itemOptionFlag == "Y" && vm.itemOptionInfo.addOptionList.length == 0) {
                        alert("옵션을 선택해주세요.");
                        return false;
                    }
                    for (var i = 0; i < naverOrder.length; i++) {
                        // 상품
                        var item = naverOrder[i];
                        var quantity = 0;
                        var itemPrice = item.presentPrice;

                        // 옵션
                        var options = vm.itemOptionInfo.addOptionList;
                        var optionPrice = 0;
                        var optionArray = new Array();
                        if (options.length > 0) {
                            for (var j=0;j<options.length;j++) {
                                var option = options[j];

                                var optionText = option.optionName;
                                var optionQuantity = option.quantity;
                                var basePrice = option.tempOptionPrice == '' ? 0 : Number(option.tempOptionPrice);
                                optionPrice += option.baseOptionPrice;
                                quantity += Number(optionQuantity);

                                var optionList = {
                                    "optionText": optionText,
                                    "optionPrice": option.baseOptionPrice,
                                    "optionQuantity": optionQuantity
                                };

                                // option을 optionArray에 담아 vo로 전달
                                optionArray.push(optionList);
                            }
                        }

                        var orderPayAmount = vm.optionItemTotal;

                        var shippingAmount = 0;
                        if (item.shippingType == '1') {
                            shippingAmount = 0;
                        } else if (item.shippingType == '2' || item.shippingType == '3' || item.shippingType == '4') {

                            if (orderPayAmount >= item.shippingFreeAmount) {
                                shippingAmount = 0;
                            } else {
                                shippingAmount = item.shipping;
                            }
                        } else if (item.shippingType == '5') {
                            var count = Math.round(quantity / item.shippingItemCount);
                            shippingAmount = item.shipping * count;
                        } else if (item.shippingType == '6') {
                            shippingAmount = item.shipping;
                        }

                        if (isNaN(optionPrice)) {
                            optionPrice = 0;
                        }

                        var orderItem = {
                            'itemId': item.itemId,
                            'itemName': item.itemName,
                            'quantity': quantity,
                            'optionPrice': optionPrice,
                            'price': itemPrice,
                            'itemPrice': itemPrice,
                            'shipping': shippingAmount,
                            'itemUserCode' : item.itemUserCode,
                            'imageSrc' : item.imageSrc
                        };

                        if (vm.item.itemOptionFlag == "Y") {
                            orderItem.optionArray = optionArray;
                        }
                        data.push(orderItem);
                    }
                } else {
                    // 장바구니일때
                    for (var i = 0; i < naverOrder.length; i++) {
                        // 상품
                        var item = naverOrder[i];
                        var quantity = 0;
                        var itemPrice = item.item.presentPrice;

                        // 옵션
                        var options = item.optionList;
                        var optionPrice = 0;
                        var optionArray = new Array();
                        if (options != null) {
                            for (var j=0;j<options.length;j++) {
                                if (options.length > 4) {
                                    return false;
                                }
                                var option = options[j];
                                var optionText = item.options;
                                var optionQuantity = item.itemPrice.quantity;

                                optionPrice += item.itemPrice.optionPrice;
                                quantity += Number(optionQuantity);


                                var optionList = {
                                    "optionText": optionText,
                                    "optionPrice": item.itemPrice.optionPrice,
                                    "optionQuantity": optionQuantity
                                };

                                // option을 optionArray에 담아 vo로 전달
                                optionArray.push(optionList);
                            }
                        } else {
                            quantity = item.itemPrice.quantity;
                        }

                        var orderPayAmount = item.itemPrice.saleAmount;

                        var shippingAmount = 0;
                        if (item.item.shippingType == '1') {
                            shippingAmount = 0;
                        } else if (item.item.shippingType == '2' || item.item.shippingType == '3' || item.item.shippingType == '4') {

                            if (orderPayAmount >= item.item.shippingFreeAmount) {
                                shippingAmount = 0;
                            } else {
                                shippingAmount = item.item.shipping;
                            }
                        } else if (item.shippingType == '5') {
                            var count = Math.round(quantity / item.item.shippingItemCount);
                            shippingAmount = item.item.shipping * count;
                        } else if (item.shippingType == '6') {
                            shippingAmount = item.item.shipping;
                        }

                        if (isNaN(optionPrice)) {
                            optionPrice = 0;
                        }

                        var orderItem = {
                            'itemId': item.item.itemId,
                            'itemName': item.item.itemName,
                            'quantity': quantity,
                            'optionPrice': optionPrice,
                            'price': itemPrice,
                            'itemPrice': itemPrice,
                            'shipping': shippingAmount,
                            'itemUserCode' : item.itemUserCode,
                            'imageSrc' : item.item.imageSrc
                        };

                        if (item.item.itemOptionFlag == "Y") {
                            orderItem.optionArray = optionArray;
                        }
                        data.push(orderItem);
                    }
                }
                return {
                    'isValid': true,
                    'orderType': orderType,
                    'deviceType': vm.isMobile ? 'mobile' : 'web',
                    'data': data
                }
            },
            // 구매하기
            NaverPay_buyButtonHandler: function () {

                var naverPayParam = this.getNaverPayParamAfterVerification('order', this.orderType);

                if (!naverPayParam.isValid) {
                    return false;
                }

                $s.api.naverPay(JSON.stringify(naverPayParam), function (response) {
                    var resp = response.json;
                    var payUrl = resp.payUrl;
                    if (resp.flag1) {
                        if (vm.isMobile) {
                            window.open(payUrl + '?ORDER_ID=' + resp.orderKey + '&SHOP_ID=' + resp.shopId + '&TOTAL_PRICE=' + resp.price,'','width=480, height=800')
                        } else {
                            window.open(payUrl + '?ORDER_ID=' + resp.orderKey + '&SHOP_ID=' + resp.shopId + '&TOTAL_PRICE=' + resp.price,'','width=1200, height=800')
                        }
                    } else {
                        $s.alert('네트워크 문제로 실패되었습니다.');
                    }
                });
            },
            // 찜하기
            NaverPay_wishlistButtonHandler: function () {
                var naverPayParam = this.getNaverPayParamAfterVerification('wish', this.orderType);

                if (!naverPayParam.isValid) {
                    return false;
                }

                $s.api.naverZzim(JSON.stringify(naverPayParam), function (response) {
                    var resp = response.json;
                    var zzimUrl = resp.zzimUrl;

                    if (resp.flag1) {
                        if (vm.isMobile) {
                            window.open(zzimUrl + '?SHOP_ID=' + resp.shopId + '&ITEM_ID=' + resp.itemId,'','width=380, height=800')
                        } else {
                            window.open(zzimUrl + '?SHOP_ID=' + resp.shopId + '&ITEM_ID=' + resp.itemId,'','width=1200, height=800')
                        }
                    } else {
                        $s.alert('네트워크 문제로 실패되었습니다.');
                    }
                });
            },
        },
    }
</script>