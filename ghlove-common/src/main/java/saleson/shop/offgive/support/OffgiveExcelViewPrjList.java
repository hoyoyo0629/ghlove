package saleson.shop.offgive.support;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFCreationHelper;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.code.domain.Code;
import saleson.shop.designateddonation.domain.DesignatedDonation;

public class OffgiveExcelViewPrjList extends AbstractSXSSFExcelView{

	//private final String storageResourceLocation = "file:///C:/workspace/ghlove/ghlove-web/storage/upload/prj/";

	private List<DesignatedDonation> list;
	private List<Code> bsnsList;
	private List<Code> subBsnsList;

	public OffgiveExcelViewPrjList() {

		//1. 파일명 생성
		setFileName("지정기부목록_" + DateUtils.getToday(Const.DATE_FORMAT) + ".xlsx");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void buildExcelDocument(Map<String, Object> model, SXSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

//		String storageResourceLocation = model.get("storageResourceLocation").toString();
		list = (List<DesignatedDonation>) model.get("list");
		bsnsList = (List<Code>) model.get("bsnsList");
		subBsnsList = (List<Code>) model.get("subBsnsList");

		//2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list, bsnsList, subBsnsList);
	}

	//텍스트 엑셀
	private void buildItemSheet(SXSSFWorkbook workbook, List<DesignatedDonation> list
								,List<Code> bsnsList, List<Code> subBsnsList) throws ParseException {

		if (list == null) return;

		//3. 시트생성
		String sheetTitle = "특정사업 기부";
		String title = "특정사업 기부목록";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(500, 	"No."),
			new HeaderCell(7500, 	"사업구분"),
			new HeaderCell(3500, 	"사업부문"),
			new HeaderCell(3500, 	"지자체명"),
			new HeaderCell(3500, 	"사업명"),
			new HeaderCell(2000, 	"시작일"),
			new HeaderCell(2000, 	"종료일"),
			new HeaderCell(3000, 	"목표금액"),
			new HeaderCell(3000, 	"모금액"),
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(sheetTitle);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title);

		// 테이블
		int rowIndex = 2;
		int index = 0;

		//지정기부 사업구분 코드 조회
		for(DesignatedDonation obj : list) {

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			//사업구분코드 조회
			String bsnsName = "";
			for(Code cd : bsnsList) {
				if(cd.getId().equals(obj.getBsnsType())){
					bsnsName = cd.getDetail();
					break;
				}
			}

			//사업부문코드 조회
			String subBsnsName ="";
			for(Code cd : subBsnsList) {
				if(cd.getId().equals(obj.getBsnsSubType())){
					subBsnsName = cd.getDetail();
					break;
				}
			}

			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
	        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

	        Date stDate = inputFormat.parse(String.valueOf(obj.getPrjStDt()));
	        Date edDate = inputFormat.parse(String.valueOf(obj.getPrjEdDt()));

			setText(sheet, rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, rowIndex, cellIndex, bsnsName);
			setText(sheet, rowIndex, cellIndex, subBsnsName);
			setText(sheet, rowIndex, cellIndex, obj.getLocgovNm());
			setTextLeft(sheet, rowIndex, cellIndex, obj.getPrjSubject());
			setText(sheet, rowIndex, cellIndex, outputFormat.format(stDate));
			setText(sheet, rowIndex, cellIndex, outputFormat.format(edDate));
			setTextRight(sheet, rowIndex, cellIndex, String.valueOf(obj.getTargetAmt()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ","));
			setTextRight(sheet, rowIndex, cellIndex, String.valueOf(obj.getSumAmt()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ","));

			rowIndex++;
		}
	}

	// 이미지 엑셀
	@SuppressWarnings("unused")
	private void buildItemSheetImg(SXSSFWorkbook workbook, List<DesignatedDonation> list
			,List<Code> bsnsList, List<Code> subBsnsList, String storageResourceLocation) throws ParseException {

		if (list == null) return;

		//3. 시트생성
		String sheetTitle = "특정사업 기부";
		String title = "특정사업 기부목록";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
				new HeaderCell(500, 	"No."),
				new HeaderCell(5000, 	"이미지"),
				new HeaderCell(7500, 	"사업구분"),
				new HeaderCell(3500, 	"사업부문"),
				new HeaderCell(3500, 	"지자체명"),
				new HeaderCell(3500, 	"사업명"),
				new HeaderCell(2000, 	"시작일"),
				new HeaderCell(2000, 	"종료일"),
				new HeaderCell(3000, 	"목표금액"),
				new HeaderCell(3000, 	"모금액"),
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(sheetTitle);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title);

		// 테이블
		int rowIndex = 2;
		int index = 0;

		//지정기부 사업구분 코드 조회
		for(DesignatedDonation obj : list) {

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 3000);

			//Cell index 지정
			CellIndex cellIndex;
			cellIndex = new CellIndex(-1);

			//사업구분코드 조회
			String bsnsName = "";
			for(Code cd : bsnsList) {
				if(cd.getId().equals(obj.getBsnsType())){
					bsnsName = cd.getDetail();
					break;
				}
			}

			//사업부문코드 조회
			String subBsnsName ="";
			for(Code cd : subBsnsList) {
				if(cd.getId().equals(obj.getBsnsSubType())){
					subBsnsName = cd.getDetail();
					break;
				}
			}

			// No
			setText(sheet, rowIndex, cellIndex, String.valueOf(++index));

			// 이미지 경로
			String imgName = obj.getPrjImage().replace("XS", "M"); // 이미지 사이즈 XS -> M 으로 변경
			String imgUrl = storageResourceLocation+"/upload/prj/"+obj.getPrjId()+"/"+imgName ;

			// 이미지 넣기
			imgHandler(workbook,sheet,imgUrl,rowIndex);

			//Cell index 변경
			cellIndex = new CellIndex(1);

			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

			Date stDate = inputFormat.parse(String.valueOf(obj.getPrjStDt()));
			Date edDate = inputFormat.parse(String.valueOf(obj.getPrjEdDt()));

			setText(sheet, rowIndex, cellIndex, bsnsName);
			setText(sheet, rowIndex, cellIndex, subBsnsName);
			setText(sheet, rowIndex, cellIndex, obj.getLocgovNm());
			setText(sheet, rowIndex, cellIndex, obj.getPrjSubject());
			setText(sheet, rowIndex, cellIndex, outputFormat.format(stDate));
			setText(sheet, rowIndex, cellIndex, outputFormat.format(edDate));
			setNumberFormat(sheet, rowIndex, cellIndex, (int) obj.getTargetAmt());
			setNumberFormat(sheet, rowIndex, cellIndex, (int)obj.getSumAmt());

			rowIndex++;
		}
	}

	public void imgHandler(SXSSFWorkbook workbook, Sheet sheet, String imgUrl, int rowIndex ) {

		//이미지 넣기
		SXSSFCreationHelper helper = (SXSSFCreationHelper) workbook.getCreationHelper();
		SXSSFDrawing drawing = (SXSSFDrawing) sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		int pictureIdx = 0;

		try {

			InputStream is = new URL(imgUrl).openStream();
			byte[] bytes = IOUtils.toByteArray(is);

			pictureIdx = workbook.addPicture(bytes, SXSSFWorkbook.PICTURE_TYPE_JPEG);
			anchor.setCol1(1);
			anchor.setCol2(2);
			anchor.setRow1(rowIndex);

			drawing.createPicture(anchor, pictureIdx).resize(1, 1);
		}catch (IOException e) {
			e.getStackTrace();
		}
	}
}
