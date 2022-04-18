package com.simplisticshop.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecordsActivity extends AppCompatActivity {

    private RecyclerView mrecyclerview;
    private DatabaseReference mdatabaseReference;
    private RecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Quantity Out");
        mrecyclerview = findViewById(R.id.recyclerViews);
        mrecyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mrecyclerview.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<Records> options =
                new FirebaseRecyclerOptions.Builder<Records>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Quantity Out"), Records.class)
                        .build();

        mAdapter = new RecordAdapter(options);
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