package com.example.receiver1;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final int MY_PERMISSIONS_REQUEST_READ_SMS=1;

	private BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	protected void onResume() {

		CheckPermissions();
		super.onResume();
	}
	@Override
	protected void onPause() {
		if(mReceiver!=null) {
			unregisterReceiver(mReceiver);
			mReceiver=null;
		}
		super.onPause();
	}

	public void registerSMSReceiver(){

		IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
		mReceiver = new MySMSNestedReceiver();
		registerReceiver(mReceiver, filter);
	}
	private void CheckPermissions(){
		// Here, thisActivity is the current activity
		if ((checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) ||
		(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)){

			// Should we show an explanation?
			if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {

				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {

				// No explanation needed, we can request the permission.

				requestPermissions(	new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
						MY_PERMISSIONS_REQUEST_READ_SMS);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
		else registerSMSReceiver();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_READ_SMS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					registerSMSReceiver();

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	public class MySMSNestedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			final Bundle bundle = intent.getExtras();
			try {

				if (bundle != null) {

					final Object[] pdusObj = (Object[]) bundle.get("pdus");

					for (int i = 0; i < pdusObj.length; i++) {

						SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
						String phoneNumber = currentMessage.getDisplayOriginatingAddress();

						String senderNum = phoneNumber;
						String message = currentMessage.getDisplayMessageBody();

						Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


						// Show alert
						int duration = Toast.LENGTH_LONG;
						Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
						toast.show();

					} // end for loop
				} // bundle is null

			} catch (Exception e) {
				Log.e("SmsReceiver", "Exception smsReceiver" +e);

			}
		}
	}

}
