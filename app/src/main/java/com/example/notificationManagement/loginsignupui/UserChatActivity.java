package com.example.notificationManagement.loginsignupui;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserChatActivity extends AppCompatActivity {

    //constants
    private String messageReceiverID = "95SXwZNvQwS4fSQV4o7EPUEPe6J3";

    //member variables

    private ImageButton sendMessageButton;
    private EditText messageToBeSend;
    private String messageSenderId;

    private final List<UserInstantMessage> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private userChatAdapter mAdapter;
    private RecyclerView usersChatView;

    //Firebase variables

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    public ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_chat_activity);
        InitialisingObjects();

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        messageSenderId = mAuth.getCurrentUser().getUid();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SendMessage();
            }
        });

    }
    public void InitialisingObjects(){

        sendMessageButton = (ImageButton) findViewById(R.id.sendButton);
        messageToBeSend = (EditText) findViewById(R.id.sendText);

        mAdapter = new userChatAdapter(messageList);
        usersChatView = (RecyclerView) findViewById(R.id.user_chat_listview);
        linearLayoutManager = new LinearLayoutManager(this);
        usersChatView.setLayoutManager(linearLayoutManager);
        usersChatView.setAdapter(mAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();

        RootRef.child("Messages").child(messageSenderId).child(messageReceiverID)
                .addChildEventListener(mChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        UserInstantMessage message = dataSnapshot.getValue(UserInstantMessage.class);
                        messageList.add(message);
                        mAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }





    private void SendMessage(){

        String messageInput = messageToBeSend.getText().toString();

        if(TextUtils.isEmpty(messageInput)){
            Toast.makeText(this, "Please Write a message to send!", Toast.LENGTH_SHORT).show();
        }
        else{

            String messageSenderRef = "Messages/" + messageSenderId + "/" + messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderId;


            DatabaseReference userMessageKeyRef = RootRef.child("Messages").child(messageSenderId)
                    .child(messageReceiverID).push();

            String messagePushId = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message",messageInput);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderId);

            UserInstantMessage chats = new UserInstantMessage(messageSenderId,messageInput,"text");

            Map messageBodydetails = new HashMap();
            messageBodydetails.put(messageSenderRef + "/" + messagePushId,messageTextBody);
            messageBodydetails.put(messageReceiverRef + "/" + messagePushId,messageTextBody);


            RootRef.updateChildren(messageBodydetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()){

                        Toast.makeText(UserChatActivity.this, "Message Sent Successfully..", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        Toast.makeText(UserChatActivity.this, "Error sending the message..", Toast.LENGTH_SHORT).show();
                    }
                    messageToBeSend.setText("");

                }
            });



        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        RootRef.removeEventListener(mChildEventListener);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        RootRef.removeEventListener(mChildEventListener);
    }
}
