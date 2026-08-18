package saleson.shop.statistics.domain;


import com.onlinepowers.framework.util.StringUtils;

public class MonthLocgovItemStat {
	
	private String locgovCode;

	private long itemTotalCnt;

	private long tourCnt;

	private long processCnt;

	private long dailyCnt;

	private long ticketCnt;

	private long farmCnt;

	private long aquaticCnt;

	private long orderTotalCnt;

	private String salePrice;

	private String top1;

	private String top2;

	private String top3;

	private String upperLocgovNm;

	private String locgovNm;

	private long itemTotalAll;

	private long tourTotalAll;

	private long processTotalAll;

	private long dailyTotalAll;

	private long ticketTotalAll;

	private long farmTotalAll;

	private long aquaticTotalAll;

	private long orderTotalAll;

	private long salePriceTotalAll;

	private String allTop1;

	private String allTop2;

	private String allTop3;

	
	
	
	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public long getItemTotalCnt() {
		return itemTotalCnt;
	}

	public void setItemTotalCnt(long itemTotalCnt) {
		this.itemTotalCnt = itemTotalCnt;
	}

	public long getTourCnt() {
		return tourCnt;
	}

	public void setTourCnt(long tourCnt) {
		this.tourCnt = tourCnt;
	}

	public long getProcessCnt() {
		return processCnt;
	}

	public void setProcessCnt(long processCnt) {
		this.processCnt = processCnt;
	}

	public long getDailyCnt() {
		return dailyCnt;
	}

	public void setDailyCnt(long dailyCnt) {
		this.dailyCnt = dailyCnt;
	}

	public long getTicketCnt() {
		return ticketCnt;
	}

	public void setTicketCnt(long ticketCnt) {
		this.ticketCnt = ticketCnt;
	}

	public long getFarmCnt() {
		return farmCnt;
	}

	public void setFarmCnt(long farmCnt) {
		this.farmCnt = farmCnt;
	}

	public long getAquaticCnt() {
		return aquaticCnt;
	}

	public void setAquaticCnt(long aquaticCnt) {
		this.aquaticCnt = aquaticCnt;
	}

	public long getOrderTotalCnt() {
		return orderTotalCnt;
	}

	public void setOrderTotalCnt(long orderTotalCnt) {
		this.orderTotalCnt = orderTotalCnt;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getTop1() {
		return top1;
	}

	public void setTop1(String top1) {
		this.top1 = top1;
	}

	public String getTop2() {
		return top2;
	}

	public void setTop2(String top2) {
		this.top2 = top2;
	}

	public String getTop3() {
		return top3;
	}

	public void setTop3(String top3) {
		this.top3 = top3;
	}

	public String getUpperLocgovNm() {
		return upperLocgovNm;
	}

	public void setUpperLocgovNm(String upperLocgovNm) {
		this.upperLocgovNm = upperLocgovNm;
	}

	public String getLocgovNm() {
		return locgovNm;
	}

	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}
	
	public String getTop1Name() {
		if (StringUtils.hasLength(top1) && strToLong(getTop1Cnt()) > 0) {
			try {
				while(top1.indexOf("^") == 0) {
					top1 = top1.substring(1);
				}
				String[] top1List = top1.split("\\^");
				int length = top1List.length;
				boolean hasEmptyFirst = false;
				if (top1.indexOf("\\^") == 0) {
					hasEmptyFirst = true;
				}
				String[] itemNames;
				if (hasEmptyFirst) {
					itemNames = new String[length - 1];
				} else {
					itemNames = new String[length];	
				}
				for (int i = 0 ; i < length ; i++) {
					if (hasEmptyFirst) {
						if (i > 0) {
							itemNames[i-1] = top1List[i].split("\\|")[0];
						}
					} else {
						itemNames[i] = top1List[i].split("\\|")[0];
					}
				}
				return String.join("\n", itemNames);
			} catch (ArrayIndexOutOfBoundsException e	 ) {
				return " ";
			}
		} else {
			return " ";
		}
	}
	
	public String getTop2Name() {
		if (StringUtils.hasLength(top2) && strToLong(getTop2Cnt()) > 0) {
			try {
				while(top2.indexOf("^") == 0) {
					top2 = top2.substring(1);
				}
				String[] top2List = top2.split("\\^");
				int length = top2List.length;
				boolean hasEmptyFirst = false;
				if (top1.indexOf("\\^") == 0) {
					hasEmptyFirst = true;
				}
				String[] itemNames;
				if (hasEmptyFirst) {
					itemNames = new String[length - 1];
				} else {
					itemNames = new String[length];	
				}
				for (int i = 0 ; i < length ; i++) {
					if (hasEmptyFirst) {
						if (i > 0) {
							itemNames[i-1] = top2List[i].split("\\|")[0];
						}
					} else {
						itemNames[i] = top2List[i].split("\\|")[0];
					}
				}
				return String.join("\n", itemNames);
			} catch (ArrayIndexOutOfBoundsException e	 ) {
				return " ";
			}
		} else {
			return " ";
		}
	}
	
