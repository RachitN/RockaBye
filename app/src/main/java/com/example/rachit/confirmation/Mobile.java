package com.example.rachit.confirmation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import  java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Mobile extends AppCompatActivity implements View.OnClickListener {
    EditText code,mob_no;
    Button register;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private int btn_type=0;
    private FirebaseAuth mAuth;
    private String cre = "com.google.firebase.auth.PhoneAuthCredential@";
    private String cr;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        code = findViewById(R.id.verify);
        mob_no = findViewById(R.id.mob);
        register = findViewById(R.id.Register);
        register.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        i = new Intent(this, Profile.class);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                cr = String.valueOf(phoneAuthCredential);
               cr = cr.replaceFirst(cre,"");
                Toast.makeText(getApplicationContext(),"Your Credential is ",Toast.LENGTH_LONG).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;
                mob_no.setVisibility(View.INVISIBLE);
                code.setVisibility(View.VISIBLE);
                register.setText("Verify User");
                register.setEnabled(true);
                btn_type=1;


            }

        };

    }


    @Override
    public void onClick(View view) {
        if(btn_type == 0) {

            String mobile_no = mob_no.getText().toString();

            if(TextUtils.isEmpty(mobile_no))
            {
                Toast.makeText(getApplicationContext(),"Please Enter Your NO",Toast.LENGTH_LONG).show();
                return;
            }
            mob_no.setEnabled(false);
            register.setEnabled(false);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    mobile_no,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks);

        }
        else
        {
            String Code = code.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, Code);
            cr = String.valueOf(credential);
            cr = cr.replaceFirst(cre,"");
            Toast.makeText(getApplicationContext(),"Your Credential is "+cr,Toast.LENGTH_LONG).show();
            signInWithPhoneAuthCredential(credential);
        }


    }
  private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
      final Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()) {


                          FirebaseUser user = task.getResult().getUser();

                          startActivity(i);


                      } else {


                          if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                          }
                      }
                  }
              });
  }
}
