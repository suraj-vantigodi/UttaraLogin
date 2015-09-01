package com.example.uttara;

import com.example.uttara.MainActivity;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class signup extends Activity {

	Button createAcc, login;
	EditText UserNM, PWD, confirmPWD, Email;
	TextView tv1;

	String AppId = "3ykqFqEbNxyB5NG4LQt0gyQIg9EX0padc4GKegQt";
	String ClientID = "wc4AX3dr0HwGPkRu3SH8D4yvMiXmSyX4p02lv2mv";

	ParseObject testObject = new ParseObject("UserDetails");

	ParseUser user = new ParseUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		Parse.initialize(this, AppId, ClientID);

		UserNM = (EditText) findViewById(R.id.username);
		PWD = (EditText) findViewById(R.id.PWD);
		confirmPWD = (EditText) findViewById(R.id.confirmPWD);
		Email = (EditText) findViewById(R.id.Email);
		createAcc = (Button) findViewById(R.id.create);
		login = (Button) findViewById(R.id.login);
		tv1 = (TextView) findViewById(R.id.textView1);

		createAcc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopulateDB();

			}
		});
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserNM.setText("");
				PWD.setText("");
				confirmPWD.setText("");
				Email.setText("");
				changeActivity();

			}
		});

	}

	void PopulateDB() {
		String userName = UserNM.getText().toString();
		String password = PWD.getText().toString();
		String confirmPassword = confirmPWD.getText().toString();
		String EmailStr = Email.getText().toString();

		if (userName.equals("") || password.equals("")
				|| confirmPassword.equals("")) {
			tv1.setText("One or More Fields are empty");
			return;
		}
		if (!password.equals(confirmPassword)) {
			tv1.setText("Passwords do not match");
			return;
		} else {
			// tv1.setText("Populating DataBase");
			user.setUsername(userName);
			user.setPassword(password);
			user.setEmail(EmailStr);

			user.signUpInBackground(new SignUpCallback() {
				@Override
				public void done(com.parse.ParseException e) {
					// TODO Auto-generated method stub
					if (e == null) {
						// Hooray! Let them use the app now.
						Toast.makeText(
								signup.this,
								"Account Successfully created. You Can login now",
								Toast.LENGTH_LONG).show();
						createAcc.setEnabled(false);
						login.setEnabled(true);

					} else {
						// Sign up didn't succeed. Look at the ParseException
						// to figure out what went wrong
						// tv1.setText("Error Message is " +e.getMessage());
						tv1.setText("Failure");
						Toast.makeText(signup.this,
								"Please try again " + e.getMessage(),
								Toast.LENGTH_LONG).show();
					}

				}
			});

		}

		ParseUser.logOut();
	}

	void changeActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

}
