package com.rightbrainsolution.batb;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FormActivity extends Activity {
	
	private Button saveButton;
	private LinearLayout formContainerLinearLayout;
	private ArrayList<EditText> brandAmountEditTextList;
	private Spinner outletSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_layout);

		ArrayAdapter<String> outletList = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ApplicationStorage.outletNameList);
		outletList.setDropDownViewResource(android.R.layout.simple_spinner_item);
		outletSpinner = (Spinner) findViewById(R.id.outletSpinner);
		outletSpinner.setAdapter(outletList);
		
		brandAmountEditTextList = new ArrayList<EditText>();
		
		formContainerLinearLayout = (LinearLayout) findViewById(R.id.formContainerLinearLayout);
		
		for (int i = 0; i < ApplicationStorage.brandList.size(); i++) {
			
			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			linearLayout.setGravity(Gravity.CENTER);
			
			TextView textView = new TextView(this);
			textView.setWidth(200);
			textView.setText(ApplicationStorage.brandList.get(i).getBrandName().toString());
			linearLayout.addView(textView);
			
			EditText editText = new EditText(this);
			editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			editText.setText("0");
			linearLayout.addView(editText);
			
			formContainerLinearLayout.addView(linearLayout);
			
			brandAmountEditTextList.add(editText);
		}
		
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				ApplicationStorage.selectedOutletId = (int) ApplicationStorage.outletList.get(outletSpinner.getSelectedItemPosition()).getOutletId();
				ApplicationStorage.selectedOutletName = ApplicationStorage.outletList.get(outletSpinner.getSelectedItemPosition()).getOutletName();
				
				for (int i = 0; i < ApplicationStorage.brandList.size(); i++) {
					
					double price = (double) ApplicationStorage.brandList.get(i).getBrandPrice();
					int amount = Integer.parseInt(brandAmountEditTextList.get(i).getText().toString());
					
					ApplicationStorage.brandList.get(i).setBrandAmount(amount);
					ApplicationStorage.brandList.get(i).setBrandTotalPrice(price * amount);
				}
				
				Intent intent = new Intent(FormActivity.this, ConfirmActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.uploadData) {
	    	Intent intent = new Intent(FormActivity.this, ListActivity.class);
			startActivity(intent);
	    }
	    return true;
	}
}
