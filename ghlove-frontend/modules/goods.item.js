var $goodsItem = {
  const: {},
  errorImage: function (e) {
    e.target.src = $s.config.noImage;
    e.target.classList.remove("item_img");
  },
  isShowCartIcon(v) {
    if ((v.adultItemYn === "Y" && this.adultYn === "N") || v.itemOptionFlag === "Y" || v.itemSoldOutFlag === "Y") return false;

    return true;
  },
  addToWishList(e, itemId) {
    e.stopPropagation(); // 상위 a tag 동작하지 않게...
    try {
      this.checkLogin({ loginPage: true });
      let obj = e.target;

      let id = $(obj).attr("id");

      vm.showLoading(true);

      if ($(obj).hasClass("on")) {
        $s.api.removeToWishList(
          id,
          function (response) {
            vm.showLoading(false);
            if (response.status == "OK") {
              $(obj).removeClass("on");
              $s.toast("해당 답례품이 관심답례품에서 삭제되었습니다.");
              $("#" + id).attr("title", "관심 답례품 해제 상태, 등록하기");
              $("#" + id).attr("aria-label", "관심 답례품 해제 상태, 등록하기");
            } else {
              $(obj).addClass("on");
              $s.toast("잠시 후 다시 시도해주세요.");
              $("#" + id).attr("title", "관심 답례품 등록 상태, 해제하기");
              $("#" + id).attr("aria-label", "관심 답례품 등록 상태, 해제하기");
            }
          },
          function (error) {
            vm.showLoading(false);
            $(obj).addClass("on");
            $s.api.handleApiExeption(error);
          }
        );
      } else {
        $s.api.addToWishList(
          $(obj).attr("id"),
          function (response) {
            vm.showLoading(false);
            if (response.status == "OK") {
              $(obj).addClass("on");
              $s.toast("해당 답례품이 관심답례품에 담겼습니다.");
              $("#" + id).attr("title", "관심 답례품 등록 상태, 해제하기");
              $("#" + id).attr("aria-label", "관심 답례품 등록 상태, 해제하기");
            } else {
              $(obj).removeClass("on");
              $s.toast("잠시 후 다시 시도해주세요.");
              $("#" + id).attr("title", "관심 답례품 해제 상태, 등록하기");
              $("#" + id).attr("aria-label", "관심 답례품 해제 상태, 등록하기");
            }
          },
          function (error) {
            vm.showLoading(false);
            $(obj).removeClass("on");
            $("#" + id).attr("title", "관심 답례품 해제 상태, 등록하기");
            $s.api.handleApiExeption(error);
          }
        );
      }
    } catch (e) {
      $s.error();
    }
  },
  dateFormat(val) {
    return val ? val.substring(0, 8).replace(/(\d{4})(\d{2})(\d{2})/, "$1.$2.$3") : "";
  },
  addToCart(itemId, adultItemYn, adultYn, focusId) {
    try {
      this.checkLogin({ loginPage: true });

      if (adultItemYn == "Y" && adultYn != "Y") {
        $s.alert("19세 미만의 청소년은 이용할 수 없습니다.", focusId);
        return;
      }

      let param = {
        itemId: itemId,
        quantity: 1,
      };
      $s.api.addToCart(param, function (response) {
        try {
          vm.$refs.layoutHeader.getCartInfo();
        } catch (e) {
          $s.error();
        }
        if (response.status == "OK") {
          $s.alert("장바구니에 넣었습니다.", focusId);
        } else {
          $s.alert("실패했습니다.", focusId);
        }
      });
    } catch (e) {
      $s.error();
    }
  },
  getLocGovInfo(cityCode) {
    if (!cityCode) {
      return;
    }

    //location.href = "/donation/donation.html?locgovCode=" + cityCode;
    $s.donation.goDonationPage("?locgovCode=" + cityCode);
  },
  checkLogin(options) {
    //this.requestContext = $s.core.parseUrl(location.href);
    //$s.debug(this.requestContext);

    // 인증설정
    try {
      var isLoginPage = false;
      var isGuestLoginPage = false;
      var initSeoFlag = true;

      if (typeof options != "undefined") {
        try {
          isLoginPage = options.loginPage;
          isGuestLoginPage = options.guestLoginPage;
          initSeoFlag = options.initSeoFlag;
        } catch (e) {
          $s.error();
        }
      }

      $s.core.authenticationFilter(function () {
        if (!$s.pages.isAllowAnonymous()) {
          var alertFlag;
          if (isGuestLoginPage) {
            alertFlag = !$s.isGuestLogin();
          } else {
            alertFlag = isLoginPage;
          }

          if (alertFlag) {
            $s.authenticationException("로그인 후 이용이 가능합니다.");
          }
        }
      });
    } catch (e) {
      $s.handleException(e);
      throw e;
    }
  },
  loadSiGungu(sidoCode) {
    return new Promise((resolve, reject) => {
      $s.api.getSiGunGu(
        { sidoCode },
        function (response) {
          let data = [{ cityCode: "", cityName: "시·군·구 선택" }];
          response.resultList.cityList.forEach((v, i, arr) => {
            data.push({ cityCode: v.cityCode, cityName: v.cityName });
          });
          resolve(data);
        },
        function (error) {
          $s.api.handleApiExeption(error);
          reject(error);
        }
      );
    });
  },
};
