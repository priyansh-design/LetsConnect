package com.example.letsconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class setProfile extends AppCompatActivity {


    private CardView cardView;
    private ImageView getImageFromSetProfile;
    private static int PICK_IMAGE=123;
    private Uri imagePath;
    private EditText getUsername;
    private Button saveProfile;
    private FirebaseAuth firebaseAuth;
    private String name;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String ImageUriToken;
    private FirebaseFirestore firebaseFirestore;
    ProgressBar progressBarOfSetProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        getUsername=findViewById(R.id.userName);
        cardView=findViewById(R.id.saveprofilecardview);
        getImageFromSetProfile=findViewById(R.id.userImage);
        saveProfile=findViewById(R.id.saveProfile);
        progressBarOfSetProfile=findViewById(R.id.progessBarinSaveProfile);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=getUsername.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Name is Empty",Toast.LENGTH_SHORT).show();

                }
                else if(imagePath==null){
                    Toast.makeText(getApplicationContext(),"Image is Not set",Toast.LENGTH_SHORT).show();

                }
                else{
                    progressBarOfSetProfile.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    progressBarOfSetProfile.setVisibility(View.INVISIBLE);
                    Intent intent=new Intent(setProfile.this,chatActivity.class);
                    startActivity(intent);
                    finish();
                }
                
            }
        });



    }

    private void sendDataForNewUser(){
        sendDatatorealtimedatabase();
    }
    private void sendDatatorealtimedatabase(){
        name=getUsername.getText().toString();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile user=new UserProfile(name,firebaseAuth.getUid());
        databaseReference.setValue(user);
        Toast.makeText(getApplicationContext(),"User Proflie added success",Toast.LENGTH_SHORT).show();
        sendImageTostorage();




    }
    private void sendImageTostorage(){
        StorageReference imageref=storageReference.child("images").child(firebaseAuth.getUid()).child("profile picture");
        //image compression
        Bitmap bitmap=null;
        try {
            bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data= byteArrayOutputStream.toByteArray();

        //putting image to storage
        UploadTask uploadTask=imageref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriToken=uri.toString();
                        Toast.makeText(getApplicationContext(),"URI added successfully",Toast.LENGTH_SHORT).show();
                        sendDataToCloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"URI addition failed",Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(),"Image is uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image not uploaded",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendDataToCloudFirestore(){
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String,Object> userdata=new HashMap<>();
        userdata.put("name",name);
        userdata.put("image",ImageUriToken);
        userdata.put("userUid",firebaseAuth.getUid());
        userdata.put("status","Online");
        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Data on cloud store is send successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Data cant be send to cloud storage",Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imagePath=data.getData();
            getImageFromSetProfile.setImageURI(imagePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}