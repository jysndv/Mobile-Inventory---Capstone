package com.simplisticshop.mainactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener{

    public static TextView Date, Time;
    private CardView products, records, viewuser, loginrecords, logoutrecords, quantityIn;
    TextView email,position;
    ImageButton btnLogOut;
    FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnLogOut = findViewById(R.id.btnLogout);
        email = findViewById(R.id.email_dashboard);
        position = findViewById(R.id.user_position);

        Date = findViewById(R.id.txt_date);
        Time = findViewById(R.id.txt_time);

        databaseReference = FirebaseDatabase.getInstance().getReference("Logout Record");

        products = findViewById(R.id.card_products);
        records = findViewById(R.id.card_records);
        viewuser = findViewById(R.id.card_view_user);
        loginrecords = findViewById(R.id.card_login_records);
        logoutrecords = findViewById(R.id.card_logout_records);
        quantityIn = findViewById(R.id.card_quantity_in);

        products.setOnClickListener(this);
        records.setOnClickListener(this);
        viewuser.setOnClickListener(this);
        logoutrecords.setOnClickListener(this);
        loginrecords.setOnClickListener(this);
        quantityIn.setOnClickListener(this);

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
                    String result = finaluser.substring(0, finaluser.indexOf("@"));
                    String resultemail = result.replace(".","");
                    email.setText(finaluser);
                    position.setText("User");

                }
                if (usertype==1){
                    final FirebaseUser users = mAuth.getCurrentUser();
                    String finaluser=users.getEmail();
                    String result = finaluser.substring(0, finaluser.indexOf("@"));
                    String resultemail = result.replace(".","");
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDashboardActivity.this);
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

                        AdminDashboardActivity.super.onBackPressed();
                        mAuth.signOut();
                        finish();
                        Toast.makeText(AdminDashboardActivity.this , "Logout Successfully", Toast.LENGTH_SHORT).show();
                        AdminDashboardActivity.this.startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
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
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
        }
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.card_products : i = new Intent(this,AdminInventoryActivity.class);startActivity(i); break;
            case R.id.card_records: i = new Intent(this,RecordsActivity.class);startActivity(i); break;
            case R.id.card_view_user: i = new Intent(this,ViewUser.class);startActivity(i); break;
            case R.id.card_login_records: i = new Intent(this,LoginRecordsActivity.class);startActivity(i); break;
            case R.id.card_logout_records: i = new Intent(this,LogoutRecordsActivity.class);startActivity(i); break;
            case R.id.card_quantity_in: i = new Intent(this,QuantityInRecords.class);startActivity(i); break;
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

                AdminDashboardActivity.super.onBackPressed();
                mAuth.signOut();
                finish();
                Toast.makeText(AdminDashboardActivity.this , "Logout Successfully.", Toast.LENGTH_SHORT).show();
                AdminDashboardActivity.this.startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
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