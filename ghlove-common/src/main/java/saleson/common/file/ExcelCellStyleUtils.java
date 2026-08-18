package saleson.common.file;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelCellStyleUtils {

	private final CellStyle titleStyle;
	private final CellStyle headerStyle;
	private final CellStyle dataStyle;
	private final SXSSFWorkbook workbook;

	public ExcelCellStyleUtils(SXSSFWorkbook workbook) {
		this.titleStyle		= createTitleStyle(workbook);
		this.headerStyle	= createHeaderStyle(workbook);
		this.dataStyle		= createDataStyle(workbook);
		this.workbook		= workbook;
	}

	public Cell title(Row row, int column, String value) {
		return createCell(row, column, titleStyle, value);
	}

	public Cell header(Row row, int column, String value) {
		return createCell(row, column, headerStyle, value);
	}

	public Cell header(Row row, int column, String value, String memo, Sheet sheet) {
		return createCell(row, column, headerStyle, value, memo, sheet);
	}

	public Cell data(Row row, int column, String value) {
		return createCell(row, column, dataStyle, value);
	}

	// 셀 생성
	private Cell createCell(Row row, int column, CellStyle style, String value) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
		cell.setCellStyle(style);

		return cell;
	}

	private Cell createCell(Row row, int column, CellStyle style, String value, String memo, Sheet sheet) {
		Cell cell = row.createCell(column);

		CreationHelper helper = workbook.getCreationHelper();
		Drawing<?> drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();

		Comment comment = drawing.createCellComment(anchor);
		comment.setString(helper.createRichTextString(memo));
		comment.setAddress(cell.getAddress());

		cell.setCellValue(value);
		cell.setCellStyle(style);
		cell.setCellComment(comment);

		return cell;
	}

	// titleRow 스타일 설정
	private CellStyle createTitleStyle(SXSSFWorkbook workbook) {
		CellStyle titleStyle = workbook.createCellStyle();
		Font titleFont = workbook.createFont();

		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 14);

		titleStyle.setFont(titleFont);
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return titleStyle;
	}

	// headerRow 스타일 설정
	private CellStyle createHeaderStyle(SXSSFWorkbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();

		headerFont.setBold(true);
		headerStyle.setFont(headerFont);

		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return headerStyle;
	}

	// 데이터 중앙 정렬
	private CellStyle createDataStyle(SXSSFWorkbook workbook) {
		CellStyle dataStyle = workbook.createCellStyle();

		dataStyle.setAlignment(HorizontalAlignment.CENTER);
		dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return dataStyle;
	}


}
