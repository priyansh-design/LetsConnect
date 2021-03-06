package com.example.letsconnect;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ChatFragment extends Fragment {


    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    ImageView imageviewofuser;
    FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder> chatAdapter;
    RecyclerView chatrecyclerView;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chatfragment,container,false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        chatrecyclerView=view.findViewById(R.id.recyclerview);
        Query query=firebaseFirestore.collection("Users").whereNotEqualTo("userUid",firebaseAuth.getUid());
        FirestoreRecyclerOptions<FirebaseModel> allusername=new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query,FirebaseModel.class).build();
        chatAdapter=new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allusername) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull FirebaseModel firebaseModel) {
                noteViewHolder.nameofuser.setText(firebaseModel.getName());
                String uri=firebaseModel.getImage();
                Picasso.get().load(uri).into(imageviewofuser);
                if(firebaseModel.getStatus().equals("Online")){
                    noteViewHolder.statusofuser.setText(firebaseModel.getStatus());
                    noteViewHolder.statusofuser.setTextColor(Color.GREEN);
                }
                else{
                    noteViewHolder.statusofuser.setText(firebaseModel.getStatus());
                    noteViewHolder.statusofuser.setTextColor(Color.GRAY);

                }
                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),SpecifiChat.class);
                        intent.putExtra("name",firebaseModel.getName());
                        intent.putExtra("receiveruid",firebaseModel.getUserUid());
                        intent.putExtra("imageuri",firebaseModel.getImage());
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        chatrecyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        chatrecyclerView.setLayoutManager(linearLayoutManager);
        chatrecyclerView.setAdapter(chatAdapter);

        return view;







    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView nameofuser;
        private TextView statusofuser;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            nameofuser=itemView.findViewById(R.id.nameofuser);
            statusofuser=itemView.findViewById(R.id.statusofuser);
            imageviewofuser=itemView.findViewById(R.id.imageofuserincard);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null){
            chatAdapter.stopListening();
        }
    }
}
