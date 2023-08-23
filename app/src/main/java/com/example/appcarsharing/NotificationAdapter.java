package com.example.appcarsharing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificheList;

    public NotificationAdapter(List<Notification> notificheList) {
        this.notificheList = notificheList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notifica = notificheList.get(position);
        holder.bind(notifica);
    }

    @Override
    public int getItemCount() {
        return notificheList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView mittenteTextView,messaggioTextView,dataTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            mittenteTextView = itemView.findViewById(R.id.textview);
            dataTextView = itemView.findViewById(R.id.textview2);
            messaggioTextView = itemView.findViewById(R.id.textview3);
        }

        public void bind(Notification notifica) {
             mittenteTextView.setText(notifica.getMittente());
             dataTextView.setText("ciao");
             messaggioTextView.setText(notifica.getMessaggio());
        }

    }

}