	public String getTop3Name() {
		if (StringUtils.hasLength(top3) && strToLong(getTop3Cnt()) > 0) {
			try {
				while(top3.indexOf("^") == 0) {
					top3 = top3.substring(1);
				}
				String[] top3List = top3.split("\\^");
				int length = top3List.length;
				boolean hasEmptyFirst = false;
				if (top1.indexOf("\\^") == 0) {
					hasEmptyFirst = true;
				}
				String[] itemNames;
				if (hasEmptyFirst) {
					itemNames = new String[length - 1];
				} else {
					itemNames = new String[length];	
				}
				for (int i = 0 ; i < length ; i++) {
					if (hasEmptyFirst) {
						if (i > 0) {
							itemNames[i-1] = top3List[i].split("\\|")[0];
						}
					} else {
						itemNames[i] = top3List[i].split("\\|")[0];
					}
				}
				return String.join("\n", itemNames);
			} catch (ArrayIndexOutOfBoundsException e	 ) {
				return " ";
			}
		} else {
			return " ";
		}
	}
	
	public String getTop1Cnt() {
		if (StringUtils.hasLength(top1)) {
			try {
				if (top1.indexOf("^") == 0) {
					return "0".equalsIgnoreCase(top1.split("\\^")[1].split("\\|")[1]) ? "" : top1.split("\\^")[1].split("\\|")[1];
				}
				return "0".equalsIgnoreCase(top1.split("\\^")[0].split("\\|")[1]) ? "" : top1.split("\\^")[0].split("\\|")[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				return "";
			}
		} else {
			return "";
		}
	}
	
	public String getTop2Cnt() {
		if (StringUtils.hasLength(top2)) {
			try {
				if (top2.indexOf("^") == 0) {
					return "0".equalsIgnoreCase(top2.split("\\^")[1].split("\\|")[1]) ? "" : top2.split("\\^")[1].split("\\|")[1];
				}
				return "0".equalsIgnoreCase(top2.split("\\^")[0].split("\\|")[1]) ? "" : top2.split("\\^")[0].split("\\|")[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				return "";
			}
		} else {
			return "";
		}
	}
	
	public String getTop3Cnt() {
		if (StringUtils.hasLength(top3)) {
			try {
				if (top3.indexOf("^") == 0) {
					return "0".equalsIgnoreCase(top3.split("\\^")[1].split("\\|")[1]) ? "" : top3.split("\\^")[1].split("\\|")[1];
				}
				return "0".equalsIgnoreCase(top3.split("\\^")[0].split("\\|")[1]) ? "" : top3.split("\\^")[0].split("\\|")[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public long getItemTotalAll() {
		return itemTotalAll;
	}

	public void setItemTotalAll(long itemTotalAll) {
		this.itemTotalAll = itemTotalAll;
	}

	public long getTourTotalAll() {
		return tourTotalAll;
	}

	public void setTourTotalAll(long tourTotalAll) {
		this.tourTotalAll = tourTotalAll;
	}

	public long getProcessTotalAll() {
		return processTotalAll;
	}

	public void setProcessTotalAll(long processTotalAll) {
		this.processTotalAll = processTotalAll;
	}

	public long getDailyTotalAll() {
		return dailyTotalAll;
	}

	public void setDailyTotalAll(long dailyTotalAll) {
		this.dailyTotalAll = dailyTotalAll;
	}

	public long getTicketTotalAll() {
		return ticketTotalAll;
	}

	public void setTicketTotalAll(long ticketTotalAll) {
		this.ticketTotalAll = ticketTotalAll;
	}

	public long getFarmTotalAll() {
		return farmTotalAll;
	}

	public void setFarmTotalAll(long farmTotalAll) {
		this.farmTotalAll = farmTotalAll;
	}

	public long getAquaticTotalAll() {
		return aquaticTotalAll;
	}

	public void setAquaticTotalAll(long aquaticTotalAll) {
		this.aquaticTotalAll = aquaticTotalAll;
	}

	public long getOrderTotalAll() {
		return orderTotalAll;
	}

	public void setOrderTotalAll(long orderTotalAll) {
		this.orderTotalAll = orderTotalAll;
	}

	public long getSalePriceTotalAll() {
		return salePriceTotalAll;
	}

	public void setSalePriceTotalAll(long salePriceTotalAll) {
		this.salePriceTotalAll = salePriceTotalAll;
	}

	public String getAllTop1() {
		return allTop1;
	}

	public void setAllTop1(String allTop1) {
		this.allTop1 = allTop1;
	}

	public String getAllTop2() {
		return allTop2;
	}

	public void setAllTop2(String allTop2) {
		this.allTop2 = allTop2;
	}

	public String getAllTop3() {
		return allTop3;
	}

	public void setAllTop3(String allTop3) {
		this.allTop3 = allTop3;
	}

	public String getAllTop1Name() {
		if (StringUtils.hasLength(allTop1) && strToLong(getAllTop1Cnt()) > 0) {
			try {
				if (allTop1.indexOf("^") == 0) {
					allTop1 = allTop1.substring(1);
				}
				String[] allTop1List = allTop1.split("\\^");
				int length = allTop1List.length;
				boolean hasEmptyFirst = false;
				if (allTop1.indexOf("\\^") == 0) {
					hasEmptyFirst = true;
				}
				String[] itemNames;
				if (hasEmptyFirst) {
					itemNames = new String[length - 1];
				} else {
					itemNames = new String[length];	
				}
				for (int i = 0 ; i < length ; i++) {
					if (hasEmptyFirst) {
						if (i > 0) {
							itemNames[i-1] = allTop1List[i].split("\\|")[0];
						}
					} else {
						itemNames[i] = allTop1List[i].split("\\|")[0];
					}
				}
				return String.join("\n", itemNames);
			} catch (ArrayIndexOutOfBoundsException e	 ) {
				return " ";
			}
		} else {
			return " ";
		}
	}

	public String getAllTop2Name() {
		if (StringUtils.hasLength(allTop2) && strToLong(getAllTop2Cnt()) > 0) {
			try {
				if (allTop2.indexOf("^") == 0) {
					allTop2 = allTop2.substring(1);
				}
				String[] allTop2List = allTop2.split("\\^");
				int length = allTop2List.length;
				boolean hasEmptyFirst = false;
				if (allTop2.indexOf("\\^") == 0) {
					hasEmptyFirst = true;
				}
				String[] itemNames;
				if (hasEmptyFirst) {
					itemNames = new String[length - 1];
				} else {
					itemNames = new String[length];	
				}
				for (int i = 0 ; i < length ; i++) {
					if (hasEmptyFirst) {
						if (i > 0) {
							itemNames[i-1] = allTop2List[i].split("\\|")[0];
						}
					} else {
						itemNames[i] = allTop2List[i].split("\\|")[0];
					}
				}
				return String.join("\n", itemNames);
			} catch (ArrayIndexOutOfBoundsException e	 ) {
				return " ";
			}
		} else {
			return " ";
		}
	}

	public String getAllTop3Name() {
		if (StringUtils.hasLength(allTop3) && strToLong(getAllTop3Cnt()) > 0) {
			try {
				if (allTop3.indexOf("^") == 0) {
					allTop3 = allTop3.substring(1);
				}
				String[] allTop3List = allTop3.split("\\^");
				int length = allTop3List.length;
				boolean hasEmptyFirst = false;
				if (allTop3.indexOf("\\^") == 0) {
					hasEmptyFirst = true;
				}
				String[] itemNames;
				if (hasEmptyFirst) {
					itemNames = new String[length - 1];
				} else {
					itemNames = new String[length];	
				}
				for (int i = 0 ; i < length ; i++) {
					if (hasEmptyFirst) {
						if (i > 0) {
							itemNames[i-1] = allTop3List[i].split("\\|")[0];
						}
					} else {
						itemNames[i] = allTop3List[i].split("\\|")[0];
					}
				}
				return String.join("\n", itemNames);
			} catch (ArrayIndexOutOfBoundsException e	 ) {
				return " ";
			}
		} else {
			return " ";
		}
	}

	public String getAllTop1Cnt() {
		if (StringUtils.hasLength(allTop1)) {
			try {
				if (allTop1.indexOf("^") == 0) {
					return "0".equalsIgnoreCase(allTop1.split("\\^")[1].split("\\|")[1]) ? "" : allTop1.split("\\^")[1].split("\\|")[1];
				}
				return "0".equalsIgnoreCase(allTop1.split("\\^")[0].split("\\|")[1]) ? "" : allTop1.split("\\^")[0].split("\\|")[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public String getAllTop2Cnt() {
		if (StringUtils.hasLength(allTop2)) {
			try {
				if (allTop2.indexOf("^") == 0) {
					return "0".equalsIgnoreCase(allTop2.split("\\^")[1].split("\\|")[1]) ? "" : allTop2.split("\\^")[1].split("\\|")[1];
				}
				return "0".equalsIgnoreCase(allTop2.split("\\^")[0].split("\\|")[1]) ? "" : allTop2.split("\\^")[0].split("\\|")[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public String getAllTop3Cnt() {
		if (StringUtils.hasLength(allTop3)) {
			try {
				if (allTop3.indexOf("^") == 0) {
					return "0".equalsIgnoreCase(allTop3.split("\\^")[1].split("\\|")[1]) ? "" : allTop3.split("\\^")[1].split("\\|")[1];
				}
				return "0".equalsIgnoreCase(allTop3.split("\\^")[0].split("\\|")[1]) ? "" : allTop3.split("\\^")[0].split("\\|")[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				return "";
			}
		} else {
			return "";
		}
	}
	
	private long strToLong(String number) {
		try {
			return Long.parseLong(number);
		} catch(NumberFormatException e) {
			return 0;
		}
	}
	
}
