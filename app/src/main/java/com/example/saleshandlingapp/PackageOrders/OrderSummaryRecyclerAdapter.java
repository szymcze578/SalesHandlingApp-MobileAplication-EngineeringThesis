package com.example.saleshandlingapp.PackageOrders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.R;

import java.util.List;

public class OrderSummaryRecyclerAdapter extends RecyclerView.Adapter<OrderSummaryRecyclerAdapter.MyViewHolder>{
    List<Product> productsInCart;

    public OrderSummaryRecyclerAdapter(List<Product> productsInCart){
        this.productsInCart = productsInCart;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView itemImage;
        private TextView itemType;
        private TextView amount;
        private TextView price;
        private TextView totalPrice;

        public MyViewHolder(final View view){
            super(view);
            itemImage = view.findViewById(R.id.itemImage);
            itemType = view.findViewById(R.id.itemLabel);
            amount = view.findViewById(R.id.ammount);
            price = view.findViewById(R.id.price);
            totalPrice = view.findViewById(R.id.total);
        }

    }
    @NonNull
    @Override
    public OrderSummaryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item,parent,false);
        return new OrderSummaryRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryRecyclerAdapter.MyViewHolder holder, int position) {

        Product product = productsInCart.get(position);
        String filename = product.getImageName();
        holder.itemType.setText(product.getProductName());
        holder.amount.setText(Integer.toString(product.getAmount()));
        holder.price.setText(Double.toString(product.getPrice()) + " zł");
        holder.totalPrice.setText(Double.toString(product.getPrice()*product.getAmount()) + " zł");
        int id = holder.itemView.getContext().getResources().getIdentifier(filename,"drawable",holder.itemView.getContext().getPackageName());
        holder.itemImage.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return productsInCart.size();
    }

}
