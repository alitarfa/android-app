package com.add.smarthome.api;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.add.smarthome.ui.notification.NotificationHandler;

import java.net.URI;

import tech.gusavila92.websocketclient.WebSocketClient;


/**
 * This Class Handel the web socket connection
 */
public class ConnectionHandler extends WebSocketClient {

    private Context context;
    private String title;
    private String body;
    private Intent intent;

    /**
     * Initialize all the variables
     *
     * @param uri URI of the WebSocket server
     */
    public ConnectionHandler(URI uri, Context context, String title, String body, Intent intent) {
        super(uri);
        this.context = context;
        this.title = title;
        this.body = body;
        this.intent = intent;
    }

    @Override
    public void onOpen() {
        Log.e("OPEN","CONNECTION");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTextReceived(String message) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBinaryReceived(byte[] data) {
        Log.e("MESSAGE",new String(data));
        new NotificationHandler().showNotification(context, title, new String(data), intent);
    }

    @Override
    public void onPingReceived(byte[] data) {

    }

    @Override
    public void onPongReceived(byte[] data) {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onCloseReceived() {

    }
}
