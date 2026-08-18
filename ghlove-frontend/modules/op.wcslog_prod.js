var nfl = '';
if(location.pathname.indexOf("step2.html") < 0){
    nfl += '<script type="text/javascript" src="//wcs.naver.net/wcslog.js"></script>';
}
nfl +='<script type="text/javascript">\n'
    +'if (!wcs_add) var wcs_add={};\n'
    +'wcs_add["wa"] = "s_f9843f39efb";\n';
if(location.pathname.indexOf("step2.html") > 0){
// 주문완료
    nfl += 'if(!_nasa) var _nasa = {};\n'
        +'_nasa["cnv"] = wcs.cnv("1", '+vm.amt+')\n'
        +'wcs_do(_nasa);';
} else if(location.pathname.indexOf("join-complete.html") > 0){
// 회원가입
    nfl += 'if(!_nasa) var _nasa = {};\n'
        +'_nasa["cnv"] = wcs.cnv("2", "1")\n'
        +'wcs_do(_nasa);';
} else if(location.pathname.indexOf("step1.html") > 0){
// 주문페이지(장바구니용)
    nfl += 'if(!_nasa) var _nasa = {};\n'
        +'_nasa["cnv"] = wcs.cnv("3", "1")\n'
        +'wcs_do(_nasa);';
} else {
    <!-- 공통 적용 스크립트 , 모든 페이지에 노출되도록 설치. 단 전환페이지 설정값보다 항상 하단에 위치해야함 -->
    nfl +='wcs.mileageWhitelist = [];'
        +'wcs.checkoutWhitelist = [];'
        +'wcs.inflow();\n'
        +'wcs_do();\n';
}
nfl+='</script>';
if(location.pathname.indexOf("step2.html") > 0){
    $("body").append(nfl);
} else {
    document.write(nfl);
}
