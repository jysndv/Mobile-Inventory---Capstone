package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ScanItemsActivity extends AppCompatActivity{

    public static EditText resultsearcheview;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
    TextView noresult;
    private ProgressBar progressBar;

    private InventoryAdapter mAdapter;
    private ArrayList<Model> mProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_items);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        noresult = findViewById(R.id.txt_noresult);
        noresult.clearFocus();

        mProducts = new ArrayList<>();

        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        resultsearcheview = findViewById(R.id.searchfield);

        mrecyclerview = findViewById(R.id.recyclerViews);

        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setHasFixedSize(true);

        onStart("");

        resultsearcheview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null){
                    onStart(s.toString());

                }
                else {
                    onStart("");

                }

            }
        });

    }



    public void onStart(String data) {
        super.onStart();

        Query firebaseSearchQuery = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("prodname")
                .startAt(data)
                .endAt(data + "\uf8ff");

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(firebaseSearchQuery, Model.class)
                        .setLifecycleOwner(this)
                        .build();

        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    mrecyclerview.setVisibility(View.INVISIBLE);
                }
                else {
                    mrecyclerview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            mAdapter = new InventoryAdapter(options);
            mAdapter.startListening();
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
