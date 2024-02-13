package com.example.saleshandlingapp.Reports;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.saleshandlingapp.Database.Database;
import com.example.saleshandlingapp.Database.OrderItemAndProduct;
import com.example.saleshandlingapp.Database.OrderWithOrderItemsAndProducts;
import com.example.saleshandlingapp.Enum.OrderStatus;
import com.example.saleshandlingapp.Enum.OrderType;
import com.example.saleshandlingapp.Formatters.DateFormatter;
import com.example.saleshandlingapp.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateReportFragment extends Fragment {


    private Button startDateButton, endDateButton, generateReport;
    private TextView startDateTextView, endDateTextView;

    private static final int PERMISSION_REQUEST_CODE = 99;

    public GenerateReportFragment() {
        // Required empty public constructor
    }

    public static GenerateReportFragment newInstance(String param1, String param2) {
        GenerateReportFragment fragment = new GenerateReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkPermission()) {
            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generate_report, container, false);
        startDateButton = view.findViewById(R.id.startDateButton);
        endDateButton = view.findViewById(R.id.endDateButton);
        startDateTextView = view.findViewById(R.id.startDateView);
        endDateTextView = view.findViewById(R.id.endDateView);

        generateReport = view.findViewById(R.id.generateReport);

        startDateButton.setOnClickListener(v -> {
            showDatePickerDialog(startDateTextView);
        });

        endDateButton.setOnClickListener(v -> {
            showDatePickerDialog(endDateTextView);
        });

        generateReport.setOnClickListener(v -> {
            generateReport();
        });

        return view;
    }

    private void showDatePickerDialog(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
                    textView.setText(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void generateReport() {
        try {
            LocalDateTime startDate = LocalDate.parse(startDateTextView.getText().toString(), DateFormatter.formatToReport()).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(endDateTextView.getText().toString(), DateFormatter.formatToReport()).atTime(23, 59, 59, 999999999);
            if (startDate.isAfter(endDate)) {
                Toast.makeText(this.getContext(), "Nieodpowiedni przedział datowy!", Toast.LENGTH_LONG).show();
                return;
            }
            List<OrderWithOrderItemsAndProducts> ordersBetween = Database.getInstance(getContext()).orderDao().getOrdersBetween(startDate, endDate, OrderStatus.COMPLETED,OrderType.ORDER);
            if (!ordersBetween.isEmpty()) {
                Map<String, Map.Entry<Integer, Double>> raportSummary = prepareReportData(ordersBetween);
                PDFService.createPdfReport(raportSummary,startDate,endDate,getContext());
            }
            else {
                Toast.makeText(this.getContext(), "Nie ma zamówień w tym okresie!", Toast.LENGTH_LONG).show();
            }
        } catch (DateTimeParseException e) {
            Toast.makeText(this.getContext(), "Wybierz przedział datowy!", Toast.LENGTH_LONG).show();
        }
    }


    private Map<String, Map.Entry<Integer, Double>> prepareReportData(List<OrderWithOrderItemsAndProducts> orders) {
        Map<String, Map.Entry<Integer, Double>> productSummary = new LinkedHashMap<>();
        double ordersTotalPrice = 0.0;

        for (OrderWithOrderItemsAndProducts order : orders) {
            for (OrderItemAndProduct orderItemAndProduct : order.orderItems) {
                String productName = orderItemAndProduct.product.getProductName();
                int amount = orderItemAndProduct.orderItem.getAmount();
                double priceForProducts = orderItemAndProduct.orderItem.getItemPrice() * amount;

                if (productSummary.containsKey(productName)) {
                    // Jeśli produkt już istnieje w mapie, aktualizuj ilość i cenę
                    Map.Entry<Integer, Double> existingEntry = productSummary.get(productName);
                    int totalAmount = existingEntry.getKey();
                    double totalPrice = existingEntry.getValue();

                    existingEntry = Map.entry(totalAmount + amount, totalPrice + priceForProducts);
                    productSummary.put(productName, existingEntry);
                } else {
                    // Jeśli produkt nie istnieje w mapie, dodaj nowy wpis
                    Map.Entry<Integer, Double> newEntry = Map.entry(amount, priceForProducts);
                    productSummary.put(productName, newEntry);
                }
            }
            ordersTotalPrice += order.order.getTotalPrice();
        }
        productSummary.put("Zamówienia", Map.entry(orders.size(), ordersTotalPrice));
        return productSummary;
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

}