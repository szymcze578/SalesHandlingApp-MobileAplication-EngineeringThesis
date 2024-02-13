package com.example.saleshandlingapp.PackageOrders;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
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
import com.example.saleshandlingapp.Formatters.DateFormatter;
import com.example.saleshandlingapp.Enum.OrderStatus;
import com.example.saleshandlingapp.R;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryRecyclerAdapter extends RecyclerView.Adapter<OrderHistoryRecyclerAdapter.MyViewHolder> {

    private List<OrderWithOrderItemsAndProducts> orders;

    public OrderHistoryRecyclerAdapter(List<OrderWithOrderItemsAndProducts> orders){
        this.orders = orders;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView number,ID,name,city,address,totalPrice,type,isRealized, creationDate,realizationDate;
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
            isRealized = view.findViewById(R.id.isRealized);
            fulfill = view.findViewById(R.id.fulfillButton);
            creationDate = view.findViewById(R.id.creationDate);
            realizationDate = view.findViewById(R.id.realizationDate);
        }
    }

    @NonNull
    @Override
    public OrderHistoryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item,parent,false);
        return new OrderHistoryRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryRecyclerAdapter.MyViewHolder holder, int position) {
        Client client = orders.get(position).client;
        holder.number.setText(Integer.toString(position+1));
        holder.name.setText(client.getName() + " " + client.getSurname());
        holder.city.setText(client.getCity());
        holder.address.setText(client.getAddress());

        Order order = orders.get(position).order;
        holder.ID.setText("#"+ order.getOrderID());
        holder.totalPrice.setText(Double.toString(order.getTotalPrice()));
        holder.type.setText(order.getType().getDescription());


        holder.creationDate.setText(DateFormatter.formatDate(order.getCreationDate()));
        if(order.getRealizationDate()!=null) {
            holder.realizationDate.setText(DateFormatter.formatDate(order.getRealizationDate()));
        }else
            holder.realizationDate.setText("");

        if(order.getOrderStatus()==OrderStatus.COMPLETED){
                Drawable drawable = ResourcesCompat.getDrawable(holder.isRealized.getResources(), R.drawable.check,null);
                holder.isRealized.setText("ZAKOŃCZONE");
                holder.isRealized.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        } else if (order.getOrderStatus()==OrderStatus.CANCELED) {
                Drawable drawable = ResourcesCompat.getDrawable(holder.isRealized.getResources(), R.drawable.cancel,null);
                holder.isRealized.setText("ANULOWANE");
                holder.isRealized.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        }

        holder.itemView.setOnClickListener(v -> createSummaryDialog(v,position));

    }


    private void createSummaryDialog(View view,int itemID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.order_summary_dialog, null);

        RecyclerView productsInOrder = dialogView.findViewById(R.id.productsInCart);
        TextView price;
        Button fulfill;
        Button cancel;

        price = dialogView.findViewById(R.id.totalSum);
        fulfill = dialogView.findViewById(R.id.fulfillOrder);
        cancel = dialogView.findViewById(R.id.cancelOrder);

        fulfill.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

        OrderSummaryRecyclerAdapter summaryRecyclerAdapter = new OrderSummaryRecyclerAdapter(createProductList(orders.get(itemID).orderItems));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        productsInOrder.setLayoutManager(layoutManager);
        productsInOrder.setItemAnimator(new DefaultItemAnimator());
        productsInOrder.setAdapter(summaryRecyclerAdapter);

        price.setText(Double.toString(orders.get(itemID).order.getTotalPrice()));
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void changeOrderStatus(View view, int orderID, OrderStatus status){
        OrderDao orderDao = Database.getInstance(view.getContext()).orderDao();

        orderDao.updateOrderStatus(orders.get(orderID).order.getOrderID(), status);
        orderDao.updateRealizationDate(orders.get(orderID).order.getOrderID(), LocalDateTime.now());
        orders.remove(orderID);
        //notifyDataSetChanged();
        notifyItemRemoved(orderID);
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
