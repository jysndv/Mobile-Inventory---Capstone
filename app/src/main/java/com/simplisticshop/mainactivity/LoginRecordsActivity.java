package com.simplisticshop.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginRecordsActivity extends AppCompatActivity {

    private RecyclerView mrecyclerview;
    private DatabaseReference mdatabaseReference;
    private LoginRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_records);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Login Record");
        mrecyclerview = findViewById(R.id.recyclerViewLoginRecord);
        mrecyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mrecyclerview.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<LoginRecord> options =
                new FirebaseRecyclerOptions.Builder<LoginRecord>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Login Record"), LoginRecord.class)
                        .build();

        mAdapter = new LoginRecordAdapter(options);
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
