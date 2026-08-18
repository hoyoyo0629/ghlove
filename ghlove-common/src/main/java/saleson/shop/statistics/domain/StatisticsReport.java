package saleson.shop.statistics.domain;

import com.onlinepowers.framework.util.StringUtils;

public class StatisticsReport {
	
	/**
	 * 공용
	 */
	private String crtrYr;	//기준년도
	private String crtrMm;	//기준월
	private String mctpvNm;	//광역자치단체명
	private String mctpvCd;	//광역자치단체코드
	private String lclgvNm;	//지방자치단체명
	private String lclgvCd;	//지방자치단체코드
	
	/**
	 * 회원 수 현황
	 */
	private long mbrTnocs;	// 회원수 합계
	private long mbrCntJan;	// 회원수_1월
	private long mbrCntFeb;	// 회원수_2월
	private long mbrCntMar;	// 회원수_3월
	private long mbrCntApr;	// 회원수_4월
	private long mbrCntMay;	// 회원수_5월
	private long mbrCntJun;	// 회원수_6월
	private long mbrCntJul;	// 회원수_7월
	private long mbrCntAug;	// 회원수_8월
	private long mbrCntSep;	// 회원수_9월
	private long mbrCntOct;	// 회원수_10월
	private long mbrCntNov;	// 회원수_11월
	private long mbrCntDec;	// 회원수_12월
	
	/**
	 * 기부 및 답례품 현황
	 */
	private long nowYr;					// 올해 총계
	private long prvyr;					// 전년도 총계
	private long nowYrJan;				// 올해_월
	private long prvyrJan;				// 전년도_월
	private String prvyrNowYrRtJan;		// 전년대비_월
	private long nowYrFeb;
	private long prvyrFeb;
	private String prvyrNowYrRtFeb;
	private long nowYrMar;
	private long prvyrMar;
	private String prvyrNowYrRtMar;
	private long nowYrApr;
	private long prvyrApr;
	private String prvyrNowYrRtApr;
	private long nowYrMay;
	private long prvyrMay;
	private String prvyrNowYrRtMay;
	private long nowYrJun;
	private long prvyrJun;
	private String prvyrNowYrRtJun;
	private long nowYrJul;
	private long prvyrJul;
	private String prvyrNowYrRtJul;
	private long nowYrAug;
	private long prvyrAug;
	private String prvyrNowYrRtAug;
	private long nowYrSep;
	private long prvyrSep;
	private String prvyrNowYrRtSep;
	private long nowYrOct;
	private long prvyrOct;
	private String prvyrNowYrRtOct;
	private long nowYrNov;
	private long prvyrNov;
	private String prvyrNowYrRtNov;
	private long nowYrDec;
	private long prvyrDec;
	private String prvyrNowYrRtDec;
	
	private String dntnPath;
	private String dntnAmt;
	private String dntnAge;
	private String habMctpvNm;
	private String dntnAmtCd;
	private String dntnAgeCd;
	private long gramt;
	private String gramtrt;
	private long tnocs;
	private String rt;
	
