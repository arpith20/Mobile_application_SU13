package com.agnt.locate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		if (!isNetworkAvailable()) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(Menu.this);
			alertDialog.setTitle("No Internet Connectivity");
			alertDialog
					.setMessage("Please note that this application will not work without an active internet connection\n\n\nConnect to WiFi or 3G network and then continue!");
			alertDialog.setNeutralButton("OK", null);
			alertDialog.show();
		}

		Button play = (Button) findViewById(R.id.button1);
		Button help = (Button) findViewById(R.id.button2);
		Button about = (Button) findViewById(R.id.button3);

		play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(getBaseContext(), MainActivity.class);
				startActivity(i1);
			}
		});

		help.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i2 = new Intent(getBaseContext(), Help.class);
				startActivity(i2);
			}
		});

		about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i3 = new Intent(getBaseContext(), About.class);
				startActivity(i3);
			}
		});
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
}
