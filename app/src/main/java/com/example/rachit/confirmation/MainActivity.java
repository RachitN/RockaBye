package com.example.rachit.confirmation;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView New, Forgot;
    EditText Email_id, Password;
    Button Login;
    FirebaseAuth fa;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)                               //to check if user logged in or not
        {

            if (user.isEmailVerified())                 //to check if the email is verified or not

            {
                final String user_name = user.getDisplayName();
                Intent i = new Intent(getApplicationContext(), Profile.class);
                i.putExtra("Name", user_name);
                startActivity(i);
                finish();
            }

            else

            {
                Toast.makeText(getApplicationContext(), "Email Not Verified", Toast.LENGTH_LONG).show();
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fAuth.signOut();    //to get log out from profile page
            }

        }

        New = (TextView) findViewById(R.id.New);
        Forgot = (TextView) findViewById(R.id.Forgot);
        Email_id = (EditText) findViewById(R.id.Email_id);
        Password = (EditText) findViewById(R.id.Password);
        Login = (Button) findViewById(R.id.Login);
        New.setOnClickListener(this);
        Login.setOnClickListener(this);
        Forgot.setOnClickListener(this);
        fa = FirebaseAuth.getInstance();

    }

    private void sign_in() {

        String email_id = Email_id.getText().toString();

        String password = Password.getText().toString();

        if (TextUtils.isEmpty((email_id)))
        {
            //email is empty
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
            return;
            //to stop the Execution
        }

        if (TextUtils.isEmpty(password))
        {
            //password is empty
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_LONG).show();
            return;
            //to stop the Execution
        }

        fa.signInWithEmailAndPassword(email_id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {    //to make user sign with email and password


                if (task.isSuccessful())                 //to check if email_id or password is correct or not
                {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified())                       //to check if the email is verified or not

                    {
                        String user_name = user.getDisplayName();
                        Intent i = new Intent(getApplicationContext(), Profile.class);
                        i.putExtra("Name", user_name);
                        startActivity(i);                            //to open profile page
                        finish();
                    }

                    else

                    {
                        Toast.makeText(getApplicationContext(), "Email Not Verified", Toast.LENGTH_LONG).show();
                    }

                }

                else

                {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    @Override
    public void onClick(View view) {

        if (view == New)                             //if new user link is clicked
        {
            Intent i = new Intent(this, sign_up.class);
            startActivity(i);
            finish();
        }

        else if (view == Login)                        //if Login utton is clicked
        {
            sign_in();
        }

        else                                          //if forgot password link is clicked
        {
            Intent i = new Intent(this, Forget.class);
            startActivity(i);
        }

    }

}


