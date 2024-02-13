package com.example.saleshandlingapp.PackageMessages.PackageCreateOrder;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.R;

import java.util.List;

public class CartListRecyclerAdapter extends RecyclerView.Adapter<CartListRecyclerAdapter.MyViewHolder> {

    List<Product> productsInCart;
    List<Product> productList;
    ProductListRecyclerAdapter adapter;
    TextView totalSum;
    TextView amountInCart;

    public CartListRecyclerAdapter(List<Product> productsInCart, List<Product>allProducts,ProductListRecyclerAdapter adapter, TextView summaryTextView, TextView amountInCart){
        this.productsInCart = productsInCart;
        this.productList = allProducts;
        this.adapter = adapter;
        this.totalSum = summaryTextView;
        this.amountInCart = amountInCart;
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
    public CartListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item,parent,false);
        return new CartListRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListRecyclerAdapter.MyViewHolder holder, int position) {

        Product product = productsInCart.get(position);
        String filename = product.getImageName();
        holder.itemType.setText(product.getProductName());
        holder.amount.setText(Integer.toString(product.getAmount()));
        holder.price.setText(Double.toString(product.getPrice()) + " zł");
        holder.totalPrice.setText(Double.toString(product.getPrice()*product.getAmount()) + " zł");

        holder.itemView.setOnLongClickListener(v -> {
            removeFromCartDialog(v,position,product);
            return true;
        });

        int id = holder.itemView.getContext().getResources().getIdentifier(filename,"drawable",holder.itemView.getContext().getPackageName());
        holder.itemImage.setImageResource(id);
    }

    private void removeFromCartDialog(View view,int position, Product product){

        AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(view.getContext());
        confirmationBuilder.setMessage("Czy na pewno chcesz usunąć produkt z koszyka?");
        confirmationBuilder.setCancelable(true);

        confirmationBuilder.setPositiveButton(
                "Tak",
                (dialog, id) -> {
                    productList.get(product.getID()-1).increaseAmount(product.getAmount());
                    productsInCart.remove(position);
                    Database.getInstance(view.getContext()).cartItemDao().deleteProductFromCart(product.getID());
                    adapter.notifyItemChanged(product.getID()-1);
                    notifyItemRemoved(position);
                    totalSum.setText(Double.toString(Product.calculateValue(productsInCart)) +" zł");
                    amountInCart.setText(String.valueOf(productsInCart.size()));
                    dialog.dismiss();
                }
        );

        confirmationBuilder.setNegativeButton(
                "Nie",
                (dialog, id) -> dialog.cancel());
        AlertDialog confirm = confirmationBuilder.create();
        confirm.show();
    }

    @Override
    public int getItemCount() {
        return productsInCart.size();
    }

}
