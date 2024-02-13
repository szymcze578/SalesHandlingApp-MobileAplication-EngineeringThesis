package com.example.saleshandlingapp.PackageClients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saleshandlingapp.Listeners.RecyclerViewClickListener;
import com.example.saleshandlingapp.Database.Client;
import com.example.saleshandlingapp.R;

import java.util.List;

public class ClientsListRecyclerAdapter extends RecyclerView.Adapter<ClientsListRecyclerAdapter.ViewHolder> {

    private List<Client> clients;
    private RecyclerViewClickListener listener;

    public ClientsListRecyclerAdapter(List<Client> clients, RecyclerViewClickListener listener) {
        this.clients = clients;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView position;
        private TextView fullname;
        private TextView phoneNumber;
        private TextView city;
        private TextView address;

        public ViewHolder(final View view) {
            super(view);
            position = view.findViewById(R.id.position);
            fullname = view.findViewById(R.id.clientName);
            phoneNumber = view.findViewById(R.id.phoneNumber);
            city = view.findViewById(R.id.city);
            address = view.findViewById(R.id.address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ClientsListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.clients_list_item, parent, false);
        return new ClientsListRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsListRecyclerAdapter.ViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.position.setText(Integer.toString(position+1));
        holder.fullname.setText(client.getName() + " " + client.getSurname());
        holder.phoneNumber.setText(client.getPhoneNumber());
        holder.city.setText(client.getCity());
        holder.address.setText(client.getAddress());
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }
}

