<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="mobile-web-app-capable" content="yes" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <meta name="format-detection" content="telephone=no" />
    <link rel="icon" href="/content/images/common/favicon.ico" />
    <title>기부하기 > 특정사업에 기부하기 상세 | 고향사랑e음</title>
    <link rel="stylesheet" href="/content/modules/front/bootstrap/bootstrap.css" />
    <link rel="stylesheet" href="/content/opmanager/css/swiper.min.css" />
    <link rel="stylesheet" href="/content/opmanager/css/front/common.css" />
    <link rel="stylesheet" href="/content/opmanager/css/front/default_ali.css" />
    <link rel="stylesheet" href="/content/opmanager/css/front/item.css" />
    <link rel="stylesheet" href="/content/opmanager/css/front/designation.css" />
  </head>

  <body>
    <div id="saleson">
      <section class="page-title-box mall center">
        <span class="ali_breadcrumb">
          <a href="/" v-if="!exportMode"><img class="icon-img" src="/content/opmanager/images/items/cli-icon_home.png" alt="홈으로" /></a>
          <span class="txt-arrow"></span>
          <a href="#">기부하기</a>
          <span class="txt-arrow"></span>
          <a href="#">특정사업에 기부하기</a>
        </span>
      </section>

      <section class="center prj_details" id="contents" style="padding-top: 0px">
        <div class="goods_view_top prj">
          <div class="prj_img">
            <div class="target_img">
              <!-- 상품 이미지 -->
              <div class="swiper-container" id="mainSwiper">
                <div class="swiper-wrapper">
            	<c:forEach items="${designatedDonation.prjImages}" var="prjImage">
                	<div class="swiper-slide"><img src="${prjImage.imageName}"/></div>
                </c:forEach>
                </div>
                <div class="swiper-button-prev pc main-button-prev"><span class="screen_out">이전 특정사업에 기부하기 이미지 보기</span></div>
                <div class="swiper-button-next pc main-button-next"><span class="screen_out">다음 특정사업에 기부하기 이미지 보기</span></div>
                <div class="swiper-pagination"></div>
              </div>
              <!-- //상품 이미지 -->
            </div>

            <!-- 상품 이미지 썸네일 -->
            <div class="thumb_box">
              <button type="button" class="swiper-button-prev thumb-button-prev">
                <img src="/content/opmanager/images/items/thumb_arrow-prev.png" alt="이전 특정사업에 기부하기 이미지 보기" />
              </button>
              <div class="swiper-container thumb_area d-lg-block" id="mainThumbSwiper">
                <div class="swiper-wrapper">
                  	<c:forEach items="${designatedDonation.prjImages}" var="prjImages">
                		<div class="swiper-slide">
                    		<button type="button" class="cursor swiper_img"><img src="${prjImages.imageName}"/></button>
	                  </div>
                	</c:forEach>
                </div>
              </div>
              <button type="button" class="swiper-button-next thumb-button-next">
                <img src="/content/opmanager/images/items/thumb_arrow-next.png" alt="다음 특정사업에 기부하기 이미지 보기" />
              </button>
            </div>
            <!-- //상품 이미지 썸네일 -->
          </div>
          <!-- //프로젝트 이미지 -->

          <!-- 프로젝트 정보 -->
          <div class="goods_info prj_info">
            <div class="info_box">
              <div class="prj_progress">
                <div class="brand_mall d_area"><img src="/content/opmanager/images/items/d-area.png" alt="지역 위치 모양 마커" />${fn:escapeXml(designatedDonation.locgovNm)}
                <div class="info_title">
                  <h2>${fn:escapeXml(designatedDonation.prjSubject)}</h2>
                  <p class="s-txt">${fn:escapeXml(designatedDonation.prjStDt)} ~ ${fn:escapeXml(designatedDonation.prjEdDt)}</p>
                </div>
              </div>

              <div class="donation_amount">
                <span class="txt">기부총액 </span><span class="pointblue"><strong>${op:numberFormat(designatedDonation.sumAmt)}</strong> 원</span>
              </div>

              <div class="prj_progress">
                <div class="prj_gauge">
                  <div class="gauge_back">
                    <!--
                        .gauge_result + 조건 class 추가
                        'lt_goal': prjCard.rateAmt < 100,
                        'goal': prjCard.rateAmt === 100,
                        'gt_goal': prjCard.rateAmt > 100
                    -->
                    <div class="gauge_result lt_goal" style="width: 0%"></div>
                  </div>
                </div>
                <div class="inner_wrap">
                  <div class="prj_gaugePer">
                    <img src="/content/opmanager/images/items/cli-icon_btn-donation2.png" alt="초과달성" /><!-- 초과달성 시 -->
                    <span class="perNum">${fn:escapeXml(designatedDonation.rateAmt)}</span>%
                  </div>
                  <div class="on prj_status">D-${fn:escapeXml(designatedDonation.leftDay)}</div>
                </div>
              </div>

              <div class="info_row etc nomargin">
                <div class="line top-nomargin"></div>
                <div class="shop_info">
                  <div class="title_col">
                    <p>목표금액</p>
                  </div>
                  <div class="para_col">
                    <p class="txt">${op:numberFormat(designatedDonation.targetAmt)} 원</p>
                  </div>
                </div>
                <div class="shop_info">
                  <div class="title_col">
                    <p>기부참여</p>
                  </div>
                  <div class="para_col">
                    <p class="txt">${fn:escapeXml(designatedDonation.cntrCnt)} 명</p>
                  </div>
                </div>
                <div class="line bt-nomargin"></div>
              </div>
              <p class="txt">특정사업에 기부하기는 지역이 가진 문제를 해결하기 위해 고향사랑기부제를 프로젝트화하고 그 취지에 공감하는 특정사업을 직접 선택하여 기부할 수 있습니다.</p>

              <div class="prj_donation-btns">
                <button class="formBtn donation" type="button" id="donationBtn"><img src="/content/opmanager/images/items/cli-icon_btn-donation2.png" alt="하트와 월계수" /><span>기부하기</span></button>
              </div>
            </div>
            <!--// 프로젝트 정보 -->
          </div>
        </div>
      </section>

      <!-- 프로젝트소개, 기부내역, 자주묻는질문 -->
      <section>
        <div class="item_tab prj_tab" id="item_tab_wrap">
          <div class="sticky-prj_info">
            <div class="center">
              <div class="prj_progress_wrap prj_details">
                <div class="prj_progress">
                  <div class="prj_gauge">
                    <div class="gauge_back">
                      <!--
                            .gauge_result  + 주건부 class 명 추가
                            lt_goal: prjCard.rateAmt < 100,
                            goal: prjCard.rateAmt === 100,
                            gt_goal: prjCard.rateAmt > 100,
                        -->
                      <div class="gauge_result lt_goal" style="width: 4.48%"></div>
                    </div>
                  </div>
                  <div class="inner_wrap">
                    <div class="prj_gaugePer">
                      <img src="/content/opmanager/images/items/cli-icon_btn-donation2.png" alt="초과달성" v-show="prjCard.rateAmt > 100" />
                      <span class="perNum">${fn:escapeXml(designatedDonation.rateAmt)}</span>%
                    </div>
                    <div class="prj_status on">D-${fn:escapeXml(designatedDonation.leftDay)}</div>
                  </div>
                </div>
                <div class="donation_amount">
                  <span class="txt">기부총액 </span>
                  <span class="pointblue"><strong> ${fn:escapeXml(designatedDonation.sumAmt)}</strong> 원</span>
                </div>
              </div>
              <button class="formBtn donation" type="button" id="tabDonationBtn"><img src="/content/opmanager/images/items/cli-icon_btn-donation2.png" alt="" /><span>기부하기</span></button>
            </div>
          </div>
          <ul class="nav nav-tabs nav-justified">
            <li class="nav-item">
              <a href="#nav-detail" class="nav-link active" title="선택됨">
                <span class="txt">사업소개</span>
              </a>
            </li>
            <li class="nav-item">
              <a href="#nav-review" class="nav-link" title="선택됨">
                <span class="txt">응원메시지<br />(기부내역)</span>
              </a>
            </li>
            <li class="nav-item">
              <a href="#nav-qna" class="nav-link" title="선택됨">
                <span class="txt">공지사항</span>
              </a>
            </li>
          </ul>
        </div>
        <div class="center tab_container">
          <div class="tab-content item_view">
            <!-- 프로젝트소개  -->
            <div id="nav-detail" class="tab-pane show active">
              <div class="item_detail">
                <div class="total_top">
                  <p class="total">사업 소개<span class="pointblue"></span></p>
                </div>
                <div class="detailContent">
                  <div class="sampleContents">
                    <p class="pointGray">
                      <!-- 사업 소개 내용 -->
						${designatedDonation.prjCn}
                      <!-- //사업 소개 내용 -->
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <!--// 프로젝트 소개-->

            <!--  -->
            <div id="nav-review" class="tab-pane">
              <div class="item_review">
                <div class="total_top"></div>
                <div class="total-noti-wrap">
                  <div class="noti_wrap">
                    <div class="noti-box">
                      <div class="noti_txt">"총 ${fn:escapeXml(designatedDonation.cntrCnt)}건이 기부되었습니다."</div>
                    </div>
                    <img src="/content/opmanager/images/items/dsg-donation_gohyangee.png" alt="고향이" class="gohyangee" />
                  </div>
                </div>
                <div class="list_wrap review_list">
                  <ul>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">100,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>김**</p></div>
                            <div class="review_id"><p>upk71***</p></div>
                            <div class="review_date"><p>2025-06-12</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">5,000,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>박**</p></div>
                            <div class="review_id"><p>p27iw***</p></div>
                            <div class="review_date"><p>2025-03-28</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">100,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>박**</p></div>
                            <div class="review_id"><p>p27iw***</p></div>
                            <div class="review_date"><p>2025-03-28</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">10,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>정**</p></div>
                            <div class="review_id"><p>a5r3y***</p></div>
                            <div class="review_date"><p>2025-03-21</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">500,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>강**</p></div>
                            <div class="review_id"><p>neb93***</p></div>
                            <div class="review_date"><p>2025-03-19</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">10,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>김**</p></div>
                            <div class="review_id"><p>i8fdk***</p></div>
                            <div class="review_date"><p>2025-03-13</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">100,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>김**</p></div>
                            <div class="review_id"><p>s99m3***</p></div>
                            <div class="review_date"><p>2024-07-27</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">100,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>김**</p></div>
                            <div class="review_id"><p>kafi***</p></div>
                            <div class="review_date"><p>2024-07-24</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">100,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>허**</p></div>
                            <div class="review_id"><p>f62p1***</p></div>
                            <div class="review_date"><p>2024-07-24</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="list_area">
                      <div class="review_open">
                        <div class="list_top">
                          <div class="review_para" style="width: 70%">
                            <p class="prj-tab-list_point-txt"><strong class="pointRed">100,000</strong>원 참여</p>
                          </div>
                          <div class="review_para" style="display: flex; justify-content: space-between; align-items: center; width: 100%; padding-right: 0px">
                            <!---->
                            <!---->
                            <div><p style="white-space: normal"></p></div>
                          </div>
                          <div class="m-field" style="min-width: 30%">
                            <div class="review_id"><p>성**</p></div>
                            <div class="review_id"><p>msn6***</p></div>
                            <div class="review_date"><p>2024-07-23</p></div>
                          </div>
                        </div>
                      </div>
                    </li>
                  </ul>
                  <!-- [case] 기부내역이 없을 때 -->
                  <div class="list-none">
                    <img src="/content/opmanager/images/items/non-list.png" alt="기부내역이 없음" />
                    기부내역이 없습니다.
                  </div>
                  <!-- //[case] 기부내역이 없을 때 -->
                </div>
                <div class="pagination_ali">
                  <ul class="pagination-frame">
                    <li>
                      <button type="button" class="fist arrow_btn">
                        <img src="/content/opmanager/images/items/paging_btn-first.png" alt="처음" />
                      </button>
                    </li>
                    <li>
                      <button class="prev arrow_btn" type="button">
                        <img src="/content/opmanager/images/items/paging_btn-prev.png" alt="이전" />
                      </button>
                    </li>
                    <li class="selected-page">
                      <button class="page-text" type="button">1</button>
                    </li>
                    <li>
                      <button class="page-text" type="button">2</button>
                    </li>
                    <li>
                      <button class="page-text" type="button">3</button>
                    </li>
                    <li>
                      <button class="next arrow_btn" type="button">
                        <img src="/content/opmanager/images/items/paging_btn-next.png" alt="다음" />
                      </button>
                    </li>
                    <li>
                      <button class="last arrow_btn" type="button">
                        <img src="/content/opmanager/images/items/paging_btn-last.png" alt="마지막" />
                      </button>
                    </li>
                  </ul>
                </div>
              </div>
            </div>

            <div id="nav-qna" class="tab-pane">
              <div class="item_qna">
                <div class="total_top">
                  <p class="total">
                    공지사항
                    <span class="pointblue">0</span>
                  </p>
                </div>

                <div class="list_wrap qna_list">
                  <ul>
                    <li class="list_top dropdown cursor" v-for="(data, index) in result.noticeList.content" :key="index" accordion-option="toggle">
                      <button aria-level="2" class="dropdown-toggle" data-toggle="dropdown">
                        <div class="header-faq">
                          <div class="faq_txt" style="white-space: normal; padding-right: 70px">공지사항 샘플</div>
                          <div class="col_group" style="right: 50px; position: absolute">
                            <div class="">공지사항 작성자</div>
                          </div>
                        </div>
                      </button>
                      <div class="dropdown-menu" role="region" :aria-labelledby="'notice-t-' + index" :ref="'notice_' + data.id" style="z-index: 500" @keydown="keyHandler($event)">
                        <div tabindex="0" class="notice-wrap">
                          <div class="header-faq">
                            <div class="faq_txt" style="padding: 0px 20px">
                              <span v-html="appendCdnDomain(data.prjNoticeCn)"></span>
                            </div>
                          </div>
                        </div>
                        <br />
                        <br />
                        <div class="header-faq" v-if="data.prjFiles.length > 0">
                          <div class="faq_txt" style="padding: 0px 20px">- 첨부파일 표기</div>
                        </div>
                        <div v-for="(file, index) in data.prjFiles" :key="'file_' + index" style="padding-top: 5px">
                          <div class="header-faq">
                            <div class="faq_txt" style="padding: 0px 20px">
                              <a @click="fileDownload($event, file)" :title="file.orgFileName + ' 파일 다운로드'" style="cursor: pointer"></a>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                  </ul>
                  <div class="list-none">
                    <img src="/content/opmanager/images/items/non-list.png" alt="" />
                    게시글이 없습니다.
                  </div>
                </div>

                <div class="pagination_ali">
                  <ul class="pagination-frame">
                    <li>
                      <button type="button" class="fist arrow_btn">
                        <img src="/content/opmanager/images/items/paging_btn-first.png" alt="처음" />
                      </button>
                    </li>
                    <li>
                      <button class="prev arrow_btn" type="button">
                        <img src="/content/opmanager/images/items/paging_btn-prev.png" alt="이전" />
                      </button>
                    </li>
                    <li class="selected-page">
                      <button class="page-text" type="button">1</button>
                    </li>
                    <li>
                      <button class="page-text" type="button">2</button>
                    </li>
                    <li>
                      <button class="page-text" type="button">3</button>
                    </li>
                    <li>
                      <button class="next arrow_btn" type="button">
                        <img src="/content/opmanager/images/items/paging_btn-next.png" alt="다음" />
                      </button>
                    </li>
                    <li>
                      <button class="last arrow_btn" type="button">
                        <img src="/content/opmanager/images/items/paging_btn-last.png" alt="마지막" />
                      </button>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <div class="center" style="margin-top: 100px" v-if="movePage">
            <div class="btn-box many mb_b60">
              <button type="button" title="특정사업에 기부하기 목록으로 이동" class="blueBtn u-confirm" @click="goList()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 목록 페이지 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
            </div>
          </div>
        </div>
      </section>
    </div>

    <!-- JavaScript -->
    <script src="/content/modules/jquery/jquery-3.4.1.min.js"></script>
    <script src="/content/modules/swiper.min.js"></script>
    <script src="/content/modules/front/bootstrap/vendor/popper.min.js"></script>
    <script src="/content/modules/front/bootstrap/bootstrap.min.js"></script>

    <script>
      //썸네일 Swiper
      var galleryThumbs = new Swiper("#mainThumbSwiper", {
        spaceBetween: 8,
        slidesPerView: 5.5,
        touchRatio: 0.2,
        slidToClickedSlide: true,
        navigation: {
          nextEl: ".thumb-button-next",
          prevEl: ".thumb-button-prev",
        },
      });
      //메인 이미지 Swiper
      var galleryTop = new Swiper("#mainSwiper", {
        spaceBetween: 15,
        slidToClickedSlide: true,
        navigation: {
          nextEl: ".main-button-next",
          prevEl: ".main-button-prev",
        },
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
        thumbs: {
          swiper: galleryThumbs,
        },
      });

      //탭
      window.onscroll = function () {
        myFunction();
      };
      var itemTab = document.getElementById("item_tab_wrap");
      let sticky = itemTab.offsetTop - 30;
      function myFunction() {
        if (window.pageYOffset >= sticky) {
          itemTab.classList.add("sticky");
        } else {
          itemTab.classList.remove("sticky");
        }
      }
      $(".nav-item a").click(function () {
        $(this).addClass("active").parent().siblings().children("a").removeClass("active");
      });
    </script>
    <style scoped>
      .item_tab .nav-tabs {
        justify-content: center;
      }
      .nav-justified .nav-item {
        flex-basis: unset;
        flex-grow: unset;
        width: 20%;
      }
      .item_tab .nav-link .txt {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 70px;
        padding: 0;
      }
      .item_tab.prj_tab.sticky + .tab_container .tab-content > .tab-pane {
        padding-top: 190px;
      }
    </style>
  </body>