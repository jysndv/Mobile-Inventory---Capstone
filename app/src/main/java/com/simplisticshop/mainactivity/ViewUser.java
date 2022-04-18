package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewUser extends AppCompatActivity {

    private RecyclerView mrecyclerview;
    private DatabaseReference mdatabaseReference;
    private UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference("User").child("Users");
        mrecyclerview = findViewById(R.id.recyclerViewUser);
        mrecyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mrecyclerview.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<NewUser> options =
                new FirebaseRecyclerOptions.Builder<NewUser>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("User").child("Users"), NewUser.class)
                        .build();

        mAdapter = new UserAdapter(options);
        mrecyclerview.setAdapter(mAdapter);
    }


    @Override
    protected void onStart(){
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mAdapter.stopListening();
    }
}