package com.add.smarthome.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.add.smarthome.R;
import com.add.smarthome.api.ConnectionHandler;
import com.add.smarthome.ui.notification.NotificationHandler;
import com.add.smarthome.utils.Utils;
import com.google.android.material.snackbar.Snackbar;


import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.WebSocket;

import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity {

    private ImageView actionHandler;
    private ImageView settingHandler;
    private TextView status;
    private WebSocketClient webSocketClient;
    private static boolean isActivate = false;
    private ConstraintLayout  main;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup the UIs
        actionHandler = findViewById(R.id.activeSystemBtn);
        settingHandler = findViewById(R.id.settingBtn);
        main = findViewById(R.id.main);
        status =findViewById(R.id.status);
        status.setText("Disconnected");

        actionHandler.setOnClickListener(v-> {
            try {
                onChangeActivation(Utils.URL_SERVER);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        settingHandler.setOnClickListener(v -> onOpenSetting());

    }


    /**
     * Change the activation System
     */
    @SuppressLint("SetTextI18n")
    private void onChangeActivation(String url) throws URISyntaxException {

        if (isActivate) {
            actionHandler.setImageDrawable(getResources().getDrawable(R.drawable.ic_power_off));
            if (webSocketClient != null) {
                webSocketClient.close();
                ShowSnackbar("You are Disconnected");
                status.setText("Disconnected");
            }
            isActivate = false;
        }else {
            actionHandler.setImageDrawable(getResources().getDrawable(R.drawable.ic_power));
            connection(url);
            isActivate = true;
            ShowSnackbar("You are Connected");
            status.setText("Connected");
        }

    }

    /**
     * open Dialog Setting
     */
    private void onOpenSetting() {

    }

    private void onChangeStatus() {

    }

    /**
     * Create The connection
     */
    private void connection(String url) throws URISyntaxException {
        webSocketClient = new ConnectionHandler(new URI(url),this, "Danger case", " you have problem in your home", new Intent(this,MainActivity.class));
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void ShowSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(main,message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
