package com.simplisticshop.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogoutRecordsActivity extends AppCompatActivity {

    private RecyclerView mrecyclerview;
    private DatabaseReference mdatabaseReference;
    private LogoutRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_records);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Logout Records");
        mrecyclerview = findViewById(R.id.recyclerViewLogoutRecord);
        mrecyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mrecyclerview.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<LogoutRecord> options =
                new FirebaseRecyclerOptions.Builder<LogoutRecord>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Logout Record"), LogoutRecord.class)
                        .build();

        mAdapter = new LogoutRecordAdapter(options);
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