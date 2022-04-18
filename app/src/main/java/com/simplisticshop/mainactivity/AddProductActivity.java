package com.simplisticshop.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    public static TextView Date, Time, currentEmail;
    public  static EditText prodbarcode, prodname, quantity, description, price;
    Button addprod, scanbarcode;
    ImageView prodimg;
    ImageButton upload;

    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    private ProgressDialog processDialog;

    private FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        firebaseAuth = FirebaseAuth.getInstance();


        processDialog = new ProgressDialog(this);
        prodname = findViewById(R.id.edit_prod_name);
        quantity = findViewById(R.id.edit_quantity);
        description = findViewById(R.id.edit_description);
        price = findViewById(R.id.edit_itemprice);
        currentEmail = findViewById(R.id.current_email);
        addprod = findViewById(R.id.btn_add_product);
        prodbarcode = findViewById(R.id.txt_barcode);
        scanbarcode = findViewById(R.id.btn_scan);
        upload = findViewById(R.id.btn_upload);
        prodimg = findViewById(R.id.prodimg);
        Date = findViewById(R.id.txt_date);
        Time = findViewById(R.id.txt_time);

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String result = finaluser.substring(0, finaluser.indexOf("@"));
        String resultemail = result.replace(".","");

        String datecreated = Date.getText().toString();
        String timecreated = Time.getText().toString();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        scanbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScannerActivity.class));
            }
        });

        addprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null)
                {
                    addProducts(imageUri);
                    addprod.setEnabled(false);

                }
                else
                    {
                    Toast.makeText(AddProductActivity.this, "Please complete all the fields.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        prodname.addTextChangedListener(textWatcher);
        prodbarcode.addTextChangedListener(textWatcher);
        quantity.addTextChangedListener(textWatcher);
        description.addTextChangedListener(textWatcher);
        price.addTextChangedListener(textWatcher);
        currentEmail.setText(resultemail);
        Date.setText(datecreated);
        Time.setText(timecreated);


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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String productBarcode = prodbarcode.getText().toString().trim();
            String productName = prodname.getText().toString().trim();
            String productQuantity = quantity.getText().toString().trim();
            String productPrice = price.getText().toString().trim();
            String productDescription = description.getText().toString().trim();



            addprod.setEnabled(!productBarcode.isEmpty()
                    && !productName.isEmpty()
                    && !productQuantity.isEmpty()
                    && !productPrice.isEmpty()
                    && !productDescription.isEmpty());

        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageUri = data.getData();
            Picasso.get()
                    .load(imageUri)
                    .fit()
                    .centerCrop()
                    .into(prodimg);

        }
    }

    private String GetFileExtension(Uri mUri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(mUri));

    }


    private void addProducts(Uri uri) {

            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Model model = new Model(uri.toString(),
                                    prodname.getText().toString(),
                                    prodbarcode.getText().toString(),
                                    quantity.getText().toString(),
                                    description.getText().toString(),
                                    price.getText().toString(),
                                    currentEmail.getText().toString(),
                                    Date.getText().toString(),
                                    Time.getText().toString());

                            String modelId = databaseReference.push().getKey();
                            databaseReference.child(modelId).setValue(model);

                            processDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, "Added Successfully.", Toast.LENGTH_SHORT).show();

                            prodimg.setImageBitmap(null);
                            prodname.getText().clear();
                            prodbarcode.setText(" ");
                            quantity.getText().clear();
                            description.getText().clear();
                            price.getText().clear();

                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    processDialog.setMessage("Adding Product..");
                    processDialog.show();
                    processDialog.setCancelable(false);
                    processDialog.setCanceledOnTouchOutside(false);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    processDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                }
            });

        }



    public void onBackPressed(){
        finish();
    }

}