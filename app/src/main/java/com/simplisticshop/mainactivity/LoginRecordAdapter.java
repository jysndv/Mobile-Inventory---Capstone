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

public class LoginRecordAdapter extends FirebaseRecyclerAdapter <LoginRecord, LoginRecordAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public LoginRecordAdapter(@NonNull FirebaseRecyclerOptions<LoginRecord> options) {
        super(options);
    }


    @NonNull
    @Override
    public LoginRecordAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_record, parent, false);
        return new myViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull LoginRecordAdapter.myViewHolder holder, int position, @NonNull LoginRecord model) {
        holder.txtEmail.setText(model.getEmail());
        holder.txtDateLogin.setText(model.getDate());
        holder.txtTimeLogin.setText(model.getTime());

        holder.container.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_transition_animation));
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        public TextView txtEmail;
        public TextView txtDateLogin;
        public TextView txtTimeLogin;

        public RelativeLayout container;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.action_container);

            txtEmail = itemView.findViewById(R.id.viewuser_email);
            txtDateLogin = itemView.findViewById(R.id.viewdate_login);
            txtTimeLogin = itemView.findViewById(R.id.viewtime_login);

        }
    }
}
