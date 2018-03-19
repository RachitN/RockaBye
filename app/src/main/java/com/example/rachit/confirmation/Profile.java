package com.example.rachit.confirmation;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    TextView Profile,age;
    Intent i;
    Button LogOut, update,date;
    EditText Moileno,tdate;
    Spinner Gender;
    DatabaseReference ref;
    String name;
    private DatePickerDialog.OnDateSetListener mdatepicker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ref=FirebaseDatabase.getInstance().getReference("Information");
        Profile = (TextView) findViewById(R.id.Profile);
        age = (TextView) findViewById(R.id.age);

        i = getIntent();
        name = i.getStringExtra("Name");
        Profile.setText("Hello " + name);
        LogOut = (Button) findViewById(R.id.LogOut);
        update = (Button) findViewById(R.id.Update);
        date = (Button) findViewById(R.id.Date);
        Moileno = (EditText) findViewById(R.id.MobileNo);
        tdate = (EditText) findViewById(R.id.tdate);
        Gender = (Spinner) findViewById(R.id.gender);
        update.setOnClickListener(this);
        LogOut.setOnClickListener(this);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Profile.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mdatepicker,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });
        mdatepicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                     month=month+1;
                     tdate.setText(day+"/"+month+"/"+year);
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);
                Month=Month+1;
                int Age = Year-year;
                if(Month<=month)
                {   if(Day<day)
                     Age--;


                }
                String age1= String.valueOf(Age);
                age.setText(age1);

            }
        };

    }

    @Override
    public void onClick(View view)   //onclick for log out button
    {
        if (view == LogOut) {
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();    //to get log out from profile page
            i = new Intent(getApplicationContext(), MainActivity.class);
             startActivity(i);  //to go back to login page after log out
            finish();
        }
       /* else if (view == Date)
        {

        }*/
        else {
                   String mobileno= Moileno.getText().toString();
                   String gender = Gender.getSelectedItem().toString();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            name = user.getEmail();
            name= name.replaceFirst("@gmail.com","");
                   if(!TextUtils.isEmpty(mobileno))
                   {
                     String id = name;
                     Information info = new Information(mobileno,gender);
               try {
                   ref.child(id).setValue(info);
                   Toast.makeText(this, "Info Has Been Updated" + name, Toast.LENGTH_LONG).show();
               }
               catch (Exception e)
               {
                   Toast.makeText(this,id,Toast.LENGTH_LONG).show();
               }

                   }
                   else {
                       Toast.makeText(this,"Mobile No Is Empty",Toast.LENGTH_LONG).show();
                   }
        }

    }
}
