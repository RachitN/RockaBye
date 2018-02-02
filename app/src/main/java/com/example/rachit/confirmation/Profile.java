package com.example.rachit.confirmation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity implements View.OnClickListener {
TextView Profile;
Intent i;
Button LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Profile = (TextView) findViewById(R.id.Profile) ;
        i = getIntent();
        String name = i.getStringExtra("Name");
        Profile.setText("Hello "+name);
        LogOut = (Button) findViewById(R.id.LogOut);
        LogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)   //onclick for log out button
    {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();    //to get log out from profile page
        i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);  //to go back to login page after log out
        finish();

    }
}
