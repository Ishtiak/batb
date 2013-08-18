package com.rightbrainsolution.batb;

public class Brand {
	private int brandId;
	private String brandName;
	private double brandPrice;
	private int brandAmount = 0;
	private double brandTotalPrice = 0;
	
	public int getBrandId() {
		return brandId;
	}
	
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public double getBrandPrice() {
		return brandPrice;
	}
	
	public void setBrandPrice(double brandPrice) {
		this.brandPrice = brandPrice;
	}
	
	public int getBrandAmount() {
		return brandAmount;
	}
	
	public void setBrandAmount(int brandAmount) {
		this.brandAmount = brandAmount;
	}

	public double getBrandTotalPrice() {
		return brandTotalPrice;
	}

	public void setBrandTotalPrice(double brandTotalPrice) {
		this.brandTotalPrice = brandTotalPrice;
	}
	
	
}
