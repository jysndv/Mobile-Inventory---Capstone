package com.simplisticshop.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteProductActivity extends AppCompatActivity {

    public static TextView resultdeleteview;
    Button scantodelete, deletebtn;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        resultdeleteview = findViewById(R.id.barcode_delete);
        scantodelete = findViewById(R.id.btn_scan_delete);
        deletebtn= findViewById(R.id.btn_delete_product);

        scantodelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanDeleteItems.class));
                deletebtn.setVisibility(View.VISIBLE);
            }
        });

        deletebtn.setVisibility(View.INVISIBLE);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletefrmdatabase();
            }

        });

    }

    private void deletefrmdatabase() {

        String deletebarcodevalue = resultdeleteview.getText().toString();
        String modelId = databaseReference.push().getKey();

        if(!TextUtils.isEmpty(deletebarcodevalue)){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Are you sure you want to delete this item??");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String modelId = databaseReference.push().getKey();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
                    databaseReference.child(modelId).removeValue();
                    resultdeleteview.setText("");
                    Toast.makeText(DeleteProductActivity.this,"Item is Deleted",Toast.LENGTH_SHORT).show();
                }
            });


            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();
        }

        else {
        Toast.makeText(DeleteProductActivity.this,"Please scan barcode.",Toast.LENGTH_SHORT).show();
        }

    }

}
