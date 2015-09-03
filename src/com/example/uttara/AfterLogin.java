package com.example.uttara;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AfterLogin extends Activity {

	TextView Tv1;
	String username;
	Boolean userExistance = false;
	String AppId = "3ykqFqEbNxyB5NG4LQt0gyQIg9EX0padc4GKegQt";
	String ClientID = "wc4AX3dr0HwGPkRu3SH8D4yvMiXmSyX4p02lv2mv";
	protected LocationManager locationManager;
	Button getLoc;
	TextView LocationTV;
	GPSTracker gps;
	EditText AddressET;
	Geocoder geocoder;
	List<Address> addresses;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.afterlogin);
		
		gps = new GPSTracker(AfterLogin.this);
		
		Parse.initialize(this, AppId, ClientID);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			username = extras.getString("username");
		}

		Tv1 = (TextView) findViewById(R.id.userTV);
		Tv1.setText("You have signed in as " + username);

		try {
			userExistance = checkUserAddress();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		getLoc = (Button) findViewById(R.id.LocationButton);
		LocationTV = (TextView) findViewById(R.id.LocationTV);
		AddressET = (EditText) findViewById(R.id.AddressET);

		getLoc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					GetLocation();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// if(userExistance)
		// Tv1.setText(username +" exists in userdetails DB as well ");
		// else
		// Tv1.setText(username +" does not exist in userdetails DB as well ");
	}

	Boolean checkUserAddress() throws IOException {

		// ParseObject testObject = new ParseObject("UserDetails");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserDetails");
		ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("UserDetails");
		innerQuery.whereExists("image");
		geocoder = new Geocoder(this, Locale.getDefault());
		query.whereEqualTo("UserName", username);

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0,
					com.parse.ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg1 == null) {

					ParseObject p = arg0.get(0);
					userExistance = true;
					if (arg0.size() == 1) {
						Tv1.setText(username + " exists in UserDeatils DB too "
								+ p.getString("UserName") + " object ID : "
								+ p.getObjectId() +" address : " +p.getString("ContactAddress"));
						if(p.getString("ContactAddress")==null)
						{
							Tv1.setText("Address is empty");
							
							if (gps.canGetLocation()) {
								float Latitude = (float) gps.getLatitude(); // returns latitude
								float Longitude = (float) gps.getLongitude(); // returns longitude
								
								//Tv1.setText("Address is empty " +Latitude +" " +Longitude);
								
								try {
									addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
									String address = addresses.get(0).getAddressLine(0);
									String city = addresses.get(0).getLocality();
									String state = addresses.get(0).getAdminArea();
									String country = addresses.get(0).getCountryName();
									String postalCode = addresses.get(0).getPostalCode();
									String knownName = addresses.get(0).getFeatureName();
									p.put("City",city);
									p.put("State",state);
									p.put("Country",country);
									p.put("ContactAddress",address + " " +city +" " +state +" " +country);
									p.put("UniqueID",p.getObjectId());
									p.saveInBackground();
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						
					} else if (arg0.size() > 1)
						Tv1.setText(username
								+ " exists in UserDeatils DB multiple times");
					else
						Tv1.setText(username
								+ " does not exist in UserDeatils DB");

				} else {
					Log.d("score", "Error: " + arg1.getMessage());
				}

			}
		});

		return userExistance;

	}

	void GetLocation() throws IOException {

//		Geocoder geocoder;
//		List<Address> addresses;
		geocoder = new Geocoder(this, Locale.getDefault());

		if (gps.canGetLocation()) {
			float Latitude = (float) gps.getLatitude(); // returns latitude
			float Longitude = (float) gps.getLongitude(); // returns longitude

			addresses = geocoder.getFromLocation(Latitude, Longitude, 1); // Here
																			// 1
																			// represent
																			// max
																			// location
																			// result
																			// to
																			// returned,
																			// by
																			// documents
																			// it
																			// recommended
																			// 1
																			// to
																			// 5

			String address = addresses.get(0).getAddressLine(0); // If any
																	// additional
																	// address
																	// line
																	// present
																	// than
																	// only,
																	// check
																	// with max
																	// available
																	// address
																	// lines by
																	// getMaxAddressLineIndex()
			String city = addresses.get(0).getLocality();
			String state = addresses.get(0).getAdminArea();
			String country = addresses.get(0).getCountryName();
			String postalCode = addresses.get(0).getPostalCode();
			String knownName = addresses.get(0).getFeatureName(); // Only if
																	// available
																	// else
																	// return
																	// NULL

			LocationTV.setText("gps Location " + Latitude + " " + Longitude);
			AddressET.setText("Address of user is " + city + " " + state + " "
					+ country + " " + postalCode + " " + knownName + " "
					+ address);

		} else {
			gps.showSettingsAlert();
		}

	}

}
