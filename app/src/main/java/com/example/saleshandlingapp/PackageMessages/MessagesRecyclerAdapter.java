package com.example.saleshandlingapp.PackageMessages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saleshandlingapp.Listeners.RecyclerViewClickListener;
import com.example.saleshandlingapp.Database.Message;
import com.example.saleshandlingapp.R;

import java.util.List;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.MyViewHolder> {

    private List<Message> messages;
    private RecyclerViewClickListener listener;

    private final String ORDER  = "ZAMÓWIENIE";
    private final String DELIVERY = "DOSTAWA";

    public MessagesRecyclerAdapter(List<Message> messages,RecyclerViewClickListener listener){
        this.messages = messages;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView orderType;
        private TextView message;
        private TextView customerNumber;


        public MyViewHolder(final View view){
            super(view);
            orderType = view.findViewById(R.id.typeTextView);
            message = view.findViewById(R.id.messageTextView);
            customerNumber = view.findViewById(R.id.numberTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MessagesRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesRecyclerAdapter.MyViewHolder holder, int position) {
        holder.customerNumber.setText(messages.get(position).number);
        String message = messages.get(position).message;
        if(message.contains(ORDER)){
            holder.orderType.setText(ORDER);
            holder.message.setText(message.replaceAll(ORDER,"").trim());
        }
        else if(message.contains(DELIVERY)){
            holder.orderType.setText(DELIVERY);
            holder.message.setText(message.replaceAll(DELIVERY,"").trim());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
