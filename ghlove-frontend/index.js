// 기본 설정
const express = require("express");
const cors = require("cors");
const app = express();
const PORT = 3000;

app.use(cors({
    origin: '*', // 모든 출처 허용 옵션. true 를 써도 된다.
}));

// 정적 파일 불러오기
app.use(express.static(__dirname + "/"));

// 라우팅 정의
// main
app.get("/", (req, res) => {
  	res.sendFile(__dirname + "/index.html");
});

// 서버 실행
app.listen(PORT, () => {

});