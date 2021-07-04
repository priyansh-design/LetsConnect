package com.example.letsconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SpecifiChat extends AppCompatActivity {

    EditText getmessage;
    ImageButton messagebutton;
    CardView sendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecifichat;
    ImageView imageoftheuser;
    TextView naameofuser;
    private String enteredmessage;
    Intent intent;
    String receivername,sendername,receiveruid,senderuid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderoom,recieveroom;
    ImageButton backbuttinofspecifichat;
    RecyclerView messagerecyclerview;
    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    MessagesAdapter messagesAdapter;
    ArrayList<Message> messageArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specifi_chat);
        getmessage=findViewById(R.id.getmessage);
        sendmessagecardview=findViewById(R.id.cardviewofsendmessage);
        messagebutton=findViewById(R.id.imageviewofsendmessage);
        mtoolbarofspecifichat=findViewById(R.id.toolbarofspecifichat);
        naameofuser=findViewById(R.id.nameofuser);
        imageoftheuser=findViewById(R.id.userprofileImage);
        backbuttinofspecifichat=findViewById(R.id.backbuttonofProfile);
        messagerecyclerview=findViewById(R.id.recyclerviewofspecifichat);
        messageArrayList=new ArrayList<>();
        intent=getIntent();
        setSupportActionBar(mtoolbarofspecifichat);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(SpecifiChat.this,messageArrayList);
        messagerecyclerview.setAdapter(messagesAdapter);





        mtoolbarofspecifichat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"toolbar is clicked",Toast.LENGTH_SHORT).show();

            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");
        senderuid=firebaseAuth.getUid();
        receiveruid=getIntent().getStringExtra("receiveruid");
        receivername=getIntent().getStringExtra("name");
        senderoom=senderuid+receiveruid;
        recieveroom=receiveruid+senderuid;
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats").child(senderoom).child("messages");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Message message=snap.getValue(Message.class);
                    Log.d("onrun","message");
                    messageArrayList.add(message);
                    messagesAdapter.notifyDataSetChanged();
                    messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagesAdapter.notifyDataSetChanged();



        backbuttinofspecifichat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        naameofuser.setText(receivername);
        String uri=intent.getStringExtra("imageuri");
        if(uri.isEmpty()){
            Toast.makeText(getApplicationContext(),"null is received",Toast.LENGTH_SHORT).show();

        }
        else{
            Picasso.get().load(uri).into(imageoftheuser);
        }


        messagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredmessage=getmessage.getText().toString();
                if(enteredmessage.isEmpty()){
                    Toast.makeText(getApplicationContext(),"plese entr the message first",Toast.LENGTH_SHORT).show();

                }
                else{
                    Date date=new Date();
                    currentTime=simpleDateFormat.format((calendar.getTime()));
                    Message messages=new Message(enteredmessage,firebaseAuth.getUid(),date.getTime(),currentTime);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats").child(senderoom).child("messages").push()
                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("chats")
                                    .child(recieveroom).child("messages")
                                    .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                    getmessage.setText(null);
                    messagesAdapter.notifyDataSetChanged();
                    



                }







            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null){
            messagesAdapter.notifyDataSetChanged();
        }
    }
}