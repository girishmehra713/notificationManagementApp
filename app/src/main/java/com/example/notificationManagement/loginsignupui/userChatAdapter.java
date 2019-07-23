package com.example.notificationManagement.loginsignupui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class userChatAdapter extends RecyclerView.Adapter<userChatAdapter.MessageViewHolder> {

    private List<UserInstantMessage> userInstantMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    public userChatAdapter(List<UserInstantMessage> userInstantMessageList){

        this.userInstantMessageList = userInstantMessageList;
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessageText,receiverMessageText;
        public CircleImageView receiverprofileImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
            receiverprofileImage = (CircleImageView) itemView.findViewById(R.id.profile_image);


        }
    }





    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_list_item,viewGroup,false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);

    }






    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        String SenderMessageId = mAuth.getCurrentUser().getUid();

        UserInstantMessage messages = userInstantMessageList.get(position);

        String FromUserId = messages.getFrom();
        String messageType = messages.getType();


        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FromUserId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("image")){

                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).placeholder(R.drawable.ic_person_black_24dp).into(holder.receiverprofileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(messageType.equals("text")){

            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.receiverprofileImage.setVisibility(View.INVISIBLE);

            if(FromUserId.equals(SenderMessageId)){

                holder.senderMessageText.setBackgroundResource(R.drawable.sender_message_background);
                holder.senderMessageText.setText(messages.getMessage());

            }
            else if(FromUserId.equals("95SXwZNvQwS4fSQV4o7EPUEPe6J3")){

                holder.senderMessageText.setVisibility(View.INVISIBLE);
                holder.receiverprofileImage.setVisibility(View.VISIBLE);
                holder.receiverMessageText.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message);
                holder.receiverMessageText.setText(messages.getMessage());

            }

        }


    }






    @Override
    public int getItemCount() {
        return userInstantMessageList.size();
    }








}

