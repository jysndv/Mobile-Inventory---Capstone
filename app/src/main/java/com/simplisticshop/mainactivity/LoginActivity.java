package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static TextView Date, Time;
    TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView forgotPass, registerHere;
    Button btnLogin;
    FirebaseAuth mAuth;


    private DatabaseReference reference;
    private DatabaseReference databaseReference;

    private ProgressDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        processDialog = new ProgressDialog(this);
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        btnLogin = findViewById(R.id.btnLogin);

        forgotPass = (TextView) findViewById(R.id.forgot_pass);
        registerHere = (TextView) findViewById(R.id.register_here);
        Date = findViewById(R.id.txt_date);
        Time = findViewById(R.id.txt_time);

        mAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("Users").child("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Login Record");

        forgotPass.setOnClickListener(this);
        registerHere.setOnClickListener(this);

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.loginUser();
            }
        });

    }
    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etLoginEmail.setError("Please provide a valid email!");
            etLoginEmail.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{

            processDialog.setTitle("Checking Credentials");
            processDialog.setMessage("Logging in...");
            processDialog.show();
            processDialog.setCancelable(false);
            processDialog.setCanceledOnTouchOutside(false);

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child("User").child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("usertype").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    LoginRecord loginRecord = new LoginRecord(etLoginEmail.getText().toString(),
                                            Date.getText().toString(),
                                            Time.getText().toString());

                                    String modelId = databaseReference.push().getKey();
                                    databaseReference.child(modelId).setValue(loginRecord);

                                    int usertype = snapshot.getValue(Integer.class);

                                    if (usertype == 1) {
                                        processDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Admin Successfully Login.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                        startActivity(intent);
                                    }


                                    if (user.isEmailVerified() && usertype == 0) {
                                        processDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "User Login Successfully.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        processDialog.dismiss();
                                    }

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    }
                    else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        processDialog.dismiss();
                    }
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_pass: startActivity(new Intent(this, ForgotPassActivity.class)); break;
            case R.id.register_here: startActivity(new Intent(this, RegisterActivity.class)); break;
            default:break;

        }
    }

}