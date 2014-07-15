package com.overture.questdroid.fragment;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.overture.questdroid.R;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

public class ItemRow {
	// Overview information
	private String imgSrc;
	private String cashValue;      
	private String winnerReward; 
	private String contestName;   
	private String contestCoin;   
	private String beginDate; 
	private String colabrationNumber;
	
	// Details
	private String rectBanner;
	private String winnerNums;
	private String description;	
	
	public ItemRow(ItemRow row){
		super();
		this.imgSrc = row.getCashValue();
		this.cashValue = row.getCashValue();
		this.winnerReward = row.getWinnerReward();
		this.contestName = row.getContestName();
		this.contestCoin = row.getContestCoin();
		this.beginDate = row.getBeginDate();
		this.colabrationNumber = row.getColabrationNumber();
		
//		this.rectBanner = row.getRectBanner();
		this.winnerNums = row.getWinnerNums();
		this.description = row.getDescription();
	}
	
	public ItemRow(String imgSrc, String cashValue, String winnerReward, String contestName, 
				String contestCoin, String beginDate, String colabrationNumber, 
				String rectBanner, String winnerNums, String description) {
		super();
		this.imgSrc = imgSrc;
		this.cashValue = cashValue;
		this.winnerReward = winnerReward;
		this.contestName = contestName;
		this.contestCoin = contestCoin;
		this.beginDate = beginDate;
		this.colabrationNumber = colabrationNumber;
		
		this.rectBanner = rectBanner;
		this.winnerNums = winnerNums;
		this.description = description;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getCashValue() {
		return cashValue;
	}

	public void setCashValue(String cashValue) {
		this.cashValue = cashValue;
	}

	public String getWinnerReward() {
		return winnerReward;
	}

	public void setWinnerReward(String winnerReward) {
		this.winnerReward = winnerReward;
	}

	public String getContestName() {
		return contestName;
	}

	public void setContestName(String contestName) {
		this.contestName = contestName;
	}

	public String getContestCoin() {
		return contestCoin;
	}

	public void setContestCoin(String contestCoin) {
		this.contestCoin = contestCoin;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getColabrationNumber() {
		return colabrationNumber;
	}

	public void setColabrationNumber(String colabrationNumber) {
		this.colabrationNumber = colabrationNumber;
	}

	public String getRectBanner() {
		return rectBanner;
	}

	public void setRectBanner(String rectBanner) {
		this.rectBanner = rectBanner;
	}

	public String getWinnerNums() {
		return winnerNums;
	}

	public void setWinnerNums(String winnerNums) {
		this.winnerNums = winnerNums;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
