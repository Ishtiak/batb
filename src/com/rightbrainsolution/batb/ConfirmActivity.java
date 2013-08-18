package com.rightbrainsolution.batb;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfirmActivity extends Activity {
	
	private TextView outletTextView, totalTextView;
	private LinearLayout confirmContainerLinearLayout;
	private Button yesButton, noButton;
	private DbAdapter dbAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_layout);
		
		dbAdapter = new DbAdapter(this);
		
		NumberFormat format = new DecimalFormat("#0.00");
		
		ButtonClickHandler buttonClickHandler = new ButtonClickHandler();
		
		yesButton = (Button) findViewById(R.id.yesButton);
		yesButton.setOnClickListener(buttonClickHandler);
		
		noButton = (Button) findViewById(R.id.noButton);
		noButton.setOnClickListener(buttonClickHandler);
		
		outletTextView = (TextView) findViewById(R.id.outletTextView);
		outletTextView.setText(ApplicationStorage.selectedOutletName);
		
		confirmContainerLinearLayout = (LinearLayout) findViewById(R.id.confirmContainerLinearLayout);
		
		ApplicationStorage.totalPrice = (double) 0;
		
		for (int i = 0; i < ApplicationStorage.brandList.size(); i++) {
			
			String brandPrice = format.format(ApplicationStorage.brandList.get(i).getBrandPrice());
			String brandAmount = String.valueOf(ApplicationStorage.brandList.get(i).getBrandAmount());
			String brandTotalPrice = format.format(ApplicationStorage.brandList.get(i).getBrandTotalPrice());
			
			View view = getLayoutInflater().inflate(R.layout._calculation_layout, null);
			
			TextView brandNameTextView = (TextView) view.findViewById(R.id.brandNameTextView);
			brandNameTextView.setText(ApplicationStorage.brandList.get(i).getBrandName());
			
			TextView purchaseInfoTextView = (TextView) view.findViewById(R.id.totalPriceTextView);
			purchaseInfoTextView.setText(brandAmount + " x " + brandPrice + " = ");
			
			TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);
			priceTextView.setText(brandTotalPrice);
			
			confirmContainerLinearLayout.addView(view);
			
			
			ApplicationStorage.totalPrice = ApplicationStorage.totalPrice + ApplicationStorage.brandList.get(i).getBrandTotalPrice();
		}
		
		totalTextView = (TextView) findViewById(R.id.totalTextView);
		totalTextView.setText(format.format(ApplicationStorage.totalPrice));
	}
	
	private class ButtonClickHandler implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			
			if (view == yesButton) {
				dbAdapter.open();
				ContentValues values = new ContentValues();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				
				values.put(ApplicationStorage.SALES_SALE_OUTLET, ApplicationStorage.selectedOutletName);
				values.put(ApplicationStorage.SALES_SALE_TOTAL_PRICE, ApplicationStorage.totalPrice);
				values.put(ApplicationStorage.SALES_SALE_DATE, dateFormat.format(date));
				values.put(ApplicationStorage.SALES_SALE_SINK_FLAG, 1);
				dbAdapter.insert(ApplicationStorage.TABLE_SALES, null, values);
				
				dbAdapter.close();
				
				Intent intent = new Intent(ConfirmActivity.this, ListActivity.class);
				startActivity(intent);
				finish();
				
			} else if (view == noButton) {
				finish();
			}
		}
		
	}
}
