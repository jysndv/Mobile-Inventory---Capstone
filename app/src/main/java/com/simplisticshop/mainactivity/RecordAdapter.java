package com.simplisticshop.mainactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecordAdapter extends FirebaseRecyclerAdapter<Records, RecordAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecordAdapter(@NonNull FirebaseRecyclerOptions<Records> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Records model) {
        holder.email.setText(model.getEmail());
        holder.time.setText(model.getTime());
        holder.prodquantity.setText(model.getProdquantity());
        holder.prodname.setText(model.getProdname());
        holder.prodbarcode.setText(model.getProdbarcode());
        holder.qtyOut.setText(model.getQuantityOut());

        holder.container.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_transition_animation));



    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.records, parent, false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView email, time, prodbarcode, prodname, prodquantity, qtyOut;

        public RelativeLayout container;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.action_container);

            email = (TextView)itemView.findViewById(R.id.view_record_email);
            time = (TextView)itemView.findViewById(R.id.view_record_date);
            prodbarcode = (TextView)itemView.findViewById(R.id.view_record_barcode);
            prodname = (TextView)itemView.findViewById(R.id.view_record_name);
            prodquantity = (TextView)itemView.findViewById(R.id.view_record_quantity);
            qtyOut = (TextView)itemView.findViewById(R.id.view_qty_out);

        }
    }
}
