package saleson.common.file.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.common.enumeration.ProgramName;

@Data
@NoArgsConstructor
public class DownloadProgramDataParam {

	private ProgramName programName;
	
	private long programId;
	
	private int fileSeq;
	
	private String fileName;
	
	private String orgFileName;
	
}
