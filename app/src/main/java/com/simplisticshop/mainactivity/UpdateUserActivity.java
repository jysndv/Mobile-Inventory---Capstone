package com.simplisticshop.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateUserActivity extends AppCompatActivity {

    public static TextView Date, Time;
    TextView txt_email;
    EditText edit_name, edit_pass;
    DatabaseReference databaseReference;
    Button btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Date = findViewById(R.id.txt_date);
        Time = findViewById(R.id.txt_time);

        txt_email = findViewById(R.id.txt_email);
        edit_name = findViewById(R.id.edit_name);
        edit_pass = findViewById(R.id.edit_password);
        btnDelete = findViewById(R.id.btn_delete_user);

        String Uemail=getIntent().getStringExtra("email");
        String Uname=getIntent().getStringExtra("name");
        String Upass=getIntent().getStringExtra("password");

        txt_email.setText(Uemail);
        edit_name.setText(Uname);
        edit_pass.setText(Upass);


        Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = month + "-" + day + "-" + year;

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

}