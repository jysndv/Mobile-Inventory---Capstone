package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    public static Button btnUpdate, btnDelete;
    private RecyclerView mrecyclerview;
    private DatabaseReference mdatabaseReference;
    private StorageReference mStorageReference;
    private TextView totalnoofitem;
    private int counttotalnoofitem =0;
    private InventoryAdapter mAdapter;
    private ArrayList<Model> mProducts;

    FirebaseRecyclerOptions<Products> options;
    FirebaseRecyclerAdapter<Products,ViewHolder>adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        btnUpdate = findViewById(R.id.btn_Update);
        btnDelete = findViewById(R.id.btn_Delete);

        totalnoofitem= findViewById(R.id.totalnoitem);
        mProducts = new ArrayList<>();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mrecyclerview = findViewById(R.id.recyclerViews);
        mrecyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mrecyclerview.setLayoutManager(linearLayoutManager);


        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    counttotalnoofitem = (int) dataSnapshot.getChildrenCount();
                    totalnoofitem.setText(Integer.toString(counttotalnoofitem));
                }else{
                    totalnoofitem.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       FirebaseRecyclerOptions<Model> options =
               new FirebaseRecyclerOptions.Builder<Model>()
                       .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), Model.class)
                       .build();

       mAdapter = new InventoryAdapter(options);
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





       // FirebaseRecyclerAdapter<Products, ScanItemsActivity.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Products, ScanItemsActivity.UsersViewHolder>
                //(  Products.class,
                       // R.layout.product_item,
                        //ScanItemsActivity.ViewHolder.class,
                       // mdatabaseReference )
        //{
           // @Override
            //protected void populateViewHolder(ScanItemsActivity.UsersViewHolder viewHolder, Products model, int position){

                //viewHolder.setDetails(getApplicationContext(),model.getProdbarcode(),model.getProdname(),model.getProdquantity(),model.getProddescrip());
            //}
        //};

        //mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
