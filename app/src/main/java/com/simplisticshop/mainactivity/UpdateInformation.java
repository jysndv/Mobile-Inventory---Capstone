package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateInformation extends AppCompatActivity {


    TextView Prodbarcode, Proddate, Prodtime;
    EditText Prodname, Prodquantity, Proddescription, Prodprice;
    DatabaseReference databaseReference;
    Button btnUpdate;
    FirebaseAuth mAuth;

    private String prodbarcode, prodname, prodquantity, proddescrip, prodprice;

    int quantity = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        prodbarcode = getIntent().getStringExtra("prodbarcode");
        prodname = getIntent().getStringExtra("prodname");
        prodquantity = getIntent().getStringExtra("prodquantity");
        proddescrip = getIntent().getStringExtra("proddescrip");
        prodprice = getIntent().getStringExtra("prodprice");

        Prodbarcode = findViewById(R.id.txt_barcode);
        Prodname = findViewById(R.id.prod_name_update);
        Prodquantity = findViewById(R.id.quantity_update);
        Proddescription = findViewById(R.id.prod_description_update);
        Prodprice = findViewById(R.id.update_itemprice);
        Proddate = findViewById(R.id.txt_date);
        Prodtime = findViewById(R.id.txt_time);
        btnUpdate = findViewById(R.id.btn_product_update);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");


        Prodbarcode.setText(prodbarcode);
        Prodname.setText(prodname);
        Prodquantity.setText(prodquantity);
        Proddescription.setText(proddescrip);
        Prodprice.setText(prodprice);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pbarcode = Prodbarcode.getText().toString();
                String pname = Prodname.getText().toString();
                String pquantity = Prodquantity.getText().toString();
                String pdescrip = Proddescription.getText().toString();
                String pprice = Prodprice.getText().toString();



                updateData(pbarcode, pname, pquantity, pdescrip, pprice);

                int prodquan = Integer.parseInt(Prodquantity.getText().toString());

                if (prodquan <= quantity){
                    Prodquantity.setError("Low Stock");
                    Prodquantity.requestFocus();
                }


            }
        });

        String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

        Proddate.setText(date);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
                                String currenttime = simpleDateFormat.format (calendar.getTime());
                                Prodtime.setText(currenttime);
                            }
                        });
                    }
                }
                catch (Exception e) {
                    Prodtime.setText(R.string.app_name);
                }
            }
        };
        thread.start();



    }

    private void updateData(String prodbarcode, String pname, String pquantity, String pdescrip, String pprice){
        HashMap Products = new HashMap();
        Products.put("prodname", pname);
        Products.put("prodquantity", pquantity);
        Products.put("proddescrip", pdescrip);
        Products.put("prodprice", pprice);




        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        databaseReference.child(prodbarcode).updateChildren(Products)
                .addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(UpdateInformation.this,"Product Updated.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UpdateInformation.this,"Failed to Update Product!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}