package com.example.appcarsharing;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificheList;
    private FirebaseUser user;

    private boolean isFirstRead = true;
    private DataSnapshot currentsnapshot;
    private Context appContext;


    public NotificationFragment(Context context) {
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.appContext = context;
        this.notificheList = new ArrayList<>();
        this.adapter = new NotificationAdapter(notificheList);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notifiche").child(user.getEmail().substring(0, user.getEmail().indexOf("@")));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                notificheList.clear();
                Notification ultimaNotifica = new Notification();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notifica = snapshot.getValue(Notification.class);
                    notificheList.add(0, notifica);
                    ultimaNotifica = notifica;
                }

                adapter.notifyDataSetChanged();

                if(isFirstRead){
                    isFirstRead = false;
                    return;
                }
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user.getEmail().equals(ultimaNotifica.getMittente().getEmail()))
                    return;

                //controlli necessari per evitare di mandare notifiche ad ogni apertura del fragment
                if (getContext() == null) {  //se il fragment non è aperto
                    lanciaNotifica();
                    return;
                }

                if (getContext() != null) {  //se il fragment è aperto
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appContext = context.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public void lanciaNotifica() {
        NotificationManager notificationManager = appContext.getSystemService(NotificationManager.class);
        String CHANNEL_ID = "my_channel_id";
        String channel_name = "channel_name";
        String channel_description = "channel_description";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channel_description);
            notificationManager.createNotificationChannel(channel);
        }

        //azione da compiere al click sulla notifica
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.putExtra("apriFragment", true); // Aggiungi un extra per indicare di aprire il fragment
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext, CHANNEL_ID).setSmallIcon(android.R.drawable.star_on).setContentTitle("Nuova notifica!")
                //.setContentText("Sappi che è successo questo nel tuo sistema: ...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent);
        notificationManager.notify(0, builder.build());
    }

}