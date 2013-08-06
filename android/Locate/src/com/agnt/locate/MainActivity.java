package com.agnt.locate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapClickListener {
	static LatLng GUESS = new LatLng(0.0, 0.0);
	static final LatLng ANSWER = new LatLng(40.68917, -74.04444);
	String NAME = "Statue Of Liberty";
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView name = (TextView) findViewById(R.id.textView1);
		name.setText("Find: " + NAME);
		Toast.makeText(getBaseContext(), "Please wait for the map to load.",
				Toast.LENGTH_SHORT).show();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		map.setOnMapClickListener(this);

	}

	// calculate distance
	private double distance(double lat1, double lon1, double lat2, double lon2,
			String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	@Override
	public void onMapClick(LatLng POINT) {
		// clears previously selected point
		map.clear();

		Marker guess = map.addMarker(new MarkerOptions().position(POINT)
				.title("Your Guess")
				.snippet("Click here to check submit results!"));
		guess.showInfoWindow();

		final double result = distance(POINT.latitude, POINT.longitude,
				ANSWER.latitude, ANSWER.longitude, "K");

		// moves camera to specified location
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(POINT, 7), 2000,
				null);

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				show_diag(Math.round(result));
			}

			private void show_diag(long result) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MainActivity.this);

				// set title
				alertDialogBuilder.setTitle("Your Score!");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"You are " + result + " KMs away from " + NAME
										+ "\n\nYour score is: "
										+ getScore(result)+"\n\nDo you want to play again?")
						.setCancelable(false)
						.setPositiveButton("Yes!",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										dialog.cancel();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										MainActivity.this.finish();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}

		});
	}

	private String getScore(long result) {
		if (result >= 0 && result <= 10)
			return "15 \nCongratulations!!";
		else if (result >= 0 && result <= 10)
			return "11\nGood Job!";
		else if (result >= 10 && result <= 100)
			return "7 \nNice!";
		else if (result >= 100 && result <= 1000)
			return "3 \nCool!";
		else if (result >= 1000 && result <= 10000)
			return "0 \nWOW! You were way off course!\nBetter luck next time!";
		return null;
	}
}
