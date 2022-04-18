package com.simplisticshop.mainactivity;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderUser extends RecyclerView.ViewHolder {
    View mView;
    public RelativeLayout container;

    public ViewHolderUser(@NonNull View itemView) {
        super(itemView);
        mView =itemView;
        container = mView.findViewById(R.id.action_container);
    }
    public void setDetails(Context ctx, String name,
                           String email,
                           String datecreated,
                           String timecreated )
    {
        TextView user_name = (TextView) mView.findViewById(R.id.viewuser_name);
        TextView user_email = (TextView) mView.findViewById(R.id.viewuser_email);
        TextView date_created = (TextView) mView.findViewById(R.id.viewdatecreated);
        TextView time_created = (TextView) mView.findViewById(R.id.viewtimecreated);


        user_name.setText(name);
        user_email.setText(email);
        date_created.setText(datecreated);
        time_created.setText(timecreated);

    }

}
