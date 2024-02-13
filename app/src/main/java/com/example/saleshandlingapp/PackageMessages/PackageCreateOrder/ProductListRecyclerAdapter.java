package com.example.saleshandlingapp.PackageMessages.PackageCreateOrder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saleshandlingapp.Listeners.RecyclerViewClickListener;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.R;

import java.util.List;

public class ProductListRecyclerAdapter extends RecyclerView.Adapter<ProductListRecyclerAdapter.MyViewHolder> {

    List<Product> products;
    private RecyclerViewClickListener listener;


    public ProductListRecyclerAdapter(List<Product> products,RecyclerViewClickListener listener){
        this.products = products;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView itemImage;
        private TextView itemType;
        private TextView amount;
        private TextView price;

        public MyViewHolder(final View view){
            super(view);
            itemImage = view.findViewById(R.id.itemImage);
            itemType = view.findViewById(R.id.itemLabel);
            amount = view.findViewById(R.id.ammount);
            price = view.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public ProductListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item,parent,false);
        return new ProductListRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListRecyclerAdapter.MyViewHolder holder, int position) {

        Product product = products.get(position);
        String filename = product.getImageName();
        holder.itemType.setText(product.getProductName());
        holder.amount.setText(Integer.toString(product.getAmount()));
        holder.price.setText(Double.toString(product.getPrice()) + " zł");
/*
        switch (filename){
            case "Jajka"-> holder.itemImage.setImageResource(R.drawable.egg);
            case "Miód pszczeli"-> holder.itemImage.setImageResource(R.drawable.honey);
            case "Kurczak" -> holder.itemImage.setImageResource(R.drawable.hen);
            case "Ziemniaki" -> holder.itemImage.setImageResource(R.drawable.potatoes);
            case "Pomidory" -> holder.itemImage.setImageResource(R.drawable.tomato);
            case "Ogórki" -> holder.itemImage.setImageResource(R.drawable.cucumber);
            case "Marchewki" -> holder.itemImage.setImageResource(R.drawable.carrots);
        }
*/

        int id = holder.itemView.getContext().getResources().getIdentifier(filename,"drawable",holder.itemView.getContext().getPackageName());
        holder.itemImage.setImageResource(id);
        //Uri uri= Uri.parse("https://cdn-icons-png.flaticon.com/256/7315/7315478.png");
        //holder.itemImage.setImageURI(uri);
        //Glide.with(holder.itemView.getContext()).load("https://cdn-icons-png.flaticon.com/256/7315/7315478.png").into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
