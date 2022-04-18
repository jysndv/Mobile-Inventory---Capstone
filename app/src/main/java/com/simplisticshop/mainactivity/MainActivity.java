package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView products, addprod, searchprod, updateprod;

    public static TextView Date, Time;
    TextView email,position;

    ImageButton btnLogOut;
    FirebaseAuth mAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogout);
        email = findViewById(R.id.email_dashboard);
        position = findViewById(R.id.user_position);

        Date = findViewById(R.id.txt_date);
        Time = findViewById(R.id.txt_time);

        products = findViewById(R.id.card_products);
        addprod = findViewById(R.id.card_add_products);
        searchprod = findViewById(R.id.card_search_products);
        updateprod = findViewById(R.id.card_update_product);

        databaseReference = FirebaseDatabase.getInstance().getReference("Logout Record");

        products.setOnClickListener(this);
        addprod.setOnClickListener(this);
        searchprod.setOnClickListener(this);
        updateprod.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("User").child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("usertype").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int usertype = snapshot.getValue(Integer.class);
                if (usertype==0){
                    final FirebaseUser users = mAuth.getCurrentUser();
                    String finaluser=users.getEmail();
                    email.setText(finaluser);
                    position.setText("User");

                }
                if (usertype==1){
                    final FirebaseUser users = mAuth.getCurrentUser();
                    String finaluser=users.getEmail();
                    email.setText(finaluser);
                    position.setText("Admin");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Are you sure you want Logout?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LogoutRecord logoutRecord = new LogoutRecord(email.getText().toString(),
                                Date.getText().toString(),
                                Time.getText().toString());

                        String modelId = databaseReference.push().getKey();
                        databaseReference.child(modelId).setValue(logoutRecord);

                        MainActivity.super.onBackPressed();
                        mAuth.signOut();
                        finish();
                        Toast.makeText(MainActivity.this , "Logout Successfully", Toast.LENGTH_SHORT).show();
                        MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();
            }
        });

        String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

        Date.setText(date);

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
                                Time.setText(currenttime);
                            }
                        });
                    }
                }
                catch (Exception e) {
                    Time.setText(R.string.app_name);
                }
            }
        };
        thread.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.card_add_products:i = new Intent(this,AddProductActivity.class);startActivity(i); break;
            case R.id.card_products : i = new Intent(this,InventoryActivity.class);startActivity(i); break;
            case R.id.card_search_products: i = new Intent(this,ScanItemsActivity.class);startActivity(i); break;
            case R.id.card_update_product: i = new Intent(this,ProductUpdateActivity.class);startActivity(i); break;
            default: break;
        }
    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to Logout?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LogoutRecord logoutRecord = new LogoutRecord(email.getText().toString(),
                        Date.getText().toString(),
                        Time.getText().toString());

                String modelId = databaseReference.push().getKey();
                databaseReference.child(modelId).setValue(logoutRecord);

                MainActivity.super.onBackPressed();
                mAuth.signOut();
                finish();
                Toast.makeText(MainActivity.this , "Logout Successfully.", Toast.LENGTH_SHORT).show();
                MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }


}