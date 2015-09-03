package com.example.uttara;

import com.example.uttara.signup;
import com.example.uttara.AfterLogin;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Author :Suraj Vantigodi
 * Modified on : 31/8/2015
 */

public class MainActivity extends Activity {

	Button signup, login;
	EditText username, PWD;
	TextView TvResult;
	String AppId = "3ykqFqEbNxyB5NG4LQt0gyQIg9EX0padc4GKegQt";
	String ClientID = "wc4AX3dr0HwGPkRu3SH8D4yvMiXmSyX4p02lv2mv";

	ParseObject testObject = new ParseObject("UserDetails");

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		signup = (Button) findViewById(R.id.signup);
		login = (Button) findViewById(R.id.Login);
		username = (EditText) findViewById(R.id.username);
		PWD = (EditText) findViewById(R.id.PWD);

		Parse.initialize(this, AppId, ClientID);
		
		// To track statistics around application
		ParseAnalytics.trackAppOpened(getIntent());
 
		// inform the Parse Cloud that it is ready for notifications
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentSignUP = new Intent(getApplicationContext(),
						signup.class);
				startActivity(intentSignUP);
				username.setText("");
				PWD.setText("");
			}
		});

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Boolean flag = validate();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	boolean validate() {

		String usernameSTR = username.getText().toString();
		String pwdSTR = PWD.getText().toString();
				
		ParseUser user = new ParseUser();
		
		Parse.initialize(this, AppId, ClientID);

		ParseUser.logInInBackground(usernameSTR,pwdSTR, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				// TODO Auto-generated method stub
				if (user != null) {
					// Hooray! The user is logged in.
					//TvResult.setText("Hurray..!!!! Login Successful");
					Toast.makeText(
							MainActivity.this,
							"Login Successful",
							Toast.LENGTH_LONG).show();
					HomeScreen();
					
				} else {
					// Signup failed. Look at the ParseException to see what
					// happened.
					//Log.d("exception ", );
					String errorMsg = null;
					
					if(e.getCode()== ParseException.EMAIL_NOT_FOUND)
						errorMsg = "Email Not Found";
					else if(e.getCode()==ParseException.EMAIL_MISSING || e.getCode()==ParseException.PASSWORD_MISSING)
						errorMsg = "Email or Password Missing";
					else
						errorMsg = "Email and Password do not Match";
						
					Toast.makeText(
							MainActivity.this,
							"Login Failed " +e.getMessage() +" " +errorMsg,
							Toast.LENGTH_LONG).show();
					
					//TvResult.setText("Login Failed  " +e.getMessage());
				}
			}
		});

		return false;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	void HomeScreen()
	{
		String usernameSTR = username.getText().toString();
		Intent intent = new Intent(this, AfterLogin.class);
		intent.putExtra("username",usernameSTR);
		startActivity(intent);
		this.finish();
	}
}
