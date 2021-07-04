package com.example.letsconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpVerification extends AppCompatActivity {
    TextView changeNumber;
    EditText t1,t2,t3,t4,t5,t6;
    Button verifyBtn;
    String enteredOtp;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        changeNumber=findViewById(R.id.changeNumber);
        t1=findViewById(R.id.inputtext1);
        t2=findViewById(R.id.inputtext2);
        t3=findViewById(R.id.inputtext3);
        t4=findViewById(R.id.inputtext4);
        t5=findViewById(R.id.inputtext5);
        t6=findViewById(R.id.inputtext6);
        otpjoin();
//        enteredOtp=t1.getText().toString()+t2.getText().toString()+t3.getText().toString()+t4.getText().toString()+t5.getText().toString()+t6.getText().toString();
        verifyBtn=findViewById(R.id.verifyButton);
        progressBar=findViewById(R.id.progessBarinotp);
        firebaseAuth=FirebaseAuth.getInstance();
        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(otpVerification.this,MainActivity.class);
                startActivity(intent);
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredOtp=t1.getText().toString()+t2.getText().toString()+t3.getText().toString()+t4.getText().toString()+t5.getText().toString()+t6.getText().toString();

                if(enteredOtp.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter the otp",Toast.LENGTH_SHORT).show();

                }
                else if(t1.getText().toString().isEmpty()||t2.getText().toString().isEmpty()||t3.getText().toString().isEmpty()||t4.getText().toString().isEmpty()||t5.getText().toString().isEmpty()||t6.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter the correct OTP",Toast.LENGTH_SHORT).show();

                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    String receivedotp=getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(receivedotp,enteredOtp);
                    signInWithCredential(credential);
                }
            }
        });

    }
    private void signInWithCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(otpVerification.this,setProfile.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


//   So the below function help us to send the cursor to the next EditText
//   which basically helps us to not click on different EditText to enter the otp

    private void otpjoin(){
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}