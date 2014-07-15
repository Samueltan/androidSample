package com.overture.questdroid.utility;


public class ContestItem {
	private String imgSrcList;
    private String cashValueList;
    private String winnerRewardList;
    private String contestNameList;
    private String contestCoinList;
    private String beginDateList;
    private String collabrationTotalList;
    private String rectBanner;
    private String winnerNums;
    private String description;
    
	
	
	
	public ContestItem(String imgSrcList,	
						String cashValueList,
					    String winnerRewardList,
					    String contestNameList,
					    String contestCoinList,
					    String beginDateList,
					    String collabrationTotalList,
					    String rectBanner,
					    String winnerNums,
					    String description) {
		super();
		this.imgSrcList= imgSrcList;
		this.cashValueList = cashValueList;
		this.winnerRewardList = winnerRewardList;
		this.contestNameList = contestNameList;
		this.contestCoinList = contestCoinList;
		this.beginDateList = beginDateList;
		this.collabrationTotalList = collabrationTotalList;
		this.rectBanner = rectBanner;
		this.winnerNums = winnerNums;
		this.description = description;
	}
	public String getImgSrcList() {
		return imgSrcList;
	}
	public void setImgSrcList(String imgSrcList) {
		this.imgSrcList = imgSrcList;
	}
	
	public String getCashValueList() {
		return cashValueList;
	}
	public void setCashValueList(String cashValueList) {
		this.cashValueList = cashValueList;
	}
	
	public String getWinnerRewardList() {
		return winnerRewardList;
	}
	public void setWinnerRewardList(String winnerRewardList) {
		this.winnerRewardList = winnerRewardList;
	}
	
	public String getContestNameList() {
		return contestNameList;
	}
	public void setContestNameList(String contestNameList) {
		this.contestNameList = contestNameList;
	}
	
	public String getbeginDateList() {
		return beginDateList;
	}
	public void setbeginDateList(String beginDateList) {
		this.beginDateList = beginDateList;
	}
	
	public String getcollabrationTotalList() {
		return collabrationTotalList;
	}
	public void setcollabrationTotalList(String collabrationTotalList) {
		this.collabrationTotalList = collabrationTotalList;
	}
	
	public String getrectBanner() {
		return rectBanner;
	}
	public void setrectBanner(String rectBanner) {
		this.rectBanner = rectBanner;
	}
	
	public String getcontestCoinList() {
		return contestCoinList;
	}
	public void setcontestCoinList(String contestCoinList) {
		this.contestCoinList = contestCoinList;
	}
	
	public String getwinnerNums() {
		return winnerNums;
	}
	public void setwinnerNums(String winnerNums) {
		this.winnerNums = winnerNums;
	}
	public String getdescription() {
		return description;
	}
	public void setdescription(String description) {
		this.description = description;
	}
}
