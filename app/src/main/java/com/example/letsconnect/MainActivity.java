package com.example.letsconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button getOtp;
    EditText userNumber;
    CountryCodePicker ccp;
    String countryCode;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String codeSent;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userNumber=findViewById(R.id.userNumber);
        getOtp=findViewById(R.id.sendOtpButton);
        ccp=findViewById(R.id.countryCode);
        progressBar=findViewById(R.id.progessBar);
        firebaseAuth=FirebaseAuth.getInstance();
        countryCode=ccp.getSelectedCountryCodeWithPlus();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode=ccp.getSelectedCountryCodeWithPlus();
            }
        });
        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number;
                number=userNumber.getText().toString();
                if(number.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your number",Toast.LENGTH_SHORT).show();

                }
                else if(number.length()<10){
                    Toast.makeText(getApplicationContext(),"Please enter correct number",Toast.LENGTH_SHORT).show();

                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    getOtp.setVisibility(View.GONE);
                    phoneNumber=countryCode+number;
                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(callbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }

            }
        });

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP is sent",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                codeSent=s;
                Intent intent=new Intent(MainActivity.this,otpVerification.class);
                intent.putExtra("otp",codeSent);
                startActivity(intent);
            }
        };





    }
    @Override
    protected void onStart(){
        super.onStart();
        if(firebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,chatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}