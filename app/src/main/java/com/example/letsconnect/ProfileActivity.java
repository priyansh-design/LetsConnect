  package com.example.letsconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static android.os.Build.VERSION_CODES.O;
import static android.os.Build.VERSION_CODES.P;

  public class ProfileActivity extends AppCompatActivity {



    EditText viewusername;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    TextView changeusername;
    FirebaseFirestore firebaseFirestore;
    ImageView profileimage;
    StorageReference storageReference;
    private String ImageUritoken;
    androidx.appcompat.widget.Toolbar toolbarOfprofile;
    ImageButton backbuttinofprofilepage;
    FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        viewusername=findViewById(R.id.profileuserName);
        profileimage=findViewById(R.id.userprofileImage);
        changeusername=findViewById(R.id.changename);
        firebaseFirestore=FirebaseFirestore.getInstance();
        toolbarOfprofile=findViewById(R.id.toolbarofProfile);
        backbuttinofprofilepage=findViewById(R.id.backbuttonofProfile);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        setSupportActionBar(toolbarOfprofile);
        backbuttinofprofilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storageReference=firebaseStorage.getReference();
        storageReference.child("images").child(firebaseAuth.getUid()).child("profile picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageUritoken=uri.toString();
                Picasso.get().load(uri).into(profileimage);
            }
        });
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile=snapshot.getValue(UserProfile.class);
                viewusername.setText(userProfile.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"failed to fetch details",Toast.LENGTH_SHORT).show();


            }
        });
        changeusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,UpdateProfile.class);
                startActivity(intent);
            }
        });

    }
}