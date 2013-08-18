package com.rightbrainsolution.batb;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private DbAdapter dbAdapter;
	private EditText usernameEditText, passwordEditText;
	private Button loginButton;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		
		String sql = "select * from " + ApplicationStorage.TABLE_USERS;
		Cursor userCursor = dbAdapter.rawQuery(sql,null);
		
		if (userCursor.moveToNext()) {
			
			initialize();
			
			Intent intent = new Intent(LoginActivity.this, FormActivity.class);
			startActivity(intent);
			finish();
			
		} else {
			
			setContentView(R.layout.login_layout);
			
			progressDialog = new ProgressDialog(this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			loginButton = (Button) findViewById(R.id.logInButton);
			
			usernameEditText = (EditText) findViewById(R.id.usernameEditText);
			passwordEditText = (EditText) findViewById(R.id.passwordEditText);
			
			loginButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					String username = usernameEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					
					if (username.length() == 0 || password.length() == 0) {
						Toast.makeText(LoginActivity.this, "Username and Password can't be empty", Toast.LENGTH_SHORT).show();
						return;
					}
					
					new LogInTask().execute();
				}
			});
		}
	}
	
	private class LogInTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			String username = usernameEditText.getText().toString();
			String password = passwordEditText.getText().toString();
		
			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("request", "login"));
			parameters.add(new BasicNameValuePair("username", username));
			parameters.add(new BasicNameValuePair("password", password));
			
			String responseString = NetUtils.formPostRequestAndGetData("", parameters);
			
			try {
				
				JSONObject responseObject = new JSONObject(responseString);
				String status = responseObject.getString("status");
				
				if (status.equals("Success")) {
					
					//dbAdapter.rawQuery("delete * from " + ApplicationStorage.TABLE_USERS,null);
					
					ContentValues values = new ContentValues();
					values.put(ApplicationStorage.USERS_USER_ID, username);
					dbAdapter.insert(ApplicationStorage.TABLE_USERS, null, values);
					
					JSONObject dataObject = responseObject.getJSONObject("data");
					
					JSONObject outletListObject = dataObject.getJSONObject("outlet_list");
					
					ApplicationStorage.sectionId = outletListObject.getInt("section_id");
					
					JSONArray outletsArray = outletListObject.getJSONArray("outlets");
					
					values = new ContentValues();
					for (int i = 0; i < outletsArray.length(); i++) {
						JSONObject outletObject = outletsArray.getJSONObject(i);
						
						values.put(ApplicationStorage.OUTLETS_OUTLET_ID, outletObject.getInt("outlet_id"));
						values.put(ApplicationStorage.OUTLETS_OUTLET_NAME, outletObject.getString("outlet_name"));
						dbAdapter.insert(ApplicationStorage.TABLE_OUTLETS, null, values);
					}
					
					JSONArray brandArray = dataObject.getJSONArray("brand_list");
					
					values = new ContentValues();
					for (int i = 0; i < brandArray.length(); i++) {
						JSONObject brandObject = brandArray.getJSONObject(i);

						values.put(ApplicationStorage.BRANDS_BRAND_ID, brandObject.getInt("brand_id"));
						values.put(ApplicationStorage.BRANDS_BRAND_NAME, brandObject.getString("brand_name"));
						values.put(ApplicationStorage.BRANDS_BRAND_PRICE, brandObject.getDouble("brand_price"));
						dbAdapter.insert(ApplicationStorage.TABLE_BRANDS, null, values);
					}
					
					initialize();
					
					dbAdapter.close();
				} else {
					String message = responseObject.getString("message");
					return message;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return "Couldn't communicate with server.";
			}
			
			return "";
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
			
			try {
				progressDialog.dismiss();
				
				if (result.length() == 0) {
					
					Intent intent = new Intent(LoginActivity.this, FormActivity.class);
					startActivity(intent);
					finish();

				} else {
					Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
				}
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}
	
	private void initialize() {
		
		String sql = "select * from " + ApplicationStorage.TABLE_OUTLETS;
		Cursor outletCursor = dbAdapter.rawQuery(sql,null);
		
		ApplicationStorage.outletList.clear();
		ApplicationStorage.outletNameList.clear();
		
		while (outletCursor.moveToNext()) {

			int outletId = outletCursor.getInt(outletCursor.getColumnIndex(ApplicationStorage.OUTLETS_OUTLET_ID));
			String outletName = outletCursor.getString(outletCursor.getColumnIndex(ApplicationStorage.OUTLETS_OUTLET_NAME));
			
			Outlet outlet = new Outlet();
			outlet.setOutletId(outletId);
			outlet.setOutletName(outletName);
			
			ApplicationStorage.outletList.add(outlet);
			ApplicationStorage.outletNameList.add(outletName);
		}
		
		outletCursor.close();
		
		sql = "select * from " + ApplicationStorage.TABLE_BRANDS;
		Cursor brandCursor = dbAdapter.rawQuery(sql,null);
		
		ApplicationStorage.brandList.clear();
		
		while (brandCursor.moveToNext()) {

			int brandId = brandCursor.getInt(brandCursor.getColumnIndex(ApplicationStorage.BRANDS_BRAND_ID));
			String brandName = brandCursor.getString(brandCursor.getColumnIndex(ApplicationStorage.BRANDS_BRAND_NAME));
			Double brandPrice = brandCursor.getDouble(brandCursor.getColumnIndex(ApplicationStorage.BRANDS_BRAND_PRICE));
			
			Brand brand = new Brand();
			brand.setBrandId(brandId);
			brand.setBrandName(brandName);
			brand.setBrandPrice(brandPrice);
			brand.setBrandAmount(0);
			brand.setBrandTotalPrice(0);
			
			ApplicationStorage.brandList.add(brand);
		}
		
		brandCursor.close();
		dbAdapter.close();
	}
}
