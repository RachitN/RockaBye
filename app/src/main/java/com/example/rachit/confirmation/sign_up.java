package com.example.rachit.confirmation;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class sign_up extends AppCompatActivity implements View.OnClickListener {

    private Button Register;
    private EditText Name,Password,Email_Id;
    private FirebaseAuth firebaseauth;
    private ProgressDialog prd ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Register = (Button) findViewById(R.id.Register);
        Name = (EditText) findViewById(R.id.Name);
        Password = (EditText) findViewById(R.id.Password);
        Email_Id = (EditText) findViewById(R.id.Email_id);
        Register.setOnClickListener(this);
        firebaseauth = FirebaseAuth.getInstance();
        prd = new ProgressDialog(this);

    }

     private void registeruser()
     {
        String email = Email_Id.getText().toString().trim();
        String password = Password.getText().toString().trim();
        final String name = Name.getText().toString().trim();

         if(TextUtils.isEmpty(name))
         {
             //name is empty
             Toast.makeText(this,"Please Enter Your Name",Toast.LENGTH_LONG).show();
             return;
             //to stop the Execution
         }

        if(TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(this,"Please Enter Your Email",Toast.LENGTH_LONG).show();
            return;
            //to stop the Execution
        }

         if(TextUtils.isEmpty(password))
         {
             //password is empty
             Toast.makeText(this,"Please Enter Your Password",Toast.LENGTH_LONG).show();
             return;
             //to stop the Execution
         }

         firebaseauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
         {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task)
             {
                 if(task.isSuccessful())

                 {
                     prd.show(); //to show progress dialog
                     prd.setMessage("Registering User...");
                     sendconfirmationmail();    //to send confirmation mail
                 }

                 else
                 {
                     Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                 }

             }

             private void sendconfirmationmail()
             {

                 FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                 if(user!=null)               //to check if user registered or not
                 {
                     UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build(); //to set user name as display name

                     user.updateProfile(profileUpdates);                      //to update user profile

                     user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {   //to send confirmation mail
                             if(task.isSuccessful())
                             {
                                 Toast.makeText(getApplicationContext(),"Verification Email Has Been Sent",Toast.LENGTH_LONG).show();
                                 FirebaseAuth.getInstance().signOut(); //to make user logged out to make him confirm his account from mail
                                 prd.dismiss(); //to dismiss progress dialog after confirmation mail has been sent

                             }

                             else
                             {
                                 Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                             }

                         }
                     });
                 }

             }
         });
     }

    @Override
    public void onClick(View view) //onclick for register button
    {

        registeruser();
    }
}
