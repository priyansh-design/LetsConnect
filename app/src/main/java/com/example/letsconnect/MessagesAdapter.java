package com.example.letsconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message> messageArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public MessagesAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.senderchatlayout,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(context).inflate(R.layout.receiverchatlayout,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message=messageArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.message.setText(message.getMessage());
            viewHolder.timeofmessage.setText(message.getCurrentTime());
        }
        else{
            ReceiverViewHolder viewHolder=(ReceiverViewHolder)holder;
            viewHolder.message.setText(message.getMessage());
            viewHolder.timeofmessage.setText(message.getCurrentTime());
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message=messageArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderuid())){
            return ITEM_SEND;
        }
        else{
            return ITEM_RECIEVE;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }





    class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        TextView timeofmessage;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }
    class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        TextView timeofmessage;


        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }
}
