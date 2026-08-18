<template>
	<div class="mapWrap">
		<div class="area">
			<div class="image">
				<img src="/static/images/lclgv_chc/vtmap.png" class="mapImg" alt="전국 지도" ref="mapImg"/>
				<div data-id="a1" class="addrName text1" tabindex=0 @click="chooseLocal('28000')" ref="28000" @keyup="keyChooseLocal($event, '28000')" title="인천광역시 시군구 조회하기"><span>인천</span></div>
				<div data-id="a0" class="addrName text" tabindex=0 @click="chooseLocal('11000')" ref="11000" @keyup="keyChooseLocal($event, '11000')" title="서울특별시 시군구 조회하기"><span>서울</span></div>
				<div data-id="a3" class="addrName text3" tabindex=0 @click="chooseLocal('51000')" ref="51000" @keyup="keyChooseLocal($event, '51000')" title="강원특별자치도 시군구 조회하기"><span>강원자치도</span></div>
				<div data-id="a2" class="addrName text2" tabindex=0 @click="chooseLocal('41000')" ref="41000" @keyup="keyChooseLocal($event, '41000')" title="경기도 시군구 조회하기"><span>경기도</span></div>
				<div data-id="a5" class="addrName text5" tabindex=0 @click="chooseLocal('43000')" ref="43000" @keyup="keyChooseLocal($event, '43000')" title="충청북도 시군구 조회하기"><span>충청북도</span></div>
				<div data-id="a4" class="addrName text4" tabindex=0 @click="chooseLocal('44000')" ref="44000" @keyup="keyChooseLocal($event, '44000')" title="충청남도 시군구 조회하기"><span>충청남도</span></div>
				<div data-id="a6" class="addrName text6" tabindex=0 @click="chooseLocal('36000')" ref="36000" @keyup="keyChooseLocal($event, '36000')" title="세종특별자치시 시군구 조회하기"><span>세종</span></div>
				<div data-id="a8" class="addrName text8" tabindex=0 @click="chooseLocal('47000')" ref="47000" @keyup="keyChooseLocal($event, '47000')" title="경상북도 시군구 조회하기"><span>경상북도</span></div>
				<div data-id="a7" class="addrName text7" tabindex=0 @click="chooseLocal('30000')" ref="30000" @keyup="keyChooseLocal($event, '30000')" title="대전광역시 시군구 조회하기"><span>대전</span></div>
				<div data-id="a10" class="addrName text10" tabindex=0 @click="chooseLocal('27000')" ref="27000" @keyup="keyChooseLocal($event, '27000')" title="대구광역시 시군구 조회하기"><span>대구</span></div>
				<div data-id="a9" class="addrName text9" tabindex=0 @click="chooseLocal('52000')" ref="52000" @keyup="keyChooseLocal($event, '52000')" title="전북특별자치도 시군구 조회하기"><span>전북자치도</span></div>
				<div data-id="a11" class="addrName text11" tabindex=0 @click="chooseLocal('31000')" ref="31000" @keyup="keyChooseLocal($event, '31000')" title="울산광역시 시군구 조회하기"><span>울산</span></div>
				<!-- <div data-id="a12" class="addrName text12" tabindex=0 @click="chooseLocal('29000')" ref="29000" @keyup="keyChooseLocal($event, '29000')" title="광주광역시 시군구 조회하기"><span>광주</span></div> -->
				<div data-id="a12" class="addrName text12" tabindex=0 @click="chooseLocal('12000')" ref="12000" @keyup="keyChooseLocal($event, '12000')" title="전남광주특별광역시 시군구 조회하기"><span>전남광주</span></div>
				<div data-id="a16" class="addrName text16" tabindex=0 @click="chooseLocal('48000')" ref="48000" @keyup="keyChooseLocal($event, '48000')" title="경상남도 시군구 조회하기"><span>경상남도</span></div>
				<div data-id="a15" class="addrName text15" tabindex=0 @click="chooseLocal('26000')" ref="26000" @keyup="keyChooseLocal($event, '26000')" title="부산광역시 시군구 조회하기"><span>부산</span></div>
				<!-- <div data-id="a13" class="addrName text13" tabindex=0 @click="chooseLocal('46000')" ref="46000" @keyup="keyChooseLocal($event, '46000')" title="전라남도 시군구 조회하기"><span>전라남도</span></div> -->
				<div data-id="a14" class="addrName text14" tabindex=0 @click="chooseLocal('50000')" ref="50000" @keyup="keyChooseLocal($event, '50000')" title="제주특별자치도 시군구 조회하기"><span>제주자치도</span></div>
			</div>
		</div>

		<div class="addr">
			<div id="a0" class="text_area active">
				<div class="addrTit">{{selectedLclgvUpperNm}}</div>
				<div class="addrBox">
					<ul class="addrList">
						<template v-for="(lclgvInfo, index) in lclgvList">
						<li>
							<a herf="javascript:void(0);" @click="lclgvInfo.isExist === 'Y' ? selectLclgv(lclgvInfo.lclgvCd, lclgvInfo.nm) : ''" @keyup="lclgvInfo.isExist === 'Y' ? keySelectLclgv($event, lclgvInfo.lclgvCd, lclgvInfo.nm) : ''"
							   :tabindex="lclgvInfo.isExist === null || lclgvInfo.isExist !== 'Y'? -1 : 0"
							   v-bind:title="lclgvInfo.isExist === 'Y' ? selectedLclgvUpperCd == '' ? '전체 특정사업기부 조회 페이지로 이동하기' : selectedLclgvUpperNm + ' ' + lclgvInfo.nm + ' 특정사업기부 조회 페이지로 이동하기' : ''"
							   style="cursor: pointer; text-decoration : underline; color:black;"
							   :class="{disabled : lclgvInfo.isExist === null || lclgvInfo.isExist !== 'Y'}"
							   >
								{{lclgvInfo.nm}}
							</a>
						</li>
						</template>
					</ul>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
    module.exports = {
    	// 부모에서 전송한 prjStatus 수신 (2 : 진행 / 9 : 종료)
		props: {
			prjStatus: String
		},
		// prjStatus의 상태를 변화 시 지도에서 선택된 광역지자체의 기초 지자체들을 다시 Ajax로 수신
		watch: {
			prjStatus(newVal) {
				this.status = newVal;
				const upperCd = this.findLclgvInfo(this.selectedLclgvUpperCd)?.lclgvUpperCd;
				if(upperCd) this.getLclgvList(upperCd);

			}
		},
        data() {
            return {
				lclgvUpperList: [
                    { nm: '서울특별시', lclgvUpperCd: '11000', textClass: ' text ', imgName: 'vtmap00.png'}
                    , { nm: '부산광역시', lclgvUpperCd: '26000', textClass: ' text15 ', imgName: 'vtmap01.png' }
                    , { nm: '대구광역시', lclgvUpperCd: '27000', textClass: ' text10 ', imgName: 'vtmap02.png' }
                    , { nm: '인천광역시', lclgvUpperCd: '28000', textClass: ' text1 ', imgName: 'vtmap03.png' }
                    // , { nm: '광주광역시', lclgvUpperCd: '29000', textClass: ' text12 ', imgName: 'vtmap04.png' }
					, { nm: '전남광주통합특별시', lclgvUpperCd: '12000', textClass: ' text12 ', imgName: 'vtmap04.png' }
                    , { nm: '대전광역시', lclgvUpperCd: '30000', textClass: ' text7 ', imgName: 'vtmap05.png' }
                    , { nm: '울산광역시', lclgvUpperCd: '31000', textClass: ' text11 ', imgName: 'vtmap06.png' }
                    , { nm: '세종특별자치시', lclgvUpperCd: '36000', textClass: ' text6 ', imgName: 'vtmap07.png' }
                    , { nm: '경기도', lclgvUpperCd: '41000', textClass: ' text2 ', imgName: 'vtmap08.png' }
                    , { nm: '강원자치도', lclgvUpperCd: '51000', textClass: ' text3 ', imgName: 'vtmap09.png' }
                    , { nm: '충청북도', lclgvUpperCd: '43000', textClass: ' text5 ', imgName: 'vtmap10.png' }
                    , { nm: '충청남도', lclgvUpperCd: '44000', textClass: ' text4 ', imgName: 'vtmap11.png' }
                    , { nm: '전북특별자치도', lclgvUpperCd: '52000', textClass: ' text9 ', imgName: 'vtmap12.png' }
                    // , { nm: '전라남도', lclgvUpperCd: '46000', textClass: ' text13 ', imgName: 'vtmap13.png' }
                    , { nm: '경상북도', lclgvUpperCd: '47000', textClass: ' text8 ', imgName: 'vtmap14.png' }
                    , { nm: '경상남도', lclgvUpperCd: '48000', textClass: ' text16 ', imgName: 'vtmap15.png' }
                    , { nm: '제주특별자치도', lclgvUpperCd: '50000', textClass: ' text14 ', imgName: 'vtmap16.png' }
				],
				selectedLclgvUpperCd: '',
				selectedLclgvUpperNm: '시도를 선택해주세요.',
				lclgvList: [],
				lclgvInitList: [
                    { nm: '전체', lclgvCd: '',isExist:'Y'}
				],
				imgPath: '/static/images/lclgv_chc/',
				status: '',
            }
		},

		methods: {
			initLocgovCd: function () {
				this.lclgvList = this.lclgvInitList;
				this.selectedLclgvUpperNm = '시도를 선택해주세요.';
				this.selectedLclgvUpperCd = '';
				this.initSelectUi();
			},
            /** 시도 클릭시 */
			chooseLocal: function (lclgvUpperCd) {
                if (this.selectedLclgvUpperCd == lclgvUpperCd) {		// 선택된 시도를 다시 선택했을 경우 선택 해제
                    this.selectedLclgvUpperCd = '';
				} else {
                    this.selectedLclgvUpperCd = lclgvUpperCd;
				}
				this.initSelectUi();
                let lclgvInfo = this.findLclgvInfo(this.selectedLclgvUpperCd);
                if (lclgvInfo) {		// 선택된 시도 정보가 있을 경우
					this.$refs[lclgvUpperCd].setAttribute('class', 'addrName' + lclgvInfo.textClass + 'active');
                    this.$refs[lclgvUpperCd].setAttribute('title', lclgvInfo.nm + ' 지자체 선택 해제하기');
                    this.$refs.mapImg.setAttribute('src', this.imgPath + lclgvInfo.imgName);
                    this.selectedLclgvUpperNm = lclgvInfo.nm;
                    this.getLclgvList(lclgvUpperCd);
				} else {
					// 미선택 처리
                    this.selectedLclgvUpperNm = '시도를 선택해주세요.';
					this.lclgvList = this.lclgvInitList;
				}
			},
			/** UI 초기화 */
			initSelectUi: function () {
				let vm = this;
                let length = this.lclgvUpperList.length;
                vm.$refs.mapImg.setAttribute('src', this.imgPath + 'vtmap.png');
                for (let i = 0; i < length; i++) {
                    let lclgvInfo = this.lclgvUpperList[i];
                    vm.$refs[lclgvInfo.lclgvUpperCd].setAttribute('class', 'addrName' + lclgvInfo.textClass);
                    vm.$refs[lclgvInfo.lclgvUpperCd].setAttribute('title', lclgvInfo.nm + ' 시군구 조회하기');
                }

				// 시군구 영역 초기화
			},
            /** 시도 코드로 시도 객체 정보 찾기 */
            findLclgvInfo: function (lclgvUpperCd) {
                let length = this.lclgvUpperList.length;
				for (let i = 0; i < length; i++) {
                    let lclgvInfo = this.lclgvUpperList[i];
                    if (lclgvInfo.lclgvUpperCd == lclgvUpperCd) {
						return lclgvInfo;
					}
				}
				return null;
			},
			getLclgvList: function (lclgvUpperCd) {
				let vm = this;
				this.lclgvList = [];
				this.$emit('show-loading', true);
                $s.api.getSiGunGu({ sidoCode: lclgvUpperCd, prjStatus : this.status },
                    function (response) {
                        vm.$emit('show-loading', false);
						let data = response.resultList.cityList;
						if (data && data.length > 0) {
							let length = data.length;
							for (let i = 0 ; i < length ; i++) {
								let orgData = data[i];
								let lclgvInfo = { nm: orgData.cityName, lclgvCd: orgData.cityCode, isExist: orgData.isExist };
                                vm.lclgvList.push(lclgvInfo);
							}

							//[웹접근성]시군구 포커스 이동위해 tabindex 속성 제거
							let addrNames = document.querySelectorAll('div.addrName');
							addrNames.forEach(el => {
								el.removeAttribute('tabindex');
							});

						} else {
							$s.alert('시군구 정보 조회에 실패했습니다.');
						}
                    },
                    function (error) {
                        vm.$emit('show-loading', false);
                        $s.api.handleApiExeption(error);
					}
				);
			},
			selectLclgv: function (lclgvCd, lclgvNm) {
                let upperLclgvNm = this.selectedLclgvUpperNm;
                if (!lclgvCd) {
					upperLclgvNm = '';
					lclgvNm = '';
                }
				console.log('select-lclgv', this.selectedLclgvUpperCd, lclgvCd, upperLclgvNm, lclgvNm);
                this.$emit('select-lclgv', this.selectedLclgvUpperCd, lclgvCd, upperLclgvNm, lclgvNm);
			},
            keyChooseLocal: function (event, lclgvUpperCd) {
				let keyCode = event.code;
                if (keyCode == 'Enter' || keyCode == 'Space') {
                    this.chooseLocal(lclgvUpperCd);
				}
			},
			keySelectLclgv: function (event, lclgvCd, lclgvNm) {
				let keyCode = event.code;
                if (keyCode == 'Enter' || keyCode == 'Space') {
                    this.selectLclgv(lclgvCd, lclgvNm);
				}

				/*
				// [웹접근성] 마지막 지자체 요소 접근시 상위 지자체 영역에 tabindex 0 속성 복구
				if(keyCode == 'Tab'){
					const lastElText = event.target.parentNode.parentNode.lastElementChild.innerText;
					let currentElText = event.target.innerText;

					if(lastElText == currentElText){
						let addrNames = document.querySelectorAll('div.addrName');
						addrNames.forEach(el => {
							el.setAttribute('tabindex','0');
						});
					}
				}
				*/
			},
        },
        mounted() {
            this.$nextTick(function () {
                // 페이지 로딩 후 실행
                Saleson.init();
                this.lclgvList = this.lclgvInitList;
                this.status = this.prjStatus;
            });
        },
    };
</script>

<style>
	a.disabled {
		cursor : not-allowed !important;
		color : #757575 !important;
		text-decoration : none !important;
	}
</style>