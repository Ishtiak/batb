package com.rightbrainsolution.batb;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {
	
	private DbAdapter dbAdapter;
	private LinearLayout listContainerLinearLayout;
	private Button uploadButton, backButton;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		
		dbAdapter = new DbAdapter(this);
		
		dbAdapter.open();
		
		String sql = "select * from " + ApplicationStorage.TABLE_SALES + " where " + ApplicationStorage.SALES_SALE_SINK_FLAG + " = " + 1;
		
		Cursor salesCursor = dbAdapter.rawQuery(sql,null);
		
		listContainerLinearLayout = (LinearLayout) findViewById(R.id.listContainerLinearLayout);
		
		while (salesCursor.moveToNext()) {

			View view = getLayoutInflater().inflate(R.layout._sales_layout, null);
			
			String saleOutlet = salesCursor.getString(salesCursor.getColumnIndex(ApplicationStorage.SALES_SALE_OUTLET));
			TextView outletTextView = (TextView) view.findViewById(R.id.outletTextView);
			outletTextView.setText(saleOutlet);
			
			String saleTotalPrice = salesCursor.getString(salesCursor.getColumnIndex(ApplicationStorage.SALES_SALE_TOTAL_PRICE));
			TextView totalPriceTextView = (TextView) view.findViewById(R.id.totalPriceTextView);
			totalPriceTextView.setText(saleTotalPrice + " Tk.");
			
			String saleDate = salesCursor.getString(salesCursor.getColumnIndex(ApplicationStorage.SALES_SALE_DATE));
			TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
			dateTextView.setText(saleDate);
			
			listContainerLinearLayout.addView(view);
		}
		
		salesCursor.close();
		
		dbAdapter.close();
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		ButtonClickHandler buttonClickHandler = new ButtonClickHandler();
		
		uploadButton = (Button) findViewById(R.id.uploadButton);
		uploadButton.setOnClickListener(buttonClickHandler);
		
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(buttonClickHandler);
	}
	
	private class ButtonClickHandler implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			
			if (view == uploadButton) {
				new LogInTask().execute();
				
			} else if (view == backButton) {
				Intent intent = new Intent(ListActivity.this, FormActivity.class);
				startActivity(intent);
			}
		}
		
	}
	
	private class LogInTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				/*JSONObject jsonObject = new JSONObject();
				jsonObject.put("section_id", ApplicationStorage.sectionId);
				jsonObject.put("outlet_list", new JSONArray(ApplicationStorage.outletList));
				jsonObject.put("brand_list", new JSONArray(ApplicationStorage.brandList));*/
				
				ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("request", "save"));
				parameters.add(new BasicNameValuePair("data", ""));
				
				String responseString = NetUtils.formPostRequestAndGetData("", parameters);
				
				JSONObject responseObject = new JSONObject(responseString);
				
				
				String message = responseObject.getString("message");
				return message;
				
			} catch (JSONException e) {
				e.printStackTrace();
				return "Couldn't communicate with server.";
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			progressDialog.setMessage("Please wait ...");
			progressDialog.show();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(ListActivity.this, result, Toast.LENGTH_SHORT).show();
			
			try {
				progressDialog.dismiss();
				
				Intent intent = new Intent(ListActivity.this, FormActivity.class);
				startActivity(intent);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		
	}
}
