package saleson.common.file;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import saleson.common.enumeration.ProgramName;
import saleson.common.file.service.CustomFileService;
import com.privacy.pCrypto;

@Controller
@RequestMapping("/opmanager/fileDownload")
public class ProgramFileDownloadController {

	private static Logger log = LoggerFactory.getLogger(ProgramFileDownloadController.class);

	@Autowired
	private CustomFileService customFileService;


	/**
	 * 서버에 저장된 파일명이 아닌 다른 파일명으로 다운로드 진행해야 할 경우 사용
	 * @param programName		프로그램 명
	 * @param programId			프로그램 아이디(pk id)
	 * @param fileSeq			파일 순번(pk id 하나에 이미지 여러 개 저장가능할 경우)
	 * @param fileName			서버에 저장된 파일명
	 * @param orgFileName		다운로드시 설정할 파일명
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/downloadByProgramData")
	@ResponseBody
	public ResponseEntity<byte[]> downloadByProgramData(@RequestParam(required = true) String programName
														, @RequestParam(required = false, defaultValue = "0") long programId
														, @RequestParam(required = false, defaultValue = "0") int fileSeq
														, @RequestParam(required = false, defaultValue = "") String fileName
														, @RequestParam(required = false, defaultValue = "") String orgFileName) throws IOException {


		ProgramName programNameEnum = ProgramName.valueOf(programName);
		if (fileName != null && orgFileName != null) {

			fileName = pCrypto.Decrypt("normal", fileName, "", 0);

			fileName = fileName.replaceAll("../", "").replaceAll("..\\\\", "");
			orgFileName = orgFileName.replaceAll("../", "").replaceAll("..\\\\", "");
		}
		switch (programNameEnum) {
			case PRJ_NOTICE:
//			case HNR_USER_MNG:
				return customFileService.getFileDownloadDataByProgramNameIdFileOrgName(programNameEnum.getProgramPath()
																						, programId
																						, fileName
																						, orgFileName);
			default:
				return null;
		}
	}


}
