package com.simplisticshop.mainactivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductUpdateActivity extends AppCompatActivity {

    EditText resultsearcheview;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;

    private UserProductItem mAdapter;
    private ArrayList<Model> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);

        resultsearcheview = findViewById(R.id.searchfield);
        mProducts = new ArrayList<>();

        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mrecyclerview = findViewById(R.id.recyclerViews);
        mrecyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mrecyclerview.setLayoutManager(linearLayoutManager);



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

        mAdapter = new UserProductItem(options);
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
