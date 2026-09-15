var API_DOMAIN = 'http://localhost:9080';
var CDN_DOMAIN = 'http://localhost:8080';
var VIRTUAL_DOMAIN = 'http://localhost:3000';
var OZ_DOMAIN = 'http://localhost:12000';
var SERVER_POSITION = 'local';
var IS_USE_NET_FUNNEL = false;
var IS_USE_ONEPASS = false;
// 본인인증(SCI 휴대폰/금융인증서) 외부 연동을 건너뛰고 고정 테스트 정보로 인증 성공 처리.
// 로컬에서만 true. 외부망이 없는 환경에서 회원가입/기부 흐름을 태우기 위한 것.
var IS_SKIP_EXTERNAL_AUTH = true;
var KAKAO_JAVASCRIPT_KEY = 'dd50d625a46ffbfef8c8b7377dbb4eb3';
var KAKAO_REDIRECT_URL = VIRTUAL_DOMAIN + '/users/login.html';
var KAKAO_REDIRECT_URL2 = VIRTUAL_DOMAIN + '/users/join.html';
var KAKAO_SETTLE_ID = '6985c6dc-1bdb-4021-946f-1e096e5f4835';
//var DUPLICATION_CALL_LIMIT = 5000;			// 중복 호출 가능 최소 시간(milliseconds)