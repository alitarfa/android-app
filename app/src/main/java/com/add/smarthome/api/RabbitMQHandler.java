package com.add.smarthome.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.add.smarthome.ui.notification.NotificationHandler;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class RabbitMQHandler {



    String topic = "Home";
    String content = "Hello CloudAMQP";
    int qos = 1;
    String broker = "tcp://elephant.rmq.cloudamqp.com:1883";
    String clientId = "";

    private Context context;
    private String title;
    private String body;
    private Intent intent;

    public RabbitMQHandler(Context context, String title, String body, Intent intent) {
        this.context = context;
        this.title = title;
        this.body = body;
        this.intent = intent;
    }

    public MqttClient subscribe() {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient mqttClient = null;
        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("rzsfdunb:rzsfdunb");
            char[] list = "3Smsxl-8cIO8DCwxdq3gKxLais2un1bK".toCharArray();
            connOpts.setPassword(list);
            mqttClient.connect(connOpts);
            mqttClient.subscribe(topic);

            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {
                    Log.e("RabbidMQ","Connected");
                }

                @Override
                public void connectionLost(Throwable throwable) {

                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    Log.e("RABBIDMQ" ,"Arrived MSG");
                    SharedPreferences prefs = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
                    if (prefs.getBoolean("notification",true)) {
                        new NotificationHandler().showNotification(context, title, new String(mqttMessage.getPayload()), intent);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }

        return mqttClient;


    }


}