	private long dntnCntSeoul;
	private long dntnCntBusan;
	private long dntnCntDaegu;
	private long dntnCntIncheon;
	private long dntnCntGwangju;
	private long dntnCntDaejeon;
	private long dntnCntUlsan;
	private long dntnCntSejong;
	private long dntnCntGyeonggi;
	private long dntnCntChungbuk;
	private long dntnCntChungnam;
	private long dntnCntJeonnam;
	private long dntnCntGyeongbuk;
	private long dntnCntGyeongnam;
	private long dntnCntJeju;
	private long dntnCntGangwon;
	private long dntnCntJeonbuk;
	
	
	public StatisticsReport() {
		
	}
	public String getCrtrYr() {
		return crtrYr;
	}
	public void setCrtrYr(String crtrYr) {
		this.crtrYr = crtrYr;
	}
	public String getCrtrMm() {
		return crtrMm;
	}
	public void setCrtrMm(String crtrMm) {
		this.crtrMm = crtrMm;
	}
	public String getMctpvNm() {
		return mctpvNm;
	}
	public void setMctpvNm(String mctpvNm) {
		this.mctpvNm = mctpvNm;
	}
	public String getMctpvCd() {
		return mctpvCd;
	}
	public void setMctpvCd(String mctpvCd) {
		this.mctpvCd = mctpvCd;
	}
	public String getLclgvNm() {
		return lclgvNm;
	}
	public void setLclgvNm(String lclgvNm) {
		this.lclgvNm = lclgvNm;
	}
	public String getLclgvCd() {
		return lclgvCd;
	}
	public void setLclgvCd(String lclgvCd) {
		this.lclgvCd = lclgvCd;
	}
	public long getMbrTnocs() {
		return mbrTnocs;
	}
	public void setMbrTnocs(long mbrTnocs) {
		this.mbrTnocs = mbrTnocs;
	}
	public long getMbrCntJan() {
		return mbrCntJan;
	}
	public void setMbrCntJan(long mbrCntJan) {
		this.mbrCntJan = mbrCntJan;
	}
	public long getMbrCntFeb() {
		return mbrCntFeb;
	}
	public void setMbrCntFeb(long mbrCntFeb) {
		this.mbrCntFeb = mbrCntFeb;
	}
	public long getMbrCntMar() {
		return mbrCntMar;
	}
	public void setMbrCntMar(long mbrCntMar) {
		this.mbrCntMar = mbrCntMar;
	}
	public long getMbrCntApr() {
		return mbrCntApr;
	}
	public void setMbrCntApr(long mbrCntApr) {
		this.mbrCntApr = mbrCntApr;
	}
	public long getMbrCntMay() {
		return mbrCntMay;
	}
	public void setMbrCntMay(long mbrCntMay) {
		this.mbrCntMay = mbrCntMay;
	}
	public long getMbrCntJun() {
		return mbrCntJun;
	}
	public void setMbrCntJun(long mbrCntJun) {
		this.mbrCntJun = mbrCntJun;
	}
	public long getMbrCntJul() {
		return mbrCntJul;
	}
	public void setMbrCntJul(long mbrCntJul) {
		this.mbrCntJul = mbrCntJul;
	}
	public long getMbrCntAug() {
		return mbrCntAug;
	}
	public void setMbrCntAug(long mbrCntAug) {
		this.mbrCntAug = mbrCntAug;
	}
	public long getMbrCntSep() {
		return mbrCntSep;
	}
	public void setMbrCntSep(long mbrCntSep) {
		this.mbrCntSep = mbrCntSep;
	}
	public long getMbrCntOct() {
		return mbrCntOct;
	}
	public void setMbrCntOct(long mbrCntOct) {
		this.mbrCntOct = mbrCntOct;
	}
	public long getMbrCntNov() {
		return mbrCntNov;
	}
	public void setMbrCntNov(long mbrCntNov) {
		this.mbrCntNov = mbrCntNov;
	}
	public long getMbrCntDec() {
		return mbrCntDec;
	}
	public void setMbrCntDec(long mbrCntDec) {
		this.mbrCntDec = mbrCntDec;
	}
	public long getNowYr() {
		return nowYr;
	}
	public void setNowYr(long nowYr) {
		this.nowYr = nowYr;
	}
	public long getPrvyr() {
		return prvyr;
	}
	public void setPrvyr(long prvyr) {
		this.prvyr = prvyr;
	}
	public long getNowYrJan() {
		return nowYrJan;
	}
	public void setNowYrJan(long nowYrJan) {
		this.nowYrJan = nowYrJan;
	}
	public long getPrvyrJan() {
		return prvyrJan;
	}
	public void setPrvyrJan(long prvyrJan) {
		this.prvyrJan = prvyrJan;
	}
	public String getPrvyrNowYrRtJan() {
		if(StringUtils.hasLength(prvyrNowYrRtJan)) {
			return prvyrNowYrRtJan.replaceAll(",", "");
		} else {
			return prvyrNowYrRtJan;
		}
	}
	public void setPrvyrNowYrRtJan(String prvyrNowYrRtJan) {
		this.prvyrNowYrRtJan = prvyrNowYrRtJan;
	}
	public long getNowYrFeb() {
		return nowYrFeb;
	}
	public void setNowYrFeb(long nowYrFeb) {
		this.nowYrFeb = nowYrFeb;
	}
	public long getPrvyrFeb() {
		return prvyrFeb;
	}
	public void setPrvyrFeb(long prvyrFeb) {
		this.prvyrFeb = prvyrFeb;
	}
	public String getPrvyrNowYrRtFeb() {
		if(StringUtils.hasLength(prvyrNowYrRtFeb)) {
			return prvyrNowYrRtFeb.replaceAll(",", "");
		} else {
			return prvyrNowYrRtFeb;
		}
	}
	public void setPrvyrNowYrRtFeb(String prvyrNowYrRtFeb) {
		this.prvyrNowYrRtFeb = prvyrNowYrRtFeb;
	}
	public long getNowYrMar() {
		return nowYrMar;
	}
	public void setNowYrMar(long nowYrMar) {
		this.nowYrMar = nowYrMar;
	}
	public long getPrvyrMar() {
		return prvyrMar;
	}
	public void setPrvyrMar(long prvyrMar) {
		this.prvyrMar = prvyrMar;
	}
	public String getPrvyrNowYrRtMar() {
		if(StringUtils.hasLength(prvyrNowYrRtMar)) {
			return prvyrNowYrRtMar.replaceAll(",", "");
		} else {
			return prvyrNowYrRtMar;
		}
	}
	public void setPrvyrNowYrRtMar(String prvyrNowYrRtMar) {
		this.prvyrNowYrRtMar = prvyrNowYrRtMar;
	}
	public long getNowYrApr() {
		return nowYrApr;
	}
	public void setNowYrApr(long nowYrApr) {
		this.nowYrApr = nowYrApr;
	}
	public long getPrvyrApr() {
		return prvyrApr;
	}
	public void setPrvyrApr(long prvyrApr) {
		this.prvyrApr = prvyrApr;
	}
	public String getPrvyrNowYrRtApr() {
		if(StringUtils.hasLength(prvyrNowYrRtApr)) {
			return prvyrNowYrRtApr.replaceAll(",", "");
		} else {
			return prvyrNowYrRtApr;
		}
	}
	public void setPrvyrNowYrRtApr(String prvyrNowYrRtApr) {
		this.prvyrNowYrRtApr = prvyrNowYrRtApr;
	}
	public long getNowYrMay() {
		return nowYrMay;
	}
	public void setNowYrMay(long nowYrMay) {
		this.nowYrMay = nowYrMay;
	}
	public long getPrvyrMay() {
		return prvyrMay;
	}
	public void setPrvyrMay(long prvyrMay) {
		this.prvyrMay = prvyrMay;
	}
	public String getPrvyrNowYrRtMay() {
		if(StringUtils.hasLength(prvyrNowYrRtMay)) {
			return prvyrNowYrRtMay.replaceAll(",", "");
		} else {
			return prvyrNowYrRtMay;
		}
	}
	public void setPrvyrNowYrRtMay(String prvyrNowYrRtMay) {
		this.prvyrNowYrRtMay = prvyrNowYrRtMay;
	}
	public long getNowYrJun() {
		return nowYrJun;
	}
	public void setNowYrJun(long nowYrJun) {
		this.nowYrJun = nowYrJun;
	}
	public long getPrvyrJun() {
		return prvyrJun;
	}
	public void setPrvyrJun(long prvyrJun) {
		this.prvyrJun = prvyrJun;
	}
	public String getPrvyrNowYrRtJun() {
		if(StringUtils.hasLength(prvyrNowYrRtJun)) {
			return prvyrNowYrRtJun.replaceAll(",", "");
		} else {
			return prvyrNowYrRtJun;
		}
	}
	public void setPrvyrNowYrRtJun(String prvyrNowYrRtJun) {
		this.prvyrNowYrRtJun = prvyrNowYrRtJun;
	}
	public long getNowYrJul() {
		return nowYrJul;
	}
	public void setNowYrJul(long nowYrJul) {
		this.nowYrJul = nowYrJul;
	}
	public long getPrvyrJul() {
		return prvyrJul;
	}
	public void setPrvyrJul(long prvyrJul) {
		this.prvyrJul = prvyrJul;
	}
	public String getPrvyrNowYrRtJul() {
		if(StringUtils.hasLength(prvyrNowYrRtJul)) {
			return prvyrNowYrRtJul.replaceAll(",", "");
		} else {
			return prvyrNowYrRtJul;
		}
	}
	public void setPrvyrNowYrRtJul(String prvyrNowYrRtJul) {
		this.prvyrNowYrRtJul = prvyrNowYrRtJul;
	}
	public long getNowYrAug() {
		return nowYrAug;
	}
	public void setNowYrAug(long nowYrAug) {
		this.nowYrAug = nowYrAug;
	}
	public long getPrvyrAug() {
		return prvyrAug;
	}
	public void setPrvyrAug(long prvyrAug) {
		this.prvyrAug = prvyrAug;
	}
	public String getPrvyrNowYrRtAug() {
		if(StringUtils.hasLength(prvyrNowYrRtAug)) {
			return prvyrNowYrRtAug.replaceAll(",", "");
		} else {
			return prvyrNowYrRtAug;
		}
	}
	public void setPrvyrNowYrRtAug(String prvyrNowYrRtAug) {
		this.prvyrNowYrRtAug = prvyrNowYrRtAug;
	}
	public long getNowYrSep() {
		return nowYrSep;
	}
	public void setNowYrSep(long nowYrSep) {
		this.nowYrSep = nowYrSep;
	}
	public long getPrvyrSep() {
		return prvyrSep;
	}
	public void setPrvyrSep(long prvyrSep) {
		this.prvyrSep = prvyrSep;
	}
	public String getPrvyrNowYrRtSep() {
		if(StringUtils.hasLength(prvyrNowYrRtSep)) {
			return prvyrNowYrRtSep.replaceAll(",", "");
		} else {
			return prvyrNowYrRtSep;
		}
	}
	public void setPrvyrNowYrRtSep(String prvyrNowYrRtSep) {
		this.prvyrNowYrRtSep = prvyrNowYrRtSep;
	}
	public long getNowYrOct() {
		return nowYrOct;
	}
	public void setNowYrOct(long nowYrOct) {
		this.nowYrOct = nowYrOct;
	}
	public long getPrvyrOct() {
		return prvyrOct;
	}
	public void setPrvyrOct(long prvyrOct) {
		this.prvyrOct = prvyrOct;
	}
	public String getPrvyrNowYrRtOct() {
		if(StringUtils.hasLength(prvyrNowYrRtOct)) {
			return prvyrNowYrRtOct.replaceAll(",", "");
		} else {
			return prvyrNowYrRtOct;
		}
	}
	public void setPrvyrNowYrRtOct(String prvyrNowYrRtOct) {
		this.prvyrNowYrRtOct = prvyrNowYrRtOct;
	}
	public long getNowYrNov() {
		return nowYrNov;
	}
	public void setNowYrNov(long nowYrNov) {
		this.nowYrNov = nowYrNov;
	}
	public long getPrvyrNov() {
		return prvyrNov;
	}
	public void setPrvyrNov(long prvyrNov) {
		this.prvyrNov = prvyrNov;
	}
	public String getPrvyrNowYrRtNov() {
		if(StringUtils.hasLength(prvyrNowYrRtNov)) {
			return prvyrNowYrRtNov.replaceAll(",", "");
		} else {
			return prvyrNowYrRtNov;
		}
	}
	public void setPrvyrNowYrRtNov(String prvyrNowYrRtNov) {
		this.prvyrNowYrRtNov = prvyrNowYrRtNov;
	}
	public long getNowYrDec() {
		return nowYrDec;
	}
	public void setNowYrDec(long nowYrDec) {
		this.nowYrDec = nowYrDec;
	}
	public long getPrvyrDec() {
		return prvyrDec;
	}
	public void setPrvyrDec(long prvyrDec) {
		this.prvyrDec = prvyrDec;
	}
	public String getPrvyrNowYrRtDec() {
		if(StringUtils.hasLength(prvyrNowYrRtDec)) {
			return prvyrNowYrRtDec.replaceAll(",", "");
		} else {
			return prvyrNowYrRtDec;
		}
	}
	public void setPrvyrNowYrRtDec(String prvyrNowYrRtDec) {
		this.prvyrNowYrRtDec = prvyrNowYrRtDec;
	}
	public String getDntnPath() {
		return dntnPath;
	}
	public void setDntnPath(String dntnPath) {
		this.dntnPath = dntnPath;
	}
	public String getDntnAmt() {
		return dntnAmt;
	}
	public void setDntnAmt(String dntnAmt) {
		this.dntnAmt = dntnAmt;
	}
	public String getDntnAge() {
		return dntnAge;
	}
	public void setDntnAge(String dntnAge) {
		this.dntnAge = dntnAge;
	}
	public String getHabMctpvNm() {
		return habMctpvNm;
	}
	public void setHabMctpvNm(String habMctpvNm) {
		this.habMctpvNm = habMctpvNm;
	}
	public String getDntnAmtCd() {
		return dntnAmtCd;
	}
	public void setDntnAmtCd(String dntnAmtCd) {
		this.dntnAmtCd = dntnAmtCd;
	}
	public String getDntnAgeCd() {
		return dntnAgeCd;
	}
	public void setDntnAgeCd(String dntnAgeCd) {
		this.dntnAgeCd = dntnAgeCd;
	}
	public long getTnocs() {
		return tnocs;
	}
	public void setTnocs(long tnocs) {
		this.tnocs = tnocs;
	}
	public String getRt() {
		return rt;
	}
	public void setRt(String rt) {
		this.rt = rt;
	}
	public long getGramt() {
		return gramt;
	}
	public void setGramt(long gramt) {
		this.gramt = gramt;
	}
	public String getGramtrt() {
		return gramtrt;
	}
	public void setGramtrt(String gramtrt) {
		this.gramtrt = gramtrt;
	}
	public long getDntnCntSeoul() {
		return dntnCntSeoul;
	}
	public void setDntnCntSeoul(long dntnCntSeoul) {
		this.dntnCntSeoul = dntnCntSeoul;
	}
	public long getDntnCntBusan() {
		return dntnCntBusan;
	}
	public void setDntnCntBusan(long dntnCntBusan) {
		this.dntnCntBusan = dntnCntBusan;
	}
	public long getDntnCntDaegu() {
		return dntnCntDaegu;
	}
	public void setDntnCntDaegu(long dntnCntDaegu) {
		this.dntnCntDaegu = dntnCntDaegu;
	}
	public long getDntnCntIncheon() {
		return dntnCntIncheon;
	}
	public void setDntnCntIncheon(long dntnCntIncheon) {
		this.dntnCntIncheon = dntnCntIncheon;
	}
	public long getDntnCntGwangju() {
		return dntnCntGwangju;
	}
	public void setDntnCntGwangju(long dntnCntGwangju) {
		this.dntnCntGwangju = dntnCntGwangju;
	}
	public long getDntnCntDaejeon() {
		return dntnCntDaejeon;
	}
	public void setDntnCntDaejeon(long dntnCntDaejeon) {
		this.dntnCntDaejeon = dntnCntDaejeon;
	}
	public long getDntnCntUlsan() {
		return dntnCntUlsan;
	}
	public void setDntnCntUlsan(long dntnCntUlsan) {
		this.dntnCntUlsan = dntnCntUlsan;
	}
	public long getDntnCntSejong() {
		return dntnCntSejong;
	}
	public void setDntnCntSejong(long dntnCntSejong) {
		this.dntnCntSejong = dntnCntSejong;
	}
	public long getDntnCntGyeonggi() {
		return dntnCntGyeonggi;
	}
	public void setDntnCntGyeonggi(long dntnCntGyeonggi) {
		this.dntnCntGyeonggi = dntnCntGyeonggi;
	}
	public long getDntnCntChungbuk() {
		return dntnCntChungbuk;
	}
	public void setDntnCntChungbuk(long dntnCntChungbuk) {
		this.dntnCntChungbuk = dntnCntChungbuk;
	}
	public long getDntnCntChungnam() {
		return dntnCntChungnam;
	}
	public void setDntnCntChungnam(long dntnCntChungnam) {
		this.dntnCntChungnam = dntnCntChungnam;
	}
	public long getDntnCntJeonnam() {
		return dntnCntJeonnam;
	}
	public void setDntnCntJeonnam(long dntnCntJeonnam) {
		this.dntnCntJeonnam = dntnCntJeonnam;
	}
	public long getDntnCntGyeongbuk() {
		return dntnCntGyeongbuk;
	}
	public void setDntnCntGyeongbuk(long dntnCntGyeongbuk) {
		this.dntnCntGyeongbuk = dntnCntGyeongbuk;
	}
	public long getDntnCntGyeongnam() {
		return dntnCntGyeongnam;
	}
	public void setDntnCntGyeongnam(long dntnCntGyeongnam) {
		this.dntnCntGyeongnam = dntnCntGyeongnam;
	}
	public long getDntnCntJeju() {
		return dntnCntJeju;
	}
	public void setDntnCntJeju(long dntnCntJeju) {
		this.dntnCntJeju = dntnCntJeju;
	}
	public long getDntnCntGangwon() {
		return dntnCntGangwon;
	}
	public void setDntnCntGangwon(long dntnCntGangwon) {
		this.dntnCntGangwon = dntnCntGangwon;
	}
	public long getDntnCntJeonbuk() {
		return dntnCntJeonbuk;
	}
	public void setDntnCntJeonbuk(long dntnCntJeonbuk) {
		this.dntnCntJeonbuk = dntnCntJeonbuk;
	}
	public long getNowYrMJan() {
		return (int) Math.round((double)nowYrJan / 1000000);
	}
	public long getPrvyrMJan() {
		return (int) Math.round((double)prvyrJan / 1000000);
	}
	public long getNowYrMFeb() {
		return (int) Math.round((double)nowYrFeb / 1000000);
	}
	public long getPrvyrMFeb() {
		return (int) Math.round((double)prvyrFeb / 1000000);
	}
	public long getNowYrMMar() {
		return (int) Math.round((double)nowYrMar / 1000000);
	}
	public long getPrvyrMMar() {
		return (int) Math.round((double)prvyrMar / 1000000);
	}
	public long getNowYrMApr() {
		return (int) Math.round((double)nowYrApr / 1000000);
	}
	public long getPrvyrMApr() {
		return (int) Math.round((double)prvyrApr / 1000000);
	}
	public long getNowYrMMay() {
		return (int) Math.round((double)nowYrMay / 1000000);
	}
	public long getPrvyrMMay() {
		return (int) Math.round((double)prvyrMay / 1000000);
	}
	public long getNowYrMJun() {
		return (int) Math.round((double)nowYrJun / 1000000);
	}
	public long getPrvyrMJun() {
		return (int) Math.round((double)prvyrJun / 1000000);
	}
	public long getNowYrMJul() {
		return (int) Math.round((double)nowYrJul / 1000000);
	}
	public long getPrvyrMJul() {
		return (int) Math.round((double)prvyrJul / 1000000);
	}
	public long getNowYrMAug() {
		return (int) Math.round((double)nowYrAug / 1000000);
	}
	public long getPrvyrMAug() {
		return (int) Math.round((double)prvyrAug / 1000000);
	}
	public long getNowYrMSep() {
		return (int) Math.round((double)nowYrSep / 1000000);
	}
	public long getPrvyrMSep() {
		return (int) Math.round((double)prvyrSep / 1000000);
	}
	public long getNowYrMOct() {
		return (int) Math.round((double)nowYrOct / 1000000);
	}
	public long getPrvyrMOct() {
		return (int) Math.round((double)prvyrOct / 1000000);
	}
	public long getNowYrMNov() {
		return (int) Math.round((double)nowYrNov / 1000000);
	}
	public long getPrvyrMNov() {
		return (int) Math.round((double)prvyrNov / 1000000);
	}
	public long getNowYrMDec() {
		return (int) Math.round((double)nowYrDec / 1000000);
	}
	public long getPrvyrMDec() {
		return (int) Math.round((double)prvyrDec / 1000000);
	}
	public long getNowYrK() {
		return (int) Math.round((double)nowYr / 1000);
	}
	public long getPrvyrK() {
		return (int) Math.round((double)prvyr / 1000);
	}
	public long getNowYrKJan() {
		return (int) Math.round((double)nowYrJan / 1000);
	}
	public long getPrvyrKJan() {
		return (int) Math.round((double)prvyrJan / 1000);
	}
	public long getNowYrKFeb() {
		return (int) Math.round((double)nowYrFeb / 1000);
	}
	public long getPrvyrKFeb() {
		return (int) Math.round((double)prvyrFeb / 1000);
	}
	public long getNowYrKMar() {
		return (int) Math.round((double)nowYrMar / 1000);
	}
	public long getPrvyrKMar() {
		return (int) Math.round((double)prvyrMar / 1000);
	}
	public long getNowYrKApr() {
		return (int) Math.round((double)nowYrApr / 1000);
	}
	public long getPrvyrKApr() {
		return (int) Math.round((double)prvyrApr / 1000);
	}
	public long getNowYrKMay() {
		return (int) Math.round((double)nowYrMay / 1000);
	}
	public long getPrvyrKMay() {
		return (int) Math.round((double)prvyrMay / 1000);
	}
	public long getNowYrKJun() {
		return (int) Math.round((double)nowYrJun / 1000);
	}
	public long getPrvyrKJun() {
		return (int) Math.round((double)prvyrJun / 1000);
	}
	public long getNowYrKJul() {
		return (int) Math.round((double)nowYrJul / 1000);
	}
	public long getPrvyrKJul() {
		return (int) Math.round((double)prvyrJul / 1000);
	}
	public long getNowYrKAug() {
		return (int) Math.round((double)nowYrAug / 1000);
	}
	public long getPrvyrKAug() {
		return (int) Math.round((double)prvyrAug / 1000);
	}
	public long getNowYrKSep() {
		return (int) Math.round((double)nowYrSep / 1000);
	}
	public long getPrvyrKSep() {
		return (int) Math.round((double)prvyrSep / 1000);
	}
	public long getNowYrKOct() {
		return (int) Math.round((double)nowYrOct / 1000);
	}
	public long getPrvyrKOct() {
		return (int) Math.round((double)prvyrOct / 1000);
	}
	public long getNowYrKNov() {
		return (int) Math.round((double)nowYrNov / 1000);
	}
	public long getPrvyrKNov() {
		return (int) Math.round((double)prvyrNov / 1000);
	}
	public long getNowYrKDec() {
		return (int) Math.round((double)nowYrDec / 1000);
	}
	public long getPrvyrKDec() {
		return (int) Math.round((double)prvyrDec / 1000);
	}
}
