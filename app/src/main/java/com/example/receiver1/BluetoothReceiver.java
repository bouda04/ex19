package com.example.receiver1;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("BroadcastActions", "Action "+action+" received");
 
	}

}
