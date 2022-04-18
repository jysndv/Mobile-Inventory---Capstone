package com.simplisticshop.mainactivity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserProductItem extends FirebaseRecyclerAdapter<Model, UserProductItem.myViewHolder> {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceOut;
    private DatabaseReference databaseReferenceIn;



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserProductItem(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull Model model) {

        Picasso.get()
                .load(model.getImageUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(holder.imgProductImage);

        holder.txtProductBarcode.setText(model.getProdbarcode());
        holder.txtProductName.setText(model.getProdname());
        holder.txtProductDescription.setText(model.getProddescrip());
        holder.txtProductQuantity.setText(model.getProdquantity());
        holder.txtProductPrice.setText(model.getProdprice() + ".00");

        holder.container.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_transition_animation));


        holder.updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.txtProductBarcode.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1400)
                        .create();
                //dialogPlus.show();

                View view = dialogPlus.getHolderView();

                EditText productbarcode = view.findViewById(R.id.txt_barcode);
                EditText productname = view.findViewById(R.id.prod_name_update);
                EditText productquantity = view.findViewById(R.id.quantity_update);
                EditText productprice = view.findViewById(R.id.update_itemprice);
                EditText productdescription = view.findViewById(R.id.prod_description_update);
                TextView currentEmail = view.findViewById(R.id.current_email);
                TextView currentTime = view.findViewById(R.id.txt_time);
                EditText qtyIn = view.findViewById(R.id.quantity_in);
                EditText qtyOut = view.findViewById(R.id.quantity_out);

                Button btnUpdate = view.findViewById(R.id.btn_product_update);
                Button btnQtyIn = view.findViewById(R.id.btn_qty_in);
                Button btnQtyOut = view.findViewById(R.id.btn_qty_out);


                productbarcode.setText(model.getProdbarcode());
                productname.setText(model.getProdname());
                productquantity.setText(model.getProdquantity());
                productprice.setText(model.getProdprice());
                productdescription.setText(model.getProddescrip());
                currentEmail.setText(model.getEmail());


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm:a");
                String currenttime = simpleDateFormat.format (calendar.getTime());
                currentTime.setText(currenttime);



                dialogPlus.show();

                btnQtyIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int quantityIn = 0;
                        int qtyLeft;
                        qtyLeft = Integer.parseInt(productquantity.getText().toString().trim());
                        quantityIn = Integer.parseInt(qtyIn.getText().toString().trim());
                        int totalQtyIn = qtyLeft + quantityIn;

                        productquantity.setText(Integer.toString(totalQtyIn));

                    }
                });

                btnQtyOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int quantityOut = 0;
                        int qty_Left;
                        qty_Left = Integer.parseInt(productquantity.getText().toString().trim());
                        quantityOut = Integer.parseInt(qtyOut.getText().toString().trim());
                        int totalQtyOut = qty_Left - quantityOut;

                        productquantity.setText(Integer.toString(totalQtyOut));

                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int quantity = 100;

                        int prodquan = Integer.parseInt(productquantity.getText().toString());

                        if (prodquan <= quantity){
                            productquantity.setError("Low Stock");
                            productquantity.requestFocus();
                        }

                        String pname = productname.getText().toString().trim();
                        String pprice = productprice.getText().toString().trim();
                        String pdescription = productdescription.getText().toString().trim();

                        if (TextUtils.isEmpty(pname)){
                            productname.setError("Product name cannot be empty");
                            productname.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(pprice)){
                            productprice.setError("Product price cannot be empty");
                            productprice.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(pdescription)){
                            productdescription.setError("Product description cannot be empty");
                            productdescription.requestFocus();
                            return;
                        }

                        firebaseAuth = FirebaseAuth.getInstance();
                        final FirebaseUser users = firebaseAuth.getCurrentUser();
                        String finaluser=users.getEmail();
                        String result = finaluser.substring(0, finaluser.indexOf("@"));
                        String resultemail = result.replace(".","");
                        currentEmail.setText(resultemail);


                        Map<String, Object> map = new HashMap<>();
                        map.put("prodbarcode", productbarcode.getText().toString());
                        map.put("prodname", productname.getText().toString());
                        map.put("prodquantity", productquantity.getText().toString().trim());
                        map.put("prodprice", productprice.getText().toString());
                        map.put("proddescrip", productdescription.getText().toString());
                        map.put("email", currentEmail.getText().toString());


                        FirebaseDatabase.getInstance().getReference().child("Products")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        databaseReferenceOut = FirebaseDatabase.getInstance().getReference().child("Quantity Out");
                                        databaseReferenceIn = FirebaseDatabase.getInstance().getReference().child("Quantity In");



                                        if (btnQtyOut != null){
                                            Records records = new Records(productname.getText().toString(),
                                                    productbarcode.getText().toString(),
                                                    productquantity.getText().toString(),
                                                    currentEmail.getText().toString(),
                                                    currentTime.getText().toString(),
                                                    qtyOut.getText().toString());

                                            String modelId = databaseReferenceOut.push().getKey();
                                            databaseReferenceOut.child(modelId).setValue(records);
                                            qtyOut.setText("0");
                                        }

                                        if (qtyIn != null){
                                            QuantityIn quantityIn = new QuantityIn(productname.getText().toString(),
                                                    productbarcode.getText().toString(),
                                                    productquantity.getText().toString(),
                                                    currentEmail.getText().toString(),
                                                    currentTime.getText().toString(),
                                                    qtyIn.getText().toString());

                                            String modelId = databaseReferenceIn.push().getKey();
                                            databaseReferenceIn.child(modelId).setValue(quantityIn);
                                            qtyIn.setText("0");
                                        }

                                        Toast.makeText(holder.txtProductName.getContext(), "Successfully Updated.", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.txtProductName.getContext(),"Failed to Update Product.", Toast.LENGTH_LONG).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

    }

    private void runOnUiThread(Runnable runnable) {

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_product_item, parent, false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        public TextView txtProductBarcode;
        public TextView txtProductName;
        public TextView txtProductQuantity;
        public TextView txtProductDescription;
        public TextView txtProductPrice;
        public ImageView imgProductImage;
        public Button updateProduct, qtyIn, qtyOut;
        public TextView currentEmail, txtQuantityIn, txtQuantityOut;
        public TextView currentDate, currentTime;
        public RelativeLayout container;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.action_container);

            txtProductBarcode = itemView.findViewById(R.id.viewitembarcode);
            txtProductName = itemView.findViewById(R.id.viewitemname);
            txtProductDescription = itemView.findViewById(R.id.viewitemcategory);
            txtProductQuantity = itemView.findViewById(R.id.viewitemquantityleft);
            txtProductPrice = itemView.findViewById(R.id.viewitemprice);
            imgProductImage = itemView.findViewById(R.id.imageView);
            updateProduct = itemView.findViewById(R.id.btn_Update);
            currentEmail = itemView.findViewById(R.id.current_email);
            currentDate = itemView.findViewById(R.id.txt_date);
            currentTime = itemView.findViewById(R.id.txt_time);
            txtQuantityIn = itemView.findViewById(R.id.quantity_in);
            txtQuantityOut = itemView.findViewById(R.id.quantity_out);

            qtyIn = itemView.findViewById(R.id.btn_qty_in);
            qtyOut = itemView.findViewById(R.id.btn_qty_out);


        }
    }

}
