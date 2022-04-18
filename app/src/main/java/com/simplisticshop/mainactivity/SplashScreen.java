package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private TextView first, second;
    private static int splashTimeOut=3000;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        second = (TextView)findViewById(R.id.second_txt);


        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        },splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splash_screen_animation);

        second.startAnimation(myanim);
    }

    private void startApp() {

        if (user == null) {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("User").child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("usertype").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int usertype = snapshot.getValue(Integer.class);

                    if (user.isEmailVerified() && usertype == 0) {
                        Toast.makeText(SplashScreen.this, "Welcome User.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                    }

                    if (usertype == 1) {
                        Toast.makeText(SplashScreen.this, "Welcome Admin.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashScreen.this, AdminDashboardActivity.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}