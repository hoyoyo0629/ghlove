package saleson.shop.coupon.support;

import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CouponItemExcelView extends AbstractSXSSFExcelView {

    public CouponItemExcelView() {
        setFileName("COUPON_ITEM_SAMPLE.xlsx");
    }

    @Override
	public void buildExcelDocument(Map<String, Object> model,
                                      SXSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

    	//미사용 주석처리(시큐어코딩 관련)
//        Cookie cookie = new Cookie("DOWNLOAD_STATUS", "complete");
//        cookie.setSecure(true);
//		cookie.setHttpOnly(true);
//        cookie.setPath("/");					// 모든 경로에서 접근 가능하도록
//        response.addCookie(cookie);				// 쿠키저장


        // 1. 데이터 가져오기

        // 2. 시트별 데이터 생성
        buildCouponItemSheet(workbook);
    }

    private void buildCouponItemSheet(SXSSFWorkbook workbook) {

        // 3. 시트 생성
        String sheetTitle = "쿠폰 상품 (SAMPLE)";

        // 4. Cell (컬럼) 설정
        HeaderCell[] headerCells = new HeaderCell[] {
                new HeaderCell(6000, 	"상품번호(*)", 		"사용중인 상품번호를 입력해주세요., (EX: G2000481912)")
        };

        /**
         * 상단 타이틀 및 테이블 헤더 생성.
         */
        Sheet sheet = workbook.createSheet("sample");
        Row row = sheet.createRow((short) 0);

        ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();

        row.setHeight((short) 800);
        int columCount = headerCells.length;

        for(int i = 0; i < headerCells.length; ++i) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + headerCells[i].getWidth());
        }

        Cell[] cells = new Cell[columCount];

        for(int i = 0; i < cells.length; ++i) {
            cells[i] = row.createCell(i);
            cells[i].setCellStyle(this.titleStyle);
        }

        cells[0].setCellValue(sheetTitle);
        row = sheet.createRow(1);
        row.setHeight((short) 512);

        for (int i = 0; i < cells.length; ++i) {
            cells[i] = row.createCell(i);
            cells[i].setCellStyle(this.headerStyle);
            cells[i].setCellValue(headerCells[i].getTitle());
            if (headerCells[i].getComment() != null && !headerCells[i].getComment().equals("")) {
                this.setComment(cells[i], 1, i, headerCells[i].getComment().replace(", ", ",").replace(",", "\n"), headerCells[i].getCommentCol(), headerCells[i].getCommentRow());
            }
        }
    }
}
