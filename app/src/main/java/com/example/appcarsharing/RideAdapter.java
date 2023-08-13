package com.example.appcarsharing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {

    private List<Ride> passaggiList;

    public RideAdapter(List<Ride> passaggiList) {
        this.passaggiList = passaggiList;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passaggio, parent, false);
        return new RideViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride passaggio = passaggiList.get(position);
        holder.bind(passaggio);
    }

    @Override
    public int getItemCount() {
        return passaggiList.size();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {

        private TextView sourceTextView;
        private TextView destinationTextView;
        private TextView seatsTextView;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.sorgente_text_view);
            destinationTextView = itemView.findViewById(R.id.destinazione_text_view);
            seatsTextView = itemView.findViewById(R.id.posti_text_view);
        }

        public void bind(Ride passaggio) {
            sourceTextView.setText(passaggio.getSorgente());
            destinationTextView.setText(passaggio.getDestinazione());
            seatsTextView.setText(String.valueOf(passaggio.getPosti()));
        }
    }
}
