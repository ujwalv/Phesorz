package com.example.choppingimage;

import java.math.BigInteger;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSActivity extends Activity {

	SmsManager sms;
	Random rand;
	String PhNoS= null;
	int randNo;
	Long phNoBi;
	EditText phNoEt,vcET;
	Button ok,verifyB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        phNoEt = (EditText) findViewById(R.id.editText1);
        vcET = (EditText) findViewById(R.id.editText2);
        verifyB = (Button) findViewById(R.id.button1);
        ok = (Button) findViewById(R.id.button2);
        sms = SmsManager.getDefault(); 
        rand = new Random();
        randNo = rand.nextInt(9999);
        
        
        
        ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Please wait for message", Toast.LENGTH_SHORT).show();
				sms.sendTextMessage(phNoEt.getText().toString(), null, "Your verification code is "+randNo , null, null);
				//ok.setVisibility(View.INVISIBLE);
			}
		});
        
        verifyB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				/*Intent cameraActivity = new Intent(getApplicationContext(),CameraActivity.class);
				startActivity(cameraActivity);*/
				
				if(vcET.getText().toString().equalsIgnoreCase(String.valueOf(randNo))){
					Toast.makeText(getApplicationContext(), "Verification Succesfull", Toast.LENGTH_SHORT).show();
					Intent cameraActivity = new Intent(getApplicationContext(),CameraActivity.class);
					startActivity(cameraActivity);
				}else {
					Toast.makeText(getApplicationContext(), "Phone Number Not Verified", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
       // phNoBi = Long.valueOf(phNoEt.getText().toString()); // Phone number       
        
        
               
       // sms.sendTextMessage(phNoBi.toString(), null, "Your verification code is "+randNo , null, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
