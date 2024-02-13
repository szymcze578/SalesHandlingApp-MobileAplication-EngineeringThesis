package com.example.saleshandlingapp.PackageMessages.PackageCreateOrder;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saleshandlingapp.Listeners.RecyclerViewClickListener;
import com.example.saleshandlingapp.PackageClients.CreateClientFragment;
import com.example.saleshandlingapp.Database.CartItem;
import com.example.saleshandlingapp.Database.CartItemAndProduct;
import com.example.saleshandlingapp.Database.Client;
import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.Message;
import com.example.saleshandlingapp.Database.Order;
import com.example.saleshandlingapp.Database.OrderItem;
import com.example.saleshandlingapp.Database.Product;
import com.example.saleshandlingapp.Enum.OrderStatus;
import com.example.saleshandlingapp.Enum.OrderType;
import com.example.saleshandlingapp.R;
import com.example.saleshandlingapp.SMSServices.SMSSender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOrderFragment extends Fragment {


    private List<Product> products;
    private List<Product> productsInCart;
    private ProductListRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerViewClickListener listener;
    private double cartPrice;
    private TextView amountInCart;
    private Message smsMessage;
    private Client client;
    private TextView orderTypeTextView;
    private TextView messageContent;
    private TextView clientPhoneNumber;
    private FloatingActionButton cart;

    private OrderType orderType;


    public CreateOrderFragment() {
        // Required empty public constructor
        productsInCart = new ArrayList<>();
        cartPrice = 0;
    }

    public static CreateOrderFragment newInstance(int id) {
        CreateOrderFragment fragment = new CreateOrderFragment();
        Bundle args = new Bundle();
        args.putInt("message_ID",id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        products = Database.getInstance(getContext()).productDao().getAll();

        getCart();

        getParentFragmentManager()
                .setFragmentResultListener("resultKey", this, (requestKey, result) -> {
            Product res = result.getParcelable("orderToAdd");
            if(res!=null) {

                if(!addToCart(res)){
                    productsInCart.add(res);
                    insertCartItem(res);
                }

                products.get(res.getID()-1).decreaseAmount(res.getAmount());
                amountInCart.setText(String.valueOf(productsInCart.size()));
                setAdapter();
            }
        });

        getParentFragmentManager()
                .setFragmentResultListener("clientAddedStatus", this, (requestKey, result) -> {
                    boolean isClientAdded = result.getBoolean("isAdded");
                    if(!isClientAdded){

                    }
                });

        if (getArguments() != null) {
            int message_ID = getArguments().getInt("message_ID");
            smsMessage = Database.getInstance(getContext()).messageDao().getMessageByID(message_ID);
            client = Database.getInstance(getContext()).clientDao().getClientByPhoneNumber(smsMessage.number);
        }
    }


    public void getCart(){
        List<CartItemAndProduct> cartList = Database.getInstance(getContext()).cartItemDao().getCartList();

        for(CartItemAndProduct cartItemList : cartList){
            Product product = cartItemList.product;
            product.setAmount(cartItemList.cartItem.getAmount());
            productsInCart.add(product);
            products.get(product.getID()-1).decreaseAmount(cartItemList.cartItem.getAmount());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_order, container, false);

        //List and floating button
        recyclerView = view.findViewById(R.id.ProductListRecyclerAdapter);
        amountInCart = view.findViewById(R.id.quantityOnButton);
        amountInCart.setText(String.valueOf(productsInCart.size()));

        //Message
        orderTypeTextView = view.findViewById(R.id.typeTextView);
        messageContent = view.findViewById(R.id.messageTextView);
        clientPhoneNumber = view.findViewById(R.id.numberTextView);

        setMessage();

        cart = view.findViewById(R.id.cart);
        cart.setOnClickListener(v -> createOrderDialog());

        if(!ifClientExists()){
            addClientDialog();
        }

        setOnClickListener();
        adapter = new ProductListRecyclerAdapter(products, listener);
        setAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
        if(client==null){
            client = Database.getInstance(getContext()).clientDao().getClientByPhoneNumber(smsMessage.number);
        }
    }

    private void setAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener(){
        listener = (V, position) ->{

            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim,
                            androidx.navigation.ui.R.animator.nav_default_exit_anim)
                    .replace(R.id.frame, ProductDetailFragment.newInstance(products.get(position)))
                    .addToBackStack(null)
                    .commit();
        };
    }

    private void setMessage(){
        String message = smsMessage.message;

        if(message.contains(OrderType.ORDER.getDescription())){
            orderType = OrderType.ORDER;
            orderTypeTextView.setText(orderType.getDescription());
            messageContent.setText(message.replaceAll(orderType.getDescription(),"").trim());
        } else if (message.contains(OrderType.DELIVERY.getDescription())) {
            orderType = OrderType.DELIVERY;
            orderTypeTextView.setText(orderType.getDescription());
            messageContent.setText(message.replaceAll(orderType.getDescription(),"").trim());
        }
        clientPhoneNumber.setText(smsMessage.number);
    }

    private void createOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater
                .from(getContext())
                .inflate(R.layout.product_in_order_dialog, null);

        RecyclerView productsInOrder = dialogView.findViewById(R.id.productsInCart);
        TextView totalSum = dialogView.findViewById(R.id.totalSum);
        cartPrice = Product.calculateValue(productsInCart);
        totalSum.setText(Double.toString(cartPrice));
        Button createOrder = dialogView.findViewById(R.id.createOrder);

        CartListRecyclerAdapter dialogAdapter = new CartListRecyclerAdapter(productsInCart,products,adapter, totalSum,amountInCart);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        productsInOrder.setLayoutManager(layoutManager);
        productsInOrder.setItemAnimator(new DefaultItemAnimator());
        productsInOrder.setAdapter(dialogAdapter);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        createOrder.setOnClickListener(submitView -> {
            confirmOrderCreation(getView(),dialog);
            //dialog.dismiss();
        });
        dialog.show();
    }

    private void confirmOrderCreation(View view,AlertDialog dialog){

        AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(view.getContext());
        confirmationBuilder.setMessage("Czy na pewno chcesz utworzyć nowe zamówienie?");
        confirmationBuilder.setCancelable(true);

        confirmationBuilder.setPositiveButton(
                "Tak",
                (dialog_, id) -> {
                    createOrder();
                    Toast.makeText(getContext(),"Dodano nowe zamówienie", Toast.LENGTH_LONG).show();
                    clearAndUpdateCart();
                    Database.getInstance(getContext()).messageDao().deleteMessage(smsMessage.ID);
                    getParentFragmentManager().popBackStack();
                    dialog.dismiss();
                }
        );

        confirmationBuilder.setNegativeButton(
                "Nie",
                (dialog_, id) -> dialog_.cancel());
        AlertDialog confirm = confirmationBuilder.create();
        confirm.show();
    }

    private void createOrder(){
        LocalDateTime time = LocalDateTime.now();
        Order order = new Order(client.getID(),time, OrderStatus.NOT_COMPLETED,cartPrice,orderType);
        Database database = Database.getInstance(getContext());
        long orderID = database.orderDao().insertOrders(order);

        for(Product product : productsInCart){
            OrderItem orderItem = new OrderItem();

            orderItem.setOrderID((int)orderID);
            orderItem.setProductID(product.getID());
            orderItem.setAmount(product.getAmount());
            orderItem.setItemPrice(product.getPrice());

            database.orderItemDao().insertOrderItem(orderItem);
        }
        if (orderType == OrderType.ORDER){
            updateWarehouse(productsInCart);
        }
        SMSSender.sendAdd(client.getPhoneNumber(), confirmationMessage());
        database.cartItemDao().deleteAll();
    }

    private String confirmationMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append("Utworzono nowe zamówienie na produkty:\n");

        for (Product product : productsInCart){
            builder.append(product.getProductName());
            builder.append(" ");
            builder.append(product.getPrice());
            builder.append(" zł, liczba: ");
            builder.append(product.getAmount());
            builder.append("\n");
        }
        builder.append("Całkowity koszt zamówienia: "+ cartPrice);
        return builder.toString();
    }

    private void clearAndUpdateCart(){
        productsInCart.clear();
        cartPrice = 0;
        amountInCart.setText(Integer.toString(0));
    }

    private void updateWarehouse(List<Product> productsInCart){
        Database database = Database.getInstance(getContext());
        for(Product product : productsInCart){
            database.productDao().takeFromWarehouse(product.getID(),product.getAmount());
        }
    }

    private boolean addToCart(Product newProduct){
        for(int i = 0; i< productsInCart.size();i++){
            if(productsInCart.get(i).getID()==newProduct.getID()){
                productsInCart.get(i).increaseAmount(newProduct.getAmount());
                Database.getInstance(getContext()).cartItemDao().increaseProductAmount(i+1,newProduct.getAmount());
                return true;
            }
        }
        return false;
    }

    private void insertCartItem(Product product){
        CartItem cartItem = new CartItem();
        cartItem.setProductID(product.getID());
        cartItem.setAmount(product.getAmount());
        Database.getInstance(getContext()).cartItemDao().insertCartItem(cartItem);
    }

    private void addClientDialog(){

            AlertDialog.Builder addClientDialog = new AlertDialog.Builder(getContext());
            View addClientDialogView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.create_client_dialog, null);
            addClientDialog.setCancelable(false);

            Button createClient = addClientDialogView.findViewById(R.id.addClient);

            addClientDialog.setView(addClientDialogView);
            AlertDialog dialog = addClientDialog.create();

            createClient.setOnClickListener(v -> {
                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim,
                                androidx.navigation.ui.R.animator.nav_default_exit_anim)
                        .replace(R.id.frame, CreateClientFragment.newInstance(smsMessage.number))
                        .addToBackStack(null)
                        .commit();
                dialog.dismiss();
                }
            );
            dialog.show();
    }

    private boolean ifClientExists(){
        return Database.getInstance(getContext()).clientDao().getClientByNumber(smsMessage.number);
    }
}