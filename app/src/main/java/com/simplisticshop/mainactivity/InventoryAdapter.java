package com.simplisticshop.mainactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class InventoryAdapter extends FirebaseRecyclerAdapter<Model, InventoryAdapter.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public InventoryAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull InventoryAdapter.myViewHolder holder, int position, @NonNull Model model) {

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

    }

    @NonNull
    @Override
    public InventoryAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_products, parent, false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        public TextView txtProductBarcode;
        public TextView txtProductName;
        public TextView txtProductQuantity;
        public TextView txtProductDescription;
        public TextView txtProductPrice;
        public ImageView imgProductImage;

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

        }
    }
}
