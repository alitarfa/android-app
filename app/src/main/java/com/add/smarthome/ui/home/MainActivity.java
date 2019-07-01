package com.add.smarthome.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.add.smarthome.R;
import com.add.smarthome.api.RabbitMQHandler;
import com.add.smarthome.ui.dialog.Settings;
import com.add.smarthome.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity implements Settings.OnChangeAction {

    private ImageView actionHandler;
    private ImageView settingHandler;
    private TextView status;
    private WebSocketClient webSocketClient;
    private static boolean isActivate = false;
    private ConstraintLayout  main;
    SharedPreferences prefs ;
    MqttClient mqttClient;


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
        prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
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
            if (mqttClient != null) {
                try {
                    mqttClient.close();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                ShowSnackbar("You are Disconnected");
                status.setText("Disconnected");
            }
            isActivate = false;
        }else {
            actionHandler.setImageDrawable(getResources().getDrawable(R.drawable.ic_power));
            RabbitMQHandler handler = new RabbitMQHandler(this,"Smart Home","Content to passs",new Intent(this, MainActivity.class));
            mqttClient = handler.subscribe();
            isActivate = true;
            ShowSnackbar("You are Connected");
            status.setText("Connected");
        }


    }


    /**
     * open Dialog Setting
     */
    private void onOpenSetting() {
        Settings settings = new Settings(this);
        settings.show(getSupportFragmentManager(),"Setting");
    }

    public void ShowSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(main,message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onHandelSW(boolean status) {
        prefs.edit().putBoolean("notification",status).apply();
        Log.e("Staus", String.valueOf(status));
    }
}
