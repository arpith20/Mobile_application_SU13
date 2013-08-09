package com.agnt.locate;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class MainActivity extends Activity implements OnMapClickListener {
	static final LatLng ANSWER = new LatLng(40.68917, -74.04444);
	String NAME = null;
	private GoogleMap map;
	TextView display_latlng;
	private int points = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main);

		display_latlng = (TextView) findViewById(R.id.brought_by);

		Toast.makeText(getBaseContext(), "Please wait for the map to load.",
				Toast.LENGTH_SHORT).show();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		map.setOnMapClickListener(this);

		SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		layout.setShadowDrawable(getResources().getDrawable(
				R.drawable.above_shadow));
		layout.setPanelSlideListener(new PanelSlideListener() {

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				if (slideOffset < 0.2) {
					if (getActionBar().isShowing()) {
						getActionBar().hide();
					}
				} else {
					if (!getActionBar().isShowing()) {
						getActionBar().show();
					}
				}
			}

			@Override
			public void onPanelExpanded(View panel) {

			}

			@Override
			public void onPanelCollapsed(View panel) {

			}
		});
		TextView t = (TextView) findViewById(R.id.tv_q);
		t.setText("Find: " + NAME);
		t.setMovementMethod(LinkMovementMethod.getInstance());

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
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(4);
		display_latlng.setText("Your coordinates:\n"
				+"(" +df.format(POINT.latitude) +", "+df.format(POINT.longitude)+")");

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
										+ getScore(result)
										+ "\n\nDo you want to play again?")
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
		double f = 0;
		if (result >= 0 && result <= 500) {
			if (result <= 50)
				f = 0;
			else if (result > 50 && result <= 100)
				f = 0.5;
			else if (result > 100 && result <= 200)
				f = 1;
			else if (result > 200 && result <= 300)
				f = 2;
			else if (result > 300 && result <= 1000)
				f = 3;
			points = 5000 - (int) (f * result);
		} else
			points = 0;

		if (result >= 0 && result <= 50)
			return points + " \nCongratulations!!";
		else if (result > 50 && result <= 100)
			return points + " \nAwesome!";
		else if (result > 100 && result <= 200)
			return points + " \nGood Job!";
		else if (result > 200 && result <= 300)
			return points + " \nNice!";
		else if (result > 300 && result <= 1000)
			return points + " \nCool!";
		else if (result > 1000)
			return points
					+ " \nWOW! You were way off course!\nBetter luck next time!";
		return null;
	}
}
