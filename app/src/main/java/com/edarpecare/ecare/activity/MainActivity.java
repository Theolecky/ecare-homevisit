package com.edarpecare.ecare.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.edarpecare.ecare.R;
import com.edarpecare.ecare.connectivity.ConnectivityReceiver;
import com.edarpecare.ecare.helper.SQLHandler;
import com.edarpecare.ecare.helper.SessionManager;


public class MainActivity extends Activity {
    SharedPreferences prf;
    private TextView txtName;
    private TextView inputServiceNumber;
    private TextView inputFullName;
    private Button btnLogout, btnFollowUp, btnDueMissed, btnCHWReport;

    private SQLHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = findViewById(R.id.name);
        btnLogout = findViewById(R.id.btnLogout);
        btnCHWReport = findViewById(R.id.btnCHWReport);

        // SqLite database handler
        db = new SQLHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        //HashMap<String, String> user = db.getUserDetails();
        prf = getSharedPreferences("EcareMobileLogin", MODE_PRIVATE);

        //String name = user.get("name");
        //String email = user.get("email");
        String name = prf.getString("full_name",null);
                // Displaying the user details on the screen
        txtName.setText(name);
        //txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // CHW Report Form button click event
//        btnCHWReport.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,
//                        CHWReportFormActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        //HOME Visit form button click event
        btnCHWReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        HomevisitActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false,"");

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void checkConnection() {
        boolean isConnected = com.edarpecare.ecare.connectivity.ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Alert
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }
        Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

        // register connection status listener
        //AppController.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    /*@Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }*/
}