package com.example.saleshandlingapp.PackageOrders;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saleshandlingapp.Database.Client;
import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Order;
import com.example.saleshandlingapp.Database.OrderDao;
import com.example.saleshandlingapp.Database.OrderItemAndProduct;
import com.example.saleshandlingapp.Database.OrderWithOrderItemsAndProducts;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.Enum.OrderStatus;
import com.example.saleshandlingapp.Enum.OrderType;
import com.example.saleshandlingapp.R;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.MyViewHolder> {

    private List<OrderWithOrderItemsAndProducts> orders;

    public OrdersRecyclerAdapter(List<OrderWithOrderItemsAndProducts> orders){
        this.orders = orders;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView number,ID,name,city,address,totalPrice,type;
        private Button fulfill;
        public MyViewHolder(final View view){
            super(view);
            number = view.findViewById(R.id.position);
            ID = view.findViewById(R.id.orderID);
            name = view.findViewById(R.id.clientName);
            city = view.findViewById(R.id.city);
            address = view.findViewById(R.id.address);
            totalPrice = view.findViewById(R.id.price);
            type = view.findViewById(R.id.type);
            fulfill = view.findViewById(R.id.fulfillButton);
        }
    }

    @NonNull
    @Override
    public OrdersRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_item,parent,false);
        return new OrdersRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersRecyclerAdapter.MyViewHolder holder, int position) {
        Client client = orders.get(position).client;
        holder.number.setText(Integer.toString(position+1));
        holder.name.setText(client.getName() + " " + client.getSurname());
        holder.city.setText(client.getCity());
        holder.address.setText(client.getAddress());

        Order order = orders.get(position).order;
        holder.ID.setText("#"+ order.getOrderID());
        holder.totalPrice.setText(Double.toString(order.getTotalPrice()));
        holder.type.setText(order.getType().getDescription());

        holder.itemView.setOnClickListener(v -> createSummaryDialog(v,position));

        holder.fulfill.setOnClickListener(v -> {
            fulfillOrder(holder.itemView,position,createProductList(orders.get(position).orderItems),new AlertDialog.Builder(holder.itemView.getContext()).create());
        });
    }

    private void createSummaryDialog(View view,int index) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.order_summary_dialog, null);

        RecyclerView productsInOrder = dialogView.findViewById(R.id.productsInCart);
        TextView price;
        Button fulfill;
        Button cancel;

        price = dialogView.findViewById(R.id.totalSum);
        fulfill = dialogView.findViewById(R.id.fulfillOrder);
        cancel = dialogView.findViewById(R.id.cancelOrder);

        List<Product> products = createProductList(orders.get(index).orderItems);

        OrderSummaryRecyclerAdapter summaryRecyclerAdapter = new OrderSummaryRecyclerAdapter(products);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        productsInOrder.setLayoutManager(layoutManager);
        productsInOrder.setItemAnimator(new DefaultItemAnimator());
        productsInOrder.setAdapter(summaryRecyclerAdapter);

        price.setText(Double.toString(orders.get(index).order.getTotalPrice()));
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        fulfill.setOnClickListener(submitView -> {
            fulfillOrder(view,index,products,dialog);
        });

        cancel.setOnClickListener(submitView -> {
            cancelOrder(view,index,products,dialog);
        });

        dialog.show();
    }

    private void fulfillOrder(View view,int index, List<Product>products, AlertDialog dialog){

        AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(view.getContext());
        confirmationBuilder.setMessage("Czy na pewno chcesz zrealizować zlecenie?");
        confirmationBuilder.setCancelable(true);

        confirmationBuilder.setPositiveButton(
                "Tak",
                (dialog_, id) -> {
                    changeOrderStatus(view,index, OrderStatus.COMPLETED);
                    finishOrder(view,index,products);
                    Toast.makeText(view.getContext(),"Zrealizowano zlecenie!",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                );

        confirmationBuilder.setNegativeButton(
                "Nie",
                (dialog_, id) -> dialog_.cancel());
        AlertDialog confirm = confirmationBuilder.create();
        confirm.show();
    }

    private void cancelOrder(View view,int index, List<Product>products,AlertDialog dialog){

        AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(view.getContext());
        confirmationBuilder.setMessage("Czy na pewno chcesz anulować zlecenie?");
        confirmationBuilder.setCancelable(true);

        confirmationBuilder.setPositiveButton(
                "Tak",
                (dialog_, id) -> {
                    changeOrderStatus(view,index, OrderStatus.CANCELED);
                    finishOrder(view,index,products);
                    Toast.makeText(view.getContext(),"Anulowano zlecenie!",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                });

        confirmationBuilder.setNegativeButton(
                "Nie",
                (dialog_, id) -> dialog_.cancel());
        AlertDialog confirm = confirmationBuilder.create();
        confirm.show();
    }

    private void changeOrderStatus(View view, int position,OrderStatus status){
        OrderDao orderDao = Database.getInstance(view.getContext()).orderDao();
        orderDao.updateOrderStatus(orders.get(position).order.getOrderID(), status);
        orderDao.updateRealizationDate(orders.get(position).order.getOrderID(), LocalDateTime.now());
    }

    private void finishOrder( View view, int orderID, List<Product> products){
        if(orders.get(orderID).order.getType() == OrderType.DELIVERY){
            updateWarehouse(view,products);
        }
        orders.remove(orderID);
        notifyItemRemoved(orderID);
    }

    private void updateWarehouse(View view, List<Product> productsInCart){
        Database database = Database.getInstance(view.getContext());
        for(Product product : productsInCart){
            database.productDao().addToWarehouse(product.getID(),product.getAmount());
        }
    }

    private List<Product> createProductList(List<OrderItemAndProduct> orderItemAndProducts){
        List<Product> products = new ArrayList<>();

        for(OrderItemAndProduct orderItemAndProduct : orderItemAndProducts){
            Product product = orderItemAndProduct.product;
            product.setAmount(orderItemAndProduct.orderItem.getAmount());
            product.setPrice(orderItemAndProduct.orderItem.getItemPrice());
            products.add(product);
        }
        return products;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}