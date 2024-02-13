package com.example.saleshandlingapp.PackageWarehouse;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.R;

import java.util.List;

public class WarehouseRecyclerAdapter extends RecyclerView.Adapter<WarehouseRecyclerAdapter.MyViewHolder> {

    List<Product> products;
    Fragment fragment;

    public WarehouseRecyclerAdapter(List<Product> products,Fragment fragment){
        this.products = products;
        this.fragment = fragment;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView itemImage;
        private TextView itemType;
        private TextView amount;
        private TextView price;
        private ImageButton button;

        public MyViewHolder(final View view){
            super(view);
            itemImage = view.findViewById(R.id.itemImage);
            itemType = view.findViewById(R.id.itemLabel);
            amount = view.findViewById(R.id.ammount);
            price = view.findViewById(R.id.price);
            button = view.findViewById(R.id.addButton);
        }

    }
    @NonNull
    @Override
    public WarehouseRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouse_item,parent,false);
        return new WarehouseRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WarehouseRecyclerAdapter.MyViewHolder holder, int position) {

        Product product = products.get(position);
        String filename = product.getImageName();
        holder.itemType.setText(product.getProductName());
        holder.amount.setText(Integer.toString(product.getAmount()));
        holder.price.setText(product.getPrice() + " zł");
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
        holder.button.setOnClickListener(v-> createAddingDialog(v,product.getID()));

        holder.itemView.setOnClickListener(v -> fragment.getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_exit_anim)
                .replace(R.id.frame, ChangeItemPropertiesFragment.newInstance(product.getID()))
                .addToBackStack(null)
                .commit());
    }

    private void createAddingDialog(View view,int itemID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.add_amount_dialog, null);

        EditText amountToAdd;
        Button submit;

        submit = dialogView.findViewById(R.id.addToWarehouse);
        amountToAdd = dialogView.findViewById(R.id.warehouseAddition);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        submit.setOnClickListener(submitView -> {
            String value = amountToAdd.getText().toString();
            try {
                int amount = Integer.parseInt(value);

                if (amount > 50) {
                    Toast.makeText(submitView.getContext(), "Jednorazowo możesz dodać tylko 50 artykułów!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(submitView.getContext(), "Dodano artykuł do magazynu.", Toast.LENGTH_SHORT).show();
                    Database.getInstance(submitView.getContext()).productDao().addToWarehouse(itemID, amount);
                    ((WarehouseFragment)fragment).refreshFragment();
                    dialog.dismiss();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(submitView.getContext(), "Podano nieprawidłową ilość", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
