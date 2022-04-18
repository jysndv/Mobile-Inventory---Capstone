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

public class UserAdapter extends FirebaseRecyclerAdapter <NewUser, UserAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserAdapter(@NonNull FirebaseRecyclerOptions<NewUser> options) {
        super(options);
    }


    @NonNull
    @Override
    public UserAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_account, parent, false);
        return new myViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.myViewHolder holder, int position, @NonNull NewUser model) {
        holder.txtName.setText(model.getName());
        holder.txtEmail.setText(model.getEmail());
        holder.txtDateCreated.setText(model.getDatecreated());
        holder.txtTimeCreated.setText(model.getTimecreated());

        holder.container.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_transition_animation));
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName;
        public TextView txtEmail;
        public TextView txtDateCreated;
        public TextView txtTimeCreated;

        public RelativeLayout container;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.action_container);

            txtName = itemView.findViewById(R.id.viewuser_name);
            txtEmail = itemView.findViewById(R.id.viewuser_email);
            txtDateCreated = itemView.findViewById(R.id.viewdatecreated);
            txtTimeCreated = itemView.findViewById(R.id.viewtimecreated);

        }
    }
}
