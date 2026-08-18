package saleson.common.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

import saleson.common.userauth.UserAuthService;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.domain.Seller;

public class ExcelDownloadView  extends AbstractView {

	private final UserAuthService userAuthService;

	public ExcelDownloadView(UserAuthService userAuthService) {
		this.userAuthService = userAuthService;
		setContentType(
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
				);
	}

	@Override
	// DispatcherServlet 에서 현재 클래스를 실행하게 되면, AbstractView 에서 해당 메서드를 실행(hook)
	// hook : framework가 호출
	protected void renderMergedOutputModel(
			Map<String, Object> model,
			HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {

		String password;
		if (ShopUtils.isSellerPage()) {
			// 엑셀 다운로드 비밀번호 설정을 위한 사용자 로그인 아이디 조회(판매자)
			password			= SellerUtils.getSeller().getLoginId();
		} else {
			// 엑셀 다운로드 비밀번호 설정을 위한 사용자 생년월일 조회(관리자)
			password			= UserUtils.getLoginId();
		}
		SXSSFWorkbook workbook	= (SXSSFWorkbook) model.get("workbook");
		String fileName			= (String) model.get("fileName");

		// 암호화 되지 않은 상태의 데이터를 디스크에 저장해두기 위한 파일 생성
		File plainFile			= null;
		// 암호화된 데이터를 디스크에 저장해두기 위한 파일 생성
		File encryptedFile		= null;

		// 비밀번호 조회가 불가능 한 경우, 엑셀 다운로드 취소 및 alert 전송
		if (password == null) {
			streamFileToResponse(plainFile, response, fileName, password);
			return;
		}

		try {
			// plainFile 생성
			plainFile = File.createTempFile("excel_plain_temp_file_", ".xlsx");

			// plainFile 작성을 위한 출력 스트림 인스턴스 생성(plainFile 작성을 위한 통로 생성/fos)
			try (FileOutputStream fos = new FileOutputStream(plainFile)) {
				// workbook 의 데이터를 plainFile 에 작성(스트리밍)
				// workbook 의 데이터를 fos 통로를 통해 작성(fos 가 plainFile 에 작성을 위한 통로)
				workbook.write(fos);
			} finally {
				// workbook 으로 인해 생성된 임시 파일 삭제 및 메모리 제거
				workbook.dispose();
			}

			// 디스크의 plainFile 과 암호화를 위한 비밀번호를 변수로 담아 데이터 암호화 로직 수행
			encryptedFile = encryptXlsxFile(plainFile, password);
			// 디스크에 암호화된 encryptedFile 을 response 에 담아서 전송하기 위한 로직 수행
			streamFileToResponse(encryptedFile, response, fileName, password);
		} finally {
			// 임시 파일을 삭제하여 디스크 누수 방지
			if (plainFile		!= null) plainFile.delete();
			if (encryptedFile	!= null) encryptedFile.delete();
		}
	}

	// 평문 ".xlsx" 파일을 암호화 파일로 변경
	private File encryptXlsxFile(File plainFile, String password) throws Exception {
		// 암호화된 파일을 저장할 물리적 임시 파일 생성
		// 디스크에 저장
		File encryptedFile 		= File.createTempFile("excel_encrypt_temp_file_", ".xlsx");
		// Apache POI Project
		// - Apache Software Foundation 에서 관리
		// - Java에서 Microsoft Office 문서 처리(생성, 읽기, 수정)를 위한 오픈소스 라이브러리
		// POIFSFileSystems 는 OLE2 컨테이너()를 생성
		// 생성된 컨테이너에 암호화된 ZIP(XML(텍스트 파일)을 압축 = .xlsx)을 담는 역할
		// .xlsx 확장자를 가진다는 것은 OOXML 규칙에 맞게 패키징 된 ZIP 상태로 본다.
		POIFSFileSystem fs 		= new POIFSFileSystem();
		// EncryptionMode.agile(암호화 형태)
		// - Excel 2010+ 표준
		// - 암호화, 해시 알고리즘 포함
		// - 현재 유일하게 권장되는 방식
		EncryptionInfo info 	= new EncryptionInfo(EncryptionMode.agile);
		// 선정된 안호화 방식으로 암호화를 수행하는 인스턴스 생성
		// 비밀번호 미 선정 상태
		Encryptor encryptor		= info.getEncryptor();
		// 암호화 수행 시 사용될 비밀번호 설정(암호화 키 및 랜덤 솔트 생성)
		encryptor.confirmPassword(password);

		try (
			// OPCPackage
			// - .xlsx 파일을 zip 형태로 열기 위한 클래스(즉, 파일이 아닌 모델이다.)
			// - OOXML 규칙에 따라 해석된 패키지 구조로 파일을 읽고 안전하게 수정
			// - 무결성 보장
			OPCPackage opc 			= OPCPackage.open(plainFile, PackageAccess.READ_WRITE);
			// 암호화된 데이터를 OLE2 컨테이너에 담기위한 내부 가상 스트림
			OutputStream encOut 	= encryptor.getDataStream(fs);
		) {
			// opc 가 zip 엔트리를 하나씩 읽고 암호화 실행
			// opc.save() 는 패키지를 파일 형태로 직렬화 하여 저장
			// OPCPackage는 OOXML 패키지 모델로 파일이 아니며, 저장 대상이 필요(저장 대상이 POIFSFileSystem = OLE2 Container)
			// 암호화된 데이터를 encOut 스트림으로 fs 내부 구조로 전달
			// fs 가 모든 데이터를 메모리에 적재(메모리 이슈 발생이 생긴다면, fs 즉, POIFSFileSystem 이슈)
			opc.save(encOut);
		}

		try (
			// encryptedFile 작성을 위해 출력 스트림 인스턴스 생성
			FileOutputStream fos 	= new FileOutputStream(encryptedFile)
		) {
			// POIFSFileSystem 내부 구조를 OLE2 파일 포맷으로 직렬화
			// 실제 디스크 파일 생성
			fs.writeFilesystem(fos);
			// close 하지 않으면, GC 가 동작 하기 전까지 메모리 유지
			fs.close();
		}


		return encryptedFile;
	}

	private void streamFileToResponse(File encryptedFile, HttpServletResponse response, String fileName, String password) throws IOException {
		// password 조회가 불가능 한 경우
		if (password == null) {
			// response 초기화
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");

			PrintWriter writer = response.getWriter();
			writer.write("<!DOCTYPE html>");
			writer.write("<html><head><meta charset='UTF-8'></head><body>");
			writer.write("<script>");
			writer.write("alert('비밀번호 생성이 불가능합니다.\\n마이페이지에서 개인정보 수정 후 다시 시도해주세요.');");
			writer.write("history.back();");
			writer.write("</script>");
			writer.write("</body></html>");
			writer.flush();

			return;
		}

		// response의 header setting
		String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
		response.setHeader(
				"Content-Disposition",
				"attachment; filename*=UTF-8''" + encodedFileName
				);

		try (
			// 디스크의 파일을 읽기위한 스트림 인스턴스 생성
			// new BufferedInputStream : 내부 버퍼를 사용하여 디스크 I/O 호출 횟수 감소
			InputStream in	= new BufferedInputStream(new FileInputStream(encryptedFile));
			// 서블릿 컨테이너에 버퍼를 기록 및 클라이언트 전송을 위한 스트림 인스턴스 생성
			OutputStream os = response.getOutputStream();
		) {
			// 전송 데이터를 안정적으로 전송하기위해 버퍼 생성
			// 메모리 사용과 성능의 균형을 위해 8KB로 설정
			byte[] buffer = new byte[8192];
			// 마지막 끝부분 데이터를 입력할 때, 변수로 활용
			int len;

			while((len = in.read(buffer)) != -1) {
				// 파라미터 의미
				// buffer : 파일에서 읽은 원본 바이트 데이터
				// 0 : 읽어들인 데이터 배열의 index(어디서부터 읽을건지 설정)
				// len : 현재 읽은 바이트 수(실제 1192B 를 읽은 경우, buffer 가 8KB로 설정되어있어 len이 없으면 나머지 7000B 에 대한 알 수 없는 데이터가 입력될 수 있음)
				os.write(buffer, 0, len);
			}

			// 클라이언트에게 파일 전송
			os.flush();
		}
	}
}
