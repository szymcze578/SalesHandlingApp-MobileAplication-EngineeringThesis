package com.example.saleshandlingapp;


import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.saleshandlingapp.PackageClients.ClientsFragment;
import com.example.saleshandlingapp.PackageMessages.MessagesFragment;
import com.example.saleshandlingapp.PackageOrders.OrdersFragment;
import com.example.saleshandlingapp.PackageOrders.OrdersHistoryFragment;
import com.example.saleshandlingapp.Reports.GenerateReportFragment;
import com.example.saleshandlingapp.SMSServices.SMSReceiver;
import com.example.saleshandlingapp.SMSServices.SMSReceiverListener;
import com.example.saleshandlingapp.SMSServices.SMSService;
import com.example.saleshandlingapp.PackageWarehouse.WarehouseFragment;
import com.example.saleshandlingapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SMSReceiverListener {

    private ActivityMainBinding binding;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private static final int PERMISSION_REQUEST_CODE = 200;

    AlertDialog alertDialog;

    ArrayList<String> permissionsList;
    String[] permissionsStr = {RECEIVE_SMS, READ_SMS,SEND_SMS,POST_NOTIFICATIONS,
            READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    int permissionsCount = 0;

    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onActivityResult(Map<String,Boolean> result) {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                                    permissionsList.add(permissionsStr[i]);
                                }else if (!hasPermission( permissionsStr[i])){
                                    permissionsCount++;
                                }
                            }
                            if (permissionsList.size() > 0) {
                                //Some permissions are denied and can be asked again.
                                askForPermissions(permissionsList);
                            } else if (permissionsCount > 0) {
                                //Show alert dialog
                                showPermissionDialog();
                            } else {
                                //All permissions granted. Do your stuff 🤞
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SMSReceiver.setSMSReceivedListener(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        replaceFragments(new OrdersFragment());
        this.setTitle("Zamówienia");

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.messages) {
                replaceFragments(new MessagesFragment());
                this.setTitle("Wiadomości");
            } else if (item.getItemId()== R.id.order) {
                replaceFragments(new OrdersFragment());
                this.setTitle("Zamówienia");
            } else if (item.getItemId()==R.id.warehouse) {
                replaceFragments(new WarehouseFragment());
                this.setTitle("Magazyn");
            }
            return true;
        });



        navigationView.setNavigationItemSelectedListener(this);

        /*
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 12);
            }
        }

        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 11);
        }

         */

        //checkAndRequestPermissions();


        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        //permissionsList = new ArrayList<>();
        //permissionsList.addAll(Arrays.asList(permissionsStr));
        //askForPermissions(permissionsList);
        //checkReceiveSmsPermission();

        Intent smsServiceIntent = new Intent(this, SMSService.class);
        startService(smsServiceIntent);
    }

    private boolean hasPermission(String permissionStr) {
        return ContextCompat.checkSelfPermission(this, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }
    private void askForPermissions(ArrayList<String> permissionsList) {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
        /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
        which will lead them to app details page to enable permissions from there. */
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Some permissions are need to be allowed to use this app without any problems.")
                .setPositiveButton("Ustawienia", (dialog, which) -> {
                    startActivity(
                            new Intent(
                                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null)
                            )
                    );
                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

    private void replaceFragments(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_exit_anim)
                .replace(R.id.frame,fragment)
                .commit();
    }

    private  boolean checkAndRequestPermissions() {
        int permissionReceiveMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int notificationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permissionReceiveMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) {
            if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),1);
            return false;
        }
        return true;
    }

    private boolean checkPermission() {
        int receiveSMS = ContextCompat.checkSelfPermission(this, RECEIVE_SMS);
        int sendSMS = ContextCompat.checkSelfPermission(this, SEND_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, READ_SMS);
        int postNotification = ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS);
        int writeStorage = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return receiveSMS == PackageManager.PERMISSION_GRANTED && sendSMS == PackageManager.PERMISSION_GRANTED &&
                readSMS == PackageManager.PERMISSION_GRANTED && postNotification == PackageManager.PERMISSION_GRANTED &&
                writeStorage == PackageManager.PERMISSION_GRANTED && readStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{RECEIVE_SMS, SEND_SMS,READ_SMS,POST_NOTIFICATIONS,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.clients){
            replaceFragments(new ClientsFragment());
            setTitle("Klienci");
        } else if (id ==R.id.orderHistory) {
            replaceFragments(new OrdersHistoryFragment());
            setTitle("Historia zleceń");
        }
        else if (id ==R.id.raports) {
            replaceFragments(new GenerateReportFragment());
            setTitle("Raport");
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSMSReceived() {

    }

}