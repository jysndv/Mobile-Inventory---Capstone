package com.simplisticshop.mainactivity;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    ImageView imageView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView =itemView;
    }
    public void setDetails(Context ctx, String prodbarcode,
                           String prodname,
                           String prodquantity,
                           String proddescrip,
                           String prodprice,
                           String proddate,
                           String prodtime){
        TextView item_barcode = (TextView) mView.findViewById(R.id.viewitembarcode);
        TextView item_name = (TextView) mView.findViewById(R.id.viewitemname);
        TextView item_quantity = (TextView) mView.findViewById(R.id.viewitemquantityleft);
        TextView item_category = (TextView) mView.findViewById(R.id.viewitemcategory);
        TextView item_price = (TextView) mView.findViewById(R.id.viewitemprice);
        TextView item_date_added = (TextView) mView.findViewById(R.id.viewdateadded);
        TextView item_time_added = (TextView) mView.findViewById(R.id.viewtimeadded);
        imageView = itemView.findViewById(R.id.imageView);


        item_barcode.setText(prodbarcode);
        item_name.setText(prodname);
        item_quantity.setText(prodquantity);
        item_category.setText(proddescrip);
        item_price.setText(prodprice + ".00");
        item_date_added.setText(proddate);
        item_time_added.setText(prodtime);



    }

}
