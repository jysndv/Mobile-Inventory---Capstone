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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView forgotPass;
    Button btnLogin;
    FirebaseAuth mAuth;

    private DatabaseReference reference;

    private ProgressDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        processDialog = new ProgressDialog(this);
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        forgotPass = (TextView) findViewById(R.id.forgot_pass);
        forgotPass.setOnClickListener(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Admin");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    etLoginEmail.setError("Email cannot be empty");
                    etLoginEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etLoginEmail.setError("Please provide a valid email!");
                    etLoginEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    etLoginPassword.setError("Password cannot be empty");
                    etLoginPassword.requestFocus();
                    return;
                }

                loginValidation(email, password);


            }

        });
    }


    private void loginValidation(final String email, final String password) {
        Query query = reference.orderByChild("admin").equalTo(true);

        processDialog.setMessage("Logging in...");
        processDialog.show();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.exists() && snapshot.hasChild("email"))
                    {
                        String emailDB = snapshot.child("email").getValue(String.class);
                        if (emailDB.equalsIgnoreCase(email)) {
                            loginUser(email, password);
                        }
                        else
                        {
                            Toast.makeText(AdminLoginActivity.this, "Please input admin email.", Toast.LENGTH_SHORT).show();
                            processDialog.dismiss();
                        }
                    }
                    else
                    {
                        Toast.makeText(AdminLoginActivity.this, "Please input admin email.", Toast.LENGTH_SHORT).show();
                        processDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminLoginActivity.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password){
        processDialog.setMessage("Logging in...");
        processDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            processDialog.dismiss();
                            startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                            Toast.makeText(AdminLoginActivity.this, "Admin Login Successfully.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            processDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_pass:
                startActivity(new Intent(this, ForgotPassActivity.class));
                break;
        }
    }

}