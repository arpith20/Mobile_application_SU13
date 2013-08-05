package com.agnt.locate;

import android.app.Activity;
import android.os.Bundle;
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
	static LatLng GUESS = new LatLng(53.558, 9.927);
	static final LatLng ANSWER = new LatLng(53.551, 9.993);
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// Marker kiel = map.addMarker(new MarkerOptions()
		// .position(ANSWER)
		// .title("Kiel")
		// .snippet("This place is cool!!")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.ic_launcher)));

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
		// TODO Auto-generated method stub
		// clears previously selected point
		map.clear();

		Marker guess = map.addMarker(new MarkerOptions().position(POINT)
				.title("Your Guess")
				.snippet("Click here to check your results!"));
		guess.showInfoWindow();

		final double result = distance(POINT.latitude, POINT.longitude,
				ANSWER.latitude, ANSWER.longitude, "K");

		// moves camera to specified location
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(POINT, 7), 3000,
				null);

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				Toast.makeText(getBaseContext(),
						"Distance: " + Math.round(result) + " KMs apart!",
						Toast.LENGTH_LONG).show();
			}
		});
	}

}
