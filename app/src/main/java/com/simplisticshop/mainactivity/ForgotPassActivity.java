package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText fpEmail;
    private Button fpResetPass;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_pass);

        fpEmail = (EditText) findViewById(R.id.fp_email);
        fpResetPass = (Button) findViewById(R.id.fp_reset_pass);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();

        fpResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword(){
        String email = fpEmail.getText().toString().trim();

        if (email.isEmpty()){
            fpEmail.setError("Email is required!");
            fpEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            fpEmail.setError("Please provide a valid email!");
            fpEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        fpResetPass.setVisibility(View.INVISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassActivity.this, "Check your email to reset your password.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    fpResetPass.setVisibility(View.VISIBLE);
                    fpEmail.getText().clear();
                }
                else {
                    Toast.makeText(ForgotPassActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    fpResetPass.setVisibility(View.VISIBLE);
                    fpEmail.getText().clear();
                }
            }
        });
    }
}