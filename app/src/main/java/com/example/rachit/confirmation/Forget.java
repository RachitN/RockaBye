package com.example.rachit.confirmation;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget extends AppCompatActivity implements View.OnClickListener {
    EditText Email;
    Button Reset;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        Email = (EditText) findViewById(R.id.Email_id);
        Reset = (Button) findViewById(R.id.Reset);
        Reset.setOnClickListener(this);
        fauth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view)
    {

        String email = Email.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(this,"Please Enter Your Email",Toast.LENGTH_LONG).show();
            return;
            //to stop the Execution
        }

        fauth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override

            public void onComplete(@NonNull Task<Void> task) { //to send mail for reseting password

                if (task.isSuccessful())            //to check if mail if correct or not
                {
                    Toast.makeText(getApplicationContext(),"Reset Email has been Sent",Toast.LENGTH_LONG).show();
                }

                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
