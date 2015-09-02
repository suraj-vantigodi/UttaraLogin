package com.example.uttara;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AfterLogin extends Activity {

	TextView Tv1;
	String username;
	Boolean userExistance = false;
	String AppId = "3ykqFqEbNxyB5NG4LQt0gyQIg9EX0padc4GKegQt";
	String ClientID = "wc4AX3dr0HwGPkRu3SH8D4yvMiXmSyX4p02lv2mv";
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.afterlogin);
		
		Parse.initialize(this, AppId, ClientID);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			username = extras.getString("username");
		}
		
		Tv1 = (TextView)findViewById(R.id.userTV);
		Tv1.setText("You have signed in as " +username);
		
		userExistance = checkUserAddress();
//		if(userExistance)
//			Tv1.setText(username +" exists in userdetails DB as well ");
//		else
//			Tv1.setText(username +" does not exist in userdetails DB as well ");
	}
	
	Boolean checkUserAddress()
	{	
		ParseObject testObject = new ParseObject("UserDetails");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserDetails");
		query.whereEqualTo("UserName",username);
		query.findInBackground(new FindCallback<ParseObject>() {
			
		    @Override
			public void done(List<ParseObject> arg0,
					com.parse.ParseException arg1) {
				// TODO Auto-generated method stub
				 if (arg1 == null) {
					 
					 	userExistance = true;
					 	if(arg0.size()==1)
					 		Tv1.setText(username +" exists in UserDeatils DB too");
					 	else if(arg0.size()>1)
					 		Tv1.setText(username +" exists in UserDeatils DB multiple times");
					 	else
					 		Tv1.setText(username +" does not exist in UserDeatils DB");
			            
			        } else {
			            Log.d("score", "Error: " + arg1.getMessage());
			        }
				
			}
		});
		
		return userExistance;
		
	}

}
