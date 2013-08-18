package com.rightbrainsolution.batb;

import java.util.ArrayList;

public class ApplicationStorage {
	public static String TABLE_USERS = "users";
	public static String USERS_USER_ID = "user_id";
	public static String USERS_USER_TOKEN = "user_token";
	
	public static String TABLE_OUTLETS = "outlets";
	public static String OUTLETS_OUTLET_ID = "outlet_id";
	public static String OUTLETS_OUTLET_NAME = "outlet_name";
	
	public static String TABLE_BRANDS = "brands";
	public static String BRANDS_BRAND_ID = "brand_id";
	public static String BRANDS_BRAND_NAME = "brand_name";
	public static String BRANDS_BRAND_PRICE = "brand_price";
	
	public static String TABLE_SALES = "sales";
	public static String SALES_SALE_ID = "sale_id";
	public static String SALES_SALE_OUTLET = "sale_outlet";
	public static String SALES_SALE_TOTAL_PRICE = "sale_total_price";
	public static String SALES_SALE_DATE = "sale_date";
	public static String SALES_SALE_SINK_FLAG = "sale_sink_flag";
	
	public static String TABLE_SALES_DETAILS = "sales_details";
	public static String SALES_DETAILS_SALE_ID = "sale_id";
	public static String SALES_DETAILS_BRAND_ID = "brand_id";
	public static String SALES_DETAILS_BRAND_AMOUNT = "brand_amount";
	public static String SALES_DETAILS_BRAND_TOTAL_PRICE = "brand_total_price";
	
	public static int sectionId;
	
	public static ArrayList<Outlet> outletList = new ArrayList<Outlet>();
	public static ArrayList<String> outletNameList = new ArrayList<String>();
	
	public static ArrayList<Brand> brandList = new ArrayList<Brand>();
	
	public static int selectedOutletId;
	public static String selectedOutletName;
	
	public static Double totalPrice;
}
