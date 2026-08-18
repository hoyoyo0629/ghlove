package saleson.shop.give.givestate.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CallState {
	private long present;
	private long userAmt;
	private long totalcntr;
	private long totalcntramt;
	private long onlinecntr;
	private long onlinecntramt;
	private long offlinecntr;
	private long offlinecntramt;
	private long totalpresent;
	private long totalpresentamt;
	private long totalCntrCal;
	private long totalCntrAmtCal;
	
	private long onlineprjcntr;
	private long onlineprjcntramt;
	private long offlineprjcntr;
	private long offlineprjcntramt;
	
	

	private long callAmt;
	private long callKookmin;
	private long callLov;
	private long callGiver;
	private long callNhbank;
	private long callTotal;
	private long frstregisterid;

	
}
